package cinematch.model;

import java.util.Objects;

/**
 * Resultado de uma recomendação: agrupa o filme recomendado, seu score e uma
 * justificativa textual legível pelo usuário. Imutável após a construção.
 */
public final class Recomendacao {

	private final Filme filme;
	private final double score;
	private final String justificativa;

	public Recomendacao(Filme filme, double score, String justificativa) {
		this.filme = Objects.requireNonNull(filme, "filme não pode ser nulo");
		this.score = score;
		this.justificativa = Objects.requireNonNull(justificativa, "justificativa não pode ser nula");
	}

	public Filme getFilme() {
		return filme;
	}

	public double getScore() {
		return score;
	}

	public String getJustificativa() {
		return justificativa;
	}

	@Override
	public String toString() {
		return String.format("Recomendacao{filme='%s', score=%.1f}", filme.getTitulo(), score);
	}
}
