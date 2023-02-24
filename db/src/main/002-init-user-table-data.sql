SET SEARCH_PATH TO "doc-main";

INSERT INTO "user" ("username", "password", "email")
VALUES ('user1', '$2a$10$fXwepveGHZt1poxgYBO7hecLyScDmnlV5c933nTGgN7bsUSk5RPb6', 'user1'),
       ('user2', '$2a$10$/Kwg1KTowgmtaFtatV6ljuxnI5Gk9AivN21tMgr/s25p0TPPf/RU2', 'user2');

INSERT INTO "doc_role" ("name")
VALUES ('DIRECTOR'), ('EXPERT'), ('MANAGER'), ('STAFF');

INSERT INTO "user_role" ("user_id", "role_id")
VALUES (1, 1), (1, 2), (1, 3), (2, 1), (2, 2);