package view;

import dao.CidadeDAO;
import dao.ClienteDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Cidade;
import model.Cliente;

// tela de cadastro de clientes com operacoes de incluir, alterar, excluir e pesquisar
public class TelaCliente extends JFrame {

  // dao responsavel pelas operacoes de cliente no banco
  private final ClienteDAO clienteDAO = new ClienteDAO();

  // dao usado para carregar as cidades no combobox
  private final CidadeDAO cidadeDAO = new CidadeDAO();

  // campos de texto do formulario
  private JTextField campoCodigo, campoNome, campoCpf, campoTelefone, campoEmail, campoPesquisa;

  // combobox que lista as cidades para o usuario escolher
  private JComboBox<Cidade> comboCidade;

  // tabela que lista os clientes cadastrados
  private JTable tabela;

  // modelo que controla os dados exibidos na tabela
  private DefaultTableModel modeloTabela;

  // guarda o cliente que esta selecionado no momento, null quando for um cadastro novo
  private Cliente clienteAtual = null;

  public TelaCliente() {
    // titulo da janela
    setTitle("Cadastro de Clientes");
    // tamanho da janela em pixels, maior que a de cidades por ter mais campos
    setSize(800, 560);
    // centraliza a janela na tela
    setLocationRelativeTo(null);
    // ao fechar apenas essa janela, a aplicacao continua rodando
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    // cria e posiciona todos os componentes visuais
    iniciarComponentes();
    // carrega as cidades no combobox
    carregarCidades();
    // carrega os clientes cadastrados na tabela ao abrir a tela
    carregarTabela(clienteDAO.listarTodos());
  }

  // cria e organiza todos os componentes da tela
  private void iniciarComponentes() {
    // espaco de 10px entre as regioes do BorderLayout
    setLayout(new BorderLayout(10, 10));

    // painel do formulario com borda e titulo
    JPanel painelFormulario = new JPanel(new GridBagLayout());
    painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));

    // restricoes controla o posicionamento de cada componente no GridBagLayout
    GridBagConstraints restricoes = new GridBagConstraints();
    // espaco de 5px em volta de cada componente
    restricoes.insets = new Insets(5, 5, 5, 5);
    // faz os componentes ocuparem toda a largura da celula
    restricoes.fill = GridBagConstraints.HORIZONTAL;

    // campo de codigo somente leitura, preenchido automaticamente pelo banco
    campoCodigo = new JTextField(10);
    campoCodigo.setEditable(false);

    // campos editaveis pelo usuario
    campoNome = new JTextField(30);
    campoCpf = new JTextField(15);
    campoTelefone = new JTextField(15);
    campoEmail = new JTextField(25);

    // combobox que sera preenchido com as cidades do banco
    comboCidade = new JComboBox<>();

    // linha 0: codigo
    restricoes.gridx = 0;
    restricoes.gridy = 0;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("Código:"), restricoes);
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoCodigo, restricoes);

    // linha 1: nome
    restricoes.gridx = 0;
    restricoes.gridy = 1;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("Nome:"), restricoes);
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoNome, restricoes);

    // linha 2: cpf
    restricoes.gridx = 0;
    restricoes.gridy = 2;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("CPF:"), restricoes);
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoCpf, restricoes);

    // linha 3: telefone
    restricoes.gridx = 0;
    restricoes.gridy = 3;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("Telefone:"), restricoes);
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoTelefone, restricoes);

    // linha 4: email
    restricoes.gridx = 0;
    restricoes.gridy = 4;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("E-mail:"), restricoes);
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoEmail, restricoes);

    // linha 5: cidade (combobox com as cidades cadastradas)
    restricoes.gridx = 0;
    restricoes.gridy = 5;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("Cidade:"), restricoes);
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(comboCidade, restricoes);

    // painel com os tres botoes de acao centralizados
    JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    JButton botaoNovo = new JButton("Novo");
    JButton botaoSalvar = new JButton("Salvar");
    JButton botaoExcluir = new JButton("Excluir");

    // cada botao chama o metodo correspondente
    botaoNovo.addActionListener(e -> novo());
    botaoSalvar.addActionListener(e -> salvar());
    botaoExcluir.addActionListener(e -> excluir());

    painelBotoes.add(botaoNovo);
    painelBotoes.add(botaoSalvar);
    painelBotoes.add(botaoExcluir);

    // painel que junta formulario e botoes para ocupar a parte de cima da tela
    JPanel painelSuperior = new JPanel(new BorderLayout());
    painelSuperior.add(painelFormulario, BorderLayout.CENTER);
    painelSuperior.add(painelBotoes, BorderLayout.SOUTH);
    add(painelSuperior, BorderLayout.NORTH);

    // painel de pesquisa com campo de texto e botoes
    JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
    campoPesquisa = new JTextField(25);
    JButton botaoPesquisar = new JButton("Pesquisar");
    JButton botaoListarTodos = new JButton("Listar Todos");

    // ao clicar em pesquisar chama o metodo de busca
    botaoPesquisar.addActionListener(e -> pesquisar());
    // listar todos recarrega a tabela com todos os clientes sem filtro
    botaoListarTodos.addActionListener(e -> carregarTabela(clienteDAO.listarTodos()));
    // pressionar Enter no campo de pesquisa tambem dispara a busca
    campoPesquisa.addActionListener(e -> pesquisar());

    painelPesquisa.add(new JLabel("Pesquisar por nome:"));
    painelPesquisa.add(campoPesquisa);
    painelPesquisa.add(botaoPesquisar);
    painelPesquisa.add(botaoListarTodos);

    // modelo da tabela com as colunas definidas
    // isCellEditable retornando false impede que o usuario edite diretamente na tabela
    modeloTabela =
        new DefaultTableModel(
            new String[] {"Código", "Nome", "CPF", "Telefone", "E-mail", "Cidade"}, 0) {
          @Override
          public boolean isCellEditable(int linha, int coluna) {
            return false;
          }
        };

    // cria a tabela com o modelo acima
    tabela = new JTable(modeloTabela);
    // permite selecionar apenas uma linha por vez
    tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // quando o usuario clica em uma linha da tabela, preenche o formulario com os dados
    tabela
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              // getValueIsAdjusting evita que o evento dispare duas vezes na mesma selecao
              if (!e.getValueIsAdjusting()) selecionarDaTabela();
            });

    // coloca a tabela dentro de um scroll para suportar muitos registros
    JScrollPane rolagem = new JScrollPane(tabela);

    // painel inferior que junta a barra de pesquisa e a tabela
    JPanel painelInferior = new JPanel(new BorderLayout());
    painelInferior.add(painelPesquisa, BorderLayout.NORTH);
    painelInferior.add(rolagem, BorderLayout.CENTER);

    add(painelInferior, BorderLayout.CENTER);
  }

  // limpa o combobox e recarrega as cidades do banco
  // chamado ao abrir a tela e ao clicar em Novo para refletir cidades adicionadas recentemente
  private void carregarCidades() {
    comboCidade.removeAllItems();
    List<Cidade> cidades = cidadeDAO.listarTodos();
    // adiciona cada cidade como item do combobox
    // o texto exibido vem do metodo toString() da classe Cidade
    for (Cidade c : cidades) {
      comboCidade.addItem(c);
    }
  }

  // limpa o formulario e prepara para um novo cadastro
  private void novo() {
    // null indica que nao ha cliente selecionado, entao o salvar vai inserir
    clienteAtual = null;
    campoCodigo.setText("");
    campoNome.setText("");
    campoCpf.setText("");
    campoTelefone.setText("");
    campoEmail.setText("");
    // recarrega as cidades para refletir novos cadastros feitos na outra tela
    carregarCidades();
    // seleciona a primeira cidade da lista se houver alguma cadastrada
    if (comboCidade.getItemCount() > 0) comboCidade.setSelectedIndex(0);
    // move o cursor para o campo nome para facilitar a digitacao
    campoNome.requestFocus();
  }

  // salva ou atualiza um cliente dependendo se clienteAtual e null ou nao
  private void salvar() {
    // remove espacos extras nas pontas de cada campo
    String nome = campoNome.getText().trim();
    String cpf = campoCpf.getText().trim();
    String telefone = campoTelefone.getText().trim();
    String email = campoEmail.getText().trim();
    // pega o objeto Cidade selecionado no combobox
    Cidade cidade = (Cidade) comboCidade.getSelectedItem();

    // nome e obrigatorio
    if (nome.isEmpty()) {
      JOptionPane.showMessageDialog(
          this, "Preencha o nome do cliente.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    // cidade e obrigatoria, pode ser null se nao houver nenhuma cidade cadastrada
    if (cidade == null) {
      JOptionPane.showMessageDialog(
          this,
          "Selecione uma cidade. Cadastre cidades primeiro.",
          "Atenção",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      // se clienteAtual for null, e um cadastro novo
      if (clienteAtual == null) {
        Cliente novo = new Cliente(nome, cpf, telefone, email, cidade);
        clienteDAO.salvar(novo);
        JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
      } else {
        // se clienteAtual tiver valor, e uma edicao do registro ja existente
        clienteAtual.setNome(nome);
        clienteAtual.setCpf(cpf);
        clienteAtual.setTelefone(telefone);
        clienteAtual.setEmail(email);
        clienteAtual.setCidade(cidade);
        clienteDAO.atualizar(clienteAtual);
        JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
      }
      // limpa o formulario e recarrega a tabela com os dados atualizados
      novo();
      carregarTabela(clienteDAO.listarTodos());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

  // remove o cliente selecionado do banco
  private void excluir() {
    // nao deixa excluir se nenhum cliente estiver selecionado
    if (clienteAtual == null) {
      JOptionPane.showMessageDialog(
          this, "Selecione um cliente para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    // pede confirmacao antes de excluir
    int confirmacao =
        JOptionPane.showConfirmDialog(
            this,
            "Deseja excluir o cliente '" + clienteAtual.getNome() + "'?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION);

    if (confirmacao == JOptionPane.YES_OPTION) {
      try {
        clienteDAO.excluir(clienteAtual);
        JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
        // limpa o formulario e atualiza a tabela
        novo();
        carregarTabela(clienteDAO.listarTodos());
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  // filtra a tabela pelo nome digitado no campo de pesquisa
  private void pesquisar() {
    String termo = campoPesquisa.getText().trim();
    // se o campo estiver vazio, lista todos os clientes
    if (termo.isEmpty()) {
      carregarTabela(clienteDAO.listarTodos());
    } else {
      carregarTabela(clienteDAO.pesquisarPorNome(termo));
    }
  }

  // preenche a tabela com a lista de clientes recebida
  private void carregarTabela(List<Cliente> lista) {
    // remove todas as linhas antes de adicionar os novos dados
    modeloTabela.setRowCount(0);
    for (Cliente c : lista) {
      // cada linha tem codigo, nome, cpf, telefone, email e o nome da cidade
      modeloTabela.addRow(
          new Object[] {
            c.getId(),
            c.getNome(),
            c.getCpf(),
            c.getTelefone(),
            c.getEmail(),
            // getCidade pode voltar null em casos extremos, por isso tem o operador ternario
            c.getCidade() != null ? c.getCidade().toString() : ""
          });
    }
  }

  // preenche o formulario com os dados da linha clicada na tabela
  private void selecionarDaTabela() {
    // getSelectedRow retorna -1 se nenhuma linha estiver selecionada
    int linha = tabela.getSelectedRow();
    if (linha < 0) return;

    // pega o id da primeira coluna da linha selecionada
    Long id = (Long) modeloTabela.getValueAt(linha, 0);

    // busca o objeto completo no banco pelo id
    clienteAtual = clienteDAO.buscarPorId(id);
    if (clienteAtual != null) {
      // preenche os campos com os dados do cliente selecionado
      campoCodigo.setText(String.valueOf(clienteAtual.getId()));
      campoNome.setText(clienteAtual.getNome());
      // campos opcionais podem ser null, por isso o operador ternario
      campoCpf.setText(clienteAtual.getCpf() != null ? clienteAtual.getCpf() : "");
      campoTelefone.setText(clienteAtual.getTelefone() != null ? clienteAtual.getTelefone() : "");
      campoEmail.setText(clienteAtual.getEmail() != null ? clienteAtual.getEmail() : "");

      // recarrega as cidades e seleciona a cidade do cliente no combobox
      carregarCidades();
      if (clienteAtual.getCidade() != null) {
        // percorre os itens do combobox ate achar o que tem o mesmo id da cidade do cliente
        for (int i = 0; i < comboCidade.getItemCount(); i++) {
          if (comboCidade.getItemAt(i).getId().equals(clienteAtual.getCidade().getId())) {
            comboCidade.setSelectedIndex(i);
            // para o loop assim que achar a cidade certa
            break;
          }
        }
      }
    }
  }
}
