SET SEARCH_PATH TO "doc_main";

INSERT INTO "document_type" ("type")
VALUES ('CONTRACT'),
       ('INVOICE'),
       ('PAYMENT'),
       ('RECEIPT'),
       ('OTHER');

INSERT INTO "sending_level" ("level")
VALUES ('CITY'),
       ('DISTRICT'),
       ('SCHOOL');

INSERT INTO "distribution_organization" ("name", "symbol")
VALUES ('University of Science', 'HCMUS'),
       ('University of Technology', 'HCMUT'),
       ('University of Education', 'HCMUE');

INSERT INTO "incoming_document"
(incoming_number,
 document_type_id,
 original_symbol_number,
 distribution_org_id,
 distribution_date,
 arriving_date,
 arriving_time,
 comment,
 urgency,
 confidentiality,
 folder,
 sending_level_id)
VALUES ('1', 1, '1', 1, NOW(), '2018-01-01', '19:00:00', 'comment', 'LOW', 'LOW', 'FOLDER', 1),
         ('2', 2, '2', 2, NOW(), '2018-01-01', '19:00:00', 'comment', 'LOW', 'LOW', 'FOLDER', 2),
         ('3', 3, '3', 3, NOW(), '2018-01-01', '19:00:00', 'comment', 'LOW', 'LOW', 'FOLDER', 3),
         ('4', 4, '4', 1, NOW(), '2018-01-01', '19:00:00', 'comment', 'LOW', 'LOW', 'FOLDER', 1),
         ('5', 5, '5', 2, NOW(), '2018-01-01', '19:00:00', 'comment', 'MEDIUM', 'MEDIUM', 'FOLDER', 2),
         ('6', 1, '6', 3, NOW(), '2018-01-01', '19:00:00', 'comment', 'MEDIUM', 'MEDIUM', 'FOLDER', 3),
         ('7', 2, '7', 1, NOW(), '2018-01-01', '19:00:00', 'comment', 'MEDIUM', 'MEDIUM', 'FOLDER', 1),
         ('8', 3, '8', 2, NOW(), '2018-01-01', '19:00:00', 'comment', 'MEDIUM', 'MEDIUM', 'FOLDER', 2),
         ('9', 4, '9', 3, NOW(), '2018-01-01', '19:00:00', 'comment', 'HIGH', 'HIGH', 'FOLDER', 3),
         ('10', 5, '10', 1, NOW(), '2018-01-01', '19:00:00', 'comment', 'HIGH', 'HIGH', 'FOLDER', 1),
         ('11', 1, '11', 2, NOW(), '2018-01-01', '19:00:00', 'comment', 'HIGH', 'HIGH', 'FOLDER', 2),
         ('12', 2, '12', 3, NOW(), '2018-01-01', '19:00:00', 'comment', 'HIGH', 'HIGH', 'FOLDER', 3);

INSERT INTO "processed_document" (incoming_doc_id)
VALUES (2),
       (4),
       (6),
       (8),
       (10),
       (12);

INSERT INTO "processing_document"
(incoming_doc_id,
 status,
 is_opened,
 processing_request)
VALUES (1, 'IN_PROGRESS', FALSE, 'processing_request'),
       (3, 'IN_PROGRESS', FALSE, 'processing_request'),
       (5, 'IN_PROGRESS', FALSE, 'processing_request'),
       (7, 'IN_PROGRESS', FALSE, 'processing_request'),
       (9, 'IN_PROGRESS', FALSE, 'processing_request'),
       (11, 'IN_PROGRESS', FALSE, 'processing_request'),
       (2, 'CLOSED', TRUE, 'processing_request'),
       (4, 'CLOSED', TRUE, 'processing_request'),
       (6, 'CLOSED', TRUE, 'processing_request'),
       (8, 'CLOSED', TRUE, 'processing_request'),
       (10, 'CLOSED', TRUE, 'processing_request'),
       (12, 'CLOSED', TRUE, 'processing_request');

INSERT INTO "outgoing_document" (version)
VALUES (0),
       (0),
       (0),
       (0),
       (0),
       (0);

INSERT INTO "linked_document" (incoming_doc_id, outgoing_doc_id)
VALUES (1, 1),
       (3, 2),
       (5, 3),
       (7, 4),
       (9, 5),
       (11, 6);

INSERT INTO "extension_request" (processing_doc_id, reason, extended_until, status)
VALUES (1, 'reason', '2023-12-01', 'PENDING'),
       (2, 'reason', '2023-12-01', 'PENDING'),
       (3, 'reason', '2023-12-01', 'PENDING'),
       (4, 'reason', '2023-12-01', 'PENDING'),
       (5, 'reason', '2023-12-01', 'APPROVED'),
       (6, 'reason', '2023-12-01', 'APPROVED'),
       (7, 'reason', '2023-12-01', 'APPROVED'),
       (8, 'reason', '2023-12-01', 'APPROVED'),
       (9, 'reason', '2023-12-01', 'REJECTED'),
       (10, 'reason', '2023-12-01', 'REJECTED'),
       (11, 'reason', '2023-12-01', 'REJECTED'),
       (12, 'reason', '2023-12-01', 'REJECTED');

INSERT INTO "feedback" (processing_doc_id, content)
VALUES (1, 'content'),
       (2, 'content'),
       (3, 'content'),
       (4, 'content'),
       (5, 'content'),
       (6, 'content'),
       (7, 'content'),
       (8, 'content'),
       (9, 'content'),
       (10, 'content'),
       (11, 'content'),
       (12, 'content');