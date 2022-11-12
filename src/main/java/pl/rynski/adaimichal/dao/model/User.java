package pl.rynski.adaimichal.dao.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(250)")
	private String name;
	
	@Column(name = "email", unique = true, columnDefinition = "VARCHAR(250)")
	private String email;

	@Column(name = "password", nullable = false)
	private String password;
	
	@Column(name = "last_date_of_drawing_task", columnDefinition = "TIMESTAMP")
	private LocalDateTime lastDateOfDrawingTask;
	
	@OneToMany(mappedBy = "creator", orphanRemoval = true)
	private Set<Task> createdTasks = new HashSet<>();
	
	@OneToMany(mappedBy = "drawnUser")
	private Set<Task> drawnTasks = new HashSet<>();
}
