package view;

import dao.CidadeDAO;
import dao.ClienteDAO;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import model.Cidade;

// tela de cadastro de cidades, jdialog gerado no estilo netbeans gui builder com grouplayout
public class TelaCidade extends javax.swing.JDialog {

  // siglas de estado validas, usada na validacao de uf direto aqui, sem classe utilitaria separada
  private static final Set<String> UFS_VALIDAS =
      Set.of(
          "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB",
          "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

  // dao responsavel pelas operacoes de cidade no banco de dados
  private final CidadeDAO cidadeDAO = new CidadeDAO();
  // dao usado para verificar se ha clientes vinculados antes de excluir uma cidade
  private final ClienteDAO clienteDAO = new ClienteDAO();

  // cidade selecionada na lista, null quando for um novo cadastro
  private Cidade cidadeAtual = null;

  public TelaCidade(java.awt.Frame parent, boolean modal) {
    // repassa o dono da janela e o modo modal para o construtor do jdialog
    super(parent, modal);
    // monta todos os componentes visuais da tela
    initComponents();
    // carrega todas as cidades do banco na lista assim que a tela abre
    btnPesquisarActionPerformed(null);
  }

  // initcomponents escrito a mao no padrao gerado pelo netbeans gui builder com grouplayout
  private void initComponents() {

    // cria o label e o campo de codigo
    lblCodigo = new javax.swing.JLabel();
    txtCodigo = new javax.swing.JTextField();
    // cria o label e o campo de nome
    lblNome = new javax.swing.JLabel();
    txtNome = new javax.swing.JTextField();
    // cria o label e o campo de uf
    lblUf = new javax.swing.JLabel();
    txtUf = new javax.swing.JTextField();
    // cria os botoes de acao
    btnNovo = new javax.swing.JButton();
    btnSalvar = new javax.swing.JButton();
    btnExcluir = new javax.swing.JButton();
    // cria o label, o campo e o botao de pesquisa
    lblPesquisa = new javax.swing.JLabel();
    txtPesquisa = new javax.swing.JTextField();
    btnPesquisar = new javax.swing.JButton();
    // cria a lista de cidades dentro de um scroll pane
    jScrollPane1 = new javax.swing.JScrollPane();
    lstCidades = new javax.swing.JList<>();

    // fecha somente esta janela quando o usuario clicar em fechar
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    // define o titulo exibido na barra da janela
    setTitle("Cadastro de Cidades");

    // texto do label de codigo
    lblCodigo.setText("Código:");
    // codigo nao pode ser editado, ele vem do banco
    txtCodigo.setEditable(false);
    // codigo nao recebe foco ao navegar com tab ou clicar
    txtCodigo.setFocusable(false);

    // textos dos demais labels do formulario
    lblNome.setText("Nome:");
    lblUf.setText("UF:");

    // texto do botao novo e ligacao com o metodo de acao
    btnNovo.setText("Novo");
    btnNovo.addActionListener(evt -> btnNovoActionPerformed(evt));

    // texto do botao salvar e ligacao com o metodo de acao
    btnSalvar.setText("Salvar");
    btnSalvar.addActionListener(evt -> btnSalvarActionPerformed(evt));

    // texto do botao excluir e ligacao com o metodo de acao
    btnExcluir.setText("Excluir");
    btnExcluir.addActionListener(evt -> btnExcluirActionPerformed(evt));

    // texto do label de pesquisa
    lblPesquisa.setText("Pesquisar por nome:");

    // texto do botao pesquisar e ligacao com o metodo de acao
    btnPesquisar.setText("Pesquisar");
    btnPesquisar.addActionListener(evt -> btnPesquisarActionPerformed(evt));

    // liga o listener de selecao da lista ao metodo que preenche o formulario
    lstCidades.addListSelectionListener(evt -> lstCidadesValueChanged(evt));
    // coloca a lista dentro do scroll pane para suportar muitos registros
    jScrollPane1.setViewportView(lstCidades);

    // cria o grouplayout usado pelo netbeans gui builder e aplica no content pane
    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    // grupo horizontal: labels a esquerda, campos a direita, lista e botoes ocupando a largura
    layout.setHorizontalGroup(
        layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(
                                jScrollPane1,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                420,
                                Short.MAX_VALUE)
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addGroup(
                                        layout
                                            .createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblCodigo)
                                            .addComponent(lblNome)
                                            .addComponent(lblUf)
                                            .addComponent(lblPesquisa))
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCodigo)
                                            .addComponent(txtNome)
                                            .addComponent(txtUf)
                                            .addGroup(
                                                layout
                                                    .createSequentialGroup()
                                                    .addComponent(txtPesquisa)
                                                    .addPreferredGap(
                                                        javax.swing.LayoutStyle.ComponentPlacement
                                                            .RELATED)
                                                    .addComponent(btnPesquisar))))
                            .addGroup(
                                layout
                                    .createSequentialGroup()
                                    .addComponent(btnNovo)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnSalvar)
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btnExcluir)))
                    .addContainerGap()));
    // grupo vertical: uma linha por campo, depois os botoes, a pesquisa e a lista
    layout.setVerticalGroup(
        layout
            .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(
                layout
                    .createSequentialGroup()
                    .addContainerGap()
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCodigo)
                            .addComponent(
                                txtCodigo,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNome)
                            .addComponent(
                                txtNome,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUf)
                            .addComponent(
                                txtUf,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnNovo)
                            .addComponent(btnSalvar)
                            .addComponent(btnExcluir))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPesquisa)
                            .addComponent(
                                txtPesquisa,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnPesquisar))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(
                        jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                    .addContainerGap()));

    // calcula o tamanho ideal da janela com base nos componentes
    pack();
  }

  // limpa o formulario e prepara a tela para um novo cadastro
  private void limparCampos() {
    // apaga o codigo mostrado no campo somente leitura
    txtCodigo.setText("");
    // apaga os campos editaveis do formulario
    txtNome.setText("");
    txtUf.setText("");
  }

  private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {
    // null indica que nao ha cidade selecionada, o salvar vai inserir um novo registro
    cidadeAtual = null;
    // limpa os campos do formulario para o novo cadastro
    limparCampos();
  }

  private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {
    // le e remove espacos das pontas do nome, e deixa a uf em maiusculo
    String nome = txtNome.getText().trim();
    String uf = txtUf.getText().trim().toUpperCase();

    // nome e uf sao obrigatorios
    if (nome.isEmpty() || uf.isEmpty()) {
      JOptionPane.showMessageDialog(
          this, "Preencha Nome e UF.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }
    // uf sempre tem duas letras
    if (uf.length() != 2) {
      JOptionPane.showMessageDialog(
          this, "UF deve ter 2 caracteres.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }
    // validacao de uf feita direto aqui, sem chamar classe utilitaria separada
    if (!UFS_VALIDAS.contains(uf)) {
      JOptionPane.showMessageDialog(
          this,
          "UF inválida. Informe uma sigla de estado brasileiro (ex: SP, RJ, MG).",
          "Atenção",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // se nao ha cidade selecionada, monta um objeto novo e insere no banco
    if (cidadeAtual == null) {
      Cidade nova = new Cidade(nome, uf);
      cidadeDAO.salvar(nova);
      cidadeAtual = nova;
    } else {
      // se ja existe uma cidade selecionada, atualiza os campos e envia a alteracao para o banco
      cidadeAtual.setNome(nome);
      cidadeAtual.setUf(uf);
      cidadeDAO.atualizar(cidadeAtual);
    }

    // mostra o codigo gerado ou existente no campo somente leitura
    txtCodigo.setText(String.valueOf(cidadeAtual.getId()));
    // avisa o usuario que a operacao deu certo
    JOptionPane.showMessageDialog(this, "Cidade salva com sucesso!");
    // atualiza a lista para refletir o registro salvo
    btnPesquisarActionPerformed(null);
  }

  private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
    // nao deixa excluir se nenhuma cidade estiver selecionada
    if (txtCodigo.getText().isEmpty()) {
      JOptionPane.showMessageDialog(
          this, "Selecione uma cidade para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    // bloqueia a exclusao se existirem clientes vinculados a esta cidade
    if (clienteDAO.temClientesNaCidade(cidadeAtual.getId())) {
      JOptionPane.showMessageDialog(
          this,
          "Não é possível excluir '"
              + cidadeAtual.getNome()
              + "' pois há clientes cadastrados nessa cidade.",
          "Exclusão bloqueada",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // remove a cidade selecionada do banco de dados
    cidadeDAO.excluir(cidadeAtual);
    // volta a referencia para null, ja que o registro nao existe mais
    cidadeAtual = null;
    // limpa o formulario apos a exclusao
    limparCampos();
    // avisa o usuario que a exclusao deu certo
    JOptionPane.showMessageDialog(this, "Cidade excluída com sucesso!");
    // atualiza a lista removendo o registro excluido
    btnPesquisarActionPerformed(null);
  }

  private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
    // le o texto digitado no campo de pesquisa, sem espacos nas pontas
    String termo = txtPesquisa.getText().trim();
    // se o campo estiver vazio, lista todas as cidades, senao filtra pelo nome
    List<Cidade> lista =
        termo.isEmpty() ? cidadeDAO.listarTodos() : cidadeDAO.pesquisarPorNome(termo);

    // monta um novo modelo de lista com as cidades encontradas
    javax.swing.DefaultListModel<Cidade> modelo = new javax.swing.DefaultListModel<>();
    for (Cidade c : lista) {
      modelo.addElement(c);
    }
    // troca o modelo da jlist pelo modelo recem montado
    lstCidades.setModel(modelo);
  }

  private void lstCidadesValueChanged(javax.swing.event.ListSelectionEvent evt) {
    // getValueIsAdjusting evita que o evento dispare duas vezes durante a mesma selecao
    if (evt.getValueIsAdjusting()) return;

    // pega a cidade selecionada na lista
    Cidade selecionada = lstCidades.getSelectedValue();
    // se nenhum item estiver selecionado, nao faz nada
    if (selecionada == null) return;

    // busca a cidade completa no banco pelo id para ter todos os dados atualizados
    cidadeAtual = cidadeDAO.buscarPorId(selecionada.getId());
    // se por algum motivo o registro nao existir mais, nao faz nada
    if (cidadeAtual == null) return;

    // preenche o campo de codigo com o id da cidade
    txtCodigo.setText(String.valueOf(cidadeAtual.getId()));
    // preenche o campo de nome
    txtNome.setText(cidadeAtual.getNome());
    // preenche o campo de uf
    txtUf.setText(cidadeAtual.getUf());
  }

  // declaracao das variaveis dos componentes gerados pelo gui builder, nao modificar
  private javax.swing.JButton btnExcluir;
  private javax.swing.JButton btnNovo;
  private javax.swing.JButton btnPesquisar;
  private javax.swing.JButton btnSalvar;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel lblCodigo;
  private javax.swing.JLabel lblNome;
  private javax.swing.JLabel lblPesquisa;
  private javax.swing.JLabel lblUf;
  private javax.swing.JList<Cidade> lstCidades;
  private javax.swing.JTextField txtCodigo;
  private javax.swing.JTextField txtNome;
  private javax.swing.JTextField txtPesquisa;
  private javax.swing.JTextField txtUf;
  // fim da declaracao das variaveis
}
