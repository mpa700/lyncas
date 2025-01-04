package com.lyncas.lyncas.repository;

import com.lyncas.lyncas.entity.Conta;
import com.lyncas.lyncas.entity.Situacao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ContaRepositoryTest {

    @Autowired
    private ContaRepository contaRepository;

    @Test
    void deveSalvarEEncontrarConta() {
        Conta conta = new Conta();
        conta.setDataVencimento(LocalDate.now());
        conta.setValor(new BigDecimal("100.00"));
        conta.setDescricao("Conta de teste");
        conta.setSituacao(Situacao.PENDENTE);

        contaRepository.save(conta);

        List<Conta> contas = contaRepository.findAll();
        assertEquals(1, contas.size());
        assertEquals("Conta de teste", contas.get(0).getDescricao());
    }
}