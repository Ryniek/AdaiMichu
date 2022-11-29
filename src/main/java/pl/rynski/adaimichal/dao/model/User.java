package pl.rynski.adaimichal.dao.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
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
	
	@Column(name = "notification_send", nullable = false)
	private Boolean notificationSend = false;
	
	@OneToMany(mappedBy = "creator", orphanRemoval = true)
	private Set<Task> createdTasks = new HashSet<>();
	
	@OneToMany(mappedBy = "drawnUser")
	private Set<Task> drawnTasks = new HashSet<>();
	
	@Column(name = "assigned_user_id")
	private Long assignedUserId;
	
	@OneToOne(mappedBy = "user")
	private PasswordResetToken passwordResetToken;
	
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Set<UserRole> roles = new HashSet<>();
}
