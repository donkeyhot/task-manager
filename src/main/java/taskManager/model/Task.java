package taskManager.model;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(indexes = { @Index(name = "IDX1_COMPLETED", columnList = "COMPLETED") })
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    // PROPERTIES

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = Access.READ_ONLY)
    private Long id;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @Basic
    private String name;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    @Basic
    private String description;

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    @Basic
    @JsonProperty(access = Access.READ_ONLY)
    private Instant created;

    public Instant getCreated() {
	return created;
    }

    public void setCreated(Instant created) {
	this.created = created;
    }

    @Basic
    @Column(name = "COMPLETED")
    @JsonProperty(access = Access.READ_ONLY)
    private Instant completed;

    public Instant getCompleted() {
	return completed;
    }

    public void setCompleted(Instant completed) {
	this.completed = completed;
    }

    public boolean isPending() {
	return completed == null;
    }

    // CONSTRUCTORS

    public Task() {
    }

    // hC/eq/toSt

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + ((description == null) ? 0 : description.hashCode());
	result = prime * result + ((created == null) ? 0 : created.hashCode());
	result = prime * result + ((completed == null) ? 0 : completed.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof Task))
	    return false;

	final Task other = (Task) obj;

	// id
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;

	// name
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;

	// description
	if (description == null) {
	    if (other.description != null)
		return false;
	} else if (!description.equals(other.description))
	    return false;

	// created
	if (created == null) {
	    if (other.created != null)
		return false;
	} else if (!created.equals(other.created))
	    return false;

	// completed
	if (completed == null) {
	    if (other.completed != null)
		return false;
	} else if (!completed.equals(other.completed))
	    return false;

	return true;
    }

    @Override
    public String toString() {
	return String.format("Task[id=%1$d, name=%2$s, description=%3$s, created=%4$s, completed=%5$s]",
		id, // 1
		name != null ? ("'" + name + "'") : name, // 2
		name != null ? ("'" + description + "'") : description, // 3
		created, // 4
		completed // 5
	);
    }

    public static void main(String[] args) {
	System.out.println(new Task());
    }
}
