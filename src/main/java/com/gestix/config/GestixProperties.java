package com.gestix.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "gestix")
public class GestixProperties {

    /** URL pública de la app (para enlaces en correos). */
    private String appUrl = "http://localhost:8080";

    private Mail mail = new Mail();

    @Data
    public static class Mail {
        private boolean enabled = false;
        private String from = "noreply@gestix.app";
    }
}
