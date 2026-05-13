package cinematch;

import cinematch.exception.DuracaoInvalidaException;
import cinematch.exception.PesoInvalidoException;
import cinematch.model.PerfilCinefilo;
import cinematch.model.enums.ClassificacaoEtaria;
import cinematch.model.enums.Genero;
import cinematch.model.enums.Idioma;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
@DisplayName("PerfilCinefiloTest — criação, validações e operações do perfil")
class PerfilCinefiloTest {

	private PerfilCinefilo perfil;

	@BeforeEach
	void setUp() {
		perfil = new PerfilCinefilo();
		perfil.adicionarIdioma(Idioma.PORTUGUES);
		perfil.adicionarIdioma(Idioma.INGLES);
		perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
		perfil.setFaixaDuracao(90, 150);
	}

	// -------------------------------------------------------------------------
	// Pesos de gênero
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_ArmazenarPeso_Quando_ValorValido")
	void deve_ArmazenarPeso_Quando_ValorValido() {
		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 0.9);

		assertEquals(0.9, perfil.getPesoGenero(Genero.FICCAO_CIENTIFICA), 0.001);
	}

	@ParameterizedTest
	@CsvSource({ "-0.1", "1.1", "2.0", "-1.0" })
	@DisplayName("deve_LancarPesoInvalidoException_Quando_PesoForaDoIntervalo")
	void deve_LancarPesoInvalidoException_Quando_PesoForaDoIntervalo(double pesoInvalido) {
		assertThrows(PesoInvalidoException.class, () -> perfil.setPesoGenero(Genero.ACAO, pesoInvalido));
	}

	@Test
	@DisplayName("deve_RetornarPesoNeutro_Quando_GeneroNaoConfigurado")
	void deve_RetornarPesoNeutro_Quando_GeneroNaoConfigurado() {
		
		assertEquals(0.5, perfil.getPesoGenero(Genero.ANIMACAO), 0.001);
	}

	@Test
	@DisplayName("deve_AceitarPesosExtremos_Quando_ValoresZeroEUm")
	void deve_AceitarPesosExtremos_Quando_ValoresZeroEUm() {
		assertDoesNotThrow(() -> {
			perfil.setPesoGenero(Genero.TERROR, 0.0);
			perfil.setPesoGenero(Genero.ROMANCE, 1.0);
		});
		assertEquals(0.0, perfil.getPesoGenero(Genero.TERROR), 0.001);
		assertEquals(1.0, perfil.getPesoGenero(Genero.ROMANCE), 0.001);
	}

	// -------------------------------------------------------------------------
	// Faixa de duração
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_DefinirFaixaDuracao_Quando_MinimaInferiorMaxima")
	void deve_DefinirFaixaDuracao_Quando_MinimaInferiorMaxima() {
		perfil.setFaixaDuracao(60, 180);

		assertAll(() -> assertEquals(60, perfil.getDuracaoMinimaMinutos()),
				() -> assertEquals(180, perfil.getDuracaoMaximaMinutos()));
	}

	@Test
	@DisplayName("deve_LancarDuracaoInvalidaException_Quando_MinimaEhMaiorQueMaxima")
	void deve_LancarDuracaoInvalidaException_Quando_MinimaEhMaiorQueMaxima() {
		assertThrows(DuracaoInvalidaException.class, () -> perfil.setFaixaDuracao(200, 100));
	}

	@Test
	@DisplayName("deve_AceitarFaixaDuracao_Quando_MinimaIgualMaxima")
	void deve_AceitarFaixaDuracao_Quando_MinimaIgualMaxima() {
		assertDoesNotThrow(() -> perfil.setFaixaDuracao(120, 120));
	}

	// -------------------------------------------------------------------------
	// Histórico
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_RegistrarFilmeNoHistorico_Quando_MarcarComoAssistido")
	void deve_RegistrarFilmeNoHistorico_Quando_MarcarComoAssistido() {
		perfil.marcarComoAssistido("F01");

		assertTrue(perfil.getHistorico().contains("F01"));
		assertTrue(perfil.jaAssistiu("F01"));
	}

	@Test
	@DisplayName("deve_NaoConterFilme_Quando_NaoFoiMarcadoComoAssistido")
	void deve_NaoConterFilme_Quando_NaoFoiMarcadoComoAssistido() {
		assertFalse(perfil.jaAssistiu("F99"));
	}

	// -------------------------------------------------------------------------
	// Notas
	// -------------------------------------------------------------------------

	@ParameterizedTest
	@CsvSource({ "1", "3", "5" })
	@DisplayName("deve_RegistrarNota_Quando_ValorDentroDoIntervalo")
	void deve_RegistrarNota_Quando_ValorDentroDoIntervalo(int nota) {
		assertDoesNotThrow(() -> perfil.registrarNota("F01", nota));
		assertEquals(nota, perfil.getNotaPara("F01"));
	}

	@ParameterizedTest
	@CsvSource({ "0", "6", "-1" })
	@DisplayName("deve_LancarIllegalArgument_Quando_NotaForaDoIntervalo")
	void deve_LancarIllegalArgument_Quando_NotaForaDoIntervalo(int notaInvalida) {
		assertThrows(IllegalArgumentException.class, () -> perfil.registrarNota("F01", notaInvalida));
	}

	@Test
	@DisplayName("deve_RetornarNull_Quando_FilmeNuncaAvaliado")
	void deve_RetornarNull_Quando_FilmeNuncaAvaliado() {
		assertNull(perfil.getNotaPara("filme-nunca-avaliado"));
	}

	// -------------------------------------------------------------------------
	// Idiomas
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_ConterIdiomasAdicionados_Quando_SetupRealizado")
	void deve_ConterIdiomasAdicionados_Quando_SetupRealizado() {
		assertTrue(perfil.getIdiomasAceitos().contains(Idioma.PORTUGUES));
		assertTrue(perfil.getIdiomasAceitos().contains(Idioma.INGLES));
	}

	@Nested
	@DisplayName("QuandoPerfilSemIdiomas")
	class QuandoPerfilSemIdiomas {

		@Test
		@DisplayName("deve_RetornarConjuntoVazio_Quando_NenhumIdiomaConfigurado")
		void deve_RetornarConjuntoVazio_Quando_NenhumIdiomaConfigurado() {
			PerfilCinefilo perfilVazio = new PerfilCinefilo();
			assertTrue(perfilVazio.getIdiomasAceitos().isEmpty());
		}
	}
}
