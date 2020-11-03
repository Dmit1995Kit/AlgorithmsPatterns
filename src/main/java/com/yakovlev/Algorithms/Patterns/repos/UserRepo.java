package com.yakovlev.Algorithms.Patterns.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yakovlev.Algorithms.Patterns.domain.User;

public interface UserRepo extends JpaRepository<User, Long>{

	User findByUsername(String username);

	User findByActivationCode(String code);
}
