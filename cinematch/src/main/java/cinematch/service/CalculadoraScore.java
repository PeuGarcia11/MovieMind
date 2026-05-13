package cinematch.service;

import cinematch.model.Filme;
import cinematch.model.PerfilCinefilo;
import cinematch.model.Recomendacao;
import cinematch.model.enums.Genero;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Calcula o score de compatibilidade (0–100) entre um filme e um perfil de
 * usuário.
 *
 * <p>
 * Fórmula:
 * 
 * <pre>
 * score = (scoreGenero × PESO_GENERO) + (scoreDuracao × PESO_DURACAO) +
 * (scorePopularidade× PESO_POPULARIDADE) + (scoreAfinidade × PESO_AFINIDADE)
 */
public class CalculadoraScore {

	static final double PESO_GENERO = 0.50;
	static final double PESO_DURACAO = 0.20;
	static final double PESO_POPULARIDADE = 0.15;
	static final double PESO_AFINIDADE = 0.15;

	private static final double SCORE_MAXIMO = 100.0;
	private static final double SCORE_MINIMO = 0.0;
	private static final double NOTA_MAXIMA = 5.0;
	private static final double NOTA_NEUTRA = 3.0; // média assumida sem histórico

	public Recomendacao calcularRecomendacao(Filme filme, PerfilCinefilo perfil) {
		double scoreGenero = calcularScoreGenero(filme, perfil);
		double scoreDuracao = calcularScoreDuracao(filme, perfil);
		double scorePopularidade = filme.getPopularidade();
		double scoreAfinidade = calcularScoreAfinidade(filme, perfil);

		double scoreFinal = (scoreGenero * PESO_GENERO) + (scoreDuracao * PESO_DURACAO)
				+ (scorePopularidade * PESO_POPULARIDADE) + (scoreAfinidade * PESO_AFINIDADE);

		double scoreNormalizado = Math.max(SCORE_MINIMO, Math.min(SCORE_MAXIMO, scoreFinal));
		String justificativa = montarJustificativa(filme, scoreGenero, scoreDuracao, scoreAfinidade);

		return new Recomendacao(filme, scoreNormalizado, justificativa);
	}

	// -------------------------------------------------------------------------
	// Cálculos individuais (package-private para teste direto)
	// -------------------------------------------------------------------------

	public double calcularScoreGenero(Filme filme, PerfilCinefilo perfil) {
		List<Genero> generos = filme.getGeneros();
		if (generos.isEmpty()) {
			return 0.0;
		}
		double somaPesos = generos.stream().mapToDouble(perfil::getPesoGenero).sum();
		return (somaPesos / generos.size()) * 100.0;
	}

	public double calcularScoreDuracao(Filme filme, PerfilCinefilo perfil) {
		int duracao = filme.getDuracaoMinutos();
		int min = perfil.getDuracaoMinimaMinutos();
		int max = perfil.getDuracaoMaximaMinutos();

		if (duracao >= min && duracao <= max) {
			return 100.0;
		}

		int distanciaMinutos = duracao < min ? min - duracao : duracao - max;
		double fatorPenalidade = (double) distanciaMinutos / Math.max(max, 1);
		return Math.max(SCORE_MINIMO, 100.0 - (fatorPenalidade * 100.0));
	}

	public double calcularScoreAfinidade(Filme filme, PerfilCinefilo perfil) {
		Map<String, Integer> notas = perfil.getNotas();

		double mediaNota = notas.values().stream().mapToInt(Integer::intValue).average().orElse(NOTA_NEUTRA);

		return (mediaNota / NOTA_MAXIMA) * 100.0;
	}

	// -------------------------------------------------------------------------
	// Justificativa textual
	// -------------------------------------------------------------------------
	
	private String montarJustificativa(Filme filme, double scoreGenero, double scoreDuracao, double scoreAfinidade) {
		StringJoiner razoes = new StringJoiner("; ");

		if (scoreGenero >= 70) {
			razoes.add("compatível com seus gêneros preferidos");
		}
		if (scoreDuracao == 100.0) {
			razoes.add("duração dentro da sua faixa ideal");
		} else if (scoreDuracao < 50.0) {
			razoes.add("duração fora do ideal, mas pode valer a pena");
		}
		if (scoreAfinidade >= 70) {
			razoes.add("perfil histórico indica alta afinidade");
		}
		if (razoes.length() == 0) {
			razoes.add("recomendado com base no seu perfil geral");
		}

		return "Recomendamos \"" + filme.getTitulo() + "\" porque: " + razoes;
	}

	public List<Recomendacao> gerarRecomendacoes(List<Filme> filmes, PerfilCinefilo perfil) {

		return filmes.stream().map(filme -> calcularRecomendacao(filme, perfil))
				.sorted((r1, r2) -> Double.compare(r2.getScore(), r1.getScore())).toList();
	}

}
