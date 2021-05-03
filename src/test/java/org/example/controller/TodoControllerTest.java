package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private TodoService todoService;

	private TodoEntity expected;

	@BeforeEach
	void setUp() {
		expected = new TodoEntity();
		expected.setId(123L);
		expected.setTitle("TEST TITLE");
		expected.setCompleted(false);
		expected.setOrder(0L);
	}

	@Test
	void create() throws Exception {
		when(todoService.add(any(TodoRequest.class)))
			.then((a) -> {
				TodoRequest request = a.getArgument(0, TodoRequest.class);
				return new TodoEntity(expected.getId(),
									request.getTitle(),
									expected.getOrder(),
									expected.getCompleted());
			});

		TodoRequest request = new TodoRequest();
		request.setTitle("ANY TITLE");

		ObjectMapper objectMapper = new ObjectMapper();
		String s = objectMapper.writeValueAsString(request);

		mvc.perform(post("/")
			.contentType(MediaType.APPLICATION_JSON)
			.content(s))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.title").value(request.getTitle()));
	}

	@Test
	void readOne() {
	}
}
