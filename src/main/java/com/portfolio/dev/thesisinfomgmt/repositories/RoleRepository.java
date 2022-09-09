package com.portfolio.dev.thesisinfomgmt.repositories;

import com.portfolio.dev.thesisinfomgmt.entities.Role;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

  Optional<Role> findFirstByNameEqualsIgnoreCase(String name);
}
