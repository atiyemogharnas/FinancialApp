package org.example.financialapp.user.repository;

import org.example.financialapp.user.domain.SimpleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleUserRepository extends JpaRepository<SimpleUser, Long> {
}
