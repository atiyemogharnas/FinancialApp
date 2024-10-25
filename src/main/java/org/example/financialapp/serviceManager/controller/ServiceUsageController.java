package org.example.financialapp.serviceManager.controller;

import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.domain.ServiceUsage;
import org.example.financialapp.serviceManager.service.ServiceUsageservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-usage")
public class ServiceUsageController {

    @Autowired
    private ServiceUsageservice serviceUsageservice;


    @PostMapping("/grant/{serviceId}/{userId}")
    public ResponseEntity<Object> grantServicePermission(@PathVariable Long serviceId, @PathVariable Long userId) {
        try {
            serviceUsageservice.grantServicePermission(userId, serviceId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/revoke/{serviceId}/{userId}")
    public ResponseEntity<Object> revokeServicePermission(@PathVariable Long serviceId, @PathVariable Long userId) {
        try {
            serviceUsageservice.revokeServicePermission(userId, serviceId);
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/usage/{userId}")
    public ResponseEntity<Object> getServiceUsageReports(@PathVariable Long userId) {
        try {
            List<ServiceUsage> serviceUsages = serviceUsageservice.getServiceUsageReports(userId);
            return ResponseEntity.ok(serviceUsages);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/allowed/{userId}")
    public ResponseEntity<Object> getAllowedServices(@PathVariable Long userId) {
        try {
            List<ServiceManager> allowedServices = serviceUsageservice.getAllowedServices(userId);
            return ResponseEntity.ok(allowedServices);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/useService/{userId}/{serviceId}")
    public ResponseEntity<String> useService(@PathVariable Long userId, @PathVariable Long serviceId) {
        try {
            serviceUsageservice.useService(userId, serviceId);
            return ResponseEntity.ok("Service used successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
