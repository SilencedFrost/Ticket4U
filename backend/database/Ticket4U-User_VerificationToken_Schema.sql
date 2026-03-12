-- Table: verification_token (unified for email verification, password reset, etc.)

DROP TABLE IF EXISTS public.verification_token;

CREATE TABLE IF NOT EXISTS public.verification_token (
    id uuid PRIMARY KEY,
    token_hash char(64) NOT NULL,
    token_type varchar(32) NOT NULL,
    user_id uuid NOT NULL,
    expires_at timestamptz NOT NULL,
    created_at timestamptz NOT NULL,
    CONSTRAINT vtoken_fk_user FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE,
    CONSTRAINT uq_token_hash_type UNIQUE (token_hash, token_type)
);

CREATE INDEX idx_vtoken_user_id ON public.verification_token (user_id);

CREATE INDEX idx_vtoken_expires_at ON public.verification_token (expires_at);

ALTER TABLE IF EXISTS public.verification_token OWNER to postgres;