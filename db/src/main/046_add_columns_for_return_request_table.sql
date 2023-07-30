set search_path to doc_main;

alter table return_request
add column if not exists current_processing_user_id bigint not null,
add column if not exists previous_processing_user_id bigint not null,
add column if not exists incoming_doc_id bigint,
add column if not exists outgoing_doc_id bigint;

alter table return_request
add constraint return_request_user_fk FOREIGN KEY (current_processing_user_id) REFERENCES doc_main."user"(id);

alter table return_request
add constraint return_request_user_fk_2 FOREIGN KEY (previous_processing_user_id) REFERENCES doc_main."user"(id);

alter table return_request
add constraint return_request_incoming_document_fk FOREIGN KEY (incoming_doc_id) REFERENCES doc_main."incoming_document"(id);

alter table return_request
add constraint return_request_outgoing_document_fk FOREIGN KEY (outgoing_doc_id) REFERENCES doc_main."outgoing_document"(id);


alter table transfer_history add column return_request_id bigint;

alter table transfer_history
add constraint transfer_history_return_request_fk FOREIGN KEY (return_request_id) REFERENCES doc_main."return_request"(id);
