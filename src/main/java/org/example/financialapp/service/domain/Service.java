package org.example.financialapp.service.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.domain.User;

import java.util.HashSet;
import java.util.Set;

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

    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;

    @ManyToMany
    @JoinTable(
            name = "service_permission",
            joinColumns = @JoinColumn(name = "service_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<SimpleUser> allowedUsers = new HashSet<>();
}
