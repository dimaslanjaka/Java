# Spring User Management With SQLite And Thymeleaf

## Features

- email service
- register and login with email password
- role authentication based
- SQLite database

## Requirements
- Java JDK 19

## Settings
please open `src/main/resources/application.properties`

- modify smtp email config user and password to activate email service
> get app password: goto your google account -> security -> two authentication -> scroll down and select app password

## Default accounts

all default accounts generated from `src/java/com/dimaslanjaka/springusermgr/entities/DatabaseSeeder.java`

| Email | Password | ROLES |
| :--- | :--- | :--- |
| user@webmanajemen.com | user | ROLE_USER |
| admin@webmanajemen.com | admin | ROLE_ADMIN |
| multi@webmanajemen.com | multi | ROLE_USER, ROLE_ADMIN |