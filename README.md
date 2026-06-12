# CrudCidades

Sistema desktop para gerenciamento de clientes e cidades, desenvolvido em Java com interface Swing e persistência via Hibernate no banco H2.

Trabalho da disciplina de Programação Orientada a Objetos - UTFPR.

# Tecnologias

- Java 17
- Swing (interface gráfica)
- Hibernate 6 (persistência)
- Banco de dados H2 (arquivo local)
- Maven (gerenciamento de dependências)

# Estrutura do projeto

```
src/
  Main.java              ponto de entrada da aplicacao
  model/
    Cidade.java          entidade cidade mapeada para o banco
    Cliente.java         entidade cliente mapeada para o banco
  util/
    HibernateUtil.java   configura e fornece a SessionFactory
  dao/
    CidadeDAO.java       operacoes de banco para a entidade Cidade
    ClienteDAO.java      operacoes de banco para a entidade Cliente
  view/
    TelaPrincipal.java   menu principal com acesso aos cadastros
    TelaCidade.java      tela de CRUD de cidades
    TelaCliente.java     tela de CRUD de clientes
data/
  cruddb.mv.db           arquivo do banco H2 gerado automaticamente
```

# Como executar

**Pelo IntelliJ ou NetBeans**

Abra o projeto como projeto Maven e execute a classe `Main`.

**Pelo terminal com Maven**

```bash
mvn compile
mvn exec:java -Dexec.mainClass=Main
```

O banco H2 é criado automaticamente na pasta `data/` na primeira execução. Não é necessária nenhuma configuração adicional.

# Funcionalidades

# Cidades

- Cadastrar cidade informando nome e UF
- Alterar dados de uma cidade existente
- Excluir cidade (bloqueado se houver clientes vinculados)
- Pesquisar cidades pelo nome
- Listar todas as cidades

# Clientes

- Cadastrar cliente informando nome, CPF, telefone, e-mail e cidade
- Alterar dados de um cliente existente
- Excluir cliente
- Pesquisar clientes pelo nome
- Listar todos os clientes

# Relacionamento

Cada cliente está vinculado a uma cidade. A cidade é selecionada via combobox na tela de cliente. No banco, isso é representado pela coluna `cidade_id` na tabela `clientes`, que referencia a tabela `cidades`.

# Como usar

1. Abra o sistema. A tela principal aparece com dois botões.
2. Clique em **Cadastro de Cidades** e cadastre ao menos uma cidade antes de cadastrar clientes.
3. Clique em **Cadastro de Clientes** e cadastre os clientes selecionando a cidade no combobox.
4. Para editar um registro, clique na linha da tabela. Os dados vão preencher o formulário. Faça as alterações e clique em **Salvar**.
5. Para excluir, selecione o registro na tabela e clique em **Excluir**.
6. Use o campo de pesquisa para filtrar por nome. O botão **Listar Todos** remove o filtro.

# Observacoes

- Os dados ficam salvos no arquivo `data/cruddb.mv.db` e persistem entre execuções.
- Não é possível excluir uma cidade que tenha clientes cadastrados. O sistema avisa antes de tentar.
- O campo UF aceita apenas 2 caracteres.
- Nome é o único campo obrigatório para o cliente. CPF, telefone e e-mail são opcionais.
