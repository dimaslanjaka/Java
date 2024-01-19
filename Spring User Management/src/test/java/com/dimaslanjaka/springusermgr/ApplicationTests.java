package com.dimaslanjaka.springusermgr;

import com.dimaslanjaka.springusermgr.controller.TestController;
import com.dimaslanjaka.springusermgr.mailer.EmailService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(TestController.class).build();
    }

    @Test
    void hasTitleTag() throws Exception {
        this.mockMvc
                .perform(get("/test")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>")));
    }

    @Test
    void serviceNotNull() {
        Assertions.assertThat(emailService).isNotNull();
    }
}
