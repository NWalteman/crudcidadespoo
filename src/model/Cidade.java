package model;

import jakarta.persistence.*;

// marca a classe como uma entidade que sera salva no banco de dados
@Entity
// define o nome da tabela que sera criada no banco
@Table(name = "cidades")
public class Cidade {

  // marca o campo como chave primaria
  @Id
  // o banco gera o valor automaticamente a cada novo registro
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // campo obrigatorio, nao pode ser salvo vazio
  @Column(nullable = false)
  private String nome;

  // sigla do estado, obrigatoria e com no maximo 2 caracteres
  @Column(nullable = false, length = 2)
  private String uf;

  // construtor sem argumentos exigido pelo Hibernate para criar objetos ao buscar do banco
  public Cidade() {}

  // construtor usado na tela para criar uma cidade nova antes de salvar
  public Cidade(String nome, String uf) {
    this.nome = nome;
    this.uf = uf;
  }

  // retorna o id gerado pelo banco
  public Long getId() {
    return id;
  }

  // permite setar o id manualmente se necessario
  public void setId(Long id) {
    this.id = id;
  }

  // retorna o nome da cidade
  public String getNome() {
    return nome;
  }

  // atualiza o nome da cidade
  public void setNome(String nome) {
    this.nome = nome;
  }

  // retorna a sigla do estado
  public String getUf() {
    return uf;
  }

  // atualiza a sigla do estado
  public void setUf(String uf) {
    this.uf = uf;
  }

  // o JComboBox chama esse metodo para mostrar o texto de cada item na lista
  @Override
  public String toString() {
    return nome + " - " + uf;
  }
}
