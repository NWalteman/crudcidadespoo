package model;

import jakarta.persistence.*;

// @Entity informa ao Hibernate que esta classe representa uma tabela no banco de dados
@Entity
// @Table define o nome exato da tabela que sera criada ou usada no banco
@Table(name = "cidades")
public class Cidade {

  // @Id marca este campo como a chave primaria da tabela
  @Id
  // @GeneratedValue faz o banco gerar o valor do id automaticamente a cada novo registro
  // IDENTITY usa o mecanismo de auto incremento do proprio banco (H2, MySQL, etc.)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  // tipo Long para suportar numeros grandes como identificadores
  private Long id;

  // @Column define que este campo corresponde a uma coluna na tabela
  // nullable = false significa que o banco nao aceita salvar uma cidade sem nome
  @Column(nullable = false)
  private String nome;

  // sigla do estado: obrigatoria e com no maximo 2 caracteres (ex: PR, SP, RJ)
  // length = 2 limita o tamanho da coluna no banco de dados
  @Column(nullable = false, length = 2)
  private String uf;

  // construtor sem argumentos exigido pelo Hibernate para criar objetos ao buscar registros do banco
  public Cidade() {}

  // construtor utilizado na tela para criar um objeto Cidade antes de salvar no banco
  public Cidade(String nome, String uf) {
    // atribui o nome informado pelo usuario ao campo nome do objeto
    this.nome = nome;
    // atribui a sigla do estado ao campo uf do objeto
    this.uf = uf;
  }

  // retorna o id gerado pelo banco apos o registro ser salvo
  public Long getId() {
    return id;
  }

  // permite definir o id manualmente quando necessario
  public void setId(Long id) {
    this.id = id;
  }

  // retorna o nome da cidade
  public String getNome() {
    return nome;
  }

  // atualiza o nome da cidade com o valor informado
  public void setNome(String nome) {
    this.nome = nome;
  }

  // retorna a sigla do estado da cidade
  public String getUf() {
    return uf;
  }

  // atualiza a sigla do estado com o valor informado
  public void setUf(String uf) {
    this.uf = uf;
  }

  // o JComboBox chama este metodo para exibir o texto de cada item na lista de cidades
  // retorna o nome e a uf separados por traco, ex: "Curitiba - PR"
  @Override
  public String toString() {
    return nome + " - " + uf;
  }
}
