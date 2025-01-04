package com.lyncas.lyncas.service;

import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

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
    
    public Conta atualizarConta(Long id, Conta contaAtualizada) {
        Conta contaExistente = contaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com o id: " + id));
        contaExistente.setDataVencimento(contaAtualizada.getDataVencimento());
        contaExistente.setDataPagamento(contaAtualizada.getDataPagamento());
        contaExistente.setValor(contaAtualizada.getValor());
        contaExistente.setDescricao(contaAtualizada.getDescricao());
        contaExistente.setSituacao(contaAtualizada.getSituacao());
        return contaRepository.save(contaExistente);
    }
    
    public List<Conta> listarContasComFiltro(LocalDate dataVencimento, String descricao) {
        if (dataVencimento != null && descricao != null) {
            return contaRepository.findByDataVencimentoAndDescricao(dataVencimento, descricao);
        } else if (dataVencimento != null) {
            return contaRepository.findByDataVencimento(dataVencimento);
        } else if (descricao != null) {
            return contaRepository.findByDescricaoContainingIgnoreCase(descricao);
        }
        return contaRepository.findAll();
    }
    
    public Conta obterContaPorId(Long id) {
        return contaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com o id: " + id));
    }
    
    
    
    public BigDecimal obterTotalPagoPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        return contaRepository.calcularTotalPagoPorPeriodo(dataInicio, dataFim)
                .orElse(BigDecimal.ZERO);
    }
    
    public void importarContas(MultipartFile file) {
        List<Conta> contas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                Conta conta = new Conta();
                conta.setDataVencimento(LocalDate.parse(dados[0]));
                conta.setValor(new BigDecimal(dados[1]));
                conta.setDescricao(dados[2]);
                conta.setSituacao(Situacao.valueOf(dados[3].toUpperCase()));
                contas.add(conta);
            }
            contaRepository.saveAll(contas);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar contas: " + e.getMessage());
        }
    }
}
