package cinematch;

import cinematch.model.Filme;
import cinematch.model.PerfilCinefilo;
import cinematch.model.Recomendacao;
import cinematch.model.enums.ClassificacaoEtaria;
import cinematch.model.enums.Genero;
import cinematch.model.enums.Idioma;
import cinematch.service.CalculadoraScore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
@DisplayName("CalculadoraScoreTest — fórmula de compatibilidade")
class CalculadoraScoreTest {

	private CalculadoraScore calculadora;
	private PerfilCinefilo perfil;

	@BeforeEach
	void setUp() {
		calculadora = new CalculadoraScore();

		perfil = new PerfilCinefilo();
		perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 0.9);
		perfil.setPesoGenero(Genero.DRAMA, 0.6);
		perfil.setPesoGenero(Genero.COMEDIA, 0.5);
		perfil.setPesoGenero(Genero.ROMANCE, 0.4);
		perfil.setPesoGenero(Genero.TERROR, 0.0);
		perfil.setFaixaDuracao(90, 150);
		perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
		perfil.adicionarIdioma(Idioma.INGLES);
		perfil.adicionarIdioma(Idioma.PORTUGUES);
	}

	// -------------------------------------------------------------------------
	// Score de gênero
	// -------------------------------------------------------------------------

	@ParameterizedTest
	@CsvSource({ "1.0, 1.0, 1.0, 100.0", "0.5, 0.5, 0.5,  50.0", "0.0, 0.0, 0.0,   0.0" })
	@DisplayName("deve_CalcularScoreGenero_ConformePesosMedios")
	void deve_CalcularScoreGenero_ConformePesosMedios(double p1, double p2, double p3, double esperado) {
		PerfilCinefilo perfilParam = new PerfilCinefilo();
		perfilParam.setPesoGenero(Genero.ACAO, p1);
		perfilParam.setPesoGenero(Genero.DRAMA, p2);
		perfilParam.setPesoGenero(Genero.COMEDIA, p3);

		Filme filme = filmeComGeneros(List.of(Genero.ACAO, Genero.DRAMA, Genero.COMEDIA));

		double score = calculadora.calcularScoreGenero(filme, perfilParam);

		assertEquals(esperado, score, 0.5);
	}

	@Test
	@DisplayName("deve_RetornarScoreGeneroMaximo_Quando_TodosGenerosAmados")
	void deve_RetornarScoreGeneroMaximo_Quando_TodosGenerosAmados() {
		PerfilCinefilo perfilAmante = new PerfilCinefilo();
		perfilAmante.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
		perfilAmante.setPesoGenero(Genero.DRAMA, 1.0);

		Filme filme = filmeComGeneros(List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA));

		double score = calculadora.calcularScoreGenero(filme, perfilAmante);

		assertEquals(100.0, score, 0.001);
	}

	@Test
	@DisplayName("deve_RetornarScoreGeneroBaixo_Quando_GeneroNaoPreferido")
	void deve_RetornarScoreGeneroBaixo_Quando_GeneroNaoPreferido() {
		
		Filme filmeTerror = filmeComGeneros(List.of(Genero.TERROR));

		PerfilCinefilo perfilSemTerror = new PerfilCinefilo();
		perfilSemTerror.setPesoGenero(Genero.TERROR, 0.1);

		double score = calculadora.calcularScoreGenero(filmeTerror, perfilSemTerror);

		assertTrue(score <= 15.0, "Score de gênero com peso 0.1 deveria ser baixo, foi: " + score);
	}

	// -------------------------------------------------------------------------
	// Score de duração
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_RetornarScoreDuracaoMaximo_Quando_DentroFaixaPreferida")
	void deve_RetornarScoreDuracaoMaximo_Quando_DentroFaixaPreferida() {
		Filme filmeDentroFaixa = filmeComDuracao(120); // dentro de [90, 150]

		double score = calculadora.calcularScoreDuracao(filmeDentroFaixa, perfil);

		assertEquals(100.0, score, 0.001);
	}

	@Test
	@DisplayName("deve_ReducirScoreDuracao_Quando_FilmeUltrapassaFaixaPreferida")
	void deve_ReducirScoreDuracao_Quando_FilmeUltrapassaFaixaPreferida() {
		Filme filmeForaDaFaixa = filmeComDuracao(180); // 30 min acima do max=150

		double scoreDentro = calculadora.calcularScoreDuracao(filmeComDuracao(120), perfil);
		double scoreForaDaFaixa = calculadora.calcularScoreDuracao(filmeForaDaFaixa, perfil);

		assertTrue(scoreForaDaFaixa < scoreDentro,
				"Score com duração fora da faixa deveria ser menor que dentro da faixa");
	}

	// -------------------------------------------------------------------------
	// Score final (limites)
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_NaoPassarDe100_Quando_TodosComponentesMaximos")
	void deve_NaoPassarDe100_Quando_TodosComponentesMaximos() {
		PerfilCinefilo perfilPerfeito = new PerfilCinefilo();
		perfilPerfeito.setPesoGenero(Genero.FICCAO_CIENTIFICA, 1.0);
		perfilPerfeito.setPesoGenero(Genero.DRAMA, 1.0);
		perfilPerfeito.setFaixaDuracao(100, 200);
		perfilPerfeito.adicionarIdioma(Idioma.INGLES);
		perfilPerfeito.registrarNota("outro", 5);

		Filme filmePerfeito = new Filme("FP", "Perfeito", 2024, 120, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
				ClassificacaoEtaria.LIVRE, Idioma.INGLES, 100);

		Recomendacao rec = calculadora.calcularRecomendacao(filmePerfeito, perfilPerfeito);

		assertTrue(rec.getScore() <= 100.0, "Score não pode ultrapassar 100");
	}

	@Test
	@DisplayName("deve_NaoFicarNegativo_Quando_TodosComponentesPiores")
	void deve_NaoFicarNegativo_Quando_TodosComponentesPiores() {
		PerfilCinefilo perfilRuim = new PerfilCinefilo();
		perfilRuim.setFaixaDuracao(10, 20); // filme com 300 min ficará longe

		Filme filmeRuim = filmeComDuracao(300);

		Recomendacao rec = calculadora.calcularRecomendacao(filmeRuim, perfilRuim);

		assertTrue(rec.getScore() >= 0.0, "Score não pode ser negativo");
	}

	// -------------------------------------------------------------------------
	// Justificativa
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_RetornarJustificativaNaoNula_Quando_ScoreCalculado")
	void deve_RetornarJustificativaNaoNula_Quando_ScoreCalculado() {
		Filme filme = filmeComGeneros(List.of(Genero.FICCAO_CIENTIFICA));

		Recomendacao rec = calculadora.calcularRecomendacao(filme, perfil);

		assertNotNull(rec.getJustificativa());
		assertFalse(rec.getJustificativa().isBlank());
	}

	// -------------------------------------------------------------------------
	// Auxiliares
	// -------------------------------------------------------------------------

	private Filme filmeComGeneros(List<Genero> generos) {
		return new Filme("FX", "Filme Teste", 2024, 120, generos, ClassificacaoEtaria.LIVRE, Idioma.INGLES, 50);
	}

	private Filme filmeComDuracao(int duracao) {
		return new Filme("FX", "Filme Teste", 2024, duracao, List.of(Genero.DRAMA), ClassificacaoEtaria.LIVRE,
				Idioma.INGLES, 50);
	}
}
