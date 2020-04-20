package com.demo.userservice.configurations;

import com.demo.userservice.tasks.Task;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.MediaType;

@Configuration
public class RepositoryConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Task.class);
        config.useHalAsDefaultJsonMediaType(false);
        config.setDefaultMediaType(MediaType.APPLICATION_JSON);
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config);
    }
}
