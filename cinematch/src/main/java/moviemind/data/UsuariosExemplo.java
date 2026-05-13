package moviemind.data;

import moviemind.model.PerfilCinefilo;
import moviemind.model.Usuario;
import moviemind.model.enums.*;

public class UsuariosExemplo {

	public static Usuario criarPedro() {

		PerfilCinefilo perfil = new PerfilCinefilo();

		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);

		perfil.setPesoGenero(Genero.DRAMA, 0.8);

		perfil.setPesoGenero(Genero.ACAO, 0.8);

		perfil.setPesoGenero(Genero.COMEDIA, 0.3);

		perfil.setPesoGenero(Genero.ROMANCE, 0.5);

		perfil.setPesoGenero(Genero.TERROR, 0.2);

		perfil.setPesoGenero(Genero.SUSPENSE, 0.3);

		perfil.setFaixaDuracao(90, 180);

		perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);

		perfil.adicionarIdioma(Idioma.INGLES);

		perfil.adicionarIdioma(Idioma.PORTUGUES);

		perfil.registrarNota("2", 5);

		Usuario usuario = new Usuario("Pedro", 20, perfil);

		usuario.setNotificacoesHabilitadas(true);

		return usuario;
	}
}
