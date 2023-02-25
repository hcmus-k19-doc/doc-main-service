package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.util.PostgresContainerExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional(rollbackFor = Throwable.class)
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
@ExtendWith(PostgresContainerExtension.class)
abstract class AbstractRepositoryTest {

  @Autowired
  protected DistributionOrganizationRepository distributionOrganizationRepository;

  @Autowired
  protected DocumentTypeRepository documentTypeRepository;

  @Autowired
  protected IncomingDocumentRepository incomingDocumentRepository;

  @Autowired
  protected ProcessedDocumentRepository processedDocumentRepository;

  @Autowired
  protected SendingLevelRepository sendingLevelRepository;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected DocSystemRoleRepository docSystemRoleRepository;

  @Autowired
  protected UserRoleRepository userRoleRepository;
}
