package view;

import java.awt.*;
import javax.swing.*;

// tela principal da aplicacao, serve de menu para acessar os cadastros
public class TelaPrincipal extends JFrame {

  public TelaPrincipal() {
    // titulo que aparece na barra da janela
    setTitle("Sistema de Controle de Clientes");
    // tamanho inicial da janela em pixels (largura x altura)
    setSize(400, 250);
    // centraliza a janela no meio da tela
    setLocationRelativeTo(null);
    // encerra a aplicacao inteira quando o usuario fechar essa janela
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    // chama o metodo que cria e posiciona todos os componentes visuais
    iniciarComponentes();
  }

  // cria e organiza todos os componentes da tela
  private void iniciarComponentes() {
    // BorderLayout divide a tela em norte, sul, leste, oeste e centro
    setLayout(new BorderLayout());

    // label de titulo que aparece no topo da janela
    JLabel labelTitulo = new JLabel("Sistema de Controle de Clientes", SwingConstants.CENTER);
    labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
    // margem interna: cima, esquerda, baixo, direita
    labelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
    // adiciona o titulo na regiao norte do BorderLayout
    add(labelTitulo, BorderLayout.NORTH);

    // painel que organiza os botoes em 2 linhas e 1 coluna, com espaco de 15px entre eles
    JPanel painelBotoes = new JPanel(new GridLayout(2, 1, 15, 15));
    painelBotoes.setBorder(BorderFactory.createEmptyBorder(15, 60, 15, 60));

    // botoes de acesso aos cadastros
    JButton botaoCidades = new JButton("🏙  Cadastro de Cidades");
    JButton botaoClientes = new JButton("👤  Cadastro de Clientes");

    botaoCidades.setFont(new Font("Arial", Font.PLAIN, 14));
    botaoClientes.setFont(new Font("Arial", Font.PLAIN, 14));
    botaoCidades.setPreferredSize(new Dimension(250, 50));
    botaoClientes.setPreferredSize(new Dimension(250, 50));

    // ao clicar abre a tela de cadastro de cidades
    botaoCidades.addActionListener(
        e -> {
          TelaCidade telaCidade = new TelaCidade();
          telaCidade.setVisible(true);
        });

    // ao clicar abre a tela de cadastro de clientes
    botaoClientes.addActionListener(
        e -> {
          TelaCliente telaCliente = new TelaCliente();
          telaCliente.setVisible(true);
        });

    painelBotoes.add(botaoCidades);
    painelBotoes.add(botaoClientes);
    // adiciona o painel de botoes no centro da janela
    add(painelBotoes, BorderLayout.CENTER);

    // rodape com nome da instituicao
    JLabel labelRodape = new JLabel("POO - UTFPR", SwingConstants.CENTER);
    labelRodape.setFont(new Font("Arial", Font.ITALIC, 11));
    labelRodape.setForeground(Color.GRAY);
    labelRodape.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
    // adiciona o rodape na regiao sul do BorderLayout
    add(labelRodape, BorderLayout.SOUTH);
  }
}
