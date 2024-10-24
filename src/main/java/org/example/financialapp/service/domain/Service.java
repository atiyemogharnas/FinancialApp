package org.example.financialapp.service.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.example.financialapp.user.domain.User;

@Entity
@Table(name = "services")
@Data
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double cost;

    @Column(nullable = false)
    private Integer maxUsage;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;
}
