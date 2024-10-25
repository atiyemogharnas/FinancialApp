package org.example.financialapp.repository;

import org.example.financialapp.serviceManager.domain.ServiceManager;
import org.example.financialapp.serviceManager.domain.ServiceUsage;
import org.example.financialapp.serviceManager.repository.ServiceManagerRepository;
import org.example.financialapp.serviceManager.repository.ServiceUsageRepository;
import org.example.financialapp.user.domain.SimpleUser;
import org.example.financialapp.user.domain.User;
import org.example.financialapp.user.repository.SimpleUserRepository;
import org.example.financialapp.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ServiceUsageRepositoryTest {

    @Autowired
    private ServiceUsageRepository serviceUsageRepository;

    @Autowired
    private SimpleUserRepository simpleUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceManagerRepository serviceManagerRepository;

    private SimpleUser testUser;
    private User adminUser;
    private ServiceManager testService;
    private ServiceUsage testServiceUsage;

    @BeforeEach
    void setUp() {
        testUser = new SimpleUser();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setUserType(User.UserType.SIMPLE);
        testUser.setCredit(100.0);
        simpleUserRepository.save(testUser);

        adminUser = new User();
        adminUser.setUsername("adminuser");
        adminUser.setPassword("password456");
        adminUser.setUserType(User.UserType.ADMIN);
        userRepository.save(adminUser);

        testService = new ServiceManager();
        testService.setName("Test Service");
        testService.setCost(100.0);
        testService.setMaxUsage(10);
        testService.setIsActive(true);
        testService.setAdmin(adminUser);
        serviceManagerRepository.save(testService);

        testServiceUsage = new ServiceUsage();
        testServiceUsage.setUser(testUser);
        testServiceUsage.setServiceManager(testService);
        testServiceUsage.setIsPermitted(true);
        testServiceUsage.setUsageCount(1);

        serviceUsageRepository.save(testServiceUsage);
    }

    @Test
    void testFindByServiceManagerIdAndUserId() {
        Optional<ServiceUsage> foundServiceUsage = serviceUsageRepository.findByServiceManagerIdAndUserId(
                testService.getId(), testUser.getId()
        );

        assertThat(foundServiceUsage).isPresent();
        assertThat(foundServiceUsage.get().getServiceManager().getId()).isEqualTo(testService.getId());
        assertThat(foundServiceUsage.get().getUser().getId()).isEqualTo(testUser.getId());
        assertThat(foundServiceUsage.get().getIsPermitted()).isTrue();
        assertThat(foundServiceUsage.get().getUsageCount()).isEqualTo(1);
    }
}

