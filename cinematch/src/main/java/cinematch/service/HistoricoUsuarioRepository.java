package cinematch.service;

import cinematch.model.Recomendacao;
import cinematch.model.Usuario;

import java.util.List;

/**
 * Contrato para persistência do histórico de recomendações do usuário. Em
 * produção, implementado com acesso a banco de dados. Nos testes, deve ser
 * mockado com Mockito.
 */
public interface HistoricoUsuarioRepository {

	
	void registrarRecomendacao(Usuario usuario, List<Recomendacao> recomendacoes);
}
