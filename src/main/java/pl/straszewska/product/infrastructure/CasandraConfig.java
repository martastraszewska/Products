package pl.straszewska.product.infrastructure;

import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.cassandra.core.cql.session.DefaultSessionFactory;
import org.springframework.data.cassandra.core.cql.session.init.ResourceKeyspacePopulator;
import org.springframework.data.cassandra.core.cql.session.init.SessionFactoryInitializer;

//@Configuration
public class CasandraConfig
{
    @Bean
    public SessionFactoryInitializer cassandraSessionInitializer(CqlSessionBuilder cqlSessionBuilder) {
        SessionFactoryInitializer session = new SessionFactoryInitializer();

        ResourceKeyspacePopulator initResourcePopulator = new ResourceKeyspacePopulator();
        initResourcePopulator.addScript(new ClassPathResource("product_table_def.cql"));
        session.setKeyspacePopulator(initResourcePopulator);

        ResourceKeyspacePopulator destroyResourcePopulator = new ResourceKeyspacePopulator();
        destroyResourcePopulator.addScript(new ClassPathResource("drop_product_table.cql"));
        session.setKeyspaceCleaner(destroyResourcePopulator);

        session.setSessionFactory(new DefaultSessionFactory(cqlSessionBuilder.build()));
        return session;
    }
}
