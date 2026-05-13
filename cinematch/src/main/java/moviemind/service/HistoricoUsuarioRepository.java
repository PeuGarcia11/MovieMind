package moviemind.service;

import moviemind.model.Recomendacao;
import moviemind.model.Usuario;

import java.util.List;

/**
 * Contrato para persistência do histórico de recomendações do usuário.
 * Em produção, implementado com acesso a banco de dados.
 * Nos testes, deve ser mockado com Mockito.
 */
public interface HistoricoUsuarioRepository {

    /**
     * Persiste as recomendações geradas para o usuário nesta sessão.
     *
     * @param usuario        usuário que recebeu as recomendações
     * @param recomendacoes  lista de recomendações a registrar
     */
    void registrarRecomendacao(Usuario usuario, List<Recomendacao> recomendacoes);
}
