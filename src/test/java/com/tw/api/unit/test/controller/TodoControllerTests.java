package com.tw.api.unit.test.controller;

import com.tw.api.unit.test.domain.todo.Todo;
import com.tw.api.unit.test.domain.todo.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TodoController.class)
@DisplayName("A example to test show service with mock strategy")
public class TodoControllerTests {

    @Autowired
    private TodoController todoController;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TodoRepository todoRepository;

    @Test
    void getAll() throws Exception {
        //given
        List<Todo> todos = new ArrayList<>();
        Todo todo = new Todo(1, "Title", false, 2);
        todos.add(todo);
        when(todoRepository.getAll()).thenReturn(todos);
        //when
        ResultActions result = mvc.perform(get("/todos"));
        //then
        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Title")));
    }

    @Test
    public void should_get_single_to_do_when_get_todos_by_id() throws Exception {
        //given
        Todo todo = new Todo(123, "Title", true, 1);
        //when
        when(todoRepository.findById(1L)).thenReturn(java.util.Optional.of(todo));
        ResultActions resultActions = mvc.perform(get("/todos/1"));
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                //.andExpect(jsonPath("$", instanceOf(Todo.class)))
                .andExpect(jsonPath("$.id", is(123)))
                .andExpect(jsonPath("$.title", is("Title")))
                .andExpect(jsonPath("$.completed", is(true)))
                .andExpect(jsonPath("$.order", is(1)));
    }
}
