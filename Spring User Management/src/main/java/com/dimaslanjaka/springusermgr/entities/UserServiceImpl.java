package com.dimaslanjaka.springusermgr.entities;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dimaslanjaka.springusermgr.CustomPassword;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings({ "ArraysAsListWithZeroOrOneArgument", "OptionalIsPresent", "FieldMayBeFinal", "Convert2MethodRef" })
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CustomPassword passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
            RoleRepository roleRepository,
            CustomPassword passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveAdmin(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAge(userDto.getAge());
        user.setPhone(userDto.getPhone());
        user.setGender(userDto.getGender());
        user.setAddress(userDto.getAddress());
        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = checkRoleExist("ROLE_ADMIN");
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName((userDto.getFirstName() + " " + userDto.getLastName()).trim());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setAge(userDto.getAge());
        user.setPhone(userDto.getPhone());
        user.setGender(userDto.getGender());
        user.setAddress(userDto.getAddress());
        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist("ROLE_USER");
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    public void deleteUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(user -> {
            user.getRoles().clear();
            userRepository.delete(user);
        });
    }

    public boolean doesUserExist(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.isPresent();
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    /**
     * find user by token from table row 'token' (@Column User.token)
     *
     * @param token
     * @return
     */
    @Override
    public User findUserByToken(String token) {
        return userRepository.findUserByToken(token);
    }

    @Override
    public User findUserByEmailPassword(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }

    public UserDto findUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return mapToUserDto(userOptional.get());
        }
        return null;
    }

    public void editUser(UserDto updatedUserDto, Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        existingUser.setName(updatedUserDto.getFirstName() + " " + updatedUserDto.getLastName());
        existingUser.setAge(updatedUserDto.getAge());
        existingUser.setPhone(updatedUserDto.getPhone());
        existingUser.setGender(updatedUserDto.getGender());
        existingUser.setAddress(updatedUserDto.getAddress());
        if (!updatedUserDto.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }
        userRepository.save(existingUser);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str.length == 2 ? str[1] : "");
        userDto.setEmail(user.getEmail());
        userDto.setAge(user.getAge());
        userDto.setPhone(user.getPhone());
        userDto.setGender(user.getGender());
        userDto.setAddress(user.getAddress());
        userDto.setRole(user.getRoles().get(0).getName());
        return userDto;
    }

    /**
     * create role when not exist
     *
     * @param ROLE_NAME ROLE_ADMIN or ROLE_USER
     * @return Role class
     */
    private Role checkRoleExist(String ROLE_NAME) {
        Role role = new Role();
        role.setName(ROLE_NAME);
        return roleRepository.save(role);
    }
}