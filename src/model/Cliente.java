package model;

import jakarta.persistence.*;

// @Entity informa ao Hibernate que esta classe representa uma tabela no banco de dados
@Entity
// @Table define o nome exato da tabela que sera criada ou usada no banco
@Table(name = "clientes")
public class Cliente {

  // @Id marca este campo como a chave primaria da tabela
  @Id
  // @GeneratedValue faz o banco gerar o valor do id automaticamente a cada novo registro
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // tipo Long para suportar numeros grandes como identificadores
  private Long id;

  // nome do cliente, campo obrigatorio: nullable = false impede salvar sem nome
  @Column(nullable = false)
  private String nome;

  // cpf do cliente, campo opcional: pode ser salvo em branco ou nulo
  private String cpf;

  // telefone do cliente, campo opcional
  private String telefone;

  // email do cliente, campo opcional
  private String email;

  // @ManyToOne define o relacionamento: muitos clientes podem pertencer a uma mesma cidade
  // mas cada cliente pertence a uma unica cidade
  @ManyToOne
  // @JoinColumn cria a coluna cidade_id na tabela clientes apontando para o id da tabela cidades
  // nullable = false impede salvar um cliente sem cidade vinculada
  @JoinColumn(name = "cidade_id", nullable = false)
  private Cidade cidade;

  // construtor sem argumentos exigido pelo Hibernate para criar objetos ao buscar registros do banco
  public Cliente() {}

  // construtor utilizado na tela para criar um objeto Cliente antes de salvar no banco
  public Cliente(String nome, String cpf, String telefone, String email, Cidade cidade) {
    // atribui cada campo informado pelo usuario ao respectivo atributo do objeto
    this.nome = nome;
    this.cpf = cpf;
    this.telefone = telefone;
    this.email = email;
    // vincula o objeto Cidade selecionado no combobox a este cliente
    this.cidade = cidade;
  }

  // retorna o id gerado pelo banco apos o registro ser salvo
  public Long getId() {
    return id;
  }

  // permite definir o id manualmente quando necessario
  public void setId(Long id) {
    this.id = id;
  }

  // retorna o nome do cliente
  public String getNome() {
    return nome;
  }

  // atualiza o nome do cliente com o valor informado
  public void setNome(String nome) {
    this.nome = nome;
  }

  // retorna o cpf do cliente, pode ser nulo se nao foi informado
  public String getCpf() {
    return cpf;
  }

  // atualiza o cpf do cliente com o valor informado
  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  // retorna o telefone do cliente, pode ser nulo se nao foi informado
  public String getTelefone() {
    return telefone;
  }

  // atualiza o telefone do cliente com o valor informado
  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  // retorna o email do cliente, pode ser nulo se nao foi informado
  public String getEmail() {
    return email;
  }

  // atualiza o email do cliente com o valor informado
  public void setEmail(String email) {
    this.email = email;
  }

  // retorna o objeto Cidade vinculado a este cliente
  public Cidade getCidade() {
    return cidade;
  }

  // vincula uma cidade ao cliente, usada ao salvar ou editar
  public void setCidade(Cidade cidade) {
    this.cidade = cidade;
  }

  // retorna o nome do cliente como representacao textual do objeto
  @Override
  public String toString() {
    return nome;
  }
}
