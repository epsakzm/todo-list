package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.model.TodoEntity;
import org.example.model.TodoRequest;
import org.example.model.TodoResponse;
import org.example.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/")
public class TodoController {

	private final TodoService todoService;

	@PostMapping
	public ResponseEntity<TodoResponse> create(@RequestBody TodoRequest request) {

		if (ObjectUtils.isEmpty(request.getTitle()))
			return ResponseEntity.badRequest().build();
		if (ObjectUtils.isEmpty(request.getOrder()))
			request.setOrder(0L);
		if (ObjectUtils.isEmpty(request.getCompleted()))
			request.setCompleted(false);

		TodoEntity add = todoService.add(request);
		return ResponseEntity.ok(new TodoResponse(add));
	}

	@GetMapping("{id}")
	public ResponseEntity<TodoResponse> readOne(@PathVariable Long id) {
		TodoEntity todoEntity = todoService.searchById(id);
		return ResponseEntity.ok(new TodoResponse(todoEntity));
	}

	@GetMapping
	public ResponseEntity<List<TodoResponse>> readAll() {
		List<TodoEntity> list = todoService.searchAll();
		return ResponseEntity.ok(list.stream().map(TodoResponse::new).collect(Collectors.toList()));
	}

	@PatchMapping("{id}")
	public ResponseEntity<TodoResponse> update(@PathVariable Long id, @RequestBody TodoRequest request) {
		TodoEntity todoEntity = todoService.updateById(id, request);
		return ResponseEntity.ok(new TodoResponse(todoEntity));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteOne(@PathVariable Long id) {
		todoService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping
	public ResponseEntity<?> deleteAll() {
		todoService.deleteAll();
		return ResponseEntity.ok().build();
	}

}
