SET SEARCH_PATH TO doc_main;

CREATE TABLE doc_main.transfer_history (
	id serial4 NOT NULL,
	sender_id int8 NOT NULL,
	receiver_id int8 NOT NULL,
	incoming_doc_ids _int8 NULL,
	outgoing_doc_ids _int8 NULL,
	processing_duration timestamp NULL,
	is_infinite_processing_time bool NULL,
	"process_method" doc_main."process_method" NULL,
	CONSTRAINT transfer_history_pk PRIMARY KEY (id),
	CONSTRAINT transfer_history_receiver_user_fk FOREIGN KEY (receiver_id) REFERENCES doc_main."user"(id),
	CONSTRAINT transfer_history_sender_user_fk FOREIGN KEY (sender_id) REFERENCES doc_main."user"(id)
)
INHERITS (doc_main.doc_base_table);

CREATE TABLE doc_main.transfer_history_incoming_document (
	transfer_history_id int8 NOT NULL,
	incoming_doc_id int8 NULL
) inherits ("doc_base_table");

CREATE TABLE doc_main.transfer_history_outgoing_document (
	transfer_history_id int8 NOT NULL,
	outgoing_doc_id int8 NULL
) inherits ("doc_base_table");