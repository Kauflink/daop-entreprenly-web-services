package online.entreprenly.platform.sales.infrastructure.persistence.jpa.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Embeddable persistence representation of a single sale line item.
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class SaleItemEmbeddable {

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_name", length = 160)
    private String productName;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "weight_kg")
    private Double weightKg;

    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @Column(name = "subtotal", nullable = false)
    private double subtotal;

    public SaleItemEmbeddable(Long productId, String productName, Integer quantity, Double weightKg,
                              double unitPrice, double subtotal) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.weightKg = weightKg;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }
}
