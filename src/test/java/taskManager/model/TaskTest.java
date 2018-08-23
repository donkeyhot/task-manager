package taskManager.model;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class TaskTest {

    @Test
    public void hashCodeEqualsContractTest() {
	EqualsVerifier.forClass(Task.class)
		.verify();
    }
}
