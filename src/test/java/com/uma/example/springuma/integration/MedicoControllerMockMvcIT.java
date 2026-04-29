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

    private void crearMedico(Medico medico) throws Exception {
        // Crear el médico
        this.mockMvc.perform(post("/medico")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());
    }

    private void actualizarMedico(Medico medico, long id, String dni, String nombre, String especialidad) throws Exception {
        // Crear el médico
        this.mockMvc.perform(post("/medico")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isCreated());
        // Actualizar el médico
        medico.setDni(dni);
        medico.setEspecialidad(especialidad);
        medico.setId(id);
        medico.setNombre(nombre);

        // Realizar la petición de actualización
        this.mockMvc.perform(put("/medico")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medico)))
                .andExpect(status().isNoContent());

        // Verificar que el médico se ha actualizado correctamente
        this.mockMvc.perform(get("/medico/" + id))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value(nombre));
    }
    private void obtenerMedico(Medico medico) throws Exception {
        // Crear el médico
        this.mockMvc.perform(post("/medico")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(medico)))
            .andExpect(status().isCreated());

        // Obtener el médico por ID
        this.mockMvc.perform(get("/medico/" + medico.getId()))
            .andDo(print())
            .andExpect(status().is2xxSuccessful())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(medico.getId()))
            .andExpect(jsonPath("$.dni").value(medico.getDni()));
        } 
    private void eliminarMedico(Medico medico) throws Exception {
        // Crear el médico
        this.mockMvc.perform(post("/medico")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(medico)))
            .andExpect(status().isCreated());

        //  Eliminar el médico por ID
        this.mockMvc.perform(delete("/medico/" + medico.getId()))
            .andExpect(status().isOk());
    } 

    @Test
    @DisplayName("Crear Médico")
    void crearMedico() throws Exception {
        crearMedico(medico);        
    }

    @Test
    @DisplayName("Actualizar Médico")   
    void actualizarMedico() throws Exception {
        actualizarMedico(medico, 1L, "123", "Lucas", "Cardiologia");
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
