# CrudCidades

Sistema desktop para cadastro e gerenciamento de **cidades** e **clientes**, desenvolvido em Java como projeto academico da disciplina de Programacao Orientada a Objetos na **UTFPR (Universidade Tecnologica Federal do Parana)**.

---

## Sumario

- [O que e este sistema?](#o-que-e-este-sistema)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Como funciona o banco de dados](#como-funciona-o-banco-de-dados)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Arquitetura em camadas](#arquitetura-em-camadas)
- [Entidades e relacionamentos](#entidades-e-relacionamentos)
- [As telas do sistema](#as-telas-do-sistema)
- [Funcionalidades disponiveis](#funcionalidades-disponiveis)
- [Como executar](#como-executar)
- [Dependencias e configuracao](#dependencias-e-configuracao)

---

## O que e este sistema?

O **CrudCidades** e um aplicativo de janela (desktop) que permite ao usuario:

- **Cadastrar, editar e excluir cidades** (nome e sigla do estado)
- **Cadastrar, editar e excluir clientes** (nome, CPF, telefone, e-mail e cidade)
- **Pesquisar** registros pelo nome
- **Listar** todos os registros cadastrados em uma tabela

O sistema garante integridade dos dados, impedindo, por exemplo, que uma cidade seja excluida enquanto houver clientes vinculados a ela.

---

## Tecnologias utilizadas

| Tecnologia | Versao | Para que serve |
|---|---|---|
| **Java** | 17 | Linguagem de programacao principal |
| **Swing** | (nativo Java) | Construcao das janelas e componentes visuais |
| **Hibernate** | 6.4.4 | Mapeamento entre objetos Java e tabelas do banco de dados (ORM) |
| **H2 Database** | 2.2.224 | Banco de dados embutido, salvo em arquivo no disco |
| **Maven** | 3+ | Gerenciamento de dependencias e build do projeto |

---

## Como funciona o banco de dados

### Tipo de banco: H2 (embutido em arquivo)

O banco de dados **nao precisa de instalacao separada**. Ele e um arquivo chamado `cruddb.mv.db` que fica dentro da pasta `data/` do projeto. Quando voce executa o sistema pela primeira vez, esse arquivo e criado automaticamente.

```
CrudCidades/
└── data/
    └── cruddb.mv.db
```

### Tabelas criadas automaticamente

O Hibernate cria e atualiza as tabelas automaticamente com a configuracao `hibernate.hbm2ddl.auto=update`. As tabelas sao:

---

#### Tabela `cidades`

Armazena as cidades cadastradas.

| Coluna | Tipo | Obrigatorio | Descricao |
|---|---|---|---|
| `id` | BIGINT | Sim | Identificador unico, gerado automaticamente |
| `nome` | VARCHAR | Sim | Nome da cidade (ex: "Curitiba") |
| `uf` | VARCHAR(2) | Sim | Sigla do estado (ex: "PR") |

---

#### Tabela `clientes`

Armazena os clientes cadastrados.

| Coluna | Tipo | Obrigatorio | Descricao |
|---|---|---|---|
| `id` | BIGINT | Sim | Identificador unico, gerado automaticamente |
| `nome` | VARCHAR | Sim | Nome completo do cliente |
| `cpf` | VARCHAR | Nao | CPF do cliente |
| `telefone` | VARCHAR | Nao | Telefone de contato |
| `email` | VARCHAR | Nao | E-mail do cliente |
| `cidade_id` | BIGINT | Sim | Referencia a cidade do cliente (chave estrangeira) |

---

### Relacionamento entre as tabelas

```
┌──────────────────┐           ┌──────────────────────────────────┐
│     cidades      │           │            clientes              │
├──────────────────┤           ├──────────────────────────────────┤
│ id (PK)          │-----------│ cidade_id (FK)                   │
│ nome             │  N para 1 │ id (PK)                          │
│ uf               │           │ nome                             │
└──────────────────┘           │ cpf                              │
                               │ telefone                         │
                               │ email                            │
                               └──────────────────────────────────┘
```

Uma cidade pode ter varios clientes vinculados a ela, mas cada cliente pertence a uma unica cidade. Isso se chama relacionamento "Um para Muitos". A coluna `cidade_id` na tabela de clientes e o elo que conecta as duas tabelas.

---

## Estrutura do projeto

```
CrudCidades/
|
|-- pom.xml                          (configuracao do Maven)
|-- data/
|   └-- cruddb.mv.db                 (arquivo do banco de dados H2)
|
└-- src/
    └-- main/
        └-- java/
            |-- Main.java            (ponto de entrada do sistema)
            |
            |-- model/               (entidades que representam as tabelas)
            |   |-- Cidade.java
            |   └-- Cliente.java
            |
            |-- util/                (utilitarios de infraestrutura)
            |   └-- HibernateUtil.java
            |
            |-- dao/                 (acesso ao banco de dados)
            |   |-- CidadeDAO.java
            |   └-- ClienteDAO.java
            |
            └-- view/                (telas do sistema)
                |-- TelaPrincipal.java
                |-- TelaCidade.java
                └-- TelaCliente.java
```

---

## Arquitetura em camadas

O sistema segue o padrao de arquitetura em camadas, que organiza bem as responsabilidades de cada parte do codigo:

```
┌─────────────────────────────────────────────────┐
│                  VIEW (Visao)                   │
│   TelaPrincipal  TelaCidade  TelaCliente        │
│   Interface grafica com Swing, o que o          │
│   usuario ve e com o que interage               │
└────────────────────────────────────────────────┘
                     |
                  chama
                     |
┌────────────────────────────────────────────────┐
│              DAO (Acesso a Dados)              │
│         CidadeDAO       ClienteDAO             │
│   Responsavel por salvar, buscar, atualizar    │
│   e excluir registros no banco de dados        │
└────────────────────────────────────────────────┘
                     |
                   usa
                     |
┌────────────────────────────────────────────────┐
│           MODEL (Modelo / Entidades)           │
│           Cidade           Cliente             │
│   Classes Java que representam as tabelas      │
│   do banco de dados                            │
└────────────────────────────────────────────────┘
                     |
               gerenciado por
                     |
┌────────────────────────────────────────────────┐
│         HIBERNATE + H2 (Banco de Dados)        │
│              HibernateUtil                     │
│   Configuracao da conexao e sessao com o BD    │
└────────────────────────────────────────────────┘
```

### Por que essa separacao?

- **View** so cuida de mostrar dados e capturar acoes do usuario. Nao sabe como o banco funciona.
- **DAO** so cuida de persistir e recuperar dados. Nao sabe como a tela funciona.
- **Model** so define a estrutura dos dados. Sem logica de tela ou banco.

Essa organizacao torna o codigo mais facil de manter, entender e evoluir.

---

## Entidades e relacionamentos

### `Cidade.java`

Representa uma cidade no sistema. A anotacao `@Entity` diz ao Hibernate que essa classe deve ser mapeada para uma tabela no banco.

```java
@Entity
@Table(name = "cidades")
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // gerado automaticamente pelo banco

    @Column(nullable = false)
    private String nome;    // obrigatorio

    @Column(nullable = false, length = 2)
    private String uf;      // obrigatorio, maximo 2 caracteres
}
```

O metodo `toString()` retorna `"nome - uf"` (ex: `"Curitiba - PR"`), que e o texto exibido no combobox da tela de clientes.

---

### `Cliente.java`

Representa um cliente no sistema.

```java
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;       // obrigatorio

    private String cpf;        // opcional
    private String telefone;   // opcional
    private String email;      // opcional

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;     // cidade vinculada, obrigatorio
}
```

A anotacao `@ManyToOne` define o relacionamento: muitos clientes podem ter a mesma cidade. O `@JoinColumn` cria a coluna `cidade_id` na tabela do banco.

---

### `HibernateUtil.java`

Classe utilitaria que cria e gerencia a conexao com o banco de dados. Usa o padrao **Singleton**, o que significa que existe apenas **uma** instancia da fabrica de sessoes durante toda a execucao do sistema.

```
1. Aplicacao inicia
2. HibernateUtil cria o SessionFactory (uma unica vez)
3. Cada operacao de banco abre uma Session, executa, e fecha a Session
4. SessionFactory e fechado quando a aplicacao encerra
```

---

### `CidadeDAO.java` e `ClienteDAO.java`

Sao as classes que fazem o acesso ao banco de dados. Cada metodo abre uma sessao com o Hibernate, executa a operacao e fecha a sessao.

| Metodo | O que faz |
|---|---|
| `salvar(entidade)` | Insere um novo registro no banco |
| `atualizar(entidade)` | Atualiza um registro existente |
| `excluir(entidade)` | Remove um registro do banco |
| `buscarPorId(id)` | Retorna o registro com o ID informado |
| `listarTodos()` | Retorna todos os registros ordenados por nome |
| `pesquisarPorNome(texto)` | Busca registros cujo nome contenha o texto |
| `temClientesNaCidade(id)` *(so CidadeDAO)* | Verifica se ha clientes vinculados a uma cidade |

Todas as operacoes de escrita usam **transacoes**: se algo der errado, a operacao e desfeita (rollback), evitando dados inconsistentes no banco.

---

## As telas do sistema

### Tela Principal (`TelaPrincipal`)

Janela inicial do sistema, com dois botoes:

```
┌─────────────────────────────────┐
│         CrudCidades             │
│                                 │
│   [ Cadastro de Cidades   ]     │
│                                 │
│   [ Cadastro de Clientes  ]     │
│                                 │
└─────────────────────────────────┘
```

Clicar em um botao abre a tela correspondente.

---

### Tela de Cidades (`TelaCidade`)

```
┌────────────────────────────────────────────────────────────────┐
│  Codigo: [____]   Nome: [________________]   UF: [__]          │
│                                                                │
│  [ Novo ]  [ Salvar ]  [ Excluir ]  [ Pesquisar ]  [ Listar ] │
│                                                                │
│  ┌──────────┬──────────────────────────┬──────┐               │
│  │  Codigo  │          Nome            │  UF  │               │
│  ├──────────┼──────────────────────────┼──────┤               │
│  │    1     │  Curitiba                │  PR  │               │
│  │    2     │  Sao Paulo               │  SP  │               │
│  │   ...    │  ...                     │  ... │               │
│  └──────────┴──────────────────────────┴──────┘               │
└────────────────────────────────────────────────────────────────┘
```

- Clicar em uma linha da tabela preenche o formulario para edicao
- O campo "Codigo" e somente leitura (gerado pelo banco)
- UF aceita no maximo 2 caracteres

---

### Tela de Clientes (`TelaCliente`)

```
┌──────────────────────────────────────────────────────────────────────────┐
│  Codigo: [____]   Nome: [________________]   CPF: [___________]          │
│  Telefone: [_____________]   E-mail: [_________________]                 │
│  Cidade: [ Curitiba - PR          v ]                                    │
│                                                                          │
│  [ Novo ]  [ Salvar ]  [ Excluir ]  [ Pesquisar ]  [ Listar Todos ]      │
│                                                                          │
│  ┌────────┬───────────┬─────────────┬───────────┬───────────┬──────────┐ │
│  │ Codigo │   Nome    │     CPF     │ Telefone  │   Email   │  Cidade  │ │
│  ├────────┼───────────┼─────────────┼───────────┼───────────┼──────────┤ │
│  │   1    │ Joao S.   │ 123.456...  │ (41)9...  │ j@...     │ Curitiba │ │
│  └────────┴───────────┴─────────────┴───────────┴───────────┴──────────┘ │
└──────────────────────────────────────────────────────────────────────────┘
```

- O combobox "Cidade" e populado automaticamente com todas as cidades cadastradas
- Clicar em uma linha da tabela preenche o formulario para edicao

---

## Funcionalidades disponiveis

### Botoes comuns as duas telas

| Botao | O que faz |
|---|---|
| **Novo** | Limpa o formulario para cadastrar um novo registro |
| **Salvar** | Salva o registro: insere se for novo, atualiza se ja existir |
| **Excluir** | Exclui o registro selecionado (com confirmacao) |
| **Pesquisar** | Busca registros cujo nome contenha o texto digitado |
| **Listar Todos** | Exibe todos os registros cadastrados na tabela |

### Validacoes e regras de negocio

- **Nome** e obrigatorio em ambas as telas
- **UF** e obrigatorio, deve ter exatamente 2 caracteres e ser uma sigla valida de estado brasileiro
- **CPF** e validado pelos digitos verificadores quando preenchido (campo opcional)
- **Telefone** deve ter 10 digitos (fixo) ou 11 digitos (celular) quando preenchido (campo opcional)
- **E-mail** deve ter formato valido quando preenchido (campo opcional)
- **Cidade** e obrigatoria ao cadastrar um cliente
- Nao e possivel excluir uma cidade que tenha clientes vinculados; o sistema exibe uma mensagem de aviso
- A pesquisa por nome e case-insensitive (nao diferencia maiusculas de minusculas) e usa busca parcial, nao precisa ser o nome exato

---

## Como executar

### Pre-requisitos

- **Java 17** ou superior instalado
- **Maven 3+** instalado (ou use a IDE: IntelliJ IDEA, NetBeans, Eclipse)

### Pela IDE (IntelliJ IDEA / NetBeans)

1. Abra a pasta do projeto como um **projeto Maven**
2. Aguarde a IDE baixar as dependencias automaticamente
3. Execute a classe `Main.java`

### Pelo terminal

```bash
# Compilar
mvn compile

# Executar
mvn exec:java -Dexec.mainClass=Main
```

O banco de dados (`data/cruddb.mv.db`) e criado automaticamente na primeira execucao. Nao e necessario configurar nada no banco.

### Ordem recomendada de uso

1. Abra o sistema. A **Tela Principal** aparece com dois botoes.
2. Clique em **Cadastro de Cidades** e cadastre ao menos uma cidade antes de cadastrar clientes.
3. Clique em **Cadastro de Clientes** e cadastre os clientes selecionando a cidade no combobox.
4. Para **editar** um registro, clique na linha da tabela. Os dados preenchem o formulario. Faca as alteracoes e clique em **Salvar**.
5. Para **excluir**, selecione o registro na tabela e clique em **Excluir**.
6. Use o campo **Pesquisar** para filtrar por nome. O botao **Listar Todos** remove o filtro.

---

## Dependencias e configuracao

### Dependencias principais (`pom.xml`)

```xml
<!-- Hibernate ORM: traduz objetos Java para SQL e vice-versa -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.4.Final</version>
</dependency>

<!-- Banco de dados H2: banco em arquivo, sem instalacao -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.224</version>
</dependency>
```

### Configuracoes do Hibernate (`HibernateUtil.java`)

| Propriedade | Valor | O que significa |
|---|---|---|
| `hibernate.connection.url` | `jdbc:h2:file:./data/cruddb` | Caminho do arquivo do banco de dados |
| `hibernate.dialect` | `H2Dialect` | Dialeto SQL para o banco H2 |
| `hibernate.hbm2ddl.auto` | `update` | Cria/atualiza as tabelas automaticamente ao iniciar |
| `hibernate.show_sql` | `true` | Exibe os SQLs gerados no console (util para depuracao) |
| `hibernate.format_sql` | `true` | Formata o SQL exibido para melhor leitura |

---

## Fluxo de uma operacao tipica

Exemplo: **salvar uma nova cidade**

```
1. Usuario clica em "Novo"
2. TelaCidade limpa o formulario
3. Usuario preenche Nome e UF e clica em "Salvar"
4. TelaCidade valida os campos (nome e UF nao podem estar vazios)
5. TelaCidade cria um objeto Cidade com os dados preenchidos
6. TelaCidade chama CidadeDAO.salvar(cidade)
7. CidadeDAO abre uma Session do Hibernate
   Chama session.persist(cidade) dentro de uma transacao
   Hibernate gera o SQL:
     INSERT INTO cidades (nome, uf) VALUES ('Curitiba', 'PR')
   O banco salva o registro e retorna o id gerado
   CidadeDAO confirma a transacao (commit) e fecha a Session
8. TelaCidade exibe mensagem de sucesso e recarrega a tabela
```

---

## Seguranca das consultas

Todas as pesquisas usam **parametros nomeados** do Hibernate, o que previne **SQL Injection**, uma vulnerabilidade comum em sistemas que montam consultas concatenando textos:

```java
Query<Cidade> query = session.createQuery(
    "FROM Cidade WHERE lower(nome) LIKE lower(:nome) ORDER BY nome",
    Cidade.class
);
query.setParameter("nome", "%" + nome + "%");
```

O texto digitado e sempre tratado como dado, nunca como comando SQL.

---

*Projeto desenvolvido para a disciplina de Programacao Orientada a Objetos - UTFPR-DV.*
