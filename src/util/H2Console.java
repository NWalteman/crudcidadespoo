package util;

import org.h2.tools.Server;

// classe utilitaria usada so para abrir o console web do banco h2 durante o desenvolvimento
public class H2Console {

  public static void main(String[] args) throws Exception {

    // cria e inicia o servidor web do h2 liberando acesso de outras maquinas na porta 8082
    Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();

    // avisa no console o endereco onde o console web ficou disponivel
    System.out.println("Console H2 iniciado em http://localhost:8082");
  }
}
