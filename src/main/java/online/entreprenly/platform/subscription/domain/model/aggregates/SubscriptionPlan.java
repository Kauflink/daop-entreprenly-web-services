package online.entreprenly.platform.subscription.domain.model.aggregates;

import online.entreprenly.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import online.entreprenly.platform.subscription.domain.model.valueobjects.BillingPeriod;
import online.entreprenly.platform.subscription.domain.model.valueobjects.Money;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Subscription plan aggregate root.
 */
@Getter
public class SubscriptionPlan extends AbstractDomainAggregateRoot<SubscriptionPlan> {

    @Setter
    private Long id;
    private String name;
    private String description;
    private Money price;
    private BillingPeriod billingPeriod;
    private List<String> features;
    private boolean active;

    public SubscriptionPlan() {
    }

    public SubscriptionPlan(String name, String description, Money price, BillingPeriod billingPeriod,
                            List<String> features, boolean active) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.billingPeriod = billingPeriod == null ? BillingPeriod.MONTHLY : billingPeriod;
        this.features = sanitizeFeatures(features);
        this.active = active;
    }

    public void restoreState(Long id, String name, String description, Money price, BillingPeriod billingPeriod,
                             List<String> features, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.billingPeriod = billingPeriod;
        this.features = sanitizeFeatures(features);
        this.active = active;
    }

    public boolean hasFeature(String feature) {
        if (feature == null || feature.isBlank()) {
            return true;
        }
        return features.stream().anyMatch(item -> item.equalsIgnoreCase(feature.trim()));
    }

    private static List<String> sanitizeFeatures(List<String> features) {
        if (features == null) {
            return new ArrayList<>();
        }
        return features.stream()
                .filter(feature -> feature != null && !feature.isBlank())
                .map(String::trim)
                .distinct()
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
    }
}
