SET SEARCH_PATH TO doc_main;

-- ALTER TABLE processing_document ADD processing_duration date NULL;

ALTER TABLE processing_user ADD processing_duration date NULL;

-- ALTER TABLE processing_document
-- DROP COLUMN processing_duration;