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