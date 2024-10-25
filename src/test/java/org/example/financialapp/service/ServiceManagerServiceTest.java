package org.example.financialapp.service;

import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.repository.ServiceManagerRepository;
import org.example.financialapp.serviceManager.service.ServiceManagerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceManagerServiceTest {

    @Mock
    private ServiceManagerRepository serviceManagerRepository;

    @InjectMocks
    private ServiceManagerService serviceManagerService;

    private ServiceManager testService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testService = new ServiceManager();
        testService.setId(1L);
        testService.setName("Test Service");
        testService.setCost(100.0);
        testService.setMaxUsage(10);
        testService.setIsActive(true);
    }

    @Test
    void testCreateService_ShouldReturnCreatedService() {
        when(serviceManagerRepository.save(any(ServiceManager.class))).thenReturn(testService);

        ServiceManager createdService = serviceManagerService.createService(testService);

        assertThat(createdService).isNotNull();
        assertThat(createdService.getName()).isEqualTo("Test Service");
        verify(serviceManagerRepository, times(1)).save(testService);
    }

    @Test
    void testUpdateService_ShouldReturnUpdatedService() {
        ServiceManager updatedService = new ServiceManager();
        updatedService.setName("Updated Service");
        updatedService.setCost(150.0);

        when(serviceManagerRepository.findById(testService.getId())).thenReturn(Optional.of(testService));
        when(serviceManagerRepository.save(any(ServiceManager.class))).thenReturn(testService);

        ServiceManager result = serviceManagerService.updateService(1L, updatedService);

        assertThat(result.getName()).isEqualTo("Updated Service");
        assertThat(result.getCost()).isEqualTo(150.0);
        verify(serviceManagerRepository, times(1)).save(testService);
    }

    @Test
    void testDeleteService_ShouldDeleteService() {
        doNothing().when(serviceManagerRepository).deleteById(testService.getId());

        serviceManagerService.deleteService(testService.getId());

        verify(serviceManagerRepository, times(1)).deleteById(testService.getId());
    }

    @Test
    void testGetAllServices_ShouldReturnAllServices() {
        ServiceManager service1 = new ServiceManager();
        service1.setName("Service 1");
        ServiceManager service2 = new ServiceManager();
        service2.setName("Service 2");

        when(serviceManagerRepository.findAll()).thenReturn(List.of(service1, service2));

        List<ServiceManager> services = serviceManagerService.getAllServices();

        assertThat(services).hasSize(2);
        assertThat(services.get(0).getName()).isEqualTo("Service 1");
        assertThat(services.get(1).getName()).isEqualTo("Service 2");
    }

    @Test
    void testChangeServiceStatus_ShouldUpdateAndReturnServiceStatus() {
        when(serviceManagerRepository.findById(testService.getId())).thenReturn(Optional.of(testService));
        when(serviceManagerRepository.save(any(ServiceManager.class))).thenReturn(testService);

        ServiceManager result = serviceManagerService.changeServiceStatus(testService.getId(), false);

        assertThat(result.getIsActive()).isFalse();
        verify(serviceManagerRepository, times(1)).save(testService);
    }

    @Test
    void testGetActiveServices_ShouldReturnOnlyActiveServices() {
        ServiceManager activeService = new ServiceManager();
        activeService.setIsActive(true);
        ServiceManager inactiveService = new ServiceManager();
        inactiveService.setIsActive(false);

        when(serviceManagerRepository.findAll()).thenReturn(List.of(activeService, inactiveService));

        List<ServiceManager> activeServices = serviceManagerService.getActiveServices();

        assertThat(activeServices).hasSize(1);
        assertThat(activeServices.get(0).getIsActive()).isTrue();
    }
}
