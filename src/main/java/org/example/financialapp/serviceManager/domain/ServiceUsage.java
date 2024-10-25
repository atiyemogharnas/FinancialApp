package org.example.financialapp.serviceManager.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.example.financialapp.user.domain.SimpleUser;

@Entity
@Data
public class ServiceUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private SimpleUser user;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceManager serviceManager;

    private Boolean isPermitted;

    private int usageCount;
}

