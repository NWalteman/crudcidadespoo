import javax.swing.*;
import view.TelaPrincipal;

// ponto de entrada da aplicacao, metodo main e chamado pelo sistema ao executar o programa
public class Main {

  public static void main(String[] args) {
    // tenta aplicar o visual nativo do sistema operacional (Windows, Linux ou Mac)
    // assim a interface fica com a aparencia padrao do SO onde o programa rodar
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignorada) {
      // se falhar, o Swing usa o visual padrao dele mesmo, nao e um erro critico
    }

    // o Swing nao e thread-safe: todos os componentes visuais devem ser criados
    // dentro da Event Dispatch Thread (EDT), que e a thread do Swing
    // invokeLater agenda a criacao da janela para o momento correto nessa thread
    SwingUtilities.invokeLater(
        () -> {
          // cria a janela principal do sistema
          TelaPrincipal tela = new TelaPrincipal();
          // torna a janela visivel na tela
          tela.setVisible(true);
        });
  }
}
