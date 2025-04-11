package com.example.runnerz.run;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunController.class)
class RunControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RunRepository repository;

    private final List<Run> runs = new ArrayList<>();
    @Autowired
    private RunRepository runRepository;

    @BeforeEach
    void setUp() {
        runs.add(new Run(1L,
                "Monday Morning Run", LocalDateTime.now(), LocalDateTime.now().plusMinutes(30), 3, Location.INDOOR));
    }

    @Test
    void shouldFindAllRuns() throws Exception {
        when(runRepository.findAll()).thenReturn(runs);
        mvc.perform(get("/api/runs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(runs.size())));

    }


}