package pl.rynski.adaimichal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;

import pl.rynski.adaimichal.dao.model.PasswordResetToken;

@DataJpaTest
@Sql("/testdata.sql")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class PasswordResetTokenRepositoryTest {
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Test
	void shouldReturnByToken() {
		//given
		//when
		PasswordResetToken result = passwordResetTokenRepository.findByToken("token").get();
		//then
		assertEquals(result.getToken(), "token");
	}

}
