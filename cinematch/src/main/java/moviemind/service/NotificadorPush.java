package moviemind.service;

import moviemind.model.Recomendacao;
import moviemind.model.Usuario;

import java.util.List;

/**
 * Contrato para envio de notificações push ao usuário.
 * Em produção, implementado via Firebase / OneSignal.
 * Nos testes, deve ser mockado com Mockito.
 */
public interface NotificadorPush {

    /**
     * Envia uma notificação informando o usuário sobre suas novas recomendações.
     *
     * @param usuario        destinatário da notificação
     * @param recomendacoes  lista de recomendações geradas
     */
    void enviar(Usuario usuario, List<Recomendacao> recomendacoes);
}
