# Spring User Management With SQLite And Thymeleaf

## Features

- Email service
- Register and login with email password
- Role authentication based
- SQLite database
- Custom password encoder [READ MORE](https://www.webmanajemen.com/2024/01/spring-boot-login-programatically.html)
- Token login without email password [READ MORE](https://www.webmanajemen.com/2024/01/spring-boot-custom-passwordEncoder.html)

## Requirements
- Java JDK 19

## Settings
please open `src/main/resources/application.properties`

- modify smtp email config user and password to activate email service
> get app password: goto your google account -> security -> two authentication -> scroll down and select app password

## Default accounts

all default accounts generated from `src/java/com/dimaslanjaka/springusermgr/entities/DatabaseSeeder.java`

| Email | Password | Roles | Token |
| :--- | :--- | :--- | :--- |
| user@webmanajemen.com | user | ROLE_USER | custom-token-for-user-account |
| admin@webmanajemen.com | admin | ROLE_ADMIN | custom-token-for-admin-account |
| multi@webmanajemen.com | multi | ROLE_USER, ROLE_ADMIN | custom-token-for-multi-account |