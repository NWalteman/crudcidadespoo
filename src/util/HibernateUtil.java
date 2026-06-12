package util;

import model.Cidade;
import model.Cliente;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// classe responsavel por configurar o Hibernate e fornecer a SessionFactory
// a SessionFactory e cara para criar, por isso ela fica em uma instancia unica para a aplicacao
public class HibernateUtil {

  // instancia unica da fabrica de sessoes, usada por todos os DAOs
  private static final SessionFactory fabricaSessoes;

  // bloco estatico roda uma unica vez quando a classe e carregada pela primeira vez
  static {
    try {
      // objeto que recebe todas as configuracoes do Hibernate
      Configuration configuracao = new Configuration();

      // driver JDBC que o Hibernate usa para falar com o banco H2
      configuracao.setProperty("hibernate.connection.driver_class", "org.h2.Driver");

      // caminho do arquivo do banco dentro da pasta data do projeto
      // AUTO_SERVER=TRUE permite que mais de uma conexao acesse o arquivo ao mesmo tempo
      configuracao.setProperty(
          "hibernate.connection.url", "jdbc:h2:./data/cruddb;AUTO_SERVER=TRUE");

      // usuario padrao do H2 em modo local
      configuracao.setProperty("hibernate.connection.username", "sa");

      // senha vazia, padrao do H2 em modo local
      configuracao.setProperty("hibernate.connection.password", "");

      // update cria as tabelas se ainda nao existirem
      // se a estrutura das entidades mudar, ele atualiza o banco automaticamente
      configuracao.setProperty("hibernate.hbm2ddl.auto", "update");

      // desativa o log de cada comando SQL executado no console
      configuracao.setProperty("hibernate.show_sql", "false");
      configuracao.setProperty("hibernate.format_sql", "false");

      // registra as classes que representam tabelas no banco
      configuracao.addAnnotatedClass(Cidade.class);
      configuracao.addAnnotatedClass(Cliente.class);

      // cria a fabricaSessoes com todas as configuracoes acima
      fabricaSessoes = configuracao.buildSessionFactory();
    } catch (Exception e) {
      // se falhar aqui a aplicacao nao tem como continuar
      throw new ExceptionInInitializerError("Erro ao inicializar Hibernate: " + e.getMessage());
    }
  }

  // retorna a fabricaSessoes para os DAOs abrirem sessoes e fazerem operacoes no banco
  public static SessionFactory obterFabricaSessoes() {
    return fabricaSessoes;
  }

  // fecha a conexao com o banco quando a aplicacao for encerrada
  public static void encerrar() {
    if (fabricaSessoes != null) {
      fabricaSessoes.close();
    }
  }
}
