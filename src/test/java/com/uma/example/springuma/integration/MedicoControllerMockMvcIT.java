package com.uma.example.springuma.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

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
    void setUp() {
        medico = new Medico();
        medico.setId(1L);
        medico.setDni("835");
        medico.setNombre("Miguel");
        medico.setEspecialidad("Ginecologia");
    }

    // Comentario de prueba
    private void crearMedico(Medico medico) throws Exception {
        this.mockMvc.perform(post("/medico")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());
    }

    private void actualizarMedico(Medico medico) throws Exception {
        // Crear el médico
        this.mockMvc.perform(post("/medico")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(medico)))
            .andExpect(status().isCreated());
        
        // Actualización del médico
        medico.setNombre("Lucas");

        // Lo reescribimos con el mismo ID para que se actualice
        this.mockMvc.perform(put("/medico")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(medico)))
            .andExpect(status().isOk());

        // Verificamos que el médico se ha actualizado
        this.mockMvc.perform(get("/medico"))
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].nombre").value("Lucas"))
            .andReturn();

    } 
    private void obtenerMedico(Medico medico) throws Exception {
        // Crear el médico
        this.mockMvc.perform(post("/medico")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(medico)))
            .andExpect(status().isCreated());

        // Obtener el médico
        this.mockMvc.perform(get("/medico"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", hasSize(0)))
            .andExpect(content().string("[]"))
            .andReturn();
        } 
    private void eliminarMedico(Medico medico) throws Exception {
        // Creamos el médico
        this.mockMvc.perform(post("/medico")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(medico)))
            .andExpect(status().isCreated());

        // Eliminamos el médico
        this.mockMvc.perform(delete("/medico")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(medico)));
    
        this.mockMvc.perform(get("/medico"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$", hasSize(0)));
    } 

    @Test
    @DisplayName("Crear Médico")
    void crearMedico() throws Exception {
        crearMedico(medico);        
    }

    @Test
    @DisplayName("Actualizar Médico")   
    void actualizarMedico() throws Exception {
        actualizarMedico(medico);
    }

    @Test
    @DisplayName("Obtener Médico") 
    void obtenerMedico() throws Exception {
        obtenerMedico(medico);
    }

    @Test
    @DisplayName("Eliminar Médico")
    void eliminarMedico() throws Exception {
        eliminarMedico(medico);
    }
    
}
