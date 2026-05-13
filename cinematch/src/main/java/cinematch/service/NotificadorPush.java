package cinematch.service;

import cinematch.model.Recomendacao;
import cinematch.model.Usuario;

import java.util.List;

/**
 * Contrato para envio de notificações push ao usuário. Em produção,
 * implementado via Firebase / OneSignal. Nos testes, deve ser mockado com
 * Mockito.
 */
public interface NotificadorPush {

	
	void enviar(Usuario usuario, List<Recomendacao> recomendacoes);
}
