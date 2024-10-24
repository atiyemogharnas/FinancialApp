package org.example.financialapp.user.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("SIMPLE")@Entity
@Data
public class SimpleUser extends User {

    private Double credit;
}
