--liquibase formatted sql
--changeset author:006

SET SEARCH_PATH TO doc_main;

ALTER TABLE "incoming_document" RENAME "comment" TO "summary";
