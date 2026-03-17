package com.ticket4u.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        // Jackson 3.x — jsr310 is now built-in, no module registration needed
        // DateTimeFeature replaces SerializationFeature for date/time settings
        return JsonMapper.builder()
                // Serialize dates as ISO strings e.g. "2026-03-17T10:00:00+07:00"
                // instead of timestamp arrays e.g. [2026, 3, 17, 10, 0, 0]
                .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }
}