##  MovieMind

Sistema de recomendação de filmes desenvolvido em Java utilizando princípios de orientação a objetos, testes unitários com JUnit 5 e mocks com Mockito.


## Integrantes
- Pedro Carneiro Franco Garcia
- Daniel Brandão Guimarães


## Tecnologias Utilizadas
- Java 21
- Maven
- JUnit 5
- Mockito
- Eclipse IDE


## Como Rodar o Projeto

1. Clone o repositório:

git clone URL_DO_REPOSITORIO

3. Abra o projeto no Eclipse.

4. Atualize as dependências Maven:
- Clique com botão direito no projeto
- Maven → Update Project

4. Execute a classe:
text
src/main/java/moviemind/Main.java

5. O sistema exibirá recomendações de filmes no console. Como o sistema é mais focado em testes, já existe um perfil ficticio para rodar o main, sendo assim escolhidos os filmes de acordo com as preferencias do usuarioExemplo.


## Como Executar os Testes

No Eclipse:

1. Clique com botão direito no projeto
2. Run As → JUnit Test

Ou execute testes individualmente clicando nas classes de teste dentro de:
src/test/java

## Cobertura de Testes

<img width="1301" height="761" alt="image" src="https://github.com/user-attachments/assets/4a5f2a17-fcb9-40ee-9db5-7d4ca1c1152c" />


## Diagrama de Classes

<img width="961" height="1100" alt="DiagramadeClassesDanielPedro drawio" src="https://github.com/user-attachments/assets/fc4f9827-3523-4345-ab62-06464086ea9c" />


## Funcionalidades

- Recomendação personalizada de filmes
- Filtro por classificação etária
- Filtro por idioma
- Exclusão de filmes já assistidos
- Sistema de score de compatibilidade
- Notificações de recomendação
- Testes unitários com Mockito


## Estrutura do Projeto
``` text
MovieMind
│
├── README.md
├── pom.xml
├── .gitignore
│
├── docs
│   ├── cobertura-testes.png
│   ├── diagrama-classes.png
│
├── src
│   │
│   ├── main
│   │   └── java
│   │       │
│   │       └── moviemind
│   │           │
│   │           ├── Main.java
│   │           │
│   │           ├── model
│   │           │   │
│   │           │   ├── Filme.java
│   │           │   ├── PerfilCinefilo.java
│   │           │   ├── Recomendacao.java
│   │           │   └── Usuario.java
│   │           │
│   │           ├── model
│   │           │   └── enums
│   │           │       │
│   │           │       ├── ClassificacaoEtaria.java
│   │           │       ├── Genero.java
│   │           │       └── Idioma.java
│   │           │
│   │           ├── service
│   │           │   │
│   │           │   ├── CalculadoraScore.java
│   │           │   ├── FiltroFilmes.java
│   │           │   ├── RecomendadorService.java
│   │           │   ├── CatalogoFilmesAPI.java
│   │           │   ├── HistoricoUsuarioRepository.java
│   │           │   ├── NotificadorPush.java
│   │           │   └── BancoFilmesFake.java
│   │           │
│   │           ├── util
│   │           │   │
│   │           │   └── GeradorAleatorio.java
│   │           │
│   │           └── exception
│   │               │
│   │               ├── DuracaoInvalidaException.java
│   │               └── PesoInvalidoException.java
│   │
│   └── test
│       └── java
│           │
│           └── moviemind
│               │
│               ├── CalculadoraScoreTest.java
│               ├── FilmeTest.java
│               ├── FiltroFilmesTest.java
│               ├── PerfilCinefiloTest.java
│               └── RecomendadorServiceTest.java
│
└── target

