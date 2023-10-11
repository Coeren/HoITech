package elliot.hoi.tech.TextMapping;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TextMappingRepository extends JpaRepository<TextMapping, String> {
    Optional<TextMapping> findBySid(String id);
}
