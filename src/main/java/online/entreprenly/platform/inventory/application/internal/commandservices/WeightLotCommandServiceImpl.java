package online.entreprenly.platform.inventory.application.internal.commandservices;

import online.entreprenly.platform.inventory.application.commandservices.WeightLotCommandService;
import online.entreprenly.platform.inventory.domain.model.aggregates.WeightLot;
import online.entreprenly.platform.inventory.domain.model.commands.CreateWeightLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteWeightLotCommand;
import online.entreprenly.platform.inventory.domain.model.commands.UpdateWeightLotCommand;
import online.entreprenly.platform.inventory.domain.repositories.WeightLotRepository;
import online.entreprenly.platform.inventory.domain.repositories.WeightProductRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Weight lot command service implementation.
 */
@Service
public class WeightLotCommandServiceImpl implements WeightLotCommandService {

    private final WeightLotRepository weightLotRepository;
    private final WeightProductRepository weightProductRepository;

    public WeightLotCommandServiceImpl(WeightLotRepository weightLotRepository,
                                       WeightProductRepository weightProductRepository) {
        this.weightLotRepository = weightLotRepository;
        this.weightProductRepository = weightProductRepository;
    }

    @Override
    public Result<WeightLot, ApplicationError> handle(CreateWeightLotCommand command) {
        if (command.productId() == null) {
            return Result.failure(ApplicationError.validationError("productId", "A weight product is required"));
        }
        if (!weightProductRepository.existsById(command.productId())) {
            return Result.failure(ApplicationError.notFound("WeightProduct", String.valueOf(command.productId())));
        }
        if (command.quantityKg() < 0) {
            return Result.failure(ApplicationError.validationError("quantityKg", "Quantity cannot be negative"));
        }
        var weightLot = new WeightLot(command.productId(), command.codeQR(), command.entryDate(),
                command.quantityKg());
        return Result.success(weightLotRepository.save(weightLot));
    }

    @Override
    public Result<WeightLot, ApplicationError> handle(UpdateWeightLotCommand command) {
        if (command.productId() != null && !weightProductRepository.existsById(command.productId())) {
            return Result.failure(ApplicationError.notFound("WeightProduct", String.valueOf(command.productId())));
        }
        return weightLotRepository.findById(command.weightLotId())
                .<Result<WeightLot, ApplicationError>>map(weightLot -> {
                    weightLot.updateInfo(command.productId(), command.codeQR(), command.entryDate(),
                            command.quantityKg());
                    return Result.success(weightLotRepository.save(weightLot));
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("WeightLot", String.valueOf(command.weightLotId()))));
    }

    @Override
    public Result<Long, ApplicationError> handle(DeleteWeightLotCommand command) {
        if (!weightLotRepository.existsById(command.weightLotId())) {
            return Result.failure(ApplicationError.notFound("WeightLot", String.valueOf(command.weightLotId())));
        }
        weightLotRepository.deleteById(command.weightLotId());
        return Result.success(command.weightLotId());
    }
}
