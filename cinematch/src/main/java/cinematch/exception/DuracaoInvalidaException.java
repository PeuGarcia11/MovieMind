package cinematch.exception;

/**
 * Lançada quando a duração mínima configurada no perfil é maior do que a
 * máxima.
 */
public class DuracaoInvalidaException extends RuntimeException {

	public DuracaoInvalidaException(String mensagem) {
		super(mensagem);
	}
}
