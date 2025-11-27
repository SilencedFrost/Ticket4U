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