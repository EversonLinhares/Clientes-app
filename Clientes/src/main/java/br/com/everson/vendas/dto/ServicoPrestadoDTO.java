package br.com.everson.vendas.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ServicoPrestadoDTO {

	private String descricao;
	private String preco;
	private String data;
	private Long idCliente;
}
