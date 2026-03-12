import os
import torch
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional
from transformers import AutoModel
from transformers.image_utils import load_image
from contextlib import asynccontextmanager

# ── Config ────────────────────────────────────────────────────────────────────
MODEL_NAME = os.getenv("MODEL_NAME", "nvidia/llama-nemotron-embed-vl-1b-v2")
DEVICE = "cuda" if torch.cuda.is_available() else "cpu"

# ── Lifespan: load model once at startup ──────────────────────────────────────
model = None

@asynccontextmanager
async def lifespan(app: FastAPI):
    global model
    print(f"Loading model {MODEL_NAME} on {DEVICE}...")
    model = AutoModel.from_pretrained(
        MODEL_NAME,
        dtype=torch.bfloat16,
        trust_remote_code=True,
        attn_implementation="eager",
        device_map="auto",
    ).eval()
    # Default processor settings
    model.processor.p_max_length = 8192
    model.processor.max_input_tiles = 6
    model.processor.use_thumbnail = True
    print("Model ready.")
    yield
    del model

app = FastAPI(lifespan=lifespan)

# ── Request schemas ───────────────────────────────────────────────────────────
class EmbedRequest(BaseModel):
    # Provide `texts` for text/query embedding
    texts: Optional[list[str]] = None
    # Provide `image_urls` for image document embedding (URLs or local paths)
    image_urls: Optional[list[str]] = None
    # Provide both for image+text combined document embedding
    mode: str = "text"  # "text" | "query" | "image" | "image_text"

class EmbedResponse(BaseModel):
    embeddings: list[list[float]]
    dimensions: int

# ── Endpoint ──────────────────────────────────────────────────────────────────
@app.post("/internal/embed", response_model=EmbedResponse)
async def embed(req: EmbedRequest):
    if model is None:
        raise HTTPException(status_code=503, detail="Model not loaded")

    try:
        with torch.inference_mode():
            if req.mode == "query":
                # Embed search queries (text only)
                if not req.texts:
                    raise HTTPException(status_code=400, detail="texts required for query mode")
                model.processor.p_max_length = 8192
                embeddings = model.encode_queries(req.texts)

            elif req.mode == "text":
                # Embed text documents
                if not req.texts:
                    raise HTTPException(status_code=400, detail="texts required for text mode")
                model.processor.p_max_length = 8192
                embeddings = model.encode_documents(texts=req.texts)

            elif req.mode == "image":
                # Embed document page images
                if not req.image_urls:
                    raise HTTPException(status_code=400, detail="image_urls required for image mode")
                model.processor.p_max_length = 2048
                images = [load_image(url) for url in req.image_urls]
                embeddings = model.encode_documents(images=images)

            elif req.mode == "image_text":
                # Embed images + their OCR text combined (best accuracy)
                if not req.image_urls or not req.texts:
                    raise HTTPException(status_code=400, detail="Both image_urls and texts required for image_text mode")
                if len(req.image_urls) != len(req.texts):
                    raise HTTPException(status_code=400, detail="image_urls and texts must have the same length")
                model.processor.p_max_length = 10240
                images = [load_image(url) for url in req.image_urls]
                embeddings = model.encode_documents(images=images, texts=req.texts)

            else:
                raise HTTPException(status_code=400, detail=f"Unknown mode: {req.mode}")

        vecs = embeddings.cpu().to(torch.float32).tolist()
        return EmbedResponse(embeddings=vecs, dimensions=len(vecs[0]))

    except HTTPException:
        raise
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.get("/health")
def health():
    return {"status": "ok", "device": DEVICE, "model": MODEL_NAME}