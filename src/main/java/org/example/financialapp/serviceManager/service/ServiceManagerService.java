package org.example.financialapp.serviceManager.service;

import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.domain.ServiceUsage;
import org.example.financialapp.serviceManager.repository.ServiceManagerRepository;
import org.example.financialapp.serviceManager.repository.ServiceUsageRepository;
import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.repository.SimpleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceManagerService {

    @Autowired
    private ServiceManagerRepository serviceManagerRepository;

    public ServiceManager createService(ServiceManager service) {
        return serviceManagerRepository.save(service);
    }

    public ServiceManager updateService(Long serviceId, ServiceManager updatedService) {
        ServiceManager existingService = serviceManagerRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        existingService.setName(updatedService.getName() == null ? existingService.getName() : updatedService.getName());
        existingService.setCost(updatedService.getCost() == null ? existingService.getCost() : updatedService.getCost());
        existingService.setMaxUsage(updatedService.getMaxUsage()== null ? existingService.getMaxUsage() : updatedService.getMaxUsage());
        existingService.setIsActive(updatedService.getIsActive()== null ? existingService.getIsActive() : updatedService.getIsActive());
        return serviceManagerRepository.save(existingService);
    }

    public void deleteService(Long serviceId) {
        serviceManagerRepository.deleteById(serviceId);
    }

    public List<ServiceManager> getAllServices() {
        return serviceManagerRepository.findAll();
    }

    public ServiceManager changeServiceStatus(Long serviceId, Boolean serviceStatus) {
        ServiceManager service = serviceManagerRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setIsActive(serviceStatus);
        return serviceManagerRepository.save(service);
    }

    public List<ServiceManager> getActiveServices() {
        return serviceManagerRepository.findAll().stream()
                .filter(ServiceManager::getIsActive)
                .collect(Collectors.toList());
    }
}
