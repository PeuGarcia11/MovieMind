
package moviemind;

import moviemind.data.BancoFilmesFake;
import moviemind.data.UsuariosExemplo;

import moviemind.model.*;
import moviemind.service.*;
import moviemind.util.GeradorAleatorio;

import java.util.List;

public class Main {

	public static void main(String[] args) {

		Usuario usuario = UsuariosExemplo.criarPedro();

		CatalogoFilmesAPI catalogo = BancoFilmesFake::getFilmes;

		HistoricoUsuarioRepository historico = (u, r) -> System.out.println("Histórico salvo.");

		NotificadorPush notificador = (u, r) -> System.out.println("Notificação enviada.");

		GeradorAleatorio gerador = (min, max) -> min;

		RecomendadorService service = new RecomendadorService(catalogo, historico, notificador, gerador,
				new CalculadoraScore(), new FiltroFilmes());

		List<Recomendacao> recomendacoes = service.recomendar(usuario, 5);

		System.out.println();
		System.out.println("=== MOVIEMIND ===");
		System.out.println();

		for (Recomendacao r : recomendacoes) {

			System.out.println("Filme: " + r.getFilme().getTitulo());

			System.out.println("Score: " + r.getScore());

			System.out.println(r.getJustificativa());

			System.out.println();
		}
	}
}