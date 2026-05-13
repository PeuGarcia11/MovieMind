package moviemind.model;

import java.util.Objects;

/**
 * Representa um usuário da plataforma.
 * Possui dados pessoais e um {@link PerfilCinefilo} com as preferências.
 */
public class Usuario {

    private final String nome;
    private final int idade;
    private final PerfilCinefilo perfil;
    private boolean notificacoesHabilitadas;

    public Usuario(String nome, int idade, PerfilCinefilo perfil) {
        this.nome   = Objects.requireNonNull(nome, "nome não pode ser nulo");
        this.idade  = idade;
        this.perfil = Objects.requireNonNull(perfil, "perfil não pode ser nulo");
        this.notificacoesHabilitadas = false;
    }

    public String getNome()                    { return nome; }
    public int getIdade()                      { return idade; }
    public PerfilCinefilo getPerfil()          { return perfil; }
    public boolean isNotificacoesHabilitadas() { return notificacoesHabilitadas; }

    
    public void setNotificacoesHabilitadas(boolean notificacoesHabilitadas) {
        this.notificacoesHabilitadas = notificacoesHabilitadas;
    }

    public void habilitarNotificacoes()  { this.notificacoesHabilitadas = true; }
    public void desabilitarNotificacoes() { this.notificacoesHabilitadas = false; }

    @Override
    public String toString() {
        return "Usuario{nome='" + nome + "', idade=" + idade + "}";
    }
}
