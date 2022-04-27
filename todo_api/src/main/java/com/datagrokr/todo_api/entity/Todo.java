package com.datagrokr.todo_api.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document("todo")
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Todo {

    @Id
    private String id;
    private String content;
    private Boolean completed = Boolean.FALSE;

    public Todo(String content) {
        this.content = content;
    }

}
