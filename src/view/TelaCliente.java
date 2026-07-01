package view;

import dao.CidadeDAO;
import dao.ClienteDAO;
import java.util.List;
import javax.swing.JOptionPane;
import model.Cidade;
import model.Cliente;

// tela de cadastro de clientes, jdialog gerado no estilo netbeans gui builder com grouplayout
public class TelaCliente extends javax.swing.JDialog {

  // dao responsavel pelas operacoes de cliente no banco de dados
  private final ClienteDAO clienteDAO = new ClienteDAO();
  // dao usado para carregar as cidades no combobox
  private final CidadeDAO cidadeDAO = new CidadeDAO();

  // cliente selecionado na lista, null quando for um novo cadastro
  private Cliente clienteAtual = null;

  public TelaCliente(java.awt.Frame parent, boolean modal) {
    // repassa o dono da janela e o modo modal para o construtor do jdialog
    super(parent, modal);
    // monta todos os componentes visuais da tela
    initComponents();
    // popula o combobox de cidades com os registros do banco
    carregarCidades();
    // carrega todos os clientes do banco na lista assim que a tela abre
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
    // cria o label e o campo de cpf
    lblCpf = new javax.swing.JLabel();
    txtCpf = new javax.swing.JTextField();
    // cria o label e o campo de telefone
    lblTelefone = new javax.swing.JLabel();
    txtTelefone = new javax.swing.JTextField();
    // cria o label e o campo de email
    lblEmail = new javax.swing.JLabel();
    txtEmail = new javax.swing.JTextField();
    // cria o label e o combobox de cidade
    lblCidade = new javax.swing.JLabel();
    cmbCidade = new javax.swing.JComboBox<>();
    // cria os botoes de acao
    btnNovo = new javax.swing.JButton();
    btnSalvar = new javax.swing.JButton();
    btnExcluir = new javax.swing.JButton();
    // cria o label, o campo e o botao de pesquisa
    lblPesquisa = new javax.swing.JLabel();
    txtPesquisa = new javax.swing.JTextField();
    btnPesquisar = new javax.swing.JButton();
    // cria a lista de clientes dentro de um scroll pane
    jScrollPane1 = new javax.swing.JScrollPane();
    lstClientes = new javax.swing.JList<>();

    // fecha somente esta janela quando o usuario clicar em fechar
    setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    // define o titulo exibido na barra da janela
    setTitle("Cadastro de Clientes");

    // texto do label de codigo
    lblCodigo.setText("Código:");
    // codigo nao pode ser editado, ele vem do banco
    txtCodigo.setEditable(false);
    // codigo nao recebe foco ao navegar com tab ou clicar
    txtCodigo.setFocusable(false);

    // textos dos demais labels do formulario
    lblNome.setText("Nome:");
    lblCpf.setText("CPF:");
    lblTelefone.setText("Telefone:");
    lblEmail.setText("E-mail:");
    lblCidade.setText("Cidade:");

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
    lstClientes.addListSelectionListener(evt -> lstClientesValueChanged(evt));
    // coloca a lista dentro do scroll pane para suportar muitos registros
    jScrollPane1.setViewportView(lstClientes);

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
                                480,
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
                                            .addComponent(lblCpf)
                                            .addComponent(lblTelefone)
                                            .addComponent(lblEmail)
                                            .addComponent(lblCidade)
                                            .addComponent(lblPesquisa))
                                    .addPreferredGap(
                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(
                                        layout
                                            .createParallelGroup(
                                                javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtCodigo)
                                            .addComponent(txtNome)
                                            .addComponent(txtCpf)
                                            .addComponent(txtTelefone)
                                            .addComponent(txtEmail)
                                            .addComponent(
                                                cmbCidade,
                                                0,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
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
                            .addComponent(lblCpf)
                            .addComponent(
                                txtCpf,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTelefone)
                            .addComponent(
                                txtTelefone,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblEmail)
                            .addComponent(
                                txtEmail,
                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(
                        layout
                            .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblCidade)
                            .addComponent(
                                cmbCidade,
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
    txtCpf.setText("");
    txtTelefone.setText("");
    txtEmail.setText("");
    // volta o combobox de cidade para o primeiro item, se existir algum
    if (cmbCidade.getItemCount() > 0) cmbCidade.setSelectedIndex(0);
  }

  // recarrega o combobox de cidades com os registros do banco
  private void carregarCidades() {
    // remove todos os itens existentes antes de recarregar
    cmbCidade.removeAllItems();
    // adiciona cada cidade encontrada no banco como um item do combobox
    for (Cidade c : cidadeDAO.listarTodos()) {
      cmbCidade.addItem(c);
    }
  }

  private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {
    // null indica que nao ha cliente selecionado, o salvar vai inserir um novo registro
    clienteAtual = null;
    // limpa os campos do formulario para o novo cadastro
    limparCampos();
  }

  private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {
    // le e remove espacos das pontas de cada campo de texto
    String nome = txtNome.getText().trim();
    String cpf = txtCpf.getText().trim();
    String telefone = txtTelefone.getText().trim();
    String email = txtEmail.getText().trim();
    // pega a cidade selecionada no combobox
    Cidade cidade = (Cidade) cmbCidade.getSelectedItem();

    // nome e obrigatorio
    if (nome.isEmpty()) {
      JOptionPane.showMessageDialog(
          this, "Preencha o nome do cliente.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }
    // cidade e obrigatoria
    if (cidade == null) {
      JOptionPane.showMessageDialog(
          this,
          "Selecione uma cidade. Cadastre cidades primeiro.",
          "Atenção",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // validacao de cpf feita direto aqui, sem chamar classe utilitaria separada
    if (!cpf.isEmpty()) {
      // mantem so os digitos do cpf informado
      String cpfNums = cpf.replaceAll("[^0-9]", "");
      // cpf precisa ter 11 digitos e nao pode ser uma sequencia repetida
      boolean cpfOk = cpfNums.length() == 11 && cpfNums.chars().distinct().count() > 1;
      // confere o primeiro digito verificador
      if (cpfOk) {
        int soma = 0;
        for (int i = 0; i < 9; i++) soma += (cpfNums.charAt(i) - '0') * (10 - i);
        int dig1 = 11 - (soma % 11);
        if (dig1 >= 10) dig1 = 0;
        cpfOk = dig1 == (cpfNums.charAt(9) - '0');
      }
      // confere o segundo digito verificador
      if (cpfOk) {
        int soma = 0;
        for (int i = 0; i < 10; i++) soma += (cpfNums.charAt(i) - '0') * (11 - i);
        int dig2 = 11 - (soma % 11);
        if (dig2 >= 10) dig2 = 0;
        cpfOk = dig2 == (cpfNums.charAt(10) - '0');
      }
      // se algum dos digitos nao bateu, avisa e interrompe o salvamento
      if (!cpfOk) {
        JOptionPane.showMessageDialog(
            this, "CPF inválido.", "Atenção", JOptionPane.WARNING_MESSAGE);
        return;
      }
    }

    // validacao de telefone feita direto aqui: aceita 10 digitos (fixo) ou 11 (celular)
    if (!telefone.isEmpty()) {
      String telNums = telefone.replaceAll("[^0-9]", "");
      if (telNums.length() != 10 && telNums.length() != 11) {
        JOptionPane.showMessageDialog(
            this,
            "Telefone inválido. Informe 10 dígitos (fixo) ou 11 dígitos (celular).",
            "Atenção",
            JOptionPane.WARNING_MESSAGE);
        return;
      }
    }

    // validacao de e-mail feita direto aqui com uma expressao regular simples
    if (!email.isEmpty() && !email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
      JOptionPane.showMessageDialog(
          this, "E-mail inválido.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    // se nao ha cliente selecionado, monta um objeto novo e insere no banco
    if (clienteAtual == null) {
      Cliente novo = new Cliente(nome, cpf, telefone, email, cidade);
      clienteDAO.salvar(novo);
      clienteAtual = novo;
    } else {
      // se ja existe um cliente selecionado, atualiza os campos e envia a alteracao para o banco
      clienteAtual.setNome(nome);
      clienteAtual.setCpf(cpf);
      clienteAtual.setTelefone(telefone);
      clienteAtual.setEmail(email);
      clienteAtual.setCidade(cidade);
      clienteDAO.atualizar(clienteAtual);
    }

    // mostra o codigo gerado ou existente no campo somente leitura
    txtCodigo.setText(String.valueOf(clienteAtual.getId()));
    // avisa o usuario que a operacao deu certo
    JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
    // atualiza a lista para refletir o registro salvo
    btnPesquisarActionPerformed(null);
  }

  private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
    // nao deixa excluir se nenhum cliente estiver selecionado
    if (txtCodigo.getText().isEmpty()) {
      JOptionPane.showMessageDialog(
          this, "Selecione um cliente para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    // remove o cliente selecionado do banco de dados
    clienteDAO.excluir(clienteAtual);
    // volta a referencia para null, ja que o registro nao existe mais
    clienteAtual = null;
    // limpa o formulario apos a exclusao
    limparCampos();
    // avisa o usuario que a exclusao deu certo
    JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
    // atualiza a lista removendo o registro excluido
    btnPesquisarActionPerformed(null);
  }

  private void btnPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
    // le o texto digitado no campo de pesquisa, sem espacos nas pontas
    String termo = txtPesquisa.getText().trim();
    // se o campo estiver vazio, lista todos os clientes, senao filtra pelo nome
    List<Cliente> lista =
        termo.isEmpty() ? clienteDAO.listarTodos() : clienteDAO.pesquisarPorNome(termo);

    // monta um novo modelo de lista com os clientes encontrados
    javax.swing.DefaultListModel<Cliente> modelo = new javax.swing.DefaultListModel<>();
    for (Cliente c : lista) {
      modelo.addElement(c);
    }
    // troca o modelo da jlist pelo modelo recem montado
    lstClientes.setModel(modelo);
  }

  private void lstClientesValueChanged(javax.swing.event.ListSelectionEvent evt) {
    // getValueIsAdjusting evita que o evento dispare duas vezes durante a mesma selecao
    if (evt.getValueIsAdjusting()) return;

    // pega o cliente selecionado na lista
    Cliente selecionado = lstClientes.getSelectedValue();
    // se nenhum item estiver selecionado, nao faz nada
    if (selecionado == null) return;

    // busca o cliente completo no banco pelo id para ter todos os dados atualizados
    clienteAtual = clienteDAO.buscarPorId(selecionado.getId());
    // se por algum motivo o registro nao existir mais, nao faz nada
    if (clienteAtual == null) return;

    // preenche o campo de codigo com o id do cliente
    txtCodigo.setText(String.valueOf(clienteAtual.getId()));
    // preenche o campo de nome
    txtNome.setText(clienteAtual.getNome());
    // preenche o cpf, campos opcionais podem ser null, entao exibe vazio nesse caso
    txtCpf.setText(clienteAtual.getCpf() != null ? clienteAtual.getCpf() : "");
    // preenche o telefone
    txtTelefone.setText(clienteAtual.getTelefone() != null ? clienteAtual.getTelefone() : "");
    // preenche o email
    txtEmail.setText(clienteAtual.getEmail() != null ? clienteAtual.getEmail() : "");
    // seleciona no combobox a cidade vinculada ao cliente
    cmbCidade.setSelectedItem(clienteAtual.getCidade());
  }

  // declaracao das variaveis dos componentes gerados pelo gui builder, nao modificar
  private javax.swing.JButton btnExcluir;
  private javax.swing.JButton btnNovo;
  private javax.swing.JButton btnPesquisar;
  private javax.swing.JButton btnSalvar;
  private javax.swing.JComboBox<Cidade> cmbCidade;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JLabel lblCidade;
  private javax.swing.JLabel lblCodigo;
  private javax.swing.JLabel lblCpf;
  private javax.swing.JLabel lblEmail;
  private javax.swing.JLabel lblNome;
  private javax.swing.JLabel lblPesquisa;
  private javax.swing.JLabel lblTelefone;
  private javax.swing.JList<Cliente> lstClientes;
  private javax.swing.JTextField txtCodigo;
  private javax.swing.JTextField txtCpf;
  private javax.swing.JTextField txtEmail;
  private javax.swing.JTextField txtNome;
  private javax.swing.JTextField txtPesquisa;
  private javax.swing.JTextField txtTelefone;
  // fim da declaracao das variaveis
}
