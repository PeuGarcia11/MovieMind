package cinematch.service;

import cinematch.model.*;
import cinematch.util.GeradorAleatorio;

import java.util.Collections;
import java.util.List;

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

    public List<Recomendacao> recomendar(Usuario usuario, int topN) {

        try {
        	
            List<Filme> catalogoCompleto = catalogo.buscarTodos();

            List<Filme> filtrados =
                    filtro.filtrar(catalogoCompleto, usuario.getPerfil());

            List<Recomendacao> recomendacoes =
                    calculadora.gerarRecomendacoes(
                            filtrados,
                            usuario.getPerfil()
                    );

            historico.registrarRecomendacao(usuario, recomendacoes);

            if (usuario.isNotificacoesHabilitadas()) {
                notificador.enviar(usuario, recomendacoes);
            }

            return recomendacoes.stream()
                    .limit(topN)
                    .toList();

        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
