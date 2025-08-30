package com.you.require4testing;

import com.you.require4testing.domain.Requirement;
import com.you.require4testing.repository.RequirementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RequirementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequirementRepository repo;

    @Test
    @WithMockUser(username = "test", roles = {"USER"})
    void createRequirement() throws Exception {
        String json = "{"
                + "\"title\":\"Test Req\","
                + "\"description\":\"Test description\","
                + "\"createdBy\":1"
                + "}";

        mockMvc.perform(post("/api/requirements")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Req"));
    }

}
