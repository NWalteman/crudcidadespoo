package dao;

import java.util.ArrayList;
import java.util.List;
import model.Cliente;
import org.hibernate.Session;
import util.HibernateUtil;

// classe responsavel por todas as operacoes de banco de dados da entidade Cliente
// sigla dao significa Data Access Object, separa a logica de persistencia da logica de tela
public class ClienteDAO {

  // insere um novo cliente no banco de dados
  public void salvar(Cliente cliente) {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // inicia a transacao antes de qualquer escrita no banco
    session.getTransaction().begin();
    // persist envia o objeto cliente para o banco como um novo registro
    session.persist(cliente);
    // confirma a transacao e grava a mudanca no banco
    session.getTransaction().commit();
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
  }

  // atualiza os dados de um cliente que ja existe no banco de dados
  public void atualizar(Cliente cliente) {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // inicia a transacao antes de qualquer escrita no banco
    session.getTransaction().begin();
    // merge sincroniza o objeto recebido com o estado atual do banco
    session.merge(cliente);
    // confirma a transacao e grava a mudanca no banco
    session.getTransaction().commit();
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
  }

  // remove um cliente do banco de dados pelo seu id
  public void excluir(Cliente cliente) {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // inicia a transacao antes de qualquer escrita no banco
    session.getTransaction().begin();
    // o remove exige que o objeto esteja gerenciado pela sessao atual, entao busca de novo pelo id
    Cliente gerenciado = session.find(Cliente.class, cliente.getId());
    // so remove se o registro ainda existir no banco
    if (gerenciado != null) session.remove(gerenciado);
    // confirma a transacao e efetiva a exclusao no banco
    session.getTransaction().commit();
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
  }

  // busca um cliente pelo seu id, retorna null se nao encontrar
  public Cliente buscarPorId(Long id) {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao, leitura nao precisa de transacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // find busca o objeto pelo tipo e pelo id, retorna null se nao existir
    Cliente cliente = session.find(Cliente.class, id);
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
    // devolve o cliente encontrado ou null
    return cliente;
  }

  // retorna todos os clientes cadastrados, ordenados pelo nome em ordem alfabetica
  public List<Cliente> listarTodos() {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao, leitura nao precisa de transacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // jpql simples sem join e sem parametro nomeado, so ordenacao pelo nome
    List<Cliente> lista =
        session.createQuery("Select c from Cliente c order by c.nome", Cliente.class).list();
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
    // devolve a lista carregada do banco
    return lista;
  }

  // busca clientes cujo nome contenha o texto informado, ignorando maiusculas e minusculas
  // filtra em memoria sobre a lista completa, sem usar where ou parametro na query
  public List<Cliente> pesquisarPorNome(String nome) {
    // lista onde vao entrar os clientes que baterem com o filtro
    List<Cliente> resultado = new ArrayList<>();
    // percorre todos os clientes trazidos do banco
    for (Cliente c : listarTodos()) {
      // compara o nome em minusculo para ignorar maiusculas e minusculas
      if (c.getNome().toLowerCase().contains(nome.toLowerCase())) {
        // adiciona o cliente na lista de resultado quando o nome bate com o filtro
        resultado.add(c);
      }
    }
    // devolve somente os clientes que passaram no filtro
    return resultado;
  }

  // verifica se existe algum cliente vinculado a uma cidade pelo id da cidade
  // usado antes de excluir uma cidade para evitar erro de chave estrangeira no banco
  public boolean temClientesNaCidade(Long id) {
    // percorre todos os clientes trazidos do banco
    for (Cliente c : listarTodos()) {
      // compara o id da cidade do cliente com o id recebido
      if (c.getCidade() != null && c.getCidade().getId().equals(id)) {
        // encontrou um cliente vinculado, entao ja pode responder que sim
        return true;
      }
    }
    // nenhum cliente vinculado foi encontrado
    return false;
  }
}
