package br.com.everson.vendas.controller;



import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.everson.vendas.controller.exception.ApiErrors;
import br.com.everson.vendas.model.entity.Cliente;
import br.com.everson.vendas.model.repository.ClienteRepository;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

	@Autowired
	private  ClienteRepository clienteRepository;
	
	@GetMapping
	public List<Cliente> obterTodos(){
		return clienteRepository.findAll();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Cliente salvar( @RequestBody @Valid Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	@GetMapping("{id}")
	public Cliente obterPorId(@PathVariable Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		clienteRepository
		.findById(id)
		.map(cliente -> {
			clienteRepository.delete(cliente);
			return Void.TYPE;
		})
		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
	}
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar (@PathVariable Long id, @RequestBody @Valid Cliente clienteatualizado) {
		clienteRepository
		.findById(id)
		.map(cliente -> {
			cliente.setNome(clienteatualizado.getNome());
			cliente.setCpf(clienteatualizado.getCpf());
		    return clienteRepository.save(cliente);
		})
		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiErrors> handleResponseStatusException(ResponseStatusException ex) {
		String messagemErro = ex.getMessage();
		HttpStatus codigoStatus = ex.getStatus();
		ApiErrors apiErrors = new ApiErrors(messagemErro);
		return new ResponseEntity<ApiErrors>(apiErrors, codigoStatus);
	}
}
