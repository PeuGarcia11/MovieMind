package cinematch;

import cinematch.model.Filme;
import cinematch.model.enums.ClassificacaoEtaria;
import cinematch.model.enums.Genero;
import cinematch.model.enums.Idioma;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
@DisplayName("FilmeTest — atributos, igualdade e imutabilidade")
class FilmeTest {

	private Filme filmeBase;

	@BeforeEach
	void setUp() {
		filmeBase = new Filme("F01", "Duna: Parte Dois", 2024, 166, List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
				ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92);
	}

	// -------------------------------------------------------------------------
	// Atributos
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_RetornarTodosAtributos_Quando_FilmeCriado")
	void deve_RetornarTodosAtributos_Quando_FilmeCriado() {
		assertAll(() -> assertEquals("F01", filmeBase.getId()),
				() -> assertEquals("Duna: Parte Dois", filmeBase.getTitulo()),
				() -> assertEquals(2024, filmeBase.getAno()), () -> assertEquals(166, filmeBase.getDuracaoMinutos()),
				() -> assertEquals(ClassificacaoEtaria.QUATORZE, filmeBase.getClassificacao()),
				() -> assertEquals(Idioma.INGLES, filmeBase.getIdioma()),
				() -> assertEquals(92, filmeBase.getPopularidade()));
	}

	@Test
	@DisplayName("deve_ConterGenerosFiccaoCientificaEDrama_Quando_FilmeCriado")
	void deve_ConterGenerosFiccaoCientificaEDrama_Quando_FilmeCriado() {
		List<Genero> generos = filmeBase.getGeneros();
		assertAll(() -> assertTrue(generos.contains(Genero.FICCAO_CIENTIFICA)),
				() -> assertTrue(generos.contains(Genero.DRAMA)), () -> assertEquals(2, generos.size()));
	}

	// -------------------------------------------------------------------------
	// Igualdade baseada em ID
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_ConsiderarIguais_Quando_DoisFilmesComMesmoId")
	void deve_ConsiderarIguais_Quando_DoisFilmesComMesmoId() {
		Filme filmeCopia = new Filme("F01", // mesmo ID
				"Titulo Diferente", 2000, 90, List.of(Genero.COMEDIA), ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 10);

		assertEquals(filmeBase, filmeCopia);
		assertEquals(filmeBase.hashCode(), filmeCopia.hashCode());
	}

	@Test
	@DisplayName("deve_ConsiderarDiferentes_Quando_DoisFilmesComIdsDiferentes")
	void deve_ConsiderarDiferentes_Quando_DoisFilmesComIdsDiferentes() {
		Filme outroFilme = new Filme("F99", "Duna: Parte Dois", 2024, 166,
				List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA), ClassificacaoEtaria.QUATORZE, Idioma.INGLES, 92);

		assertNotEquals(filmeBase, outroFilme);
	}

	// -------------------------------------------------------------------------
	// Imutabilidade
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_LancarExcecao_Quando_TentarModificarListaDeGeneros")
	void deve_LancarExcecao_Quando_TentarModificarListaDeGeneros() {
		assertThrows(UnsupportedOperationException.class, () -> filmeBase.getGeneros().add(Genero.TERROR));
	}

	// -------------------------------------------------------------------------
	// Validação de nulls
	// -------------------------------------------------------------------------

	@Test
	@DisplayName("deve_LancarNullPointer_Quando_IdForNulo")
	void deve_LancarNullPointer_Quando_IdForNulo() {
		assertThrows(NullPointerException.class, () -> new Filme(null, "Titulo", 2024, 100, List.of(Genero.DRAMA),
				ClassificacaoEtaria.LIVRE, Idioma.PORTUGUES, 50));
	}
}
