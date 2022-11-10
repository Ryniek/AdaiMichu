package pl.rynski.adaimichal.dao.model;


import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	
    @Column(name = "is_done", nullable = false)
    private Boolean isDone = false;
    
    @Column(name = "is_started", nullable = false)
    private Boolean isStarted = false;
    
    @Column(name = "is_hidden", nullable = false)
    private Boolean isHidden = true;
    
    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;
    
    @Column(name = "expiration_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime expirationDate;
    
    @Column(name = "done_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime doneDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User creator;
}
