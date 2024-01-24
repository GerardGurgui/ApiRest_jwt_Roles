package prueba14.sqldriver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import prueba14.sqldriver.entities.Dice;

@Repository
public interface DiceRepository extends JpaRepository<Dice, Long> {
}
