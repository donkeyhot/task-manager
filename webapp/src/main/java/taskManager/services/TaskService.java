package taskManager.services;

import taskManager.model.Task;

public interface TaskService {

    /**
     * Retreives all Tasks
     * 
     * @return Iterable containing Task entities. Should not be
     *         <code>null</code> value
     */
    Iterable<Task> getAll();

    /**
     * Retreives a single Task with given ID
     * 
     * @param taskId
     *            ID of the Task. Must nut be <code>null</code>
     * 
     * @return Task entity. Should not be <code>null</code> value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs
     */
    Task getSingle(Long taskId);

    /**
     * Store new Task data
     * 
     * @param task
     *            Task data to be stored. Must nut be <code>null</code>
     * 
     * @return successfuly stored Task entity with generated ID. Should not be
     *         <code>null</code> value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when another Task is registered with the same ID. It could
     *             occurs only when passed entity id is set (not
     *             <code>null</code>)
     */
    Task create(Task Task);

    /**
     * Updates Task with given ID and new data
     * 
     * @param taskId
     *            ID of the Task. Must nut be <code>null</code>
     * @param task
     *            Task entity to be updated from. Must nut be <code>null</code>
     * 
     * @return updated Task entity. Should not be <code>null</code> value
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs or ID's
     *             doesn't match
     */
    Task update(Long taskId, Task Task);

    /**
     * Remove Task with given ID. It also removes all Users attached
     * 
     * @param taskId
     *            ID of the Task. Must nut be <code>null</code>
     * 
     * @throws NullPointerException
     *             when any of parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             when any of entites wasn't found with given IDs
     */
    void delete(Long taskId);

    Iterable<Task> getPending();

    Iterable<Task> getCompleted();

    Task complete(Long taskId);

    Task uncomplete(Long taskId);
}
