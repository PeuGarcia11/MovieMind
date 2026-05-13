
package moviemind.service;

import moviemind.model.Filme;
import moviemind.model.Recomendacao;
import moviemind.model.Usuario;
import moviemind.util.GeradorAleatorio;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Serviço principal responsável por orquestrar
 * todo o pipeline de recomendação.
 */
public class RecomendadorService {

    private final CatalogoFilmesAPI catalogo;
    private final HistoricoUsuarioRepository historico;
    private final NotificadorPush notificador;
    private final GeradorAleatorio gerador;
    private final CalculadoraScore calculadora;
    private final FiltroFilmes filtro;

    public RecomendadorService(
            CatalogoFilmesAPI catalogo,
            HistoricoUsuarioRepository historico,
            NotificadorPush notificador,
            GeradorAleatorio gerador,
            CalculadoraScore calculadora,
            FiltroFilmes filtro
    ) {
        this.catalogo = catalogo;
        this.historico = historico;
        this.notificador = notificador;
        this.gerador = gerador;
        this.calculadora = calculadora;
        this.filtro = filtro;
    }

    /**
     * Gera recomendações ordenadas por score.
     */
    public List<Recomendacao> recomendar(Usuario usuario, int topN) {

        try {

            List<Filme> filmes = catalogo.buscarTodos();

            if (filmes == null || filmes.isEmpty()) {
                return Collections.emptyList();
            }

            List<Filme> filtrados =
                    filtro.filtrar(filmes, usuario.getPerfil());

            List<Recomendacao> recomendacoes =
                    calculadora.gerarRecomendacoes(
                            filtrados,
                            usuario.getPerfil()
                    );

            recomendacoes = recomendacoes.stream()
                    .sorted(
                            Comparator.comparingDouble(Recomendacao::getScore)
                                    .reversed()
                                    .thenComparing(
                                            r -> r.getFilme().getPopularidade(),
                                            Comparator.reverseOrder()
                                    )
                    )
                    .limit(topN)
                    .toList();

            historico.registrarRecomendacao(usuario, recomendacoes);

            if (usuario.isNotificacoesHabilitadas()) {
                notificador.enviar(usuario, recomendacoes);
            }

            return recomendacoes;

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    /**
     * Retorna uma recomendação aleatória
     * dentre os filmes válidos.
     */
    public Recomendacao recomendarAleatorio(Usuario usuario) {

        try {

            List<Filme> filmes = catalogo.buscarTodos();

            List<Filme> filtrados =
                    filtro.filtrar(filmes, usuario.getPerfil());

            if (filtrados.isEmpty()) {
                return null;
            }

            int indice = gerador.sortearInteiro(
                    0,
                    filtrados.size() - 1
            );

            Filme filmeEscolhido = filtrados.get(indice);

            return calculadora.calcularRecomendacao(
                    filmeEscolhido,
                    usuario.getPerfil()
            );

        } catch (Exception e) {
            return null;
        }
    }
}
