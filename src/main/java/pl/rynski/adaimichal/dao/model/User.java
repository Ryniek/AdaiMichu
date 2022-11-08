package pl.rynski.adaimichal.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
