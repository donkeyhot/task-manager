package taskManager.services.impl;

import java.time.Instant;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import taskManager.model.Task;
import taskManager.repository.TaskRepository;
import taskManager.services.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    TaskRepository repository;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Iterable<Task> getAll() {
	return repository.findAll();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Iterable<Task> getPending() {
	return repository.findByCompletedIsNull();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Iterable<Task> getCompleted() {
	return repository.findByCompletedNotNull();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Task getSingle(Long taskId) {
	Objects.requireNonNull(taskId);
	final Task result = getAndCheckFromRepo(taskId);
	return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Task create(Task task) {
	Objects.requireNonNull(task);

	final Task origin = new Task();
	origin.setName(task.getName());
	origin.setDescription(task.getDescription());
	origin.setCreated(Instant.now());
	return repository.save(origin);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Task update(Long taskId, Task task) {
	Objects.requireNonNull(taskId);
	Objects.requireNonNull(task);

	final Task origin = getAndCheckFromRepo(taskId); // checks that already
							 // exists
	origin.setName(task.getName());
	origin.setDescription(task.getDescription());

	return repository.save(origin);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Task complete(Long taskId) throws IllegalStateException {
	Objects.requireNonNull(taskId);
	final Task origin = getAndCheckFromRepo(taskId);
	if (origin.getCompleted() != null)
	    throw new IllegalStateException("Already completed");
	origin.setCompleted(Instant.now());
	return repository.save(origin);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Task uncomplete(Long taskId) {
	Objects.requireNonNull(taskId);
	final Task origin = getAndCheckFromRepo(taskId);
	if (origin.getCompleted() == null)
	    throw new IllegalStateException("Not completed yet");
	origin.setCompleted(null);
	return repository.save(origin);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long taskId) {
	Objects.requireNonNull(taskId);

	final Task origin = getAndCheckFromRepo(taskId);
	repository.delete(origin);
    }

    // PRIVATE

    private Task getAndCheckFromRepo(Long taskId) {
	assert taskId != null;

	final Task task = repository.findById(taskId).orElse(null);
	if (task == null)
	    throw new IllegalArgumentException("Task not exists");
	return task;
    }
}
