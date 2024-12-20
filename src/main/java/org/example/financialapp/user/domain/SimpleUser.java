package org.example.financialapp.user.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.financialapp.serviceManager.domain.*;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("SIMPLE")@Entity
@Data
public class SimpleUser extends User {

    private Double credit;

    @OneToMany(mappedBy = "user")
    private Set<ServiceUsage> serviceUsages;
}
