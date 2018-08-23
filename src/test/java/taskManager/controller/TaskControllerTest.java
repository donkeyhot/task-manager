package taskManager.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import taskManager.model.Task;
import taskManager.services.TaskService;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    TaskService organizationService;

    // get tasks

    @Test
    public void givenCorretParameters_whenGetAll_thenReturnOkAndJsonStructure() throws Exception {

	// given
	final Task org1 = Task.of(200L, "Task 1");
	final Task org2 = Task.of(300L, "Task 2");

	given(organizationService.getAll())
		.willReturn(Arrays.asList(org1, org2));

	// when
	final ResultActions thenResult = mvc.perform(get("/tasks"));

	// then
	thenResult.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(2)))
		.andExpect(jsonPath("$[0].id", is(org1.getId().intValue())))
		.andExpect(jsonPath("$[0].name", is(org1.getName())))
		.andExpect(jsonPath("$[1].id", is(org2.getId().intValue())))
		.andExpect(jsonPath("$[1].name", is(org2.getName())))
		.andDo(print());

    }

    // get single org

    @Test
    public void givenCorrectParameters_whenGetSingle_thenReturnOkAndJsonStructure() throws Exception {

	// given
	final Long taskId = 200L;
	final Task org = Task.of(taskId, "Task 1");

	given(organizationService.getSingle(taskId))
		.willReturn(org);

	// when
	final ResultActions thenResult = mvc.perform(get("/tasks/{taskId}", taskId));

	// then
	thenResult.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.id", is(org.getId().intValue())))
		.andExpect(jsonPath("$.name", is(org.getName())))
		.andDo(print());
    }

    @Test
    public void givenWrongParameters_whenGetSingle_thenReturnNotFound() throws Exception {

	// given
	final Long taskId = 200L;

	given(organizationService.getSingle(taskId))
		.willThrow(IllegalArgumentException.class);

	// when
	final ResultActions thenResult = mvc.perform(get("/tasks/{taskId}", taskId));

	// then
	thenResult.andExpect(status().isNotFound())
		.andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenGetSingle_thenReturnBadRequest() throws Exception {

	// given
	final String taskId = "notANumber";

	// when
	final ResultActions thenResult = mvc.perform(get("/tasks/{taskId}", taskId));

	// then
	thenResult.andExpect(status().isBadRequest())
		.andDo(print());
    }

    // create org

    @Test
    public void givenCorrectParameters_whenCreate_thenReturnOkAndJsonStructure() throws Exception {

	// given
	final Task creatingTask = Task.of("Task 1");
	final String creatingTaskJson = "{\"name\": \"" + creatingTask.getName() + "\"}";
	final Long taskId = 1L;

	final Task createdTask = Task.of(taskId, creatingTask.getName());

	given(organizationService.create(creatingTask))
		.willReturn(createdTask);

	// when
	final ResultActions thenResult = mvc.perform(post("/tasks")
		.contentType(MediaType.APPLICATION_JSON_UTF8)
		.content(creatingTaskJson));

	// then
	thenResult.andExpect(status().isCreated())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.id", is(createdTask.getId().intValue())))
		.andExpect(jsonPath("$.name", is(createdTask.getName())))
		.andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenCreate_thenReturnBadRequest() throws Exception {

	// given

	// when
	final ResultActions thenResult = mvc.perform(post("/tasks"));

	// then
	thenResult.andExpect(status().isBadRequest())
		.andDo(print());
    }

    // update org

    @Test
    public void givenWrongParameters_whenUpdate_thenReturnNotFound() throws Exception {

	// given
	final Long taskId = 200L;
	final Task org = Task.of("UPDATED");
	final String updatingTask1Json = "{\"name\": \"UPDATED\"}";

	willThrow(IllegalArgumentException.class)
		.given(organizationService)
		.update(taskId, org);

	// when
	final ResultActions thenResult = mvc.perform(put("/tasks/{taskId}", taskId)
		.contentType(MediaType.APPLICATION_JSON_UTF8)
		.content(updatingTask1Json));

	// then
	thenResult.andExpect(status().isNotFound())
		.andDo(print());
    }

    @Test
    public void givenCorrectParameters_whenUpdate_thenReturnOkAndJsonStructure() throws Exception {

	// given
	final Long taskId = 200L;

	final Task updatingTask = Task.of("UPDATED");
	final String updatingTaskJson = "{\"name\": \"" + updatingTask.getName() + "\"}";

	final Task updatedTask = Task.of(taskId, updatingTask.getName());

	given(organizationService.update(taskId, updatingTask))
		.willReturn(updatedTask);

	// when
	final ResultActions thenResult = mvc.perform(put("/tasks/{taskId}", taskId)
		.contentType(MediaType.APPLICATION_JSON_UTF8)
		.content(updatingTaskJson));

	// then
	thenResult.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.id", is(updatedTask.getId().intValue())))
		.andExpect(jsonPath("$.name", is(updatedTask.getName())))
		.andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenUpdate_thenReturnBadRequest() throws Exception {

	{
	    // given
	    final String taskId = "notANumber";
	    final String updatingTaskJson = "{\"name\": \"UPDATED\"}";

	    // when
	    final ResultActions thenResult = mvc.perform(put("/tasks/{taskId}", taskId)
		    .contentType(MediaType.APPLICATION_JSON_UTF8)
		    .content(updatingTaskJson));

	    // then
	    thenResult.andExpect(status().isBadRequest())
		    .andDo(print());
	}

	{
	    // given
	    final Long taskId = 200L;
	    final String updatingTaskJson = "";

	    // when
	    final ResultActions thenResult = mvc.perform(put("/tasks/{taskId}", taskId)
		    .contentType(MediaType.APPLICATION_JSON_UTF8)
		    .content(updatingTaskJson));

	    // then
	    thenResult.andExpect(status().isBadRequest())
		    .andDo(print());
	}

	{
	    // given
	    final Long taskId = 200L;

	    // when
	    final ResultActions thenResult = mvc.perform(put("/tasks/{taskId}", taskId));

	    // then
	    thenResult.andExpect(status().isBadRequest())
		    .andDo(print());
	}
    }

    // delete org

    @Test
    public void givenCorrectParameters_whenDelete_thenReturnNoContent() throws Exception {

	// given
	final Long taskId = 200L;

	willDoNothing()
		.given(organizationService)
		.delete(taskId);

	// when
	final ResultActions thenResult = mvc.perform(delete("/tasks/{taskId}", taskId));

	// then
	then(organizationService)
		.should()
		.delete(taskId);

	thenResult.andExpect(status().isNoContent())
		.andDo(print());

    }

    @Test
    public void givenWrongParameters_whenDelete_thenReturnNotFound() throws Exception {

	// given
	final Long taskId = 200L;

	willThrow(IllegalArgumentException.class)
		.given(organizationService)
		.delete(taskId);

	// when
	final ResultActions thenResult = mvc.perform(delete("/tasks/{taskId}", taskId));

	// then
	thenResult.andExpect(status().isNotFound())
		.andDo(print());
    }

    @Test
    public void givenInvalidParameters_whenDelete_thenReturnBadRequest() throws Exception {

	// given
	final String taskId = "notANumber";

	// when
	final ResultActions thenResult = mvc.perform(delete("/tasks/{taskId}", taskId));

	// then
	thenResult.andExpect(status().isBadRequest())
		.andDo(print());
    }
}