package org.example.financialapp.serviceManager.controller;

import org.example.financialapp.serviceManager.domain.ServiceManager;
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
    public ResponseEntity<Object> createService(@RequestBody ServiceManager service) {
        try {
            ServiceManager createdService = serviceManagerService.createService(service);
            return ResponseEntity.ok(createdService);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/update/{serviceId}")
    public ResponseEntity<Object> updateService(@PathVariable Long serviceId, @RequestBody ServiceManager updatedService) {
        try {
            ServiceManager updated = serviceManagerService.updateService(serviceId, updatedService);
            return ResponseEntity.ok(updated);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
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

    @PutMapping("/change-status")
    public ResponseEntity<ServiceManager> activateService(@RequestParam Long serviceId, @RequestParam Boolean serviceStatus) {
        ServiceManager activatedService = serviceManagerService.changeServiceStatus(serviceId, serviceStatus);
        return ResponseEntity.ok(activatedService);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ServiceManager>> getActiveServices() {
        List<ServiceManager> activeServices = serviceManagerService.getActiveServices();
        return ResponseEntity.ok(activeServices);
    }
}

