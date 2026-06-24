package dao;

import java.util.List;
import model.Cidade;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

// classe responsavel por todas as operacoes de banco de dados da entidade Cidade
// DAO significa Data Access Object: separa a logica de persistencia da logica de tela
public class CidadeDAO {

  // insere uma nova cidade no banco de dados
  public void salvar(Cidade cidade) {
    // declara a transacao fora do try para poder acessar no catch
    Transaction transacao = null;
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco, mesmo com erro
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // inicia uma transacao: agrupa as operacoes para que sejam atomicas (tudo ou nada)
      transacao = sessao.beginTransaction();
      // persist envia o objeto Cidade para o banco como um novo registro (INSERT)
      sessao.persist(cidade);
      // commit confirma a transacao e salva as mudancas definitivamente no banco
      transacao.commit();
    } catch (Exception e) {
      // se qualquer erro ocorrer, desfaz todas as operacoes da transacao (rollback)
      if (transacao != null) transacao.rollback();
      // relanca o erro com uma mensagem clara para a tela exibir ao usuario
      throw new RuntimeException("Erro ao salvar cidade: " + e.getMessage(), e);
    }
  }

  // atualiza os dados de uma cidade que ja existe no banco de dados
  public void atualizar(Cidade cidade) {
    // declara a transacao fora do try para poder acessar no catch
    Transaction transacao = null;
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // inicia a transacao para garantir que a atualizacao seja atomica
      transacao = sessao.beginTransaction();
      // merge sincroniza o objeto que veio de fora da sessao com o estado atual do banco (UPDATE)
      sessao.merge(cidade);
      // confirma a transacao salvando as alteracoes no banco
      transacao.commit();
    } catch (Exception e) {
      // se der erro, desfaz a operacao para nao deixar o banco em estado inconsistente
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao atualizar cidade: " + e.getMessage(), e);
    }
  }

  // remove uma cidade do banco de dados pelo seu id
  public void excluir(Cidade cidade) {
    // declara a transacao fora do try para poder acessar no catch
    Transaction transacao = null;
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // inicia a transacao para garantir que a exclusao seja atomica
      transacao = sessao.beginTransaction();
      // o remove exige que o objeto esteja dentro da sessao atual (seja "gerenciado")
      // por isso buscamos o objeto novamente pelo id antes de remover
      Cidade gerenciada = sessao.get(Cidade.class, cidade.getId());
      // so remove se o objeto foi encontrado no banco
      if (gerenciada != null) {
        // remove o registro correspondente da tabela cidades (DELETE)
        sessao.remove(gerenciada);
      }
      // confirma a transacao efetivando a exclusao no banco
      transacao.commit();
    } catch (Exception e) {
      // se der erro, desfaz a operacao para nao apagar dados incorretamente
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao excluir cidade: " + e.getMessage(), e);
    }
  }

  // busca uma cidade pelo seu id, retorna null se nao encontrar
  public Cidade buscarPorId(Long id) {
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // get busca o objeto pelo tipo e pelo id, retorna null se nao existir
      return sessao.get(Cidade.class, id);
    }
  }

  // retorna todas as cidades cadastradas, ordenadas pelo nome em ordem alfabetica
  public List<Cidade> listarTodos() {
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // HQL (Hibernate Query Language): parecida com SQL mas usa o nome da classe Java
      // "FROM Cidade" equivale a "SELECT * FROM cidades" no SQL
      return sessao.createQuery("FROM Cidade ORDER BY nome", Cidade.class).list();
    }
  }

  // busca cidades cujo nome contenha o texto informado, ignorando maiusculas e minusculas
  public List<Cidade> pesquisarPorNome(String nome) {
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      return sessao
          .createQuery(
              // LOWER converte os dois lados para minusculo antes de comparar
              // LIKE com % antes e depois busca o texto em qualquer posicao do nome
              "FROM Cidade WHERE LOWER(nome) LIKE LOWER(:nome) ORDER BY nome", Cidade.class)
          // :nome e o parametro nomeado que sera substituido pelo valor real
          // o uso de parametro nomeado evita SQL Injection
          .setParameter("nome", "%" + nome + "%")
          .list();
    }
  }
}
