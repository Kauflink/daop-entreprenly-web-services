package online.entreprenly.platform.inventory.application.internal.commandservices;

import online.entreprenly.platform.inventory.application.commandservices.WeightProductCommandService;
import online.entreprenly.platform.inventory.domain.model.aggregates.WeightProduct;
import online.entreprenly.platform.inventory.domain.model.commands.CreateWeightProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.DeleteWeightProductCommand;
import online.entreprenly.platform.inventory.domain.model.commands.UpdateWeightProductCommand;
import online.entreprenly.platform.inventory.domain.repositories.WeightProductRepository;
import online.entreprenly.platform.shared.application.result.ApplicationError;
import online.entreprenly.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

/**
 * Weight product command service implementation.
 */
@Service
public class WeightProductCommandServiceImpl implements WeightProductCommandService {

    private final WeightProductRepository weightProductRepository;

    public WeightProductCommandServiceImpl(WeightProductRepository weightProductRepository) {
        this.weightProductRepository = weightProductRepository;
    }

    @Override
    public Result<WeightProduct, ApplicationError> handle(CreateWeightProductCommand command) {
        if (command.name() == null || command.name().isBlank()) {
            return Result.failure(ApplicationError.validationError("name", "A product name is required"));
        }
        if (command.codeQR() != null && !command.codeQR().isBlank()
                && weightProductRepository.existsByCodeQR(command.codeQR())) {
            return Result.failure(ApplicationError.conflict("WeightProduct",
                    "A weight product already exists with this QR code"));
        }
        var weightProduct = new WeightProduct(command.name(), command.description(), command.codeQR(),
                command.pricePerKg());
        return Result.success(weightProductRepository.save(weightProduct));
    }

    @Override
    public Result<WeightProduct, ApplicationError> handle(UpdateWeightProductCommand command) {
        return weightProductRepository.findById(command.weightProductId())
                .<Result<WeightProduct, ApplicationError>>map(weightProduct -> {
                    weightProduct.updateInfo(command.name(), command.description(), command.codeQR(),
                            command.pricePerKg());
                    return Result.success(weightProductRepository.save(weightProduct));
                })
                .orElseGet(() -> Result.failure(
                        ApplicationError.notFound("WeightProduct", String.valueOf(command.weightProductId()))));
    }

    @Override
    public Result<Long, ApplicationError> handle(DeleteWeightProductCommand command) {
        if (!weightProductRepository.existsById(command.weightProductId())) {
            return Result.failure(ApplicationError.notFound("WeightProduct",
                    String.valueOf(command.weightProductId())));
        }
        weightProductRepository.deleteById(command.weightProductId());
        return Result.success(command.weightProductId());
    }
}
