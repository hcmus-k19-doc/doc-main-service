SET SEARCH_PATH TO doc_main;

ALTER TABLE "department" ADD COLUMN "truong_phong_id" BIGINT;
UPDATE "department" SET "truong_phong_id" = 5 WHERE "id" = 1;
UPDATE "department" SET "truong_phong_id" = 6 WHERE "id" = 2;
UPDATE "department" SET "truong_phong_id" = 7 WHERE "id" = 3;
