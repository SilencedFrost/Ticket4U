-- Table: email_verification_token

DROP TABLE IF EXISTS public.email_verification_token;

CREATE TABLE IF NOT EXISTS public.email_verification_token (
    id uuid PRIMARY KEY,
    token_hash char(64) NOT NULL UNIQUE,
    user_id uuid NOT NULL,
    expires_at timestamptz NOT NULL,
    created_at timestamptz NOT NULL,
    CONSTRAINT evtoken_fk_user FOREIGN KEY (user_id) REFERENCES public.users (id) ON DELETE CASCADE
);

CREATE INDEX idx_evtoken_user_id ON public.email_verification_token (user_id);

CREATE INDEX idx_evtoken_expires_at ON public.email_verification_token (expires_at);

ALTER TABLE IF EXISTS public.email_verification_token OWNER to postgres;