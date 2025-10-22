package com.eszsz.prometheus_grafana_demo.repository;

import com.eszsz.prometheus_grafana_demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
