package pl.rynski.adaimichal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pl.rynski.adaimichal.dao.model.User;

@DataJpaTest
class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;

	@Test
	void shouldReturnUserByName() {
		//given
		User user = new User();
		String testName = "testName";
		user.setName(testName);
		user.setPassword("test");
		userRepository.save(user);
		//when
		User result = userRepository.findByName(testName).get();
		//then
		assertEquals(result.getName(), testName);
	}

}
