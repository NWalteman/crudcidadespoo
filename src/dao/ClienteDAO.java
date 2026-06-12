package dao;

import java.util.List;
import model.Cliente;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

// classe responsavel por todas as operacoes de banco de dados da entidade Cliente
public class ClienteDAO {

  // insere um cliente novo no banco
  public void salvar(Cliente cliente) {
    Transaction transacao = null;
    // try-with-resources fecha a sessao automaticamente ao terminar o bloco
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      // inicia uma transacao para garantir que a operacao seja atomica
      transacao = sessao.beginTransaction();
      // persist envia o objeto para o banco como um novo registro
      sessao.persist(cliente);
      // confirma a transacao e salva as mudancas definitivamente
      transacao.commit();
    } catch (Exception e) {
      // se algo der errado, desfaz tudo que foi feito na transacao
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao salvar cliente: " + e.getMessage(), e);
    }
  }

  // atualiza os dados de um cliente que ja existe no banco
  public void atualizar(Cliente cliente) {
    Transaction transacao = null;
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      transacao = sessao.beginTransaction();
      // merge sincroniza o objeto que veio de fora da sessao com o estado atual do banco
      sessao.merge(cliente);
      transacao.commit();
    } catch (Exception e) {
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
    }
  }

  // remove um cliente do banco pelo id
  public void excluir(Cliente cliente) {
    Transaction transacao = null;
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      transacao = sessao.beginTransaction();
      // o remove exige que o objeto esteja gerenciado pela sessao atual
      // por isso buscamos ele de novo antes de remover
      Cliente gerenciado = sessao.get(Cliente.class, cliente.getId());
      if (gerenciado != null) {
        sessao.remove(gerenciado);
      }
      transacao.commit();
    } catch (Exception e) {
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage(), e);
    }
  }

  // busca um cliente pelo id, retorna null se nao encontrar
  public Cliente buscarPorId(Long id) {
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      // get retorna o objeto ou null se o id nao existir
      return sessao.get(Cliente.class, id);
    }
  }

  // retorna todos os clientes cadastrados, ordenados pelo nome
  public List<Cliente> listarTodos() {
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      // JOIN FETCH carrega a cidade junto com o cliente na mesma consulta
      // evita o problema de LazyInitializationException ao acessar cliente.getCidade()
      return sessao
          .createQuery("FROM Cliente c JOIN FETCH c.cidade ORDER BY c.nome", Cliente.class)
          .list();
    }
  }

  // busca clientes pelo nome, aceita parte do nome e ignora maiusculas e minusculas
  public List<Cliente> pesquisarPorNome(String nome) {
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      return sessao
          .createQuery(
              // JOIN FETCH garante que a cidade de cada cliente ja venha carregada
              "FROM Cliente c JOIN FETCH c.cidade WHERE LOWER(c.nome) LIKE LOWER(:nome) ORDER BY c.nome",
              Cliente.class)
          // :nome e o parametro nomeado, evita SQL injection
          .setParameter("nome", "%" + nome + "%")
          .list();
    }
  }

  // verifica se existe algum cliente vinculado a uma cidade pelo id da cidade
  // usado antes de excluir uma cidade para evitar erro de chave estrangeira no banco
  public boolean temClientesNaCidade(Long id) {
    try (Session sessao = HibernateUtil.obterFabricaSessoes().openSession()) {
      // COUNT conta quantos clientes tem aquela cidade_id
      Long contagem =
          sessao
              .createQuery("SELECT COUNT(c) FROM Cliente c WHERE c.cidade.id = :id", Long.class)
              .setParameter("id", id)
              .uniqueResult();
      // retorna true se tiver pelo menos um cliente na cidade
      return contagem != null && contagem > 0;
    }
  }
}
