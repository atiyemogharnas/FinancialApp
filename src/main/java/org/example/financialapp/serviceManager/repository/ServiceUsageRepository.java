package org.example.financialapp.serviceManager.repository;

import org.example.financialapp.serviceManager.domain.ServiceUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceUsageRepository extends JpaRepository<ServiceUsage, Long> {

    Optional<ServiceUsage> findByServiceManagerIdAndUserId(Long serviceManagerId, Long userId);
}
