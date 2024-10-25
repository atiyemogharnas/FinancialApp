package org.example.financialapp.controller;

import org.example.financialapp.serviceManager.controller.ServiceUsageController;
import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.domain.ServiceUsage;
import org.example.financialapp.serviceManager.service.ServiceUsageservice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ServiceUsageController.class)
@ExtendWith(MockitoExtension.class)
class ServiceUsageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceUsageservice serviceUsageservice;

    @Test
    void grantServicePermission_ShouldReturnOk_WhenPermissionGranted() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/service-usage/grant/2/1"))
                .andExpect(status().isOk());
    }

    @Test
    void grantServicePermission_ShouldReturnBadRequest_WhenExceptionThrown() throws Exception {
        doThrow(new RuntimeException("Error granting permission")).when(serviceUsageservice).grantServicePermission(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/service-usage/grant/2/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error granting permission"));
    }

    @Test
    void revokeServicePermission_ShouldReturnNoContent_WhenPermissionRevoked() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/service-usage/revoke/2/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void revokeServicePermission_ShouldReturnBadRequest_WhenExceptionThrown() throws Exception {
        doThrow(new RuntimeException("Error revoking permission")).when(serviceUsageservice).revokeServicePermission(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/service-usage/revoke/2/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error revoking permission"));
    }

    @Test
    void getServiceUsageReports_ShouldReturnServiceUsageList() throws Exception {
        ServiceUsage usage1 = new ServiceUsage();
        ServiceUsage usage2 = new ServiceUsage();
        when(serviceUsageservice.getServiceUsageReports(anyLong())).thenReturn(List.of(usage1, usage2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/service-usage/usage/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getServiceUsageReports_ShouldReturnBadRequest_WhenExceptionThrown() throws Exception {
        when(serviceUsageservice.getServiceUsageReports(anyLong())).thenThrow(new RuntimeException("Error fetching reports"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/service-usage/usage/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error fetching reports"));
    }

    @Test
    void getAllowedServices_ShouldReturnAllowedServiceList() throws Exception {
        ServiceManager service1 = new ServiceManager();
        service1.setName("Service1");
        ServiceManager service2 = new ServiceManager();
        service2.setName("Service2");

        when(serviceUsageservice.getAllowedServices(anyLong())).thenReturn(List.of(service1, service2));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/service-usage/allowed/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Service1"))
                .andExpect(jsonPath("$[1].name").value("Service2"));
    }

    @Test
    void getAllowedServices_ShouldReturnBadRequest_WhenExceptionThrown() throws Exception {
        when(serviceUsageservice.getAllowedServices(anyLong())).thenThrow(new RuntimeException("Error fetching allowed services"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/service-usage/allowed/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error fetching allowed services"));
    }

    @Test
    void useService_ShouldReturnOk_WhenServiceUsedSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/service-usage/useService/1/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Service used successfully."));
    }

    @Test
    void useService_ShouldReturnBadRequest_WhenExceptionThrown() throws Exception {
        doThrow(new RuntimeException("Service not available")).when(serviceUsageservice).useService(1L, 2L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/service-usage/useService/1/2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Service not available"));
    }
}

