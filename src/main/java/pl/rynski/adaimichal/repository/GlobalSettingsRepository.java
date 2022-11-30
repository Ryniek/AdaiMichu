package pl.rynski.adaimichal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.rynski.adaimichal.dao.model.GlobalSettings;

@Repository
public interface GlobalSettingsRepository extends JpaRepository<GlobalSettings, Long> {

}
