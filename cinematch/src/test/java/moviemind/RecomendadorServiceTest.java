
package moviemind;

import moviemind.model.*;
import moviemind.model.enums.*;
import moviemind.service.*;
import moviemind.util.GeradorAleatorio;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;

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
	
	@Spy
	private CalculadoraScore calculadora;
	
	@Spy
	private FiltroFilmes filtro;

	@InjectMocks
	private RecomendadorService service;

	private Usuario usuario;

	@BeforeEach
	void setUp() {

		PerfilCinefilo perfil = new PerfilCinefilo();

		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
		perfil.setFaixaDuracao(90, 150);
		perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
		perfil.adicionarIdioma(Idioma.INGLES);

		usuario = new Usuario("Maria", 28, perfil);
	}

	@Test
	@DisplayName("deve capturar recomendações registradas no histórico")
	void deveCapturarRecomendacoesRegistradas() {

		Filme filme1 = new Filme("1", "Interestelar", 2014, 140, List.of(Genero.FICCAO_CIENTIFICA),
				ClassificacaoEtaria.DOZE, Idioma.INGLES, 90);

		Filme filme2 = new Filme("2", "A Chegada", 2016, 116, List.of(Genero.FICCAO_CIENTIFICA),
				ClassificacaoEtaria.DOZE, Idioma.INGLES, 88);

		when(catalogo.buscarTodos()).thenReturn(List.of(filme1, filme2));

		service.recomendar(usuario, 2);

		ArgumentCaptor<List<Recomendacao>> captor = ArgumentCaptor.forClass(List.class);

		verify(historico).registrarRecomendacao(eq(usuario), captor.capture());

		List<Recomendacao> registradas = captor.getValue();

		assertAll(() -> assertEquals(2, registradas.size()), () -> assertNotNull(registradas.get(0)),
				() -> assertTrue(registradas.get(0).getScore() >= registradas.get(1).getScore()));
	}

	@Test
	@DisplayName("deve retornar top N corretamente")
	void deveRetornarTopN() {

		Filme filme1 = new Filme("1", "Interestelar", 2014, 140, List.of(Genero.FICCAO_CIENTIFICA),
				ClassificacaoEtaria.DOZE, Idioma.INGLES, 90);

		when(catalogo.buscarTodos()).thenReturn(List.of(filme1));

		List<Recomendacao> resultado = service.recomendar(usuario, 1);

		assertEquals(1, resultado.size());

		verify(catalogo, atLeastOnce()).buscarTodos();

		verify(historico).registrarRecomendacao(eq(usuario), any(List.class));
	}

	@Test
	@DisplayName("deve usar stub sequencial no gerador aleatório")
	void deveUsarStubSequencial() {

		when(gerador.sortearInteiro(0, 10)).thenReturn(2, 7, 0);

		assertEquals(2, gerador.sortearInteiro(0, 10));

		assertEquals(7, gerador.sortearInteiro(0, 10));

		assertEquals(0, gerador.sortearInteiro(0, 10));
	}

	@Test
	@DisplayName("deve retornar vazio quando API falha")
	void deveRetornarListaVaziaQuandoApiFalha() {

		when(catalogo.buscarTodos()).thenThrow(new RuntimeException());

		List<Recomendacao> resultado = service.recomendar(usuario, 5);

		assertTrue(resultado.isEmpty());

		verify(notificador, never()).enviar(any(), any());
	}

	@Test
	@DisplayName("deve enviar notificação quando habilitada")
	void deveEnviarNotificacao() {

		usuario.setNotificacoesHabilitadas(true);

		Filme filme = new Filme("1", "Duna", 2024, 140, List.of(Genero.FICCAO_CIENTIFICA), ClassificacaoEtaria.DOZE,
				Idioma.INGLES, 95);

		when(catalogo.buscarTodos()).thenReturn(List.of(filme));

		service.recomendar(usuario, 1);

		verify(notificador, times(1)).enviar(eq(usuario), anyList());
	}

	@Test
	@DisplayName("modo surpresa deve retornar filme filtrado")
	void deveRetornarFilmeAleatorio() {

		Filme filme = new Filme("99", "A Chegada", 2016, 116, List.of(Genero.FICCAO_CIENTIFICA),
				ClassificacaoEtaria.DOZE, Idioma.INGLES, 88);

		when(catalogo.buscarTodos()).thenReturn(List.of(filme));

		when(gerador.sortearInteiro(0, 0)).thenReturn(0);

		Recomendacao recomendacao = service.recomendarAleatorio(usuario);

		assertNotNull(recomendacao);

		assertEquals("A Chegada", recomendacao.getFilme().getTitulo());
	}
}
