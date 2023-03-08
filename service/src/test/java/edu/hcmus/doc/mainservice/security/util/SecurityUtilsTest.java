package edu.hcmus.doc.mainservice.security.util;

import edu.hcmus.doc.mainservice.util.PostgresContainerExtension;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@ExtendWith({PostgresContainerExtension.class})
class SecurityUtilsTest {

  @Test
  @WithMockUser(username = "test")
  void getCurrentName() {
    Assertions.assertThat(SecurityUtils.getCurrentName()).isEqualTo("test");
  }
}