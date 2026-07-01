package dao;

import java.util.ArrayList;
import java.util.List;
import model.Cidade;
import org.hibernate.Session;
import util.HibernateUtil;

// classe responsavel por todas as operacoes de banco de dados da entidade Cidade
// sigla dao significa Data Access Object, separa a logica de persistencia da logica de tela
public class CidadeDAO {

  // insere uma nova cidade no banco de dados
  public void salvar(Cidade cidade) {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // inicia a transacao antes de qualquer escrita no banco
    session.getTransaction().begin();
    // persist envia o objeto cidade para o banco como um novo registro
    session.persist(cidade);
    // confirma a transacao e grava a mudanca no banco
    session.getTransaction().commit();
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
  }

  // atualiza os dados de uma cidade que ja existe no banco de dados
  public void atualizar(Cidade cidade) {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // inicia a transacao antes de qualquer escrita no banco
    session.getTransaction().begin();
    // merge sincroniza o objeto recebido com o estado atual do banco
    session.merge(cidade);
    // confirma a transacao e grava a mudanca no banco
    session.getTransaction().commit();
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
  }

  // remove uma cidade do banco de dados pelo seu id
  public void excluir(Cidade cidade) {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // inicia a transacao antes de qualquer escrita no banco
    session.getTransaction().begin();
    // o remove exige que o objeto esteja gerenciado pela sessao atual, entao busca de novo pelo id
    Cidade gerenciada = session.find(Cidade.class, cidade.getId());
    // so remove se o registro ainda existir no banco
    if (gerenciada != null) session.remove(gerenciada);
    // confirma a transacao e efetiva a exclusao no banco
    session.getTransaction().commit();
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
  }

  // busca uma cidade pelo seu id, retorna null se nao encontrar
  public Cidade buscarPorId(Long id) {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao, leitura nao precisa de transacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // find busca o objeto pelo tipo e pelo id, retorna null se nao existir
    Cidade cidade = session.find(Cidade.class, id);
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
    // devolve a cidade encontrada ou null
    return cidade;
  }

  // retorna todas as cidades cadastradas, ordenadas pelo nome em ordem alfabetica
  public List<Cidade> listarTodos() {
    // imprime um aviso simples de que a sessao vai ser aberta
    System.out.println("Sessao aberta");
    // abre uma sessao nova para esta operacao, leitura nao precisa de transacao
    Session session = HibernateUtil.getSessionFactory().openSession();
    // jpql simples sem join e sem parametro nomeado, so ordenacao pelo nome
    List<Cidade> lista =
        session.createQuery("Select c from Cidade c order by c.nome", Cidade.class).list();
    // fecha a sessao pois a operacao ja terminou
    session.close();
    // imprime um aviso simples de que a sessao foi fechada
    System.out.println("Sessao fechada");
    // devolve a lista carregada do banco
    return lista;
  }

  // busca cidades cujo nome contenha o texto informado, ignorando maiusculas e minusculas
  // filtra em memoria sobre a lista completa, sem usar where ou parametro na query
  public List<Cidade> pesquisarPorNome(String nome) {
    // lista onde vao entrar as cidades que baterem com o filtro
    List<Cidade> resultado = new ArrayList<>();
    // percorre todas as cidades trazidas do banco
    for (Cidade c : listarTodos()) {
      // compara o nome em minusculo para ignorar maiusculas e minusculas
      if (c.getNome().toLowerCase().contains(nome.toLowerCase())) {
        // adiciona a cidade na lista de resultado quando o nome bate com o filtro
        resultado.add(c);
      }
    }
    // devolve somente as cidades que passaram no filtro
    return resultado;
  }
}
