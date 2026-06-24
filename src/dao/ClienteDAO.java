package dao;

import java.util.List;
import model.Cliente;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

// classe responsavel por todas as operacoes de banco de dados da entidade Cliente
// DAO significa Data Access Object: separa a logica de persistencia da logica de tela
public class ClienteDAO {

  // insere um novo cliente no banco de dados
  public void salvar(Cliente cliente) {
    // declara a transacao fora do try para poder acessar no catch
    Transaction transacao = null;
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco, mesmo com erro
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // inicia uma transacao: agrupa as operacoes para que sejam atomicas (tudo ou nada)
      transacao = sessao.beginTransaction();
      // persist envia o objeto Cliente para o banco como um novo registro (INSERT)
      sessao.persist(cliente);
      // commit confirma a transacao e salva as mudancas definitivamente no banco
      transacao.commit();
    } catch (Exception e) {
      // se qualquer erro ocorrer, desfaz todas as operacoes da transacao (rollback)
      if (transacao != null) transacao.rollback();
      // relanca o erro com uma mensagem clara para a tela exibir ao usuario
      throw new RuntimeException("Erro ao salvar cliente: " + e.getMessage(), e);
    }
  }

  // atualiza os dados de um cliente que ja existe no banco de dados
  public void atualizar(Cliente cliente) {
    // declara a transacao fora do try para poder acessar no catch
    Transaction transacao = null;
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // inicia a transacao para garantir que a atualizacao seja atomica
      transacao = sessao.beginTransaction();
      // merge sincroniza o objeto que veio de fora da sessao com o estado atual do banco (UPDATE)
      sessao.merge(cliente);
      // confirma a transacao salvando as alteracoes no banco
      transacao.commit();
    } catch (Exception e) {
      // se der erro, desfaz a operacao para nao deixar o banco em estado inconsistente
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage(), e);
    }
  }

  // remove um cliente do banco de dados pelo seu id
  public void excluir(Cliente cliente) {
    // declara a transacao fora do try para poder acessar no catch
    Transaction transacao = null;
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // inicia a transacao para garantir que a exclusao seja atomica
      transacao = sessao.beginTransaction();
      // o remove exige que o objeto esteja dentro da sessao atual (seja "gerenciado")
      // por isso buscamos o objeto novamente pelo id antes de remover
      Cliente gerenciado = sessao.get(Cliente.class, cliente.getId());
      // so remove se o objeto foi encontrado no banco
      if (gerenciado != null) {
        // remove o registro correspondente da tabela clientes (DELETE)
        sessao.remove(gerenciado);
      }
      // confirma a transacao efetivando a exclusao no banco
      transacao.commit();
    } catch (Exception e) {
      // se der erro, desfaz a operacao para nao apagar dados incorretamente
      if (transacao != null) transacao.rollback();
      throw new RuntimeException("Erro ao excluir cliente: " + e.getMessage(), e);
    }
  }

  // busca um cliente pelo seu id, retorna null se nao encontrar
  public Cliente buscarPorId(Long id) {
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // get busca o objeto pelo tipo e pelo id, retorna null se nao existir
      return sessao.get(Cliente.class, id);
    }
  }

  // retorna todos os clientes cadastrados, ordenados pelo nome em ordem alfabetica
  public List<Cliente> listarTodos() {
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // JOIN FETCH carrega a cidade junto com o cliente em uma unica consulta ao banco
      // sem o JOIN FETCH, acessar cliente.getCidade() fora da sessao causaria erro (LazyInitializationException)
      return sessao
          .createQuery("FROM Cliente c JOIN FETCH c.cidade ORDER BY c.nome", Cliente.class)
          .list();
    }
  }

  // busca clientes cujo nome contenha o texto informado, ignorando maiusculas e minusculas
  public List<Cliente> pesquisarPorNome(String nome) {
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      return sessao
          .createQuery(
              // JOIN FETCH garante que a cidade de cada cliente ja venha carregada na consulta
              // LOWER converte os dois lados para minusculo antes de comparar (busca case-insensitive)
              // LIKE com % antes e depois busca o texto em qualquer posicao do nome
              "FROM Cliente c JOIN FETCH c.cidade WHERE LOWER(c.nome) LIKE LOWER(:nome) ORDER BY c.nome",
              Cliente.class)
          // :nome e o parametro nomeado que sera substituido pelo valor real
          // o uso de parametro nomeado evita SQL Injection
          .setParameter("nome", "%" + nome + "%")
          .list();
    }
  }

  // verifica se existe algum cliente vinculado a uma cidade pelo id da cidade
  // usado antes de excluir uma cidade para evitar erro de chave estrangeira no banco
  public boolean temClientesNaCidade(Long id) {
    // try-with-resources: a sessao e fechada automaticamente ao sair do bloco
    try (Session sessao = HibernateUtil.getSessionFactory().openSession()) {
      // COUNT conta quantos clientes possuem aquela cidade_id
      Long contagem =
          sessao
              .createQuery(
                  "SELECT COUNT(c) FROM Cliente c WHERE c.cidade.id = :id", Long.class)
              // substitui o parametro :id pelo id da cidade recebido como argumento
              .setParameter("id", id)
              // uniqueResult retorna um unico valor (o numero inteiro da contagem)
              .uniqueResult();
      // retorna true se houver pelo menos um cliente vinculado a essa cidade
      return contagem != null && contagem > 0;
    }
  }
}
