--liquibase formatted sql
--changeset author:015

drop database if exists "doc-keycloak-db";
CREATE DATABASE "doc-keycloak-db";
