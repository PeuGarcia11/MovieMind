package cinematch.model.enums;

/**
 * Classificação etária indicativa dos filmes, conforme padrão nacional. Cada
 * constante carrega a idade mínima necessária para assistir.
 */
public enum ClassificacaoEtaria {

	LIVRE(0), DEZ(10), DOZE(12), QUATORZE(14), DEZESSEIS(16), DEZOITO(18);

	private final int idadeMinima;

	ClassificacaoEtaria(int idadeMinima) {
		this.idadeMinima = idadeMinima;
	}

	public int getIdadeMinima() {
		return idadeMinima;
	}

	public boolean estaDeentroDoLimite(ClassificacaoEtaria maximaPermitida) {
		return this.idadeMinima <= maximaPermitida.idadeMinima;
	}
}
