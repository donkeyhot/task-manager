package taskManager.services.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.AdditionalAnswers.*;
import taskManager.model.Task;
import taskManager.repository.TaskRepository;
import taskManager.services.TaskService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { TaskServiceImpl.class })
public class TaskServiceImplTest {

    @Autowired
    TaskService service;

    @MockBean
    TaskRepository mockRepository;

    // getAll

    @Test
    public void givenExistingTask_whenGetAll_thenReturnIterable() {

	// given
	final Task org1 = Task.of(200L, "Task 1");
	final Task org2 = Task.of(300L, "Task 2");

	given(mockRepository.findAll())
		.willReturn(Arrays.asList(org1, org2));

	// when
	final Iterable<Task> result = service.getAll();

	// then
	assertThat(result)
		.asList()
		.containsExactlyInAnyOrder(org1, org2);
    }

    // getSingle

    @Test
    public void givenExistingTask_whenGetSingle_thenReturnTask() {

	// given
	final Long taskId = 200L;
	final Task task = Task.of(taskId, "Task 1");

	given(mockRepository.findById(taskId))
		.willReturn(Optional.of(task));

	// when
	final Task result = service.getSingle(taskId);

	// then
	assertThat(result)
		.isNotNull()
		.isEqualTo(task);
    }

    @Test
    public void givenNonExistingTask_whenGetSingle_thenThrowsException() {

	// given
	final Long taskId = 200L;

	// when
	final Throwable result = catchThrowable(() -> service.getSingle(taskId));

	// then
	assertThat(result)
		.isNotNull()
		.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullParameter_whenGetSingle_thenThrowException() {

	// given
	final Long taskId = null;

	// when
	final Throwable result = catchThrowable(() -> service.getSingle(taskId));

	// then
	assertThat(result)
		.isNotNull()
		.isInstanceOf(NullPointerException.class);

    }

    // create

    @Test
    public void givenNonExistingTask_whenCreate_thenReturnSaved() {

	// given
	given(mockRepository.save(any()))
		.will(returnsFirstArg());

	// when
	final Task result = service.create(Task.of("Task 1"));

	// then
	assertThat(result)
		.isNotNull();
    }

    @Test
    public void givenNullParameter_whenCreate_thenThrowException() {

	// given
	final Task task = null;

	// when
	final Throwable result1 = catchThrowable(() -> service.create(task));

	// then
	assertThat(result1)
		.isNotNull()
		.isInstanceOf(NullPointerException.class);
    }

    // update

    @Test
    public void givenExistingTask_whenUpdate_thenReturnSaved() {

	// given
	final Long taskId = 200L;
	final Task updatingTask = Task.of("UPDATED");
	final Task updatedTask = Task.copyOf(taskId, updatingTask);

	given(mockRepository.save(updatingTask))
		.willReturn(updatedTask);

	final Task origin = Task.of(taskId, "Origin");

	given(mockRepository.findById(taskId))
		.willReturn(Optional.of(origin));

	// when
	final Task result = service.update(taskId, updatingTask);

	// then
	assertThat(result)
		.isNotNull()
		.isEqualTo(updatedTask);
    }

    @Test
    public void givenNonExistingTask_whenUpdate_thenThrowException() {

	// given
	final Long taskId = 200L;
	final Task updatingTask = Task.of("UPDATED");

	// when
	final Throwable result = catchThrowable(() -> service.update(taskId, updatingTask));

	// then
	assertThat(result)
		.isNotNull()
		.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullParameter_whenUpdate_thenThrowException() {

	{
	    // given
	    final Long taskId = 1L;
	    final Task task = null;

	    // when
	    final Throwable result = catchThrowable(() -> service.update(taskId, task));

	    // then
	    assertThat(result)
		    .isNotNull()
		    .isInstanceOf(NullPointerException.class);
	}

	{
	    // given
	    final Long taskId = null;
	    final Task org = Task.of("");

	    // when
	    final Throwable result = catchThrowable(() -> service.update(taskId, org));

	    // then
	    assertThat(result)
		    .isNotNull()
		    .isInstanceOf(NullPointerException.class);
	}
    }
    // delete

    @Test
    public void givenExistingTask_whenDelete_thenDeleteAndFinish() {

	// given
	final Long taskId = 200L;
	final Task origin = Task.of(taskId, "Origin");

	given(mockRepository.findById(taskId))
		.willReturn(Optional.of(origin));

	// when
	service.delete(taskId);

	// then
	then(mockRepository)
		.should()
		.delete(origin);
    }

    @Test
    public void givenNonExistingTask_whenDelete_thenThrowException() {

	// given
	final Long taskId = 200L;

	// when
	final Throwable result = catchThrowable(() -> service.delete(taskId));

	// then
	assertThat(result)
		.isNotNull()
		.isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void givenNullParameter_whenDelete_thenThrowException() {

	// given
	final Long taskId = null;

	// when
	final Throwable result1 = catchThrowable(() -> service.delete(taskId));

	// then
	assertThat(result1)
		.isNotNull()
		.isInstanceOf(NullPointerException.class);
    }
}