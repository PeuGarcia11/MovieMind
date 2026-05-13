package cinematch.model;

import cinematch.model.enums.ClassificacaoEtaria;
import cinematch.model.enums.Genero;
import cinematch.model.enums.Idioma;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Representa um filme do catálogo. Imutável — todos os atributos são definidos
 * na construção.
 */
public final class Filme {

	private final String id;
	private final String titulo;
	private final int ano;
	private final int duracaoMinutos;
	private final List<Genero> generos;
	private final ClassificacaoEtaria classificacao;
	private final Idioma idioma;
	private final int popularidade; // 0 a 100

	public Filme(String id, String titulo, int ano, int duracaoMinutos, List<Genero> generos,
			ClassificacaoEtaria classificacao, Idioma idioma, int popularidade) {

		this.id = Objects.requireNonNull(id, "id não pode ser nulo");
		this.titulo = Objects.requireNonNull(titulo, "titulo não pode ser nulo");
		this.ano = ano;
		this.duracaoMinutos = duracaoMinutos;
		this.generos = Collections.unmodifiableList(Objects.requireNonNull(generos));
		this.classificacao = Objects.requireNonNull(classificacao);
		this.idioma = Objects.requireNonNull(idioma);
		this.popularidade = popularidade;
	}

	public String getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public int getAno() {
		return ano;
	}

	public int getDuracaoMinutos() {
		return duracaoMinutos;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public ClassificacaoEtaria getClassificacao() {
		return classificacao;
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public int getPopularidade() {
		return popularidade;
	}

	/** Dois filmes são iguais se têm o mesmo ID. */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Filme outro))
			return false;
		return id.equals(outro.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Filme{id='" + id + "', titulo='" + titulo + "', score=" + popularidade + "}";
	}
}
