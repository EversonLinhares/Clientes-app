package br.com.everson.vendas.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.everson.vendas.dto.ServicoPrestadoDTO;
import br.com.everson.vendas.model.entity.Cliente;
import br.com.everson.vendas.model.entity.ServicoPrestado;
import br.com.everson.vendas.model.repository.ClienteRepository;
import br.com.everson.vendas.model.repository.ServicoPrestadoRepository;
import br.com.everson.vendas.util.BigDecimalConverter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/servicos-prestados")
@RequiredArgsConstructor
public class ServicoPrestadoController {
	//O requireArgsConstructor serve para criar o construtor passando os argumentos 
	//obrigatorios para esses repository abaixo
	
	private final ServicoPrestadoRepository servicoPrestadoRepository;
	private final ClienteRepository clienteRepository;
	private final BigDecimalConverter bigDecimalConverter;
	
    @PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ServicoPrestado salvar( @RequestBody @Valid ServicoPrestadoDTO dto) {
	LocalDate data = LocalDate.parse(dto.getData(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	Long idCliente = dto.getIdCliente();	
	
	Cliente cliente =
			clienteRepository
			        .findById(idCliente)
			        .orElseThrow(() -> 
			        new ResponseStatusException(
			        		HttpStatus.BAD_REQUEST, "Cliente inexistente."));
	
			
		ServicoPrestado servicoPrestado = new ServicoPrestado();
		servicoPrestado.setDescricao(dto.getDescricao());
		servicoPrestado.setData( data );
		servicoPrestado.setCliente(cliente);
		servicoPrestado.setValor(bigDecimalConverter.converter(dto.getPreco()));
		
		return servicoPrestadoRepository.save(servicoPrestado);
	}
    
    @GetMapping
    public List<ServicoPrestado> pesquisar(
    		@RequestParam(value = "nome", required = false, defaultValue = "") String nome,
    		@RequestParam(value= "mes", required=false) Integer mes
    		){
	    return servicoPrestadoRepository.findByNomeClienteAndMes("%" + nome + "%", mes);
    }
}
