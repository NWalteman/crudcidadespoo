package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

// classe utilitaria que configura o Hibernate e fornece a SessionFactory para os DAOs
// segue o padrao Singleton: a SessionFactory e criada uma unica vez e reutilizada
public class HibernateUtil {

  // instancia unica da fabrica de sessoes, compartilhada por toda a aplicacao
  // static garante que existe apenas uma copia independente de quantos objetos forem criados
  // final impede que a referencia seja trocada apos a inicializacao
  private static final SessionFactory sessionFactory;

  // bloco estatico e executado uma unica vez quando a classe e carregada pela JVM
  // aqui e o lugar certo para inicializar recursos caros como a SessionFactory
  static {
    try {
      // new Configuration() cria o objeto de configuracao do Hibernate
      // .configure() le o arquivo hibernate.cfg.xml do classpath com as configuracoes do banco
      // .buildSessionFactory() usa essas configuracoes para criar a fabrica de sessoes
      sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (Throwable ex) {
      // se a configuracao falhar (arquivo ausente, banco inacessivel, etc.)
      // lancamos um erro de inicializacao que impede a aplicacao de subir sem banco
      throw new ExceptionInInitializerError(ex);
    }
  }

  // metodo publico que os DAOs chamam para obter a SessionFactory
  // com ela e possivel abrir Sessions para executar operacoes no banco
  public static SessionFactory getSessionFactory() {
    // retorna a instancia criada no bloco estatico acima
    return sessionFactory;
  }
}
