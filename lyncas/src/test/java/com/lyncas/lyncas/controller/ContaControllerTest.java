package com.lyncas.lyncas.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import com.lyncas.lyncas.entity.Conta;
import com.lyncas.lyncas.service.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

@WebMvcTest(ContaController.class)
class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaService contaService;

    @Test
    void deveRetornarListaDeContas() throws Exception {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDataVencimento(LocalDate.now());
        conta.setValor(100.00);
        conta.setDescricao("Conta de teste");

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Conta> contaPage = new PageImpl<>(Collections.singletonList(conta), pageable, 1);

        when(contaService.listarContas(pageable)).thenReturn(contaPage);

        mockMvc.perform(get("/api/contas?page=0&size=10&sort=id")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].descricao").value("Conta de teste"));
    }
}