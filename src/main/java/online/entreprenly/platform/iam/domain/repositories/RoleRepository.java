package online.entreprenly.platform.iam.domain.repositories;

import online.entreprenly.platform.iam.domain.model.entities.Role;
import online.entreprenly.platform.iam.domain.model.valueobjects.Roles;

import java.util.List;
import java.util.Optional;

/**
 * IAM role repository port.
 */
public interface RoleRepository {
    Optional<Role> findByName(Roles name);

    List<Role> findAll();

    Role save(Role role);

    boolean existsByName(Roles name);
}

