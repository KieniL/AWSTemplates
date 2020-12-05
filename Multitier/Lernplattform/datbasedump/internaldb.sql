DROP SCHEMA IF EXISTS internal CASCADE;

CREATE SCHEMA internal AUTHORIZATION internaluser;


CREATE TABLE internal.User(
id   SERIAL  primary key,
email VARCHAR(40),
password  VARCHAR(150)
);

CREATE TABLE internal.Schema(
id   SERIAL  primary key,
Name VARCHAR(255),
creator int
);

CREATE TABLE internal.Course(
id   SERIAL  primary key,
name VARCHAR(255),
deleted boolean,
creator int,
schema int,
FOREIGN KEY (creator) REFERENCES internal.User(id),
FOREIGN KEY (schema) REFERENCES internal.Schema(id)
);

CREATE TABLE internal.Task(
id   SERIAL  primary key,
name VARCHAR(255),
description VARCHAR(10000),
deleted boolean,
courseId int,
code character varying(100000) COLLATE pg_catalog."default",
FOREIGN KEY (courseId) REFERENCES internal.Course(id)
);
