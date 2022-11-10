package pl.rynski.adaimichal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findAllByCreator(User creator);
	Optional<Task> findByIdAndCreatorAndIsStartedFalse(Long id, User creator);
}
