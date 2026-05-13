package cinematch.model;

import cinematch.exception.DuracaoInvalidaException;
import cinematch.exception.PesoInvalidoException;
import cinematch.model.enums.ClassificacaoEtaria;
import cinematch.model.enums.Genero;
import cinematch.model.enums.Idioma;

import java.util.*;

/**
 * Representa as preferências cinematográficas de um usuário. Contém pesos por
 * gênero, restrições de duração, classificação etária, idiomas aceitos,
 * histórico de filmes assistidos e notas atribuídas.
 */
public class PerfilCinefilo {

	private static final double PESO_MINIMO = 0.0;
	private static final double PESO_MAXIMO = 1.0;
	private static final int NOTA_MINIMA = 1;
	private static final int NOTA_MAXIMA = 5;

	private final Map<Genero, Double> pesosGenero;
	private int duracaoMinimaMinutos;
	private int duracaoMaximaMinutos;
	private ClassificacaoEtaria classificacaoMaxima;
	private final Set<Idioma> idiomasAceitos;
	private final Set<String> historico; 
	private final Map<String, Integer> notas; 

	public PerfilCinefilo() {
		this.pesosGenero = new EnumMap<>(Genero.class);
		this.idiomasAceitos = new HashSet<>();
		this.historico = new HashSet<>();
		this.notas = new HashMap<>();
		this.duracaoMinimaMinutos = 0;
		this.duracaoMaximaMinutos = Integer.MAX_VALUE;
		this.classificacaoMaxima = ClassificacaoEtaria.DEZOITO;
	}

	// -------------------------------------------------------------------------
	// Pesos de gênero
	// -------------------------------------------------------------------------

	public void setPesoGenero(Genero genero, double peso) {
		if (peso < PESO_MINIMO || peso > PESO_MAXIMO) {
			throw new PesoInvalidoException(
					"Peso inválido para " + genero + ": " + peso + ". O valor deve estar entre 0.0 e 1.0.");
		}
		pesosGenero.put(genero, peso);
	}

	public double getPesoGenero(Genero genero) {
		return pesosGenero.getOrDefault(genero, 0.5);
	}

	public Map<Genero, Double> getPesosGenero() {
		return Collections.unmodifiableMap(pesosGenero);
	}

	// -------------------------------------------------------------------------
	// Faixa de duração
	// -------------------------------------------------------------------------

	public void setFaixaDuracao(int minimaMinutos, int maximaMinutos) {
		if (minimaMinutos > maximaMinutos) {
			throw new DuracaoInvalidaException("Duração mínima (" + minimaMinutos + " min) "
					+ "não pode ser maior que a máxima (" + maximaMinutos + " min).");
		}
		this.duracaoMinimaMinutos = minimaMinutos;
		this.duracaoMaximaMinutos = maximaMinutos;
	}

	public int getDuracaoMinimaMinutos() {
		return duracaoMinimaMinutos;
	}

	public int getDuracaoMaximaMinutos() {
		return duracaoMaximaMinutos;
	}

	// -------------------------------------------------------------------------
	// Classificação etária
	// -------------------------------------------------------------------------

	public void setClassificacaoMaxima(ClassificacaoEtaria classificacaoMaxima) {
		this.classificacaoMaxima = Objects.requireNonNull(classificacaoMaxima);
	}

	public ClassificacaoEtaria getClassificacaoMaxima() {
		return classificacaoMaxima;
	}

	// -------------------------------------------------------------------------
	// Idiomas
	// -------------------------------------------------------------------------

	public void adicionarIdioma(Idioma idioma) {
		idiomasAceitos.add(Objects.requireNonNull(idioma));
	}

	public void removerIdioma(Idioma idioma) {
		idiomasAceitos.remove(idioma);
	}

	public Set<Idioma> getIdiomasAceitos() {
		return Collections.unmodifiableSet(idiomasAceitos);
	}

	// -------------------------------------------------------------------------
	// Histórico de filmes assistidos
	// -------------------------------------------------------------------------

	public void marcarComoAssistido(String idFilme) {
		historico.add(Objects.requireNonNull(idFilme));
	}

	public Set<String> getHistorico() {
		return Collections.unmodifiableSet(historico);
	}

	public boolean jaAssistiu(String idFilme) {
		return historico.contains(idFilme);
	}

	// -------------------------------------------------------------------------
	// Notas
	// -------------------------------------------------------------------------

	public void registrarNota(String idFilme, int nota) {
		if (nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
			throw new IllegalArgumentException(
					"Nota inválida: " + nota + ". Deve estar entre " + NOTA_MINIMA + " e " + NOTA_MAXIMA + ".");
		}
		notas.put(Objects.requireNonNull(idFilme), nota);
	}

	public Integer getNotaPara(String idFilme) {
		return notas.get(idFilme);
	}

	public Map<String, Integer> getNotas() {
		return Collections.unmodifiableMap(notas);
	}
}
