package com.lyncas.lyncas.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.lyncas.lyncas.entity.Conta;
import com.lyncas.lyncas.entity.Situacao;
import com.lyncas.lyncas.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
public class ContaServiceTest {

    @InjectMocks
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void pagarConta_DeveAlterarSituacaoParaPaga() {
        // Arrange
        Long id = 1L;
        Conta conta = new Conta();
        conta.setId(id);
        conta.setDataVencimento(LocalDate.now());
        conta.setValor(100.00);
        conta.setDescricao("Conta de teste");
        conta.setSituacao(Situacao.PENDENTE);

        when(contaRepository.findById(id)).thenReturn(Optional.of(conta));

        // Act
        Conta contaPaga = contaService.pagarConta(id);

        // Assert
        assertNotNull(contaPaga);
        assertEquals(Situacao.PAGA, contaPaga.getSituacao());
        verify(contaRepository, times(1)).save(conta);
    }
}