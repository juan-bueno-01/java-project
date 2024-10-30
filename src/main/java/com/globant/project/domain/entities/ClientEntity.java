package com.globant.project.domain.entities;

import java.time.LocalDateTime;
//import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClientEntity
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clients")
public class ClientEntity {

    @Id
    @Size(min = 1, max = 20, message = "Document must be between 1 and 20 characters")
    @Column(unique = true)
    private String document;

    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @Size(min = 1, max = 255, message = "Email must be between 1 and 255 characters")
    private String email;

    @Size(min = 1, max = 10, message = "Phone must be between 1 and 10 characters")
    private String phone;

    @Size(min = 1, max = 500, message = "Address must be between 1 and 500 characters")
    private String deliveryAddress;

    private String invoiceClientId;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // @OneToMany(mappedBy = "clientDocument")
    // private Set<OrderEntity> orders;

    public ClientEntity(String document, String name, String email, String phone, String deliveryAddress,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.document = document;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
