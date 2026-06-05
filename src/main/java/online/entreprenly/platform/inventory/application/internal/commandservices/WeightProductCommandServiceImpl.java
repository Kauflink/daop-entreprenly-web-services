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
 * Weight product command service implementation. All operations are scoped to the owner account.
 */
@Service
public class WeightProductCommandServiceImpl implements WeightProductCommandService {

    private final WeightProductRepository weightProductRepository;

    public WeightProductCommandServiceImpl(WeightProductRepository weightProductRepository) {
        this.weightProductRepository = weightProductRepository;
    }

    @Override
    public Result<WeightProduct, ApplicationError> handle(CreateWeightProductCommand command) {
        if (command.ownerEmail() == null || command.ownerEmail().isBlank()) {
            return Result.failure(ApplicationError.validationError("owner", "An authenticated owner is required"));
        }
        if (command.name() == null || command.name().isBlank()) {
            return Result.failure(ApplicationError.validationError("name", "A product name is required"));
        }
        if (command.codeQR() != null && !command.codeQR().isBlank()
                && weightProductRepository.existsByCodeQRAndOwnerEmail(command.codeQR(), command.ownerEmail())) {
            return Result.failure(ApplicationError.conflict("WeightProduct",
                    "A weight product already exists with this QR code"));
        }
        var weightProduct = new WeightProduct(command.ownerEmail(), command.name(), command.description(),
                command.codeQR(), command.pricePerKg());
        return Result.success(weightProductRepository.save(weightProduct));
    }

    @Override
    public Result<WeightProduct, ApplicationError> handle(UpdateWeightProductCommand command) {
        return weightProductRepository.findByIdAndOwnerEmail(command.weightProductId(), command.ownerEmail())
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
        if (!weightProductRepository.existsByIdAndOwnerEmail(command.weightProductId(), command.ownerEmail())) {
            return Result.failure(ApplicationError.notFound("WeightProduct",
                    String.valueOf(command.weightProductId())));
        }
        weightProductRepository.deleteById(command.weightProductId());
        return Result.success(command.weightProductId());
    }
}
