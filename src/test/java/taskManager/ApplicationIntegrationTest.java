package taskManager;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import taskManager.model.Task;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIntegrationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void overallProcess() {

	// store task

	Task o;
	{
	    final Task templ = Task.of("task");

	    final ResponseEntity<Task> response = restTemplate.postForEntity("/tasks", templ,
		    Task.class);

	    assertThat(response.getStatusCode())
		    .isEqualTo(HttpStatus.CREATED);

	    final Task test = response.getBody();

	    assertThat(test)
		    .isNotNull();

	    assertThat(test.getId())
		    .isNotNull();

	    assertThat(test.getName())
		    .isNotNull()
		    .isEqualTo(templ.getName());

	    o = test;
	}

	// update task

	{
	    final Task templ = Task.copyOf(o);
	    templ.setName(o.getName() + " has changed");

	    final ResponseEntity<Task> response = restTemplate.exchange("/tasks/{taskId}",
		    HttpMethod.PUT,
		    new HttpEntity<>(templ),
		    Task.class,
		    o.getId());

	    assertThat(response.getStatusCode())
		    .isEqualTo(HttpStatus.OK);

	    final Task test = response.getBody();

	    assertThat(test)
		    .isNotNull();

	    assertThat(test.getId())
		    .isNotNull()
		    .isEqualTo(templ.getId());

	    assertThat(test.getName())
		    .isNotNull()
		    .isEqualTo(templ.getName());

	    o = test;
	}

	// get all tasks

	{
	    final ResponseEntity<Task[]> response = restTemplate.getForEntity("/tasks", Task[].class);

	    assertThat(response.getStatusCode())
		    .isEqualTo(HttpStatus.OK);

	    final Task[] test = response.getBody();

	    assertThat(test)
		    .isNotNull();

	    assertThat(test)
		    .isNotNull()
		    .extracting("id", "name")
		    .hasSize(1)
		    .containsExactly(tuple(o.getId(), o.getName()));
	}

	// get single task

	{
	    final ResponseEntity<Task> response = restTemplate.getForEntity("/tasks/{taskId}",
		    Task.class, o.getId());

	    assertThat(response.getStatusCode())
		    .isEqualTo(HttpStatus.OK);

	    final Task test = response.getBody();

	    assertThat(test)
		    .isNotNull();

	    assertThat(test.getId())
		    .isNotNull()
		    .isEqualTo(o.getId());

	    assertThat(test.getName())
		    .isNotNull()
		    .isEqualTo(o.getName());
	}

	// delete task

	{
	    final ResponseEntity<?> response = restTemplate.exchange("/tasks/{taskId}",
		    HttpMethod.DELETE,
		    null,
		    Object.class,
		    o.getId());

	    assertThat(response.getStatusCode())
		    .isEqualTo(HttpStatus.NO_CONTENT);

	    assertThat(restTemplate.getForEntity("/tasks/{taskId}",
		    Object.class,
		    o.getId()).getStatusCode())
			    .isEqualTo(HttpStatus.NOT_FOUND);

	    o = null;
	}
    }
}
