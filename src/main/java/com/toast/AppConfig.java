package com.toast;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.AsyncRestTemplate;

/**
 * Created by lbwang on 9/21/16.
 */
@Configuration
public class AppConfig {
    @Bean
    public AsyncRestTemplate restTemplate() {
        return new AsyncRestTemplate();
    }
}
