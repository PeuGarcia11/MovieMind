package moviemind.service;

import moviemind.model.Filme;
import moviemind.model.PerfilCinefilo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Aplica regras de exclusão ao catálogo, removendo filmes incompatíveis com o perfil.
 *
 * <p>Regras aplicadas (em ordem):
 * <ol>
 *   <li>Filmes já assistidos são excluídos.</li>
 *   <li>Filmes cuja classificação etária ultrapassa o limite do perfil são excluídos.</li>
 *   <li>Filmes em idioma não aceito pelo perfil são excluídos.</li>
 *   <li>Filmes que contêm ao menos um gênero com peso 0.0 são excluídos.</li>
 * </ol>
 *
 * <p>Lógica pura — não acessa APIs nem banco. Nunca deve ser mockada nos testes.
 */
public class FiltroFilmes {

    /**
     * Filtra a lista de filmes com base no perfil do usuário.
     *
     * @param filmes  catálogo a filtrar
     * @param perfil  preferências do usuário
     * @return nova lista contendo apenas os filmes elegíveis; nunca null
     */
    public List<Filme> filtrar(List<Filme> filmes, PerfilCinefilo perfil) {
        if (filmes == null || filmes.isEmpty()) {
            return Collections.emptyList();
        }

        return filmes.stream()
            .filter(filme -> !foiAssistido(filme, perfil))
            .filter(filme -> classificacaoCompativel(filme, perfil))
            .filter(filme -> idiomaAceito(filme, perfil))
            .filter(filme -> !possuiGeneroBloqueado(filme, perfil))
            .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Regras de filtragem (package-private para teste isolado)
    // -------------------------------------------------------------------------

    /** Regra 1: filmes já no histórico do perfil são ignorados. */
    boolean foiAssistido(Filme filme, PerfilCinefilo perfil) {
        return perfil.getHistorico().contains(filme.getId());
    }

    /** Regra 2: classificação do filme não pode ultrapassar o máximo do perfil. */
    boolean classificacaoCompativel(Filme filme, PerfilCinefilo perfil) {
        return filme.getClassificacao().estaDeentroDoLimite(perfil.getClassificacaoMaxima());
    }

    /** Regra 3: idioma do filme precisa estar na lista de idiomas aceitos. */
    boolean idiomaAceito(Filme filme, PerfilCinefilo perfil) {
        return perfil.getIdiomasAceitos().contains(filme.getIdioma());
    }

    /**
     * Regra 4: se o perfil definiu peso 0.0 para algum gênero presente no filme,
     * o filme é bloqueado (usuário explicitamente não quer esse gênero).
     */
    boolean possuiGeneroBloqueado(Filme filme, PerfilCinefilo perfil) {
        return filme.getGeneros().stream()
            .anyMatch(genero -> perfil.getPesosGenero().containsKey(genero)
                             && perfil.getPesosGenero().get(genero) == 0.0);
    }
}
