
/* =====================================================
 * وحدة المخزون - Inventory Module
 * =====================================================
 */

/* ---- File: src/main/java/com/ecommerce/multistore/inventory/domain/InventoryMovement.java ---- */

package com.ecommerce.multistore.inventory.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * كيان حركة المخزون
 * Inventory Movement Entity
 */
@Entity
@Table(name = "inventory_movements")
public class InventoryMovement {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(name = "display_id", unique = true)
    private String displayId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reference_type")
    private String referenceType;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public InventoryMovement() {
        this.createdAt = LocalDateTime.now();
    }

    public InventoryMovement(UUID productId, MovementType movementType, Integer quantity) {
        this();
        this.productId = productId;
        this.movementType = movementType;
        this.quantity = quantity;
    }

    // Business Methods
    public boolean isInbound() {
        return movementType == MovementType.IN || movementType == MovementType.RELEASED;
    }

    public boolean isOutbound() {
        return movementType == MovementType.OUT || movementType == MovementType.RESERVED;
    }

    public Integer getEffectiveQuantity() {
        return isInbound() ? quantity : -quantity;
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getDisplayId() { return displayId; }
    public void setDisplayId(String displayId) { this.displayId = displayId; }

    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }

    public MovementType getMovementType() { return movementType; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getReferenceType() { return referenceType; }
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }

    public UUID getReferenceId() { return referenceId; }
    public void setReferenceId(UUID referenceId) { this.referenceId = referenceId; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    /**
     * أنواع حركة المخزون
     * Movement Types
     */
    public enum MovementType {
        IN("دخول"),
        OUT("خروج"),
        ADJUSTMENT("تعديل"),
        RESERVED("محجوز"),
        RELEASED("محرر");

        private final String arabicName;

        MovementType(String arabicName) {
            this.arabicName = arabicName;
        }

        public String getArabicName() {
            return arabicName;
        }
    }
}

