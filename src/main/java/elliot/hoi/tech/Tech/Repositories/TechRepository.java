package elliot.hoi.tech.Tech.Repositories;

import elliot.hoi.tech.Tech.TechBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechRepository extends JpaRepository<TechBase, Integer> {
}
