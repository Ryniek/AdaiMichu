package pl.rynski.adaimichal.dao.model;


import java.time.LocalDateTime;

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
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false, columnDefinition = "VARCHAR(250)")
	private String name;
	
	@Column(name = "comment", columnDefinition = "VARCHAR(250)")
	private String comment;
	
    @Column(name = "hidden", nullable = false)
    private Boolean done;
    
    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;
    
    @Column(name = "done_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime doneDate;
    
    
}
