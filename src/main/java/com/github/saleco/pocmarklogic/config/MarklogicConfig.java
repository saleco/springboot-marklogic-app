package com.github.saleco.pocmarklogic.config;

import com.github.saleco.pocmarklogic.config.properties.MarklogicProperties;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.JSONDocumentManager;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.query.QueryManager;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MarklogicConfig {

    private final MarklogicProperties marklogicProperties;

    @Bean
    public DatabaseClient databaseClient() {
        return DatabaseClientFactory.newClient(marklogicProperties.getHost(),
          marklogicProperties.getPort(),
          new DatabaseClientFactory.DigestAuthContext(marklogicProperties.getUsername(),marklogicProperties.getPassword()));
    }

    @Bean
    public QueryManager queryManager() {
        return databaseClient().newQueryManager();
    }

    @Bean
    public XMLDocumentManager getXMLDocumentManager() {
        return databaseClient().newXMLDocumentManager();
    }

    @Bean
    public JSONDocumentManager getJSONDocumentManager() {
        return databaseClient().newJSONDocumentManager();
    }

    @Bean
    public String getMarkLogicBaseURL() {
        return String.format("http://%s:%d", marklogicProperties.getHost(), marklogicProperties.getPort());
    }

}
