package livelance.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import livelance.app.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
