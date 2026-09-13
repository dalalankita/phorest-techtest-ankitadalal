package com.example.fruitmachine.web;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Web-layer integration tests: real HTTP through MockMvc, checking wiring, validation, and error
 * mapping. The game rules themselves are covered by the domain unit tests, so here we assert
 * status codes and response shape. The service holds one shared machine, so the Spring context
 * is reset before each test to guarantee a clean, unconfigured start.
 */
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FruitMachineControllerTest {

    @Autowired
    private MockMvc mvc;

    private static final String VALID_CONFIG = """
            {"slotCount":4,"colourCount":4,"k":2,"playCost":1,"startingFloat":100}
            """;

    @Test
    void configure_then_read_state_returns_the_configuration() throws Exception {
        mvc.perform(post("/fruit-machine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CONFIG))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currentFloat").value(100));

        mvc.perform(get("/fruit-machine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.config.slotCount").value(4))
                .andExpect(jsonPath("$.config.k").value(2));
    }

    @Test
    void invalid_config_is_rejected_with_400() throws Exception {
        mvc.perform(post("/fruit-machine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"slotCount":4,"colourCount":4,"k":40,"playCost":1,"startingFloat":100}
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void missing_field_is_rejected_with_400() throws Exception {
        mvc.perform(post("/fruit-machine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {"slotCount":4,"colourCount":4,"playCost":1,"startingFloat":100}
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void play_before_configure_returns_409() throws Exception {
        mvc.perform(post("/fruit-machine/play")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void play_after_configure_returns_a_result() throws Exception {
        mvc.perform(post("/fruit-machine")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_CONFIG))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.currentFloat").value(100));

        mvc.perform(post("/fruit-machine/play")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}