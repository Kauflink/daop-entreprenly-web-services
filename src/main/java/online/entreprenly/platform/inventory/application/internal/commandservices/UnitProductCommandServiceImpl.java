package online.entreprenly.platform.inventory.application.internal.commandservices;

import online.entreprenly.platform.inventory.application.commandservices.UnitProductCommandService;
import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.domain.model.commands.CreateUnitProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteUnitProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.UpdateUnitProductCommand;
import online.entreprenly.platform.inventory.domain.repositories.UnitProductRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Unit product command service implementation.
 */
@Service
public class UnitProductCommandServiceImpl implements UnitProductCommandService {

    private final UnitProductRepository unitProductRepository;

    public UnitProductCommandServiceImpl(UnitProductRepository unitProductRepository) {
        this.unitProductRepository = unitProductRepository;
    }

    @Override
    public Result<UnitProduct, ApplicationError> handle(CreateUnitProductCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            return Result.failure(ApplicationError.validationError("name", "A product name is required"));
        }
        if (command.codeQR() != null && !command.codeQR().isBlank()
                && unitProductRepository.existsByCodeQR(command.codeQR())) {
            return Result.failure(ApplicationError.conflict("UnitProduct",
                    "A unit product already exists with this QR code"));
        }
        var unitProduct = new UnitProduct(command.name(), command.description(), command.codeQR(),
                command.price(), command.weightGrams(), command.brand());
        return Result.success(unitProductRepository.save(unitProduct));
    }

    @Override
    public Result<UnitProduct, ApplicationError> handle(UpdateUnitProductCommand command) {
        return unitProductRepository.findById(command.unitProductId())
                .<Result<UnitProduct, ApplicationError>>map(unitProduct -> {
                    unitProduct.updateInfo(command.name(), command.description(), command.codeQR(),
                            command.price(), command.weightGrams(), command.brand());
                    return Result.success(unitProductRepository.save(unitProduct));
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("UnitProduct", String.valueOf(command.unitProductId()))));
    }

    @Override
    public Result<Long, ApplicationError> handle(DeleteUnitProductCommand command) {
        if (!unitProductRepository.existsById(command.unitProductId())) {
            return Result.failure(ApplicationError.notFound("UnitProduct", String.valueOf(command.unitProductId())));
        }
        unitProductRepository.deleteById(command.unitProductId());
        return Result.success(command.unitProductId());
    }
}
