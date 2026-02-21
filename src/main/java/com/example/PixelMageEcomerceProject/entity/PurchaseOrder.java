package com.example.PixelMageEcomerceProject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PURCHASE_ORDERS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "po_id")
    private Integer poId;

    @Column(name = "warehouse_id", nullable = false)
    private Integer warehouseId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Supplier_id", nullable = false, referencedColumnName = "Supplier_id")
    @JsonBackReference("supplier-purchaseOrders")
    private Supplier supplier;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    @JsonManagedReference("purchaseOrder-purchaseOrderLines")
    private List<PurchaseOrderLine> purchaseOrderLines;

    @Column(name = "po_number", length = 50)
    private String poNumber;

    @Column(name = "status", length = 20)
    private String status; // DRAFT, SUBMITTED, APPROVED ,CANCELED, RECEIVED, CLOSED

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "expected_delivery")
    private LocalDateTime expectedDelivery;


    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

