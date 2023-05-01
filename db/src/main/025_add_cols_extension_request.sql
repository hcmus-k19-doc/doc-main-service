SET SEARCH_PATH TO doc_main;

ALTER TABLE "extension_request" ADD COLUMN "approved_by" BIGINT;
ALTER TABLE "extension_request" ADD COLUMN "old_expired_date" DATE NOT NULL DEFAULT CURRENT_DATE;
