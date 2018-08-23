package taskManager.repository;

import org.springframework.data.repository.CrudRepository;

import taskManager.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

    Iterable<Task> findByCompletedIsNull();

    Iterable<Task> findByCompletedNotNull();

}
