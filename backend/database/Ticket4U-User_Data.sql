-- Users table password: DefaultP4$$
insert into public.users(id, email, role_id, username, password_hash, is_active, is_deleted, created_at) values
(uuidv7(), 'customer@gmail.com'      , 0 , 'Customer', '$argon2id$v=19$m=16384,t=2,p=1$NmYyQUZDaG9VcUZURXZ2bA$aQSvREKaeHY5EvoyEw1wiGhXrnVyA83OtaaHY/xvmr0', true, false, now()),
(uuidv7(), 'eventManager@gmail.com'  , 1 , 'Event Manager', '$argon2id$v=19$m=16384,t=2,p=1$NmYyQUZDaG9VcUZURXZ2bA$aQSvREKaeHY5EvoyEw1wiGhXrnVyA83OtaaHY/xvmr0', true, false, now()),
(uuidv7(), 'organizer@gmail.com'     , 2 , 'Organizer', '$argon2id$v=19$m=16384,t=2,p=1$NmYyQUZDaG9VcUZURXZ2bA$aQSvREKaeHY5EvoyEw1wiGhXrnVyA83OtaaHY/xvmr0', true, false, now()),
(uuidv7(), 'admin@gmail.com'         , 3 , 'Admin', '$argon2id$v=19$m=16384,t=2,p=1$NmYyQUZDaG9VcUZURXZ2bA$aQSvREKaeHY5EvoyEw1wiGhXrnVyA83OtaaHY/xvmr0', true, false, now()),
(uuidv7(), 'systemAdmin@gmail.com'   , 4 , 'System Admin', '$argon2id$v=19$m=16384,t=2,p=1$NmYyQUZDaG9VcUZURXZ2bA$aQSvREKaeHY5EvoyEw1wiGhXrnVyA83OtaaHY/xvmr0', true, false, now()),
(uuidv7(), 'gatekeeper@gmail.com'    , 10, 'Gate keeper', '$argon2id$v=19$m=16384,t=2,p=1$NmYyQUZDaG9VcUZURXZ2bA$aQSvREKaeHY5EvoyEw1wiGhXrnVyA83OtaaHY/xvmr0', true, false, now()),
(uuidv7(), 'supportAgent@gmail.com'  , 11, 'Support Agent', '$argon2id$v=19$m=16384,t=2,p=1$NmYyQUZDaG9VcUZURXZ2bA$aQSvREKaeHY5EvoyEw1wiGhXrnVyA83OtaaHY/xvmr0', true, false, now()),
(uuidv7(), 'financeManager@gmail.com', 12, 'Finance Manager', '$argon2id$v=19$m=16384,t=2,p=1$NmYyQUZDaG9VcUZURXZ2bA$aQSvREKaeHY5EvoyEw1wiGhXrnVyA83OtaaHY/xvmr0', true, false, now())

INSERT INTO public.users (id, email, role_id, username, password_hash, is_active, is_deleted, created_at)
VALUES ('019bb098-c487-7bdb-9082-f51c9e8a9bb2', 'absolute@ticket4u.vn', 2, 'absolute_media', 'hash_pass', true, false, NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.users (id, email, role_id, username, password_hash, is_active, is_deleted, created_at)
VALUES ('019bb098-c487-7bdb-9082-f51c9e8a9cc5', 'catmouse@ticket4u.vn', 2, 'cat_mouse', 'hash_pass', true, false, NOW())
ON CONFLICT (id) DO NOTHING;

INSERT INTO public.organizer (id, name, description, rating, logo_url)
VALUES 
(
    '019bb098-c487-7bdb-9082-f51c9e8a9bb2', 
    'Absolute Media', 
    'Chuyên âm nhạc nghệ thuật cao cấp và giao hưởng.', 
    4.80, 
    'https://salt.tkbcdn.com/ts/ds/be/d4/a4/e6c34b2216009af82feba5f9bca782ad.jpg'
),
(
    '019bb098-c487-7bdb-9082-f51c9e8a9cc5', 
    'Cat&Mouse Live Music', 
    'Phòng trà Cat&Mouse Live Music', 
    4.80, 
    'https://salt.tkbcdn.com/ts/ds/b4/f7/5f/dab59d5d6b39401a5b0864cbadb4a6f3.png'
)
ON CONFLICT (id) DO UPDATE SET logo_url = EXCLUDED.logo_url;