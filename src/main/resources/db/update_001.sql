CREATE TABLE IF NOT EXISTS sites (
    id serial primary key not null,
    url varchar (100) NOT NULL unique,
    login varchar (36) NOT NULL unique,
    password varchar(36) NOT NULL
);

CREATE TABLE IF NOT EXISTS urls (
    id serial primary key not null,
    url varchar (100) NOT NULL unique,
    code varchar (100) NOT NULL unique,
    total integer,
    site_id int references sites(id)
);