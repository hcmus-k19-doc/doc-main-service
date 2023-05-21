package edu.hcmus.doc.mainservice.repository.custom;

import edu.hcmus.doc.mainservice.model.entity.ExtendRequest;
import java.util.List;

public interface CustomExtensionRequestRepository {

  List<ExtendRequest> getExtensionRequestsByUsername(String username);
}
