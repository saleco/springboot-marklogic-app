package com.github.saleco.pocmarklogic.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "marklogic")
@Data
public class MarklogicProperties {

    private String host;
    private int port;
    private String username;
    private String password;
}
