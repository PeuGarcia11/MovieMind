package cinematch;

import cinematch.model.*;
import cinematch.model.enums.*;
import cinematch.service.*;
import cinematch.util.GeradorAleatorio;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecomendadorServiceTest {

	@Mock
	private CatalogoFilmesAPI catalogo;

	@Mock
	private HistoricoUsuarioRepository historico;

	@Mock
	private NotificadorPush notificador;

	@Mock
	private GeradorAleatorio gerador;

	private CalculadoraScore calculadora;
	private FiltroFilmes filtro;

	private RecomendadorService service;

	private Usuario usuario;

	@BeforeEach
	void setUp() {

		calculadora = new CalculadoraScore();
		filtro = new FiltroFilmes();

		service = new RecomendadorService(catalogo, historico, notificador, gerador, calculadora, filtro);

		PerfilCinefilo perfil = new PerfilCinefilo();

		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
		perfil.setFaixaDuracao(90, 150);
		perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
		perfil.adicionarIdioma(Idioma.INGLES);

		usuario = new Usuario("Maria", 28, perfil);
	}

	@Test
	@DisplayName("deve_RetornarTopN_Quando_HaFilmesValidos")
	void deve_RetornarTopN_Quando_HaFilmesValidos() {

		// Arrange

		Filme filme1 = new Filme("1", "Interestelar", 2014, 140, List.of(Genero.FICCAO_CIENTIFICA),
				ClassificacaoEtaria.DOZE, Idioma.INGLES, 90);

		when(catalogo.buscarTodos()).thenReturn(List.of(filme1));

		// Act

		List<Recomendacao> resultado = service.recomendar(usuario, 1);

		// Assert

		assertEquals(1, resultado.size());

		verify(catalogo, times(1)).buscarTodos();

		verify(historico, times(1)).registrarRecomendacao(eq(usuario), anyList());
	}

	@Test
	@DisplayName("deve_RetornarListaVazia_Quando_APIFalha")
	void deve_RetornarListaVazia_Quando_APIFalha() {

		// Arrange

		when(catalogo.buscarTodos()).thenThrow(new RuntimeException());

		// Act

		List<Recomendacao> resultado = service.recomendar(usuario, 5);

		// Assert

		assertTrue(resultado.isEmpty());

		verify(notificador, never()).enviar(any(), any());
	}

	@Test
	@DisplayName("deve_ChamarNotificador_Quando_NotificacaoAtivada")
	void deve_ChamarNotificador_Quando_NotificacaoAtivada() {

		// Arrange

		usuario.setNotificacoesHabilitadas(true);

		Filme filme = new Filme("1", "Duna", 2024, 140, List.of(Genero.FICCAO_CIENTIFICA), ClassificacaoEtaria.DOZE,
				Idioma.INGLES, 95);

		when(catalogo.buscarTodos()).thenReturn(List.of(filme));

		// Act

		service.recomendar(usuario, 1);

		// Assert

		verify(notificador, times(1)).enviar(eq(usuario), anyList());
	}
}
