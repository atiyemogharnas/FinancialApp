package org.example.financialapp.serviceManager.service;

import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.domain.ServiceUsage;
import org.example.financialapp.serviceManager.repository.ServiceManagerRepository;
import org.example.financialapp.serviceManager.repository.ServiceUsageRepository;
import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.repository.SimpleUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceUsageservice {

    @Autowired
    private ServiceUsageRepository serviceUsageRepository;

    @Autowired
    private ServiceManagerRepository serviceManagerRepository;

    @Autowired
    private SimpleUserRepository simpleUserRepository;

    public void grantServicePermission(Long userId, Long serviceId) {
        SimpleUser user = simpleUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        ServiceManager service = serviceManagerRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));

        Optional<ServiceUsage> serviceUsage = serviceUsageRepository.findByServiceManagerIdAndUserId(serviceId, userId);
        if (serviceUsage.isEmpty()) {
            ServiceUsage newServiceUsage = new ServiceUsage();
            newServiceUsage.setServiceManager(service);
            newServiceUsage.setUser(user);
            newServiceUsage.setIsPermitted(true);
            serviceUsageRepository.save(newServiceUsage);
        }
    }

    public void revokeServicePermission(Long userId, Long serviceId) {
        ServiceUsage serviceUsage = serviceUsageRepository.findByServiceManagerIdAndUserId(serviceId, userId)
                .orElseThrow(() -> new RuntimeException("ServiceUsage not found"));
        serviceUsage.setIsPermitted(false);
        serviceUsageRepository.save(serviceUsage);

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


    public void useService(Long userId, Long serviceId) {
        ServiceUsage serviceUsage = serviceUsageRepository.findByServiceManagerIdAndUserId(serviceId, userId)
                .orElseThrow(() -> new RuntimeException("ServiceUsage not found"));

        ServiceManager serviceManager = serviceUsage.getServiceManager();
        SimpleUser user = serviceUsage.getUser();

        if (!serviceManager.getIsActive()) {
            throw new RuntimeException("Service is not active");
        }

        if (!serviceUsage.getIsPermitted()) {
            throw new RuntimeException("User does not have permission to use this service");
        }


        if (serviceUsage.getUsageCount() >= serviceManager.getMaxUsage()) {
            throw new RuntimeException("User has reached the maximum usage limit for this service");
        }


        if (user.getCredit() < serviceManager.getCost()) {
            throw new RuntimeException("User does not have enough credit to use this service");
        }

        user.setCredit(user.getCredit() - serviceManager.getCost());

        serviceUsageRepository.save(serviceUsage);
        simpleUserRepository.save(user);
    }
}
