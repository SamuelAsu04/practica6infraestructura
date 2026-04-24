package com.uma.example.springuma.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uma.example.springuma.integration.base.AbstractIntegration;
import com.uma.example.springuma.model.Medico;

public class MedicoControllerMockMvcIT extends AbstractIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Medico medico;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        medico = new Medico();
        medico.setId(1L);
        medico.setDni("835");
        medico.setNombre("Miguel");
        medico.setEspecialidad("Ginecologia");
    }

     @Test
    private void crearMedico(Medico medico) throws Exception {
        this.mockMvc.perform(post("/medico")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());
    }

    @Test
    void obtenerPorId() throws Exception {
        crearMedico(medico);

        mockMvc.perform(get("/medico/" + medico.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.nombre").value("Miguel"))
                .andExpect(jsonPath("$.dni").value("835"))
                .andExpect(jsonPath("$.especialidad").value("Ginecologia"));
    }

    @Test
    void CrearEliminarMedico() throws Exception {
            crearMedico(medico);

            mockMvc.perform(delete("/medico/" + medico.getId())).andExpect(status().isOk());

            mockMvc.perform(get("/medico/" + medico.getId())).andExpect(status().is5xxServerError());
    }

    @Test
    void CrearActualizarMedico() throws Exception 
    {
         crearMedico(medico);
         medico.setNombre("Jose");
         medico.setEspecialidad("Cardiologo");

         mockMvc.perform(put("/medico").contentType("application/json").content(objectMapper.writeValueAsString(medico))).andExpect(status().isNoContent());

         mockMvc.perform(get("/medico/" + medico.getId())).andExpect(status().isOk()).andExpect(jsonPath("$.nombre").value("Jose"))
         .andExpect(jsonPath("$.especialidad").value("Cardiologo"));
    }
}
