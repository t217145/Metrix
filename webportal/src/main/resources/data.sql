INSERT INTO myusers (
    name,
    usrcode, 
    password, 
    isactive
) values (
    'peter',
    'peter@metrix.com',
    '$2a$12$819BcFYb/dUKgmEtEkWZpOJQazLkuj6QkwgybCQ0qvhHTvX8vp9B6', --abc
    1
);

INSERT INTO myusers (
    name,
    usrcode, 
    password, 
    isactive
) values (
    'mary',
    'mary@metrix.com',
    '$2a$12$819BcFYb/dUKgmEtEkWZpOJQazLkuj6QkwgybCQ0qvhHTvX8vp9B6', --abc
    1
);

INSERT INTO myusers (
    name,
    usrcode, 
    password, 
    isactive
) values (
    'tom',
    'tom@metrix.com',
    '$2a$12$819BcFYb/dUKgmEtEkWZpOJQazLkuj6QkwgybCQ0qvhHTvX8vp9B6', --abc
    1
);

INSERT INTO myroles (
    usrcode, 
    roles
) values (
    'peter@metrix.com', 
    'ROLE_MANAGER'
);

INSERT INTO myroles (
    usrcode, 
    roles
) values (
    'mary@metrix.com', 
    'ROLE_STAFF'
);

INSERT INTO myroles (
    usrcode, 
    roles
) values (
    'tom@metrix.com', 
    'ROLE_CUSTOMER'
);