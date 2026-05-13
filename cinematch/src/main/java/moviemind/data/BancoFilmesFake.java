package moviemind.data;

import moviemind.model.Filme;
import moviemind.model.enums.*;

import java.util.List;

public class BancoFilmesFake {

	public static List<Filme> getFilmes() {

		return List.of(

				new Filme("1", "A Chegada", 2016, 116, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
						ClassificacaoEtaria.DOZE, Idioma.INGLES, 92),

				new Filme("2", "Blade Runner 2049", 2017, 164, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
						ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 95),

				new Filme("3", "Vingadores Ultimato", 2019, 181, List.of(Genero.ACAO), ClassificacaoEtaria.DOZE,
						Idioma.INGLES, 95),

				new Filme("4", "Toy Story", 1995, 81, List.of(Genero.COMEDIA), ClassificacaoEtaria.LIVRE, Idioma.INGLES,
						85));
	}
}
