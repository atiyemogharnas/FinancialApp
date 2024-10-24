package org.example.financialapp.serviceManager.controller;

import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.domain.ServiceUsage;
import org.example.financialapp.serviceManager.service.ServiceManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-manager")
public class ServiceManagerController {

    @Autowired
    private ServiceManagerService serviceManagerService;

    @PostMapping("/create")
    public ResponseEntity<ServiceManager> createService(@RequestBody ServiceManager service) {
        ServiceManager createdService = serviceManagerService.createService(service);
        return ResponseEntity.ok(createdService);
    }

    @PutMapping("/update/{serviceId}")
    public ResponseEntity<ServiceManager> updateService(@PathVariable Long serviceId, @RequestBody ServiceManager updatedService) {
        ServiceManager updated = serviceManagerService.updateService(serviceId, updatedService);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<Void> deleteService(@PathVariable Long serviceId) {
        serviceManagerService.deleteService(serviceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceManager>> getAllServices() {
        List<ServiceManager> services = serviceManagerService.getAllServices();
        return ResponseEntity.ok(services);
    }

    @PutMapping("/activate/{serviceId}")
    public ResponseEntity<ServiceManager> activateService(@PathVariable Long serviceId) {
        ServiceManager activatedService = serviceManagerService.activateService(serviceId);
        return ResponseEntity.ok(activatedService);
    }

    @PutMapping("/deactivate/{serviceId}")
    public ResponseEntity<ServiceManager> deactivateService(@PathVariable Long serviceId) {
        ServiceManager deactivatedService = serviceManagerService.deactivateService(serviceId);
        return ResponseEntity.ok(deactivatedService);
    }

    @PostMapping("/grant/{serviceId}/{userId}")
    public ResponseEntity<Void> grantServicePermission(@PathVariable Long serviceId, @PathVariable Long userId) {
        serviceManagerService.grantServicePermission(userId, serviceId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/revoke/{serviceId}/{userId}")
    public ResponseEntity<Void> revokeServicePermission(@PathVariable Long serviceId, @PathVariable Long userId) {
        serviceManagerService.revokeServicePermission(userId, serviceId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usage/{userId}")
    public ResponseEntity<List<ServiceUsage>> getServiceUsageReports(@PathVariable Long userId) {
        List<ServiceUsage> serviceUsages = serviceManagerService.getServiceUsageReports(userId);
        return ResponseEntity.ok(serviceUsages);
    }

    @GetMapping("/allowed/{userId}")
    public ResponseEntity<List<ServiceManager>> getAllowedServices(@PathVariable Long userId) {
        List<ServiceManager> allowedServices = serviceManagerService.getAllowedServices(userId);
        return ResponseEntity.ok(allowedServices);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ServiceManager>> getActiveServices() {
        List<ServiceManager> activeServices = serviceManagerService.getActiveServices();
        return ResponseEntity.ok(activeServices);
    }

    @PostMapping("/useService/{userId}/{serviceId}")
    public ResponseEntity<String> useService(@PathVariable Long userId, @PathVariable Long serviceId) {
        try {
            serviceManagerService.useService(userId, serviceId);
            return ResponseEntity.ok("Service used successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

