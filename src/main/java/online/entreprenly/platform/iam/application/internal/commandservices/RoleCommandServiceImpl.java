package online.entreprenly.platform.iam.application.internal.commandservices;

import online.entreprenly.platform.iam.application.commandservices.RoleCommandService;
import online.entreprenly.platform.iam.domain.model.commands.SeedRolesCommand;
import online.entreprenly.platform.iam.domain.model.entities.Role;
import online.entreprenly.platform.iam.domain.model.valueobjects.Roles;
import online.entreprenly.platform.iam.domain.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * Implementation of {@link RoleCommandService} to handle {@link SeedRolesCommand}.
 */
@Service
public class RoleCommandServiceImpl implements RoleCommandService {

    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void handle(SeedRolesCommand command) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(Roles.valueOf(role.name())));
            }
        });
    }
}
