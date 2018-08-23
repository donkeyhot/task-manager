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

	if (task.getId() != null && repository.existsById(task.getId()))
	    throw new IllegalArgumentException("Task exists with given id");
	return repository.save(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Task update(Long taskId, Task task) {
	Objects.requireNonNull(taskId);
	Objects.requireNonNull(task);

	if (task.getId() != null && !taskId.equals(task.getId()))
	    throw new IllegalArgumentException("IDs doesn't match");

	getAndCheckFromRepo(taskId); // checks that already exists

	task.setId(taskId);
	return repository.save(task);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Task complete(Long taskId) throws IllegalStateException {
	Objects.requireNonNull(taskId);
	final Task t = getAndCheckFromRepo(taskId);
	if (t.getCompleted() != null)
	    throw new IllegalStateException("Already completed");
	t.setCompleted(Instant.now());
	return repository.save(t);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Task uncomplete(Long taskId) {
	Objects.requireNonNull(taskId);
	final Task t = getAndCheckFromRepo(taskId);
	if (t.getCompleted() == null)
	    throw new IllegalStateException("Not completed yet");
	t.setCompleted(null);
	return repository.save(t);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long taskId) {
	Objects.requireNonNull(taskId);

	final Task org = getAndCheckFromRepo(taskId);
	repository.delete(org);
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
