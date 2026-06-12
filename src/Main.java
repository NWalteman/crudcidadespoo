import javax.swing.*;
import view.TelaPrincipal;

// ponto de entrada da aplicacao
public class Main {

  public static void main(String[] args) {
    // tenta usar o visual nativo do sistema operacional (Windows, Linux, Mac)
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignorada) {
    }

    // o Swing precisa rodar na sua propria thread de interface
    // invokeLater garante que a janela seja criada no momento certo
    SwingUtilities.invokeLater(
        () -> {
          // cria a tela principal e torna ela visivel
          TelaPrincipal tela = new TelaPrincipal();
          tela.setVisible(true);
        });
  }
}
