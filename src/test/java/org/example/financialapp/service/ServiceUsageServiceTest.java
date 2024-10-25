package org.example.financialapp.service;

import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.domain.ServiceUsage;
import org.example.financialapp.serviceManager.repository.ServiceManagerRepository;
import org.example.financialapp.serviceManager.repository.ServiceUsageRepository;
import org.example.financialapp.serviceManager.service.ServiceUsageservice;
import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.domain.User;
import org.example.financialapp.user.repository.SimpleUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceUsageServiceTest {

    @Mock
    private ServiceUsageRepository serviceUsageRepository;

    @Mock
    private ServiceManagerRepository serviceManagerRepository;

    @Mock
    private SimpleUserRepository simpleUserRepository;

    @InjectMocks
    private ServiceUsageservice serviceUsageService;

    private SimpleUser testUser;
    private User adminUser;
    private ServiceManager testService;
    private ServiceUsage testServiceUsage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new SimpleUser();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setUserType(User.UserType.SIMPLE);
        testUser.setCredit(200.0);

        adminUser = new User();
        adminUser.setUsername("adminuser");
        adminUser.setPassword("password456");
        adminUser.setUserType(User.UserType.ADMIN);

        testService = new ServiceManager();
        testService.setId(1L);
        testService.setName("Test Service");
        testService.setCost(50.0);
        testService.setMaxUsage(5);
        testService.setAdmin(adminUser);
        testService.setIsActive(true);

        testServiceUsage = new ServiceUsage();
        testServiceUsage.setServiceManager(testService);
        testServiceUsage.setUser(testUser);
        testServiceUsage.setIsPermitted(true);
        testServiceUsage.setUsageCount(0);
    }

    @Test
    void testGrantServicePermission_WhenServiceUsageDoesNotExist_ShouldCreateNew() {
        when(simpleUserRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(serviceManagerRepository.findById(testService.getId())).thenReturn(Optional.of(testService));
        when(serviceUsageRepository.findByServiceManagerIdAndUserId(testService.getId(), testUser.getId()))
                .thenReturn(Optional.empty());

        serviceUsageService.grantServicePermission(testUser.getId(), testService.getId());

        verify(serviceUsageRepository, times(1)).save(any(ServiceUsage.class));
    }

    @Test
    void testGrantServicePermission_WhenServiceUsageExists_ShouldNotCreateNew() {
        when(simpleUserRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(serviceManagerRepository.findById(testService.getId())).thenReturn(Optional.of(testService));
        when(serviceUsageRepository.findByServiceManagerIdAndUserId(testService.getId(), testUser.getId()))
                .thenReturn(Optional.of(testServiceUsage));

        serviceUsageService.grantServicePermission(testUser.getId(), testService.getId());

        verify(serviceUsageRepository, times(0)).save(any(ServiceUsage.class));
    }

    @Test
    void testRevokeServicePermission_ShouldSetIsPermittedToFalse() {
        when(serviceUsageRepository.findByServiceManagerIdAndUserId(testService.getId(), testUser.getId()))
                .thenReturn(Optional.of(testServiceUsage));

        serviceUsageService.revokeServicePermission(testUser.getId(), testService.getId());

        assertThat(testServiceUsage.getIsPermitted()).isFalse();
        verify(serviceUsageRepository, times(1)).save(testServiceUsage);
    }

    @Test
    void testGetServiceUsageReports_ShouldReturnFilteredActiveServiceUsages() {
        testUser.setServiceUsages(Set.of(testServiceUsage));
        when(simpleUserRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        List<ServiceUsage> result = serviceUsageService.getServiceUsageReports(testUser.getId());

        assertThat(result).containsExactly(testServiceUsage);
    }

    @Test
    void testGetAllowedServices_ShouldReturnDistinctActiveServices() {
        testUser.setServiceUsages(Set.of(testServiceUsage));
        when(simpleUserRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));

        List<ServiceManager> result = serviceUsageService.getAllowedServices(testUser.getId());

        assertThat(result).containsExactly(testService);
    }

    @Test
    void testUseService_WhenUserHasPermissionAndSufficientCredit_ShouldReduceCredit() {
        testServiceUsage.setUsageCount(1);
        when(serviceUsageRepository.findByServiceManagerIdAndUserId(testService.getId(), testUser.getId()))
                .thenReturn(Optional.of(testServiceUsage));

        serviceUsageService.useService(testUser.getId(), testService.getId());

        assertThat(testUser.getCredit()).isEqualTo(150.0);
        verify(serviceUsageRepository, times(1)).save(testServiceUsage);
        verify(simpleUserRepository, times(1)).save(testUser);
    }

    @Test
    void testUseService_WhenServiceIsInactive_ShouldThrowException() {
        testService.setIsActive(false);
        when(serviceUsageRepository.findByServiceManagerIdAndUserId(testService.getId(), testUser.getId()))
                .thenReturn(Optional.of(testServiceUsage));

        assertThrows(RuntimeException.class, () -> serviceUsageService.useService(testUser.getId(), testService.getId()));
    }

    @Test
    void testUseService_WhenUserUsesMoreThanAllowed_ShouldThrowException() {
        testServiceUsage.setUsageCount(testService.getMaxUsage());
        when(serviceUsageRepository.findByServiceManagerIdAndUserId(testService.getId(), testUser.getId()))
                .thenReturn(Optional.of(testServiceUsage));

        assertThrows(RuntimeException.class, () -> serviceUsageService.useService(testUser.getId(), testService.getId()));
    }

    @Test
    void testUseService_WhenUserHasInsufficientCredit_ShouldThrowException() {
        testUser.setCredit(10.0);
        when(serviceUsageRepository.findByServiceManagerIdAndUserId(testService.getId(), testUser.getId()))
                .thenReturn(Optional.of(testServiceUsage));

        assertThrows(RuntimeException.class, () -> serviceUsageService.useService(testUser.getId(), testService.getId()));
    }
}

