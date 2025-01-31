package pl.straszewska.product;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.cassandra.CassandraContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@RunWith(SpringRunner.class)
class BaseIntegrationTest {
    public static final CassandraContainer cassandra
            = new CassandraContainer("cassandra:3.11.2").withExposedPorts(9042);
}
