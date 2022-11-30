package pl.rynski.adaimichal.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "global_settings")
public class GlobalSettings {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "minutes_between_drawing", nullable = false)
	private Long minutesBetweenDrawing;
	
	@Column(name = "reset_password_token_validity", nullable = false)
	private Long resetPasswordTokenValidity;
}
