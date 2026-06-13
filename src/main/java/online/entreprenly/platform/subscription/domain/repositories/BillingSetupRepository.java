package online.entreprenly.platform.subscription.domain.repositories;

import online.entreprenly.platform.subscription.domain.model.aggregates.BillingSetup;

import java.util.Optional;

/**
 * Billing setup repository port.
 */
public interface BillingSetupRepository {
    Optional<BillingSetup> findByUserId(Long userId);

    BillingSetup save(BillingSetup billingSetup);
}
