package com.datagrokr.todo_api.mongo;

import org.springframework.context.annotation.Bean;

public class MongoConfig {
    @Bean
    public CascadeSaveMongoListener cascadeSaveMongoListener() {
        return new CascadeSaveMongoListener();
    }
}
