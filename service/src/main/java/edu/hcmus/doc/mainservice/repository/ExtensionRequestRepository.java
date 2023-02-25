package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.ExtensionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtensionRequestRepository extends JpaRepository<ExtensionRequest, Long> {

}