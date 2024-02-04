package prueba14.sqldriver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prueba14.sqldriver.entities.Roles;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long>{

    Optional<Roles> findRoleByName(String name);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
            "FROM Player p JOIN p.roles r " +
            "WHERE r.name = :roleName")
    boolean existsByRoleName(@Param("roleName") String roleName);


}
