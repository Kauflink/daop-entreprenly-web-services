package online.entreprenly.platform.iam.application.internal.queryservices;

import online.entreprenly.platform.iam.application.queryservices.RoleQueryService;
import online.entreprenly.platform.iam.domain.model.entities.Role;
import online.entreprenly.platform.iam.domain.model.queries.GetAllRolesQuery;
import online.entreprenly.platform.iam.domain.model.queries.GetRoleByNameQuery;
import online.entreprenly.platform.iam.domain.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service that resolves IAM role read queries.
 */
@Service
public class RoleQueryServiceImpl implements RoleQueryService {
    private final RoleRepository roleRepository;

    public RoleQueryServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> handle(GetAllRolesQuery query) {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> handle(GetRoleByNameQuery query) {
        return roleRepository.findByName(query.name());
    }
}
