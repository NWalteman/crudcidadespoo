package view;

import dao.CidadeDAO;
import dao.ClienteDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Cidade;

// tela de cadastro de cidades com operacoes de incluir, alterar, excluir e pesquisar
public class TelaCidade extends JFrame {

  // dao responsavel pelas operacoes de cidade no banco
  private final CidadeDAO cidadeDAO = new CidadeDAO();

  // dao usado para verificar se ha clientes vinculados antes de excluir uma cidade
  private final ClienteDAO clienteDAO = new ClienteDAO();

  // campos de texto do formulario
  private JTextField campoCodigo, campoNome, campoUF, campoPesquisa;

  // tabela que lista as cidades cadastradas
  private JTable tabela;

  // modelo que controla os dados exibidos na tabela
  private DefaultTableModel modeloTabela;

  // guarda a cidade que esta selecionada no momento, null quando for um cadastro novo
  private Cidade cidadeAtual = null;

  public TelaCidade() {
    // titulo da janela
    setTitle("Cadastro de Cidades");
    // tamanho da janela em pixels
    setSize(700, 500);
    // centraliza a janela na tela
    setLocationRelativeTo(null);
    // ao fechar apenas essa janela, a aplicacao continua rodando
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    // cria e posiciona todos os componentes visuais
    iniciarComponentes();
    // carrega as cidades cadastradas na tabela ao abrir a tela
    carregarTabela(cidadeDAO.listarTodos());
  }

  // cria e organiza todos os componentes da tela
  private void iniciarComponentes() {
    // espaco de 10px entre as regioes do BorderLayout
    setLayout(new BorderLayout(10, 10));

    // painel do formulario com borda e titulo
    JPanel painelFormulario = new JPanel(new GridBagLayout());
    painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados da Cidade"));

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
    campoUF = new JTextField(5);

    // posiciona o label "Codigo" na coluna 0, linha 0
    restricoes.gridx = 0;
    restricoes.gridy = 0;
    painelFormulario.add(new JLabel("Código:"), restricoes);
    // posiciona o campo de codigo na coluna 1, mesma linha
    restricoes.gridx = 1;
    painelFormulario.add(campoCodigo, restricoes);

    // label "Nome" na coluna 0, linha 1
    restricoes.gridx = 0;
    restricoes.gridy = 1;
    painelFormulario.add(new JLabel("Nome:"), restricoes);
    // campo de nome na coluna 1, weightx=1 faz ele expandir horizontalmente
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoNome, restricoes);

    // label "UF" na coluna 0, linha 2
    restricoes.gridx = 0;
    restricoes.gridy = 2;
    // reseta o peso para que o campo de UF nao se expanda demais
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("UF:"), restricoes);
    // campo de UF na coluna 1
    restricoes.gridx = 1;
    painelFormulario.add(campoUF, restricoes);

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
    // listar todos recarrega a tabela com todas as cidades sem filtro
    botaoListarTodos.addActionListener(e -> carregarTabela(cidadeDAO.listarTodos()));
    // pressionar Enter no campo de pesquisa tambem dispara a busca
    campoPesquisa.addActionListener(e -> pesquisar());

    painelPesquisa.add(new JLabel("Pesquisar por nome:"));
    painelPesquisa.add(campoPesquisa);
    painelPesquisa.add(botaoPesquisar);
    painelPesquisa.add(botaoListarTodos);

    // modelo da tabela com as colunas definidas
    // isCellEditable retornando false impede que o usuario edite diretamente na tabela
    modeloTabela =
        new DefaultTableModel(new String[] {"Código", "Nome", "UF"}, 0) {
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

  // limpa o formulario e prepara para um novo cadastro
  private void novo() {
    // null indica que nao ha cidade selecionada, entao o salvar vai inserir
    cidadeAtual = null;
    campoCodigo.setText("");
    campoNome.setText("");
    campoUF.setText("");
    // move o cursor para o campo nome para facilitar a digitacao
    campoNome.requestFocus();
  }

  // salva ou atualiza uma cidade dependendo se cidadeAtual e null ou nao
  private void salvar() {
    // remove espacos extras nas pontas e converte UF para maiusculo
    String nome = campoNome.getText().trim();
    String uf = campoUF.getText().trim().toUpperCase();

    // valida se os campos obrigatorios foram preenchidos
    if (nome.isEmpty() || uf.isEmpty()) {
      JOptionPane.showMessageDialog(
          this, "Preencha Nome e UF.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    // valida se a UF tem exatamente 2 caracteres
    if (uf.length() != 2) {
      JOptionPane.showMessageDialog(
          this, "UF deve ter 2 caracteres.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      // se cidadeAtual for null, e um cadastro novo
      if (cidadeAtual == null) {
        Cidade nova = new Cidade(nome, uf);
        cidadeDAO.salvar(nova);
        JOptionPane.showMessageDialog(this, "Cidade cadastrada com sucesso!");
      } else {
        // se cidadeAtual tiver valor, e uma edicao do registro ja existente
        cidadeAtual.setNome(nome);
        cidadeAtual.setUf(uf);
        cidadeDAO.atualizar(cidadeAtual);
        JOptionPane.showMessageDialog(this, "Cidade atualizada com sucesso!");
      }
      // limpa o formulario e recarrega a tabela com os dados atualizados
      novo();
      carregarTabela(cidadeDAO.listarTodos());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

  // remove a cidade selecionada do banco
  private void excluir() {
    // nao deixa excluir se nenhuma cidade estiver selecionada
    if (cidadeAtual == null) {
      JOptionPane.showMessageDialog(
          this, "Selecione uma cidade para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    // bloqueia a exclusao se existirem clientes cadastrados nessa cidade
    // isso evita erro de chave estrangeira no banco
    if (clienteDAO.temClientesNaCidade(cidadeAtual.getId())) {
      JOptionPane.showMessageDialog(
          this,
          "Nao e possivel excluir '"
              + cidadeAtual.getNome()
              + "' pois ha clientes cadastrados nessa cidade.",
          "Exclusão bloqueada",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    // pede confirmacao antes de excluir
    int confirmacao =
        JOptionPane.showConfirmDialog(
            this,
            "Deseja excluir a cidade '" + cidadeAtual.getNome() + "'?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION);

    if (confirmacao == JOptionPane.YES_OPTION) {
      try {
        cidadeDAO.excluir(cidadeAtual);
        JOptionPane.showMessageDialog(this, "Cidade excluída com sucesso!");
        // limpa o formulario e atualiza a tabela
        novo();
        carregarTabela(cidadeDAO.listarTodos());
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  // filtra a tabela pelo nome digitado no campo de pesquisa
  private void pesquisar() {
    String termo = campoPesquisa.getText().trim();
    // se o campo estiver vazio, lista todas as cidades
    if (termo.isEmpty()) {
      carregarTabela(cidadeDAO.listarTodos());
    } else {
      carregarTabela(cidadeDAO.pesquisarPorNome(termo));
    }
  }

  // preenche a tabela com a lista de cidades recebida
  private void carregarTabela(List<Cidade> lista) {
    // remove todas as linhas antes de adicionar os novos dados
    modeloTabela.setRowCount(0);
    for (Cidade c : lista) {
      // cada linha tem o codigo, nome e uf da cidade
      modeloTabela.addRow(new Object[] {c.getId(), c.getNome(), c.getUf()});
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
    cidadeAtual = cidadeDAO.buscarPorId(id);
    if (cidadeAtual != null) {
      // preenche os campos do formulario com os dados da cidade selecionada
      campoCodigo.setText(String.valueOf(cidadeAtual.getId()));
      campoNome.setText(cidadeAtual.getNome());
      campoUF.setText(cidadeAtual.getUf());
    }
  }
}
