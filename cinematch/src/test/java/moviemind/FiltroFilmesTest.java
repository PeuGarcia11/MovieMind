package moviemind;

import moviemind.model.Filme;
import moviemind.model.PerfilCinefilo;
import moviemind.model.enums.ClassificacaoEtaria;
import moviemind.model.enums.Genero;
import moviemind.model.enums.Idioma;
import moviemind.service.FiltroFilmes;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unitario")
@DisplayName("FiltroFilmesTest — regras de exclusão do catálogo")
class FiltroFilmesTest {

    private FiltroFilmes filtro;
    private PerfilCinefilo perfil;

    // Filmes do catálogo de teste
    private Filme filmeFC;
    private Filme filmeTerror;
    private Filme filmeJaAssistido;
    private Filme filmeClassificacaoAlta;
    private Filme filmeIdiomaErrado;

    @BeforeEach
    void setUp() {
        filtro = new FiltroFilmes();

        perfil = new PerfilCinefilo();
        perfil.setPesoGenero(Genero.FICCAO_CIENTIFICA, 0.9);
        perfil.setPesoGenero(Genero.DRAMA,             0.6);
        perfil.setPesoGenero(Genero.TERROR,            0.0); // bloqueado
        perfil.setFaixaDuracao(90, 150);
        perfil.setClassificacaoMaxima(ClassificacaoEtaria.DEZESSEIS);
        perfil.adicionarIdioma(Idioma.INGLES);
        perfil.adicionarIdioma(Idioma.PORTUGUES);

        filmeFC = new Filme(
            "F07", "A Chegada", 2016, 116,
            List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
            ClassificacaoEtaria.DOZE, Idioma.INGLES, 84
        );

        filmeTerror = new Filme(
            "F03", "O Iluminado", 1980, 146,
            List.of(Genero.TERROR),
            ClassificacaoEtaria.DEZOITO, Idioma.INGLES, 88
        );

        filmeJaAssistido = new Filme(
            "F04", "Interestelar", 2014, 169,
            List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA),
            ClassificacaoEtaria.DOZE, Idioma.INGLES, 95
        );
        perfil.marcarComoAssistido("F04"); // marca F04 no histórico

        filmeClassificacaoAlta = new Filme(
            "F05", "Tropa de Elite", 2007, 115,
            List.of(Genero.ACAO, Genero.DRAMA),
            ClassificacaoEtaria.DEZOITO, // > limite 16
            Idioma.PORTUGUES, 80
        );

        filmeIdiomaErrado = new Filme(
            "F08", "Parasite", 2019, 132,
            List.of(Genero.DRAMA, Genero.SUSPENSE),
            ClassificacaoEtaria.QUATORZE,
            Idioma.COREANO, // não aceito
            90
        );
    }

    // -------------------------------------------------------------------------
    // Regra 1 — Histórico
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("deve_RemoverFilme_Quando_JaFoiAssistido")
    void deve_RemoverFilme_Quando_JaFoiAssistido() {
        List<Filme> resultado = filtro.filtrar(List.of(filmeJaAssistido, filmeFC), perfil);

        assertFalse(resultado.contains(filmeJaAssistido),
            "Filme já assistido não deve aparecer na lista filtrada");
    }

    @Test
    @DisplayName("deve_MaterFilme_Quando_NaoEstaNaHistorico")
    void deve_MaterFilme_Quando_NaoEstaNaHistorico() {
        List<Filme> resultado = filtro.filtrar(List.of(filmeFC), perfil);

        assertTrue(resultado.contains(filmeFC));
    }

    // -------------------------------------------------------------------------
    // Regra 2 — Classificação etária
    // -------------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({
        "DEZOITO,    DEZESSEIS, false",
        "DEZESSEIS,  DEZESSEIS,  true",
        "QUATORZE,  DEZESSEIS,  true"
    })
    @DisplayName("deve_AceitarOuRejeitar_FilmePorClassificacao")
    void deve_AceitarOuRejeitar_FilmePorClassificacao(
            ClassificacaoEtaria classificacaoFilme,
            ClassificacaoEtaria classificacaoMaxPerfil,
            boolean esperaAceitar) {

        PerfilCinefilo perfilLocal = new PerfilCinefilo();
        perfilLocal.setClassificacaoMaxima(classificacaoMaxPerfil);
        perfilLocal.adicionarIdioma(Idioma.INGLES);

        Filme filme = new Filme("FT", "Teste", 2024, 120,
            List.of(Genero.DRAMA), classificacaoFilme, Idioma.INGLES, 50);

        List<Filme> resultado = filtro.filtrar(List.of(filme), perfilLocal);

        assertEquals(esperaAceitar, resultado.contains(filme));
    }

    @Test
    @DisplayName("deve_RemoverFilme_Quando_ClassificacaoAcimaDoLimiteDoPerfil")
    void deve_RemoverFilme_Quando_ClassificacaoAcimaDoLimiteDoPerfil() {
        List<Filme> resultado = filtro.filtrar(List.of(filmeClassificacaoAlta, filmeFC), perfil);

        assertFalse(resultado.contains(filmeClassificacaoAlta));
        assertTrue(resultado.contains(filmeFC));
    }

    // -------------------------------------------------------------------------
    // Regra 3 — Idioma
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("deve_RemoverFilme_Quando_IdiomaForaDaListaAceita")
    void deve_RemoverFilme_Quando_IdiomaForaDaListaAceita() {
        List<Filme> resultado = filtro.filtrar(List.of(filmeIdiomaErrado, filmeFC), perfil);

        assertFalse(resultado.contains(filmeIdiomaErrado),
            "Filme em idioma não aceito deve ser removido");
        assertTrue(resultado.contains(filmeFC));
    }

    // -------------------------------------------------------------------------
    // Regra 4 — Gênero bloqueado (peso 0.0)
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("deve_RemoverFilme_Quando_ContemGeneroBloqueadoPeloPerfil")
    void deve_RemoverFilme_Quando_ContemGeneroBloqueadoPeloPerfil() {
        // O perfil definiu Terror com peso 0.0
        List<Filme> resultado = filtro.filtrar(List.of(filmeTerror, filmeFC), perfil);

        assertFalse(resultado.contains(filmeTerror),
            "Filme com gênero bloqueado (peso 0.0) deve ser removido");
    }

    // -------------------------------------------------------------------------
    // Catálogo vazio
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("deve_RetornarListaVazia_Quando_CatalogoEstaVazio")
    void deve_RetornarListaVazia_Quando_CatalogoEstaVazio() {
        List<Filme> resultado = filtro.filtrar(Collections.emptyList(), perfil);

        assertNotNull(resultado, "Resultado não pode ser null");
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("deve_RetornarListaVazia_Quando_TodosFilmesForamFiltrados")
    void deve_RetornarListaVazia_Quando_TodosFilmesForamFiltrados() {
        List<Filme> catalogoTodosBloqueados = List.of(
            filmeTerror,           // gênero bloqueado
            filmeJaAssistido,      // já assistido
            filmeClassificacaoAlta,// classificação alta
            filmeIdiomaErrado      // idioma errado
        );

        List<Filme> resultado = filtro.filtrar(catalogoTodosBloqueados, perfil);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // -------------------------------------------------------------------------
    // Cenário feliz — múltiplos filmes elegíveis
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("deve_ManterApenasFimesElegiveis_Quando_CatalogoMisto")
    void deve_ManterApenasFimesElegiveis_Quando_CatalogoMisto() {
        Filme filmeExtra = new Filme(
            "F02", "Ela (Her)", 2013, 126,
            List.of(Genero.FICCAO_CIENTIFICA, Genero.DRAMA, Genero.ROMANCE),
            ClassificacaoEtaria.DEZESSEIS, Idioma.INGLES, 78
        );

        List<Filme> catalogo = List.of(
            filmeFC, filmeExtra,            // elegíveis
            filmeTerror, filmeJaAssistido,  // bloqueados
            filmeClassificacaoAlta, filmeIdiomaErrado
        );

        List<Filme> resultado = filtro.filtrar(catalogo, perfil);

        assertAll(
            () -> assertEquals(2, resultado.size()),
            () -> assertTrue(resultado.contains(filmeFC)),
            () -> assertTrue(resultado.contains(filmeExtra))
        );
    }
}
