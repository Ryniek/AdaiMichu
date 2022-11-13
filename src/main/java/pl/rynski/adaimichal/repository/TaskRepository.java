package pl.rynski.adaimichal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findAllByCreator(User creator, Sort sort);
	Optional<Task> findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(Long id, User creator);
	List<Task> findAllByIsStartedTrueAndIsFinishedFalse(Sort sort);
	List<Task> findAllByIsStartedFalseAndIsHiddenFalse();
	Optional<Task> findByIdAndDrawnUser(Long id, User drawnUser);
	List<Task> findAllByIsFinishedTrue(Sort sort);
}
