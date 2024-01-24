package prueba14.sqldriver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba14.sqldriver.entities.Player;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player,Long> {

    Optional<Player>getPlayerByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
