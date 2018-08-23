package taskManager.controller;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import taskManager.model.Task;
import taskManager.services.TaskService;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    /**
     * Post to /tasks endpoint creates and returns task. Response status is 201.
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody @NotNull Task task) {
	final Task result = taskService.create(task);
	return ResponseEntity.status(HttpStatus.CREATED)
		.body(result);
    }

    /**
     * Patch to /tasks/{taskId} endpoint updates, saves and returns task with
     * id=taskId.
     */
    @PatchMapping("/{taskId}")
    public ResponseEntity<?> update(@PathVariable(value = "taskId") Long taskId,
	    @RequestBody @NotNull Task task) {
	try {
	    final Task result = taskService.update(taskId, task);
	    return ResponseEntity.ok(result);
	} catch (IllegalArgumentException e) {
	    return ResponseEntity.notFound().build();
	}
    }

    /**
     * Post to /tasks/{taskId}/uncomplete endpoint marks task with id=taskId as
     * completed. If already completed returns 409 ("conflict")
     */
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<?> taskComplete(@PathVariable(value = "taskId") Long taskId) {
	try {
	    final Task result = taskService.complete(taskId);
	    return ResponseEntity.ok(result);
	} catch (IllegalStateException e) {
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
    }

    /**
     * Post to /tasks/{taskId}/uncomplete endpoint marks task with id=taskId as
     * uncompleted. If not completed yet returns 409 ("conflict")
     */
    @PatchMapping("/{taskId}/uncomplete")
    public ResponseEntity<?> taskUncomplete(@PathVariable(value = "taskId") Long taskId) {
	try {
	    final Task result = taskService.uncomplete(taskId);
	    return ResponseEntity.ok(result);
	} catch (IllegalStateException e) {
	    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}
    }

    /**
     * Get to /tasks/{taskId} endpoint fetches and returns task with id=taskId.
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<?> get(@PathVariable(value = "taskId") @NotNull Long taskId) {
	try {
	    final Task result = taskService.getSingle(taskId);
	    return ResponseEntity.ok(result);
	} catch (IllegalArgumentException e) {
	    return ResponseEntity.notFound().build();
	}
    }

    /**
     * Delete to /tasks/{taskId} endpoint deletes task with id=taskId. Response
     * status is 204.
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> delete(@PathVariable(value = "taskId") @NotNull Long taskId) {
	try {
	    taskService.delete(taskId);
	    return ResponseEntity.status(HttpStatus.NO_CONTENT)
		    .build();
	} catch (IllegalArgumentException e) {
	    return ResponseEntity.notFound().build();
	}
    }

    /**
     * Get to /tasks endpoint returns list of all tasks
     */
    @GetMapping
    public ResponseEntity<?> all() {
	final Iterable<Task> result = taskService.getAll();
	return ResponseEntity.ok(result);
    }

    /**
     * Get to /tasks/pending endpoint returns list of all pending tasks
     */
    @GetMapping("/pending")
    public ResponseEntity<?> pending() {
	final Iterable<Task> result = taskService.getPending();
	return ResponseEntity.ok(result);
    }

    /**
     * Get to /tasks/pending endpoint returns list of all completed tasks
     */
    @GetMapping("/completed")
    public ResponseEntity<?> completed() {
	final Iterable<Task> result = taskService.getCompleted();
	return ResponseEntity.ok(result);
    }
}
