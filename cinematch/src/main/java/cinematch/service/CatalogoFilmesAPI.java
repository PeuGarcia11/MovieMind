package cinematch.service;

import cinematch.model.Filme;

import java.util.List;

/**
 * Contrato para acesso ao catálogo de filmes. Em produção, implementado como
 * chamada HTTP a uma API externa (ex.: TMDB). Nos testes, deve ser mockado com
 * Mockito.
 */
public interface CatalogoFilmesAPI {

	
	List<Filme> buscarTodos();
}
