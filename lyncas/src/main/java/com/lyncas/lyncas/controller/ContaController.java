package com.lyncas.lyncas.controller;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lyncas.lyncas.entity.Conta;
import java.math.BigDecimal;

import com.lyncas.lyncas.service.ContaDTO;
import com.lyncas.lyncas.service.ContaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;


@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    @Operation(summary = "Cadastra uma nova conta")
    @ApiResponse(responseCode = "200", description = "Conta cadastrada com sucesso")
    public ResponseEntity<Conta> cadastrar(@RequestBody @Valid ContaDTO conta) {
        Conta novaConta = contaService.cadastrarConta(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping
    @Operation(summary = "Listar todas as contas")
    @ApiResponse(responseCode = "200", description = "Contas listadas com sucesso")
    public ResponseEntity<Page<Conta>> listar(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "1") int size,
                                               @RequestParam(required = false) String sort) {
        // Cria o Pageable de forma personalizada
        Pageable pageable;
        if (sort == null || sort.isEmpty()) {
            pageable = PageRequest.of(page, size); // Sem ordenação
        } else {
            pageable = PageRequest.of(page, size, Sort.by(sort)); // Ordenação com um único critério
        }

        Page<Conta> contas = contaService.listarContas(pageable);
        return ResponseEntity.ok(contas);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma conta")
    @ApiResponse(responseCode = "200", description = "Conta atualizada com sucesso")
    public ResponseEntity<Conta> atualizarConta(@PathVariable Long id, @RequestBody @Valid ContaDTO contaDTO) {
        Conta contaAtualizada =  new Conta();
        contaAtualizada.setDataPagamento(contaDTO.getDataPagamento());
        contaAtualizada.setDataVencimento(contaDTO.getDataVencimento());
        contaAtualizada.setDescricao(contaDTO.getDescricao());
        contaAtualizada.setSituacao(contaDTO.getSituacao());
        contaAtualizada.setValor(contaDTO.getValor());
    	Conta conta = contaService.atualizarConta(id, contaAtualizada);
        return ResponseEntity.ok(conta);
    }

    @PutMapping("/{id}/pagar")
    @Operation(summary = "Paga uma conta")
    @ApiResponse(responseCode = "200", description = "Conta paga com sucesso")
    public ResponseEntity<Conta> pagar(@PathVariable Long id) {
        Conta contaPaga = contaService.pagarConta(id);
        return ResponseEntity.ok(contaPaga);
    }
    
    @GetMapping("/filtro")
    @Operation(summary = "Lista contas com filtro de data de vencimento e descrição")
    @ApiResponse(responseCode = "200", description = "Lista de contas retornada com sucesso")
    public ResponseEntity<List<Conta>> listarContasComFiltro(
            @RequestParam(required = false) LocalDate dataVencimento,
            @RequestParam(required = false) String descricao) {
        List<Conta> contas = contaService.listarContasComFiltro(dataVencimento, descricao);
        return ResponseEntity.ok(contas);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtém uma conta pelo ID")
    @ApiResponse(responseCode = "200", description = "Conta encontrada com sucesso")
    public ResponseEntity<Conta> obterContaPorId(@PathVariable Long id) {
        Conta conta = contaService.obterContaPorId(id);
        return ResponseEntity.ok(conta);
    }
    
    @PostMapping("/importar")
    @Operation(
        summary = "Importa contas a pagar a partir de um arquivo CSV",
        requestBody = @RequestBody(
            content = @Content(
                mediaType = "multipart/form-data",
                schema = @Schema(type = "string", format = "binary")
            )
        )
    )
    @ApiResponse(responseCode = "200", description = "Contas importadas com sucesso")
    public ResponseEntity<String> importarContas(@RequestParam("file") MultipartFile file) {
        contaService.importarContas(file);
        return ResponseEntity.ok("Importação realizada com sucesso");
    }
    
    @GetMapping("/total-pago")
    @Operation(summary = "Obtém o valor total pago por período")
    @ApiResponse(responseCode = "200", description = "Valor total pago retornado com sucesso")
    public ResponseEntity<BigDecimal> obterTotalPagoPorPeriodo(
            @RequestParam LocalDate dataInicio,
            @RequestParam LocalDate dataFim) {
        BigDecimal totalPago = contaService.obterTotalPagoPorPeriodo(dataInicio, dataFim);
        return ResponseEntity.ok(totalPago);
    }
}