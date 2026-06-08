package online.entreprenly.platform.inventory.application.internal.commandservices;

import online.entreprenly.platform.inventory.application.commandservices.UnitProductCommandService;
import online.entreprenly.platform.inventory.domain.model.aggregates.UnitProduct;
import online.entreprenly.platform.inventory.domain.model.commands.CreateUnitProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteUnitProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.UpdateUnitProductCommand;
import online.entreprenly.platform.inventory.domain.repositories.UnitLotRepository;
import online.entreprenly.platform.inventory.domain.repositories.UnitProductRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit product command service implementation. All operations are scoped to the owner account.
 */
@Service
public class UnitProductCommandServiceImpl implements UnitProductCommandService {

    private final UnitProductRepository unitProductRepository;
    private final UnitLotRepository unitLotRepository;

    public UnitProductCommandServiceImpl(UnitProductRepository unitProductRepository,
                                         UnitLotRepository unitLotRepository) {
        this.unitProductRepository = unitProductRepository;
        this.unitLotRepository = unitLotRepository;
    }

    @Override
    public Result<UnitProduct, ApplicationError> handle(CreateUnitProductCommand command) {
        if (command.ownerEmail() == null || command.ownerEmail().isBlank()) {
            return Result.failure(ApplicationError.validationError("owner", "An authenticated owner is required"));
        }
        if (command.name() == null || command.name().isBlank()) {
            return Result.failure(ApplicationError.validationError("name", "A product name is required"));
        }
        if (command.codeQR() != null && !command.codeQR().isBlank()
                && unitProductRepository.existsByCodeQRAndOwnerEmail(command.codeQR(), command.ownerEmail())) {
            return Result.failure(ApplicationError.conflict("UnitProduct",
                    "A unit product already exists with this QR code"));
        }
        var unitProduct = new UnitProduct(command.ownerEmail(), command.name(), command.description(),
                command.codeQR(), command.price(), command.weightGrams(), command.brand());
        return Result.success(unitProductRepository.save(unitProduct));
    }

    @Override
    public Result<UnitProduct, ApplicationError> handle(UpdateUnitProductCommand command) {
        return unitProductRepository.findByIdAndOwnerEmail(command.unitProductId(), command.ownerEmail())
                .<Result<UnitProduct, ApplicationError>>map(unitProduct -> {
                    unitProduct.updateInfo(command.name(), command.description(), command.codeQR(),
                            command.price(), command.weightGrams(), command.brand());
                    return Result.success(unitProductRepository.save(unitProduct));
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("UnitProduct", String.valueOf(command.unitProductId()))));
    }

    @Override
    @Transactional
    public Result<Long, ApplicationError> handle(DeleteUnitProductCommand command) {
        if (!unitProductRepository.existsByIdAndOwnerEmail(command.unitProductId(), command.ownerEmail())) {
            return Result.failure(ApplicationError.notFound("UnitProduct", String.valueOf(command.unitProductId())));
        }
        unitLotRepository.deleteByProductIdAndOwnerEmail(command.unitProductId(), command.ownerEmail());
        unitProductRepository.deleteById(command.unitProductId());
        return Result.success(command.unitProductId());
    }
}
