package util;

import org.h2.tools.Server;

// classe auxiliar que sobe o console web do H2 para inspecao do banco pelo navegador
// util para depuracao: permite visualizar tabelas e executar SQL diretamente
// para usar: execute este main separado do Main principal, depois acesse http://localhost:8082
public class H2Console {

  // metodo principal que inicia o servidor web do H2
  // throws Exception porque o metodo start() do servidor pode lancar excecoes de rede
  public static void main(String[] args) throws Exception {
    // cria e inicia um servidor web H2 na porta 8082
    // -web habilita a interface web
    // -webAllowOthers permite conexoes de outras maquinas na rede (opcional)
    // -webPort 8082 define a porta de acesso (padrao alternativo ao 8081)
    Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();

    // informa no console que o servidor subiu e qual endereco acessar
    System.out.println("Console H2 iniciado em http://localhost:8082");

    // apos abrir o navegador, use:
    // JDBC URL: jdbc:h2:./data/cruddb;AUTO_SERVER=TRUE
    // Usuario: sa
    // Senha: (deixar em branco)
  }
}
