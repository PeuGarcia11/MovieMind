package cinematch.exception;

/**
 * Lançada quando um peso de gênero informado está fora do intervalo [0.0, 1.0].
 */
public class PesoInvalidoException extends RuntimeException {

	public PesoInvalidoException(String mensagem) {
		super(mensagem);
	}
}
