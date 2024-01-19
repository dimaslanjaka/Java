package com.dimaslanjaka.springusermgr.entities;

import java.util.List;

public interface UserService {

    /**
     * save user with role admin
     */
    void saveAdmin(UserDto userDto);

    List<UserDto> findAllUsers();

    User findUserByEmail(String email);

    User findUserByEmailPassword(String email, String password);

    /**
     * find user by token from table row 'token' (@Column User.token)
     *
     * @param token
     * @return
     */
    User findUserByToken(String token);

    UserDto findUserById(Long userId);

    boolean doesUserExist(Long userId);

    void editUser(UserDto updatedUserDto, Long userId);

    /**
     * save user with role user
     */
    void saveUser(UserDto userDto);

    void deleteUserById(Long userId);

}
