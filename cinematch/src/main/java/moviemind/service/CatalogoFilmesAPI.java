package moviemind.service;

import moviemind.model.Filme;

import java.util.List;

/**
 * Contrato para acesso ao catálogo de filmes.
 * Em produção, implementado como chamada HTTP a uma API externa (ex.: TMDB).
 * Nos testes, deve ser mockado com Mockito.
 */
public interface CatalogoFilmesAPI {

    /**
     * Retorna todos os filmes disponíveis no catálogo no momento da chamada.
     *
     * @return lista de filmes; nunca null
     */
    List<Filme> buscarTodos();
}
