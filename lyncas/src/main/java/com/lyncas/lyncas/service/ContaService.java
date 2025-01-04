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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ContaService {
    private final ContaRepository contaRepository;

    @PersistenceContext
    private EntityManager entityManager;  // Injetando o EntityManager

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    
    @Transactional
    public Conta cadastrarConta(ContaDTO contaDTO) {
    	Conta novaConta = new Conta();
    	novaConta.setSituacao(Situacao.PENDENTE);
    	novaConta.setDataPagamento(contaDTO.getDataPagamento());
    	novaConta.setDataVencimento(contaDTO.getDataVencimento());
    	novaConta.setDescricao(contaDTO.getDescricao());
    	novaConta.setValor(contaDTO.getValor());
    	
    	
        return contaRepository.save(novaConta);  
    }

    public Page<Conta> listarContas(Pageable pageable) {
        return contaRepository.findAll(pageable);
    }
    
    @Transactional
    public Conta pagarConta(Long id) {
        Conta conta = contaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));
        conta.setDataPagamento(LocalDate.now());
        conta.setSituacao(Situacao.PAGA);
        return contaRepository.save(conta);
    }
    
    @Transactional
    public Conta atualizarConta(Long id, Conta contaAtualizada) {
        Conta contaExistente = contaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada com o id: " + id));

    
        contaExistente.setDataVencimento(contaAtualizada.getDataVencimento());
        contaExistente.setDataPagamento(contaAtualizada.getDataPagamento());
        contaExistente.setValor(contaAtualizada.getValor());
        contaExistente.setDescricao(contaAtualizada.getDescricao());
        contaExistente.setSituacao(contaAtualizada.getSituacao());

    
        return entityManager.merge(contaExistente);  // Usando merge para atualizar a entidade
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
    
    @Transactional
    public void importarContas(MultipartFile file) {
        List<Conta> contas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Remover BOM e espaços extras no início da linha
                linha = linha.replace("\uFEFF", "").trim();  // Remove BOM (U+FEFF) e espaços

                String[] dados = linha.split(",");
                Conta conta = new Conta();
                conta.setDataVencimento(LocalDate.parse(dados[0]));
                conta.setValor(Double.parseDouble(dados[1]));
                conta.setDescricao(dados[2]);
                conta.setSituacao(Situacao.valueOf(dados[3].toUpperCase()));
                contas.add(conta);
            }
            contaRepository.saveAll(contas); // Salvar as contas todas de uma vez
        } catch (Exception e) {
            throw new RuntimeException("Erro ao importar contas: " + e.getMessage());
        }
    }
}
