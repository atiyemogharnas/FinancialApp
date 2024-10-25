package org.example.financialapp.serviceManager.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.example.financialapp.user.domain.User;

@Entity
@Table(name = "services")
@Data
public class ServiceManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private Integer maxUsage;

    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

}
