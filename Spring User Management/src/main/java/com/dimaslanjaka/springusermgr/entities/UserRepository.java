package com.dimaslanjaka.springusermgr.entities;

import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
    User findUserByEmailAndPassword(String email, String password);
}
