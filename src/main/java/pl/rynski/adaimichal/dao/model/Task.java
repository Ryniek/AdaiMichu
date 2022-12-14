package pl.rynski.adaimichal.dao.model;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tasks")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false, columnDefinition = "VARCHAR(250)")
	private String name;
	
	@Column(name = "comment", columnDefinition = "VARCHAR(250)")
	private String comment;
	
    @Column(name = "is_finished", nullable = false)
    private Boolean isFinished = false;
    
    @Column(name = "is_started", nullable = false)
    private Boolean isStarted = false;
    
    //Hidden task cannot be drawn
    @Column(name = "is_hidden", nullable = false)
    private Boolean isHidden = true;
    
    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime creationDate;
    
    @Column(name = "days_to_use", nullable = false)
    private Long daysToUse;
    
    @Column(name = "expiration_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime expirationDate;
    
    @Column(name = "finish_date", columnDefinition = "TIMESTAMP")
    private LocalDateTime finishDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false)
    private User creator;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drawn_user_id", referencedColumnName = "id")
    private User drawnUser;
}
