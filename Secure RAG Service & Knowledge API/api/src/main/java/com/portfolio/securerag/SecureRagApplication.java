package com.portfolio.securerag;

import com.portfolio.securerag.config.AuditProperties;
import com.portfolio.securerag.config.QdrantProperties;
import com.portfolio.securerag.config.RagProperties;
import com.portfolio.securerag.config.SecurityProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        RagProperties.class,
        QdrantProperties.class,
        SecurityProperties.class,
        AuditProperties.class
})
public class SecureRagApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureRagApplication.class, args);
    }
}
