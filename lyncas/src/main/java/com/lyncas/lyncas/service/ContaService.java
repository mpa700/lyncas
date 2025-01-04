package com.lyncas.lyncas.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.lyncas.lyncas.entity.Conta;
import com.lyncas.lyncas.entity.Situacao;
import com.lyncas.lyncas.exception.ResourceNotFoundException;
import com.lyncas.lyncas.repository.ContaRepository;

@Service
public class ContaService {
    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public Conta cadastrarConta(Conta conta) {
        conta.setSituacao(Situacao.PENDENTE);
        return contaRepository.save(conta);
    }

    public Page<Conta> listarContas(Pageable pageable) {
        return contaRepository.findAll(pageable);
    }

    public Conta pagarConta(Long id) {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));
        conta.setDataPagamento(LocalDate.now());
        conta.setSituacao(Situacao.PAGA);
        return contaRepository.save(conta);
    }
}
