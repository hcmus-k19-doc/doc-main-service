package edu.hcmus.doc.mainservice.repository;

import static org.mockito.ArgumentMatchers.anyString;

import edu.hcmus.doc.mainservice.model.entity.ProcessingDocument;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProcessingDocumentRepositoryTest extends DocAbstractRepositoryTest {

  @Test
  void testGetAllProcessingDocuments() {
    // Given
    String query = anyString();
    long offset = 0;
    long limit = 3;

    // When
    List<ProcessingDocument> processingDocuments =
        processingDocumentRepository.getProcessingDocuments(
            query,
            offset,
            limit
        );

    // Then
    Assertions.assertThat(processingDocuments).isNotEmpty();
  }
}