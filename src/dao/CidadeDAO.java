package dao;

import java.util.List;
import model.Cidade;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

// classe responsavel por todas as operacoes de banco de dados da entidade Cidade
public class CidadeDAO {

  // insere uma cidade nova no banco
  public void salvar(Cidade cidade) {
    Transaction transacao = null;
    // try-with-resources fecha a sessao automaticamente ao terminar o bloco
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      // inicia uma transacao para garantir que a operacao seja atomica
      transacao = sessao.beginTransaction();
      // persist envia o objeto para o banco como um novo registro
      sessao.persist(cidade);
      // confirma a transacao e salva as mudancas definitivamente
      transacao.commit();
    } catch (Exception e) {
      // se algo der errado, desfaz tudo que foi feito na transacao
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao salvar cidade: " + e.getMessage(), e);
    }
  }

  // atualiza os dados de uma cidade que ja existe no banco
  public void atualizar(Cidade cidade) {
    Transaction transacao = null;
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      transacao = sessao.beginTransaction();
      // merge sincroniza o objeto que veio de fora da sessao com o estado atual do banco
      sessao.merge(cidade);
      transacao.commit();
    } catch (Exception e) {
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao atualizar cidade: " + e.getMessage(), e);
    }
  }

  // remove uma cidade do banco pelo id
  public void excluir(Cidade cidade) {
    Transaction transacao = null;
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      transacao = sessao.beginTransaction();
      // o remove exige que o objeto esteja gerenciado pela sessao atual
      // por isso buscamos ele de novo antes de remover
      Cidade gerenciada = sessao.get(Cidade.class, cidade.getId());
      if (gerenciada != null) {
        sessao.remove(gerenciada);
      }
      transacao.commit();
    } catch (Exception e) {
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao excluir cidade: " + e.getMessage(), e);
    }
  }

  // busca uma cidade pelo id, retorna null se nao encontrar
  public Cidade buscarPorId(Long id) {
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      // get retorna o objeto ou null se o id nao existir
      return sessao.get(Cidade.class, id);
    }
  }

  // retorna todas as cidades cadastradas, ordenadas pelo nome
  public List<Cidade> listarTodos() {
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      // HQL (linguagem de consulta do Hibernate) parecida com SQL mas usa o nome da classe
      return sessao.createQuery("FROM Cidade ORDER BY nome", Cidade.class).list();
    }
  }

  // busca cidades pelo nome, aceita parte do nome e ignora maiusculas e minusculas
  public List<Cidade> pesquisarPorNome(String nome) {
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      return sessao
          .createQuery(
              // LOWER converte os dois lados para minusculo antes de comparar
              // LIKE com % antes e depois busca o termo em qualquer posicao do nome
              "FROM Cidade WHERE LOWER(nome) LIKE LOWER(:nome) ORDER BY nome", Cidade.class)
          // :nome e o parametro nomeado, evita SQL injection
          .setParameter("nome", "%" + nome + "%")
          .list();
    }
  }
}
