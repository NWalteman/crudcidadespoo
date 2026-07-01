package view;

import java.awt.*;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

// tela principal da aplicacao, serve de menu para acessar os cadastros de cidades e clientes
public class TelaPrincipal extends JFrame {

  public TelaPrincipal() {
    // titulo que aparece na barra da janela
    setTitle("Sistema de Controle de Clientes");
    // tamanho inicial da janela em pixels (largura x altura)
    setSize(400, 250);
    // centraliza a janela no meio da tela do usuario
    setLocationRelativeTo(null);
    // encerra toda a aplicacao quando o usuario fechar esta janela
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    // chama o metodo que cria e posiciona todos os componentes visuais
    iniciarComponentes();
  }

  // cria e organiza todos os componentes da tela principal
  private void iniciarComponentes() {
    // tenta carregar a imagem de fundo do classpath
    Image imagemFundo = carregarImagem();

    // cria o painel de fundo com suporte a pintura customizada da imagem
    // extends JPanel anonimo para sobrescrever o metodo paintComponent
    JPanel painelFundo =
        new JPanel(new BorderLayout()) {
          @Override
          protected void paintComponent(Graphics g) {
            // converte para Graphics2D para ter acesso a recursos de renderizacao avancados
            Graphics2D g2 = (Graphics2D) g.create();
            // habilita interpolacao bilinear para suavizar a imagem ao redimensionar
            g2.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            if (imagemFundo != null) {
              // calcula a escala necessaria para cobrir toda a area do painel sem distorcer
              // usa Math.max para garantir que nenhuma dimensao fique menor que o painel
              double pw = getWidth(), ph = getHeight();
              double iw = imagemFundo.getWidth(null), ih = imagemFundo.getHeight(null);
              double escala = Math.max(pw / iw, ph / ih);
              int drawW = (int) (iw * escala);
              int drawH = (int) (ih * escala);
              // centraliza a imagem caso ela seja maior que o painel em alguma dimensao
              int drawX = (int) ((pw - drawW) / 2);
              int drawY = (int) ((ph - drawH) / 2);
              // desenha a imagem escalada e centralizada cobrindo todo o fundo
              g2.drawImage(imagemFundo, drawX, drawY, drawW, drawH, this);
            } else {
              // fallback: pinta o fundo com a cor padrao do painel caso a imagem nao exista
              g2.setColor(getBackground());
              g2.fillRect(0, 0, getWidth(), getHeight());
            }
            // libera os recursos graficos criados com g.create()
            g2.dispose();
          }
        };
    // define o painel de fundo como o conteudo principal da janela
    painelFundo.setOpaque(true);
    setContentPane(painelFundo);

    // label de titulo exibido no topo da janela
    JLabel labelTitulo = new JLabel("Sistema de Controle de Clientes", SwingConstants.CENTER);
    // fonte em negrito tamanho 16
    labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
    // texto branco para contrastar com a imagem de fundo
    labelTitulo.setForeground(Color.WHITE);
    // margem interna: 20px em cima, 10px nas laterais e embaixo
    labelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
    // posiciona o titulo na regiao norte do BorderLayout
    painelFundo.add(labelTitulo, BorderLayout.NORTH);

    // botao que abre a tela de cadastro de cidades
    JButton botaoCidades = new JButton("🏙  Cadastro de Cidades");
    // botao que abre a tela de cadastro de clientes
    JButton botaoClientes = new JButton("👤  Cadastro de Clientes");
    // define a fonte dos botoes
    botaoCidades.setFont(new Font("Arial", Font.PLAIN, 14));
    botaoClientes.setFont(new Font("Arial", Font.PLAIN, 14));
    // tamanho fixo para os botoes nao esticarem ao maximizar a janela
    Dimension tamBotao = new Dimension(250, 50);
    botaoCidades.setPreferredSize(tamBotao);
    botaoClientes.setPreferredSize(tamBotao);
    // tamanho maximo tambem fixo para garantir que o BoxLayout respeite o limite
    botaoCidades.setMaximumSize(tamBotao);
    botaoClientes.setMaximumSize(tamBotao);
    // centraliza os botoes horizontalmente dentro do BoxLayout
    botaoCidades.setAlignmentX(Component.CENTER_ALIGNMENT);
    botaoClientes.setAlignmentX(Component.CENTER_ALIGNMENT);

    // ao clicar, cria e exibe a tela de cadastro de cidades como dialogo modal
    botaoCidades.addActionListener(e -> new TelaCidade(this, true).setVisible(true));
    // ao clicar, cria e exibe a tela de cadastro de clientes como dialogo modal
    botaoClientes.addActionListener(e -> new TelaCliente(this, true).setVisible(true));

    // painel que empilha os botoes verticalmente e respeita o tamanho preferido deles
    JPanel painelBotoes = new JPanel();
    // empilha os componentes de cima para baixo usando BoxLayout Y_AXIS
    painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.Y_AXIS));
    // opaco false para o fundo da imagem aparecer atras dos botoes
    painelBotoes.setOpaque(false);
    // adiciona o botao de cidades ao painel
    painelBotoes.add(botaoCidades);
    // espaco de 15 pixels entre os dois botoes
    painelBotoes.add(Box.createVerticalStrut(15));
    // adiciona o botao de clientes ao painel
    painelBotoes.add(botaoClientes);

    // painel central com GridBagLayout para centralizar o painelBotoes na tela
    // sem este wrapper, o BoxLayout ocuparia toda a area do centro
    JPanel painelCentro = new JPanel(new GridBagLayout());
    // opaco false para a imagem de fundo aparecer
    painelCentro.setOpaque(false);
    // adiciona o painel de botoes centralizado dentro do painel central
    painelCentro.add(painelBotoes);
    // posiciona o painel central na regiao central do BorderLayout
    painelFundo.add(painelCentro, BorderLayout.CENTER);

    // label de rodape exibido na parte inferior da janela
    JLabel labelRodape = new JLabel("POO - UTFPR", SwingConstants.CENTER);
    // fonte italica tamanho 11
    labelRodape.setFont(new Font("Arial", Font.ITALIC, 11));
    // texto branco para contrastar com a imagem de fundo
    labelRodape.setForeground(Color.WHITE);
    // margem interna
    labelRodape.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
    // posiciona o rodape na regiao sul do BorderLayout
    painelFundo.add(labelRodape, BorderLayout.SOUTH);
  }

  // tenta carregar a imagem de fundo a partir da pasta media dentro do classpath
  // retorna null se a imagem nao for encontrada, sem travar a aplicacao
  private Image carregarImagem() {
    try {
      // busca o arquivo dentro de resources/media/ que e copiado para o classpath pelo Maven
      InputStream is = getClass().getClassLoader().getResourceAsStream("media/fundowindows.jpg");
      // so tenta ler se o arquivo foi encontrado
      if (is != null) {
        // converte o stream em um objeto Image utilizavel pelo Swing usando ImageIO.read
        return ImageIO.read(is);
      }
    } catch (Exception ignorada) {
      // se qualquer erro ocorrer, ignora e retorna null para usar o fallback de cor
    }
    // retorna null se a imagem nao existir ou falhar ao carregar
    return null;
  }
}
