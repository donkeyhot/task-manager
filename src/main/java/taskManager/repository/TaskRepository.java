package taskManager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import taskManager.model.Task;

@CrossOrigin
@RepositoryRestResource(collectionResourceRel = "tasks", path = "tasks")
public interface TaskRepository extends CrudRepository<Task, Long> {

    Iterable<Task> findByCompletedIsNull();

    Iterable<Task> findByCompletedNotNull();

}
