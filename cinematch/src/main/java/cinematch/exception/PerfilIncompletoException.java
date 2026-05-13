package cinematch.exception;

/**
 * Lançada quando uma operação exige um perfil completo, mas algum campo
 * obrigatório está ausente.
 */
public class PerfilIncompletoException extends RuntimeException {

	public PerfilIncompletoException(String mensagem) {
		super(mensagem);
	}
}
