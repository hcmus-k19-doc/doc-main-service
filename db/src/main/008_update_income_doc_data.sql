SET SEARCH_PATH TO "doc_main";

UPDATE "incoming_document" SET "arriving_date" = CURRENT_DATE + "id";
UPDATE "incoming_document" SET "arriving_time" = CURRENT_TIME + '1 hour'::INTERVAL * "id";
