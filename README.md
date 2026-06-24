# CrudCidades

Sistema desktop para cadastro e gerenciamento de **cidades** e **clientes**, desenvolvido em Java como projeto acadêmico da disciplina de Programação Orientada a Objetos na **UTFPR (Universidade Tecnológica Federal do Paraná)**.

---

## Sumário

- [O que é este sistema?](#o-que-é-este-sistema)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Como funciona o banco de dados](#como-funciona-o-banco-de-dados)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Arquitetura em camadas](#arquitetura-em-camadas)
- [Entidades e relacionamentos](#entidades-e-relacionamentos)
- [As telas do sistema](#as-telas-do-sistema)
- [Funcionalidades disponíveis](#funcionalidades-disponíveis)
- [Como executar](#como-executar)
- [Dependências e configuração](#dependências-e-configuração)

---

## O que é este sistema?

O **CrudCidades** é um aplicativo de janela (desktop) que permite ao usuário:

- **Cadastrar, editar e excluir cidades** (nome e sigla do estado)
- **Cadastrar, editar e excluir clientes** (nome, CPF, telefone, e-mail e cidade)
- **Pesquisar** registros pelo nome
- **Listar** todos os registros cadastrados em uma tabela

O sistema garante integridade dos dados, impedindo, por exemplo, que uma cidade seja excluída enquanto houver clientes vinculados a ela.

---

## Tecnologias utilizadas

| Tecnologia | Versão | Para que serve |
|---|---|---|
| **Java** | 17 | Linguagem de programação principal |
| **Swing** | (nativo Java) | Construção das janelas e componentes visuais |
| **Hibernate** | 6.4.4 | Mapeamento entre objetos Java e tabelas do banco de dados (ORM) |
| **H2 Database** | 2.2.224 | Banco de dados embutido, salvo em arquivo no disco |
| **Maven** | 3+ | Gerenciamento de dependências e build do projeto |

> **Para leigos:** Imagine o Hibernate como um "tradutor" — você trabalha com objetos Java normais (como `Cidade` e `Cliente`) e ele traduz automaticamente tudo para o banco de dados e vice-versa, sem que você precise escrever SQL manualmente na maior parte do tempo.

---

## Como funciona o banco de dados

### Tipo de banco: H2 (embutido em arquivo)

O banco de dados **não precisa de instalação separada**. Ele é um arquivo chamado `cruddb.mv.db` que fica dentro da pasta `data/` do projeto. Quando você executa o sistema pela primeira vez, esse arquivo é criado automaticamente.

```
CrudCidades/
└── data/
    └── cruddb.mv.db   ← aqui ficam todos os seus dados
```

### Tabelas criadas automaticamente

O Hibernate cria e atualiza as tabelas automaticamente com a configuração `hibernate.hbm2ddl.auto=update`. As tabelas são:

---

#### Tabela `cidades`

Armazena as cidades cadastradas.

| Coluna | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `id` | BIGINT (número inteiro) | Sim | Identificador único, gerado automaticamente |
| `nome` | VARCHAR (texto) | Sim | Nome da cidade (ex: "Curitiba") |
| `uf` | VARCHAR(2) (texto, máx. 2 letras) | Sim | Sigla do estado (ex: "PR") |

---

#### Tabela `clientes`

Armazena os clientes cadastrados.

| Coluna | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `id` | BIGINT | Sim | Identificador único, gerado automaticamente |
| `nome` | VARCHAR | Sim | Nome completo do cliente |
| `cpf` | VARCHAR | Não | CPF do cliente |
| `telefone` | VARCHAR | Não | Telefone de contato |
| `email` | VARCHAR | Não | E-mail do cliente |
| `cidade_id` | BIGINT | Sim | Referência à cidade do cliente (chave estrangeira) |

---

### Relacionamento entre as tabelas

```
┌──────────────────┐           ┌──────────────────────────────────┐
│     cidades      │           │            clientes              │
├──────────────────┤           ├──────────────────────────────────┤
│ id (PK)          │◄──────────│ cidade_id (FK)                   │
│ nome             │  N para 1 │ id (PK)                          │
│ uf               │           │ nome                             │
└──────────────────┘           │ cpf                              │
                               │ telefone                         │
                               │ email                            │
                               └──────────────────────────────────┘
```

> **Para leigos:** Uma cidade pode ter **vários clientes** vinculados a ela, mas cada cliente pertence a **uma única cidade**. Isso se chama relacionamento "Um para Muitos". A coluna `cidade_id` na tabela de clientes é o elo que conecta as duas tabelas — ela guarda o `id` da cidade à qual o cliente pertence.

---

## Estrutura do projeto

```
CrudCidades/
│
├── pom.xml                          ← Configuração do Maven (dependências)
├── data/
│   └── cruddb.mv.db                 ← Arquivo do banco de dados H2
│
└── src/
    └── main/
        └── java/
            ├── Main.java            ← Ponto de entrada do sistema
            │
            ├── model/               ← Entidades (representam as tabelas)
            │   ├── Cidade.java
            │   └── Cliente.java
            │
            ├── util/                ← Utilitários de infraestrutura
            │   └── HibernateUtil.java
            │
            ├── dao/                 ← Acesso ao banco de dados
            │   ├── CidadeDAO.java
            │   └── ClienteDAO.java
            │
            └── view/                ← Telas do sistema (interface gráfica)
                ├── TelaPrincipal.java
                ├── TelaCidade.java
                └── TelaCliente.java
```

---

## Arquitetura em camadas

O sistema segue o padrão de arquitetura em camadas, que organiza bem as responsabilidades de cada parte do código:

```
┌─────────────────────────────────────────────────┐
│                  VIEW (Visão)                   │
│   TelaPrincipal  TelaCidade  TelaCliente        │
│   Interface gráfica com Swing — o que o         │
│   usuário vê e com o que interage               │
└────────────────────┬────────────────────────────┘
                     │ chama
┌────────────────────▼────────────────────────────┐
│              DAO (Acesso a Dados)               │
│         CidadeDAO       ClienteDAO              │
│   Responsável por salvar, buscar, atualizar     │
│   e excluir registros no banco de dados         │
└────────────────────┬────────────────────────────┘
                     │ usa
┌────────────────────▼────────────────────────────┐
│           MODEL (Modelo / Entidades)            │
│           Cidade           Cliente              │
│   Classes Java que representam as tabelas       │
│   do banco de dados                             │
└────────────────────┬────────────────────────────┘
                     │ gerenciado por
┌────────────────────▼────────────────────────────┐
│         HIBERNATE + H2 (Banco de Dados)         │
│              HibernateUtil                      │
│   Configuração da conexão e sessão com o BD     │
└─────────────────────────────────────────────────┘
```

### Por que essa separação?

- **View** só cuida de mostrar dados e capturar ações do usuário. Não sabe como o banco funciona.
- **DAO** só cuida de persistir e recuperar dados. Não sabe como a tela funciona.
- **Model** só define a estrutura dos dados. Sem lógica de tela ou banco.

Essa organização torna o código mais fácil de manter, entender e evoluir.

---

## Entidades e relacionamentos

### `Cidade.java`

Representa uma cidade no sistema. A anotação `@Entity` diz ao Hibernate que essa classe deve ser mapeada para uma tabela no banco.

```java
@Entity
@Table(name = "cidades")
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        // gerado automaticamente pelo banco

    @Column(nullable = false)
    private String nome;    // obrigatório

    @Column(nullable = false, length = 2)
    private String uf;      // obrigatório, máximo 2 caracteres
}
```

O método `toString()` retorna `"nome - uf"` (ex: `"Curitiba - PR"`), que é o texto exibido no combobox da tela de clientes.

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
    private String nome;       // obrigatório

    private String cpf;        // opcional
    private String telefone;   // opcional
    private String email;      // opcional

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;     // cidade vinculada, obrigatório
}
```

A anotação `@ManyToOne` define o relacionamento: muitos clientes podem ter a mesma cidade. O `@JoinColumn` cria a coluna `cidade_id` na tabela do banco.

---

### `HibernateUtil.java`

Classe utilitária que cria e gerencia a conexão com o banco de dados. Usa o padrão **Singleton**, o que significa que existe apenas **uma** instância da fábrica de sessões durante toda a execução do sistema.

```
Aplicação inicia
     ↓
HibernateUtil cria o SessionFactory (uma única vez)
     ↓
Cada operação de banco abre uma Session, executa, e fecha a Session
     ↓
SessionFactory é fechado quando a aplicação encerra
```

---

### `CidadeDAO.java` e `ClienteDAO.java`

São as classes que fazem o acesso ao banco de dados. Cada método abre uma sessão com o Hibernate, executa a operação e fecha a sessão.

| Método | O que faz |
|---|---|
| `salvar(entidade)` | Insere um novo registro no banco |
| `atualizar(entidade)` | Atualiza um registro existente |
| `excluir(entidade)` | Remove um registro do banco |
| `buscarPorId(id)` | Retorna o registro com o ID informado |
| `listarTodos()` | Retorna todos os registros ordenados por nome |
| `pesquisarPorNome(texto)` | Busca registros cujo nome contenha o texto |
| `temClientesNaCidade(id)` *(só CidadeDAO)* | Verifica se há clientes vinculados a uma cidade |

Todas as operações de escrita usam **transações** — se algo der errado, a operação é desfeita (rollback), evitando dados inconsistentes no banco.

---

## As telas do sistema

### Tela Principal (`TelaPrincipal`)

Janela inicial do sistema, com dois botões:

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

Clicar em um botão abre a tela correspondente.

---

### Tela de Cidades (`TelaCidade`)

```
┌────────────────────────────────────────────────────────────────┐
│  Código: [____]   Nome: [________________]   UF: [__]          │
│                                                                │
│  [ Novo ]  [ Salvar ]  [ Excluir ]  [ Pesquisar ]  [ Listar ] │
│                                                                │
│  ┌──────────┬──────────────────────────┬──────┐               │
│  │  Código  │          Nome            │  UF  │               │
│  ├──────────┼──────────────────────────┼──────┤               │
│  │    1     │  Curitiba                │  PR  │               │
│  │    2     │  São Paulo               │  SP  │               │
│  │   ...    │  ...                     │  ... │               │
│  └──────────┴──────────────────────────┴──────┘               │
└────────────────────────────────────────────────────────────────┘
```

- Clicar em uma linha da tabela preenche o formulário para edição
- O campo "Código" é somente leitura (gerado pelo banco)
- UF aceita no máximo 2 caracteres

---

### Tela de Clientes (`TelaCliente`)

```
┌──────────────────────────────────────────────────────────────────────────┐
│  Código: [____]   Nome: [________________]   CPF: [___________]          │
│  Telefone: [_____________]   E-mail: [_________________]                 │
│  Cidade: [ Curitiba - PR          ▼ ]                                    │
│                                                                          │
│  [ Novo ]  [ Salvar ]  [ Excluir ]  [ Pesquisar ]  [ Listar Todos ]      │
│                                                                          │
│  ┌────────┬───────────┬─────────────┬───────────┬───────────┬──────────┐ │
│  │ Código │   Nome    │     CPF     │ Telefone  │   Email   │  Cidade  │ │
│  ├────────┼───────────┼─────────────┼───────────┼───────────┼──────────┤ │
│  │   1    │ João S.   │ 123.456...  │ (41)9...  │ j@...     │ Curitiba │ │
│  └────────┴───────────┴─────────────┴───────────┴───────────┴──────────┘ │
└──────────────────────────────────────────────────────────────────────────┘
```

- O combobox "Cidade" é populado automaticamente com todas as cidades cadastradas
- Clicar em uma linha da tabela preenche o formulário para edição

---

## Funcionalidades disponíveis

### Botões comuns às duas telas

| Botão | O que faz |
|---|---|
| **Novo** | Limpa o formulário para cadastrar um novo registro |
| **Salvar** | Salva o registro — insere se for novo, atualiza se já existir |
| **Excluir** | Exclui o registro selecionado (com confirmação) |
| **Pesquisar** | Busca registros cujo nome contenha o texto digitado no campo Nome |
| **Listar Todos** | Exibe todos os registros cadastrados na tabela |

### Validações e regras de negócio

- **Nome** é obrigatório em ambas as telas
- **UF** é obrigatório e aceita no máximo 2 caracteres (tela de Cidades)
- **Cidade** é obrigatória ao cadastrar um cliente
- **Não é possível excluir uma cidade** que tenha clientes vinculados — o sistema exibe uma mensagem de aviso
- A pesquisa por nome é **case-insensitive** (não diferencia maiúsculas de minúsculas) e usa busca parcial — não precisa ser o nome exato, basta conter o texto digitado

---

## Como executar

### Pré-requisitos

- **Java 17** ou superior instalado
- **Maven 3+** instalado (ou use a IDE — IntelliJ IDEA, NetBeans, Eclipse)

### Pela IDE (IntelliJ IDEA / NetBeans)

1. Abra a pasta do projeto como um **projeto Maven**
2. Aguarde a IDE baixar as dependências automaticamente
3. Execute a classe `Main.java`

### Pelo terminal

```bash
# Compilar
mvn compile

# Executar
mvn exec:java -Dexec.mainClass=Main
```

> O banco de dados (`data/cruddb.mv.db`) é criado automaticamente na primeira execução. Não é necessário configurar nada no banco.

### Ordem recomendada de uso

1. Abra o sistema. A **Tela Principal** aparece com dois botões.
2. Clique em **Cadastro de Cidades** e cadastre ao menos uma cidade antes de cadastrar clientes.
3. Clique em **Cadastro de Clientes** e cadastre os clientes selecionando a cidade no combobox.
4. Para **editar** um registro, clique na linha da tabela — os dados preenchem o formulário. Faça as alterações e clique em **Salvar**.
5. Para **excluir**, selecione o registro na tabela e clique em **Excluir**.
6. Use o campo **Pesquisar** para filtrar por nome. O botão **Listar Todos** remove o filtro.

---

## Dependências e configuração

### Dependências principais (`pom.xml`)

```xml
<!-- Hibernate ORM: traduz objetos Java para SQL e vice-versa -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.4.Final</version>
</dependency>

<!-- Banco de dados H2: banco em arquivo, sem instalação -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.224</version>
</dependency>
```

### Configurações do Hibernate (`HibernateUtil.java`)

| Propriedade | Valor | O que significa |
|---|---|---|
| `hibernate.connection.url` | `jdbc:h2:file:./data/cruddb` | Caminho do arquivo do banco de dados |
| `hibernate.dialect` | `H2Dialect` | Dialeto SQL para o banco H2 |
| `hibernate.hbm2ddl.auto` | `update` | Cria/atualiza as tabelas automaticamente ao iniciar |
| `hibernate.show_sql` | `true` | Exibe os SQLs gerados no console (útil para depuração) |
| `hibernate.format_sql` | `true` | Formata o SQL exibido para melhor leitura |

---

## Fluxo de uma operação típica

Exemplo: **salvar uma nova cidade**

```
1. Usuário clica em "Novo"
        ↓
2. TelaCidade limpa o formulário

3. Usuário preenche Nome e UF e clica em "Salvar"
        ↓
4. TelaCidade valida os campos (nome e UF não podem estar vazios)
        ↓
5. TelaCidade cria um objeto Cidade com os dados preenchidos
        ↓
6. TelaCidade chama CidadeDAO.salvar(cidade)
        ↓
7. CidadeDAO abre uma Session do Hibernate
   Chama session.persist(cidade) dentro de uma transação
   Hibernate gera o SQL:
     INSERT INTO cidades (nome, uf) VALUES ('Curitiba', 'PR')
   O banco salva o registro e retorna o id gerado
   CidadeDAO confirma a transação (commit) e fecha a Session
        ↓
8. TelaCidade exibe mensagem de sucesso e recarrega a tabela
```

---

## Segurança das consultas

Todas as pesquisas usam **parâmetros nomeados** do Hibernate, o que previne **SQL Injection** — uma vulnerabilidade comum em sistemas que montam consultas concatenando textos:

```java
Query<Cidade> query = session.createQuery(
    "FROM Cidade WHERE lower(nome) LIKE lower(:nome) ORDER BY nome",
    Cidade.class
);
query.setParameter("nome", "%" + nome + "%");

```

> **Para leigos:** SQL Injection é quando um usuário malicioso digita comandos SQL no lugar de um nome comum, tentando manipular ou destruir o banco de dados. O uso de parâmetros nomeados impede que isso aconteça, pois o texto digitado é sempre tratado como dado, nunca como comando.

---

*Projeto desenvolvido para a disciplina de Programação Orientada a Objetos - UTFPR-DV.*
