package online.entreprenly.platform.inventory.application.internal.commandservices;

import online.entreprenly.platform.inventory.application.commandservices.UnitLotCommandService;
import online.entreprenly.platform.inventory.domain.model.aggregates.UnitLot;
import online.entreprenly.platform.inventory.domain.model.commands.CreateUnitLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteUnitLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.UpdateUnitLotCommand;
import online.entreprenly.platform.inventory.domain.repositories.UnitLotRepository;
import online.entreprenly.platform.inventory.domain.repositories.UnitProductRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Unit lot command service implementation. All operations are scoped to the owner account.
 */
@Service
public class UnitLotCommandServiceImpl implements UnitLotCommandService {

    private final UnitLotRepository unitLotRepository;
    private final UnitProductRepository unitProductRepository;

    public UnitLotCommandServiceImpl(UnitLotRepository unitLotRepository,
                                     UnitProductRepository unitProductRepository) {
        this.unitLotRepository = unitLotRepository;
        this.unitProductRepository = unitProductRepository;
    }

    @Override
    public Result<UnitLot, ApplicationError> handle(CreateUnitLotCommand command) {
        if (command.ownerEmail() == null || command.ownerEmail().isBlank()) {
            return Result.failure(ApplicationError.validationError("owner", "An authenticated owner is required"));
        }
        if (command.productId() == null) {
            return Result.failure(ApplicationError.validationError("productId", "A unit product is required"));
        }
        if (!unitProductRepository.existsByIdAndOwnerEmail(command.productId(), command.ownerEmail())) {
            return Result.failure(ApplicationError.notFound("UnitProduct", String.valueOf(command.productId())));
        }
        if (command.quantity() < 0) {
            return Result.failure(ApplicationError.validationError("quantity", "Quantity cannot be negative"));
        }
        var unitLot = new UnitLot(command.ownerEmail(), command.productId(), command.codeQR(), command.entryDate(),
                command.quantity(), command.expiryDate());
        return Result.success(unitLotRepository.save(unitLot));
    }

    @Override
    public Result<UnitLot, ApplicationError> handle(UpdateUnitLotCommand command) {
        if (command.productId() != null
                && !unitProductRepository.existsByIdAndOwnerEmail(command.productId(), command.ownerEmail())) {
            return Result.failure(ApplicationError.notFound("UnitProduct", String.valueOf(command.productId())));
        }
        return unitLotRepository.findByIdAndOwnerEmail(command.unitLotId(), command.ownerEmail())
                .<Result<UnitLot, ApplicationError>>map(unitLot -> {
                    unitLot.updateInfo(command.productId(), command.codeQR(), command.entryDate(),
                            command.quantity(), command.expiryDate());
                    return Result.success(unitLotRepository.save(unitLot));
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("UnitLot", String.valueOf(command.unitLotId()))));
    }

    @Override
    public Result<Long, ApplicationError> handle(DeleteUnitLotCommand command) {
        if (!unitLotRepository.existsByIdAndOwnerEmail(command.unitLotId(), command.ownerEmail())) {
            return Result.failure(ApplicationError.notFound("UnitLot", String.valueOf(command.unitLotId())));
        }
        unitLotRepository.deleteById(command.unitLotId());
        return Result.success(command.unitLotId());
    }
}
