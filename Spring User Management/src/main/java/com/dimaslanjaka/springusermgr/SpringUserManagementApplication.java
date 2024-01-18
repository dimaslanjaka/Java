package com.dimaslanjaka.springusermgr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class SpringUserManagementApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringUserManagementApplication.class, args);
    }

}
