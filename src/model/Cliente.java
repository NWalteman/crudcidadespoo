package model;

import jakarta.persistence.*;

// marca a classe como uma entidade que sera salva no banco de dados
@Entity
// define o nome da tabela que sera criada no banco
@Table(name = "clientes")
public class Cliente {

  // chave primaria gerada automaticamente pelo banco
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // nome do cliente, campo obrigatorio
  @Column(nullable = false)
  private String nome;

  // cpf do cliente, campo opcional
  private String cpf;

  // telefone do cliente, campo opcional
  private String telefone;

  // email do cliente, campo opcional
  private String email;

  // relacionamento de muitos clientes para uma cidade
  // um cliente pertence a uma cidade, mas uma cidade pode ter varios clientes
  @ManyToOne
  // cria a coluna cidade_id na tabela clientes apontando para a tabela cidades
  @JoinColumn(name = "cidade_id", nullable = false)
  private Cidade cidade;

  // construtor sem argumentos exigido pelo Hibernate para criar objetos ao buscar do banco
  public Cliente() {}

  // construtor usado na tela para criar um cliente novo antes de salvar
  public Cliente(String nome, String cpf, String telefone, String email, Cidade cidade) {
    this.nome = nome;
    this.cpf = cpf;
    this.telefone = telefone;
    this.email = email;
    this.cidade = cidade;
  }

  // retorna o id gerado pelo banco
  public Long getId() {
    return id;
  }

  // permite setar o id manualmente se necessario
  public void setId(Long id) {
    this.id = id;
  }

  // retorna o nome do cliente
  public String getNome() {
    return nome;
  }

  // atualiza o nome do cliente
  public void setNome(String nome) {
    this.nome = nome;
  }

  // retorna o cpf do cliente
  public String getCpf() {
    return cpf;
  }

  // atualiza o cpf do cliente
  public void setCpf(String cpf) {
    this.cpf = cpf;
  }

  // retorna o telefone do cliente
  public String getTelefone() {
    return telefone;
  }

  // atualiza o telefone do cliente
  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  // retorna o email do cliente
  public String getEmail() {
    return email;
  }

  // atualiza o email do cliente
  public void setEmail(String email) {
    this.email = email;
  }

  // retorna o objeto Cidade associado ao cliente
  public Cidade getCidade() {
    return cidade;
  }

  // associa o cliente a uma cidade
  public void setCidade(Cidade cidade) {
    this.cidade = cidade;
  }

  // retorna o nome do cliente como texto representativo do objeto
  @Override
  public String toString() {
    return nome;
  }
}
