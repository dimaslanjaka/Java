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

## Preview

login using token programmatically
![user](https://github.com/dimaslanjaka/Java/assets/12471057/397ba233-404b-4f18-b6ad-d12190dd819f)
![admin](https://github.com/dimaslanjaka/Java/assets/12471057/46b34269-fe65-432d-b61e-5d2baa49a264)
![multi](https://github.com/dimaslanjaka/Java/assets/12471057/d8103d7e-d3ee-4833-8385-e196a30c6632)

admin dashboard
![admin dashboard](https://github.com/dimaslanjaka/Java/assets/12471057/dc2922cd-0966-41d1-ad49-eba6fb532d1b)
user dashboard
![user dashboard](https://github.com/dimaslanjaka/Java/assets/12471057/d97b24b5-f3c8-4b59-9e79-5ed7f4449520)
