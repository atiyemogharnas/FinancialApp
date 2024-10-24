package org.example.financialapp.serviceManager.repository;

import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceManagerRepository extends JpaRepository<ServiceManager, Long> {

}
