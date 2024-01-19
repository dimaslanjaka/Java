package com.dimaslanjaka.springusermgr.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// create default user and admin account

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.dimaslanjaka.springusermgr.CustomPassword;

@Component
public class DatabaseSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CustomPassword passwordEncoder;

    public DatabaseSeeder(RoleRepository roleRepository, UserRepository userRepository,
            CustomPassword passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent contextRefreshedEvent) {
        if (createRoles()) {
            if (createAdmin() && createUser() && createMultiAuthoritiesUser()) {
                System.out.println("default accounts created successfully");
            }
        }
    }

    /**
     * create user with multiple roles
     *
     * @return
     */
    private boolean createMultiAuthoritiesUser() {
        User user = new User();
        user.setName("multiple authorities user");
        user.setId(3L);
        user.setAddress("unknown");
        user.setEmail("multi@webmanajemen.com");
        user.setPhone("00000000");
        user.setAge(20);
        user.setPassword(passwordEncoder.encode("multi"));
        user.setGender("MALE");
        // set custom token for default account
        user.setToken("custom-token-for-multi-account");
        Optional<Role> role = Optional.ofNullable(roleRepository.findByName("ROLE_USER"));
        Optional<Role> role2 = Optional.ofNullable(roleRepository.findByName("ROLE_ADMIN"));
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByEmail(user.getEmail()));
        if (role.isEmpty() || role2.isEmpty()) {
            System.out.println("role not yet created");
            return false;
        } else if (optionalUser.isPresent()) {
            System.out.println("user with email " + user.getEmail() + " already exist");
            return false;
        } else {
            List<Role> roles = new ArrayList<>();
            roles.add(role.get());
            roles.add(role2.get());
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        }
    }

    /**
     * create default user account
     */
    private boolean createUser() {
        User user = new User();
        user.setName("user");
        user.setId(2L);
        user.setAddress("unknown");
        user.setEmail("user@webmanajemen.com");
        user.setPhone("00000000");
        user.setAge(20);
        user.setPassword(passwordEncoder.encode("user"));
        user.setGender("MALE");
        // set custom token for default account
        user.setToken("custom-token-for-user-account");
        Optional<Role> role = Optional.ofNullable(roleRepository.findByName("ROLE_USER"));
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByEmail(user.getEmail()));
        if (role.isEmpty()) {
            System.out.println("role not yet created");
            return false;
        } else if (optionalUser.isPresent()) {
            System.out.println("user with email " + user.getEmail() + " already exist");
            return false;
        } else {
            List<Role> roles = new ArrayList<>();
            roles.add(role.get());
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        }
    }

    /**
     * create admin account
     */
    private boolean createAdmin() {
        User user = new User();
        user.setName("admin");
        user.setId(1L);
        user.setAddress("unknown");
        user.setEmail("admin@webmanajemen.com");
        user.setPhone("00000000");
        user.setAge(20);
        user.setPassword(passwordEncoder.encode("admin"));
        user.setGender("MALE");
        // set custom token for default account
        user.setToken("custom-token-for-admin-account");
        Optional<Role> role = Optional.ofNullable(roleRepository.findByName("ROLE_ADMIN"));
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findUserByEmail(user.getEmail()));
        if (role.isEmpty()) {
            System.out.println("role not yet created");
            return false;
        } else if (optionalUser.isPresent()) {
            System.out.println("user with email " + user.getEmail() + " already exist");
            return false;
        } else {
            List<Role> roles = new ArrayList<>();
            roles.add(role.get());
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        }
    }

    /**
     * create default roles (ROLE_ADMIN, ROLE_USER)
     */
    private boolean createRoles() {
        Role role = new Role();

        try {
            role.setId(1L);
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);

            role.setId(2L);
            role.setName("ROLE_USER");
            roleRepository.save(role);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }
}
