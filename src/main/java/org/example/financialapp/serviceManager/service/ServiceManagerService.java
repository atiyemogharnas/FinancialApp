package org.example.financialapp.serviceManager.service;

import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.domain.ServiceUsage;
import org.example.financialapp.serviceManager.repository.ServiceManagerRepository;
import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.repository.SimpleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceManagerService {

    @Autowired
    private ServiceManagerRepository serviceManagerRepository;

    @Autowired
    private SimpleUserRepository simpleUserRepository;

    public ServiceManager createService(ServiceManager service) {
        return serviceManagerRepository.save(service);
    }

    public ServiceManager updateService(Long serviceId, ServiceManager updatedService) {
        ServiceManager existingService = serviceManagerRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        existingService.setName(updatedService.getName() == null ? existingService.getName() : updatedService.getName());
        existingService.setCost(updatedService.getCost());
        existingService.setMaxUsage(updatedService.getMaxUsage());
        existingService.setIsActive(updatedService.getIsActive());
        return serviceManagerRepository.save(existingService);
    }

    public void deleteService(Long serviceId) {
        serviceManagerRepository.deleteById(serviceId);
    }

    public List<ServiceManager> getAllServices() {
        return serviceManagerRepository.findAll();
    }

    public ServiceManager activateService(Long serviceId) {
        ServiceManager service = serviceManagerRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setIsActive(true);
        return serviceManagerRepository.save(service);
    }

    public ServiceManager deactivateService(Long serviceId) {
        ServiceManager service = serviceManagerRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        service.setIsActive(false);
        return serviceManagerRepository.save(service);
    }

    public void grantServicePermission(Long userId, Long serviceId) {
        SimpleUser user = simpleUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        ServiceManager service = serviceManagerRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + serviceId));

        service.getAllowedUsers().add(user);
        serviceManagerRepository.save(service);
    }

    public void revokeServicePermission(Long userId, Long serviceId) {
        SimpleUser user = simpleUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        ServiceManager service = serviceManagerRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + serviceId));
        service.getAllowedUsers().remove(user);
        serviceManagerRepository.save(service);
    }

    public List<ServiceUsage> getServiceUsageReports(Long userId) {
        SimpleUser user = simpleUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return user.getServiceUsages().stream()
                .filter(usage -> usage.getServiceManager().getIsActive())
                .collect(Collectors.toList());
    }

    public List<ServiceManager> getAllowedServices(Long userId) {
        SimpleUser user = simpleUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        return user.getServiceUsages().stream()
                .map(ServiceUsage::getServiceManager)
                .filter(ServiceManager::getIsActive)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<ServiceManager> getActiveServices() {
        return serviceManagerRepository.findAll().stream()
                .filter(ServiceManager::getIsActive)
                .collect(Collectors.toList());
    }

    public void useService(Long userId, Long serviceId) {
        SimpleUser user = simpleUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        ServiceManager service = serviceManagerRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + serviceId));

        if (!service.getIsActive()) {
            throw new RuntimeException("Service is not active");
        }

        if (!service.getAllowedUsers().contains(user)) {
            throw new RuntimeException("User does not have permission to use this service");
        }

        long usageCount = user.getServiceUsages().stream()
                .filter(usage -> usage.getServiceManager().equals(service))
                .count();

        if (usageCount >= service.getMaxUsage()) {
            throw new RuntimeException("User has reached the maximum usage limit for this service");
        }

        if (user.getCredit() < service.getCost()) {
            throw new RuntimeException("User does not have enough credit to use this service");
        }

        user.setCredit(user.getCredit() - service.getCost());

        ServiceUsage usage = new ServiceUsage();
        usage.setUser(user);
        usage.setServiceManager(service);
        user.getServiceUsages().add(usage);

        simpleUserRepository.save(user);
    }
}
