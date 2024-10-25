package org.example.financialapp.controller;

import org.example.financialapp.serviceManager.controller.ServiceManagerController;
import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.service.ServiceManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ServiceManagerController.class)
@ExtendWith(MockitoExtension.class)
class ServiceManagerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceManagerService serviceManagerService;

    @Test
    void testCreateService_ShouldReturnCreatedService() throws Exception {
        ServiceManager newService = new ServiceManager();
        newService.setName("New Service");
        newService.setCost(100.0);

        when(serviceManagerService.createService(any(ServiceManager.class))).thenReturn(newService);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/service-manager/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New Service\", \"cost\": 100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Service"))
                .andExpect(jsonPath("$.cost").value(100.0));
    }

    @Test
    void testUpdateService_ShouldReturnUpdatedService() throws Exception {
        ServiceManager updatedService = new ServiceManager();
        updatedService.setName("Updated Service");
        updatedService.setCost(150.0);

        when(serviceManagerService.updateService(anyLong(), any(ServiceManager.class))).thenReturn(updatedService);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/service-manager/update/{serviceId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Service\", \"cost\": 150.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Service"))
                .andExpect(jsonPath("$.cost").value(150.0));
    }

    @Test
    void testDeleteService_ShouldReturnNoContent() throws Exception {
        doNothing().when(serviceManagerService).deleteService(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/service-manager/delete/{serviceId}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllServices_ShouldReturnAllServices() throws Exception {
        ServiceManager service1 = new ServiceManager();
        service1.setName("Service 1");
        ServiceManager service2 = new ServiceManager();
        service2.setName("Service 2");

        when(serviceManagerService.getAllServices()).thenReturn(List.of(service1, service2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/service-manager/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Service 1"))
                .andExpect(jsonPath("$[1].name").value("Service 2"));
    }

    @Test
    void testChangeServiceStatus_ShouldReturnUpdatedServiceStatus() throws Exception {
        ServiceManager service = new ServiceManager();
        service.setId(1L);
        service.setIsActive(false);

        when(serviceManagerService.changeServiceStatus(anyLong(), anyBoolean())).thenReturn(service);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/service-manager/change-status")
                        .param("serviceId", "1")
                        .param("serviceStatus", "false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(false));
    }

    @Test
    void testGetActiveServices_ShouldReturnActiveServices() throws Exception {
        ServiceManager activeService = new ServiceManager();
        activeService.setName("Active Service");
        activeService.setIsActive(true);

        when(serviceManagerService.getActiveServices()).thenReturn(List.of(activeService));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/service-manager/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Active Service"))
                .andExpect(jsonPath("$[0].isActive").value(true));
    }
}

