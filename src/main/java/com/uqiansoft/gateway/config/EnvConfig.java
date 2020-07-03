package com.uqiansoft.gateway.config;

import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@Data
//@Builder
//@Configuration
//@ConfigurationProperties(prefix = "testenv")
//@ConditionalOnProperty(prefix = "env", name = "open", havingValue = "true")
public class EnvConfig {
   @Value("${testenv.name}")
   private String name;

   @Value("${testenv.testIps}")
   private Set<String> testIps = new HashSet<>();
}
