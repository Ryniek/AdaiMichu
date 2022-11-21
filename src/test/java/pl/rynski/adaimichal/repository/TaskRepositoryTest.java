package pl.rynski.adaimichal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import pl.rynski.adaimichal.dao.model.Task;
import pl.rynski.adaimichal.dao.model.User;

@DataJpaTest
@Sql("/testdata.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class TaskRepositoryTest {
	
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserRepository userRepository;

	@Test
	void shouldFindByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse() {
		//given
		//when
		User user = userRepository.findById(1L).get();
		Optional<Task> task = taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(1L, user);
		//then
		assertTrue(task.isPresent());
		assertFalse(task.get().getIsStarted());
		assertFalse(task.get().getIsFinished());
	}
	
	@Test
	void shouldNotFindByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse() {
		//given
		//when
		User user = userRepository.findById(2L).get();
		Optional<Task> task = taskRepository.findByIdAndCreatorAndIsStartedFalseAndIsFinishedFalse(1L, user);
		//then
		assertFalse(task.isPresent());
	}

	@Test
	void shouldFindAllByIsStartedTrueAndIsFinishedFalse() {
		//when
		List<Task> tasks = taskRepository.findAllByIsStartedTrueAndIsFinishedFalse(Sort.by("expirationDate").ascending());
		//then
		assertEquals(2, tasks.size());
		assertTrue(tasks.get(0).getIsStarted());
		assertFalse(tasks.get(0).getIsFinished());
	}

	@Test
	void shouldFindAllByIsStartedFalseAndIsHiddenFalse() {
		//when
		List<Task> tasks = taskRepository.findAllByIsStartedFalseAndIsHiddenFalseAndIsFinishedFalse();
		//then
		assertEquals(4, tasks.size());
		assertFalse(tasks.get(0).getIsStarted());
		assertFalse(tasks.get(1).getIsHidden());
		assertFalse(tasks.get(1).getIsFinished());
	}

	@Test
	void shouldFindByIdAndDrawnUser() {
		User user = userRepository.findById(1L).get();
		Optional<Task> task = taskRepository.findByIdAndDrawnUser(4L, user);
		
		assertTrue(task.isPresent());
		assertTrue(task.get().getDrawnUser().getId() == 1L);
		assertTrue(task.get().getId() == 4L);
	}

	@Test
	void shouldNotFindByIdAndDrawnUser() {
		User user = userRepository.findById(2L).get();
		Optional<Task> task = taskRepository.findByIdAndDrawnUser(4L, user);
		
		assertFalse(task.isPresent());
	}
	
	@Test
	void shouldFindAllFinishedSortedByFinishDate() {
		List<Task> tasks = taskRepository.shouldFindAllFinishedSortedByFinishDate();
		
		assertEquals(2, tasks.size());
		assertThat(tasks.get(0).getFinishDate()).isAfter(tasks.get(1).getFinishDate());
	}
}
