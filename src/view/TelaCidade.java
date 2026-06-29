package view;

import dao.CidadeDAO;
import dao.ClienteDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Cidade;
import util.Validador;

// tela de cadastro de cidades com operacoes de incluir, alterar, excluir e pesquisar
public class TelaCidade extends JFrame {

  // dao responsavel pelas operacoes de cidade no banco de dados
  private final CidadeDAO cidadeDAO = new CidadeDAO();

  // dao usado para verificar se ha clientes vinculados antes de excluir uma cidade
  private final ClienteDAO clienteDAO = new ClienteDAO();

  // campos de texto do formulario de dados da cidade
  private JTextField campoCodigo, campoNome, campoUF, campoPesquisa;

  // tabela que exibe a lista de cidades cadastradas
  private JTable tabela;

  // modelo que gerencia os dados exibidos na tabela (linhas e colunas)
  private DefaultTableModel modeloTabela;

  // guarda a cidade selecionada na tabela, null quando for um novo cadastro
  private Cidade cidadeAtual = null;

  public TelaCidade() {
    // titulo que aparece na barra da janela
    setTitle("Cadastro de Cidades");
    // tamanho da janela em pixels (largura x altura)
    setSize(700, 500);
    // centraliza a janela no meio da tela
    setLocationRelativeTo(null);
    // ao fechar esta janela, apenas ela e fechada, a aplicacao continua rodando
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    // chama o metodo que cria e posiciona todos os componentes visuais
    iniciarComponentes();
    // carrega todas as cidades do banco na tabela assim que a tela abre
    carregarTabela(cidadeDAO.listarTodos());
  }

  // cria e organiza todos os componentes visuais da tela
  private void iniciarComponentes() {
    // BorderLayout divide a tela em cinco regioes: norte, sul, leste, oeste e centro
    // os valores 10 e 10 sao o espaco horizontal e vertical entre as regioes
    setLayout(new BorderLayout(10, 10));

    // painel do formulario com titulo visivel na borda
    JPanel painelFormulario = new JPanel(new GridBagLayout());
    painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados da Cidade"));

    // GridBagConstraints controla o posicionamento de cada componente no GridBagLayout
    GridBagConstraints restricoes = new GridBagConstraints();
    // espaco de 5 pixels em volta de cada componente
    restricoes.insets = new Insets(5, 5, 5, 5);
    // faz os componentes preencherem toda a largura da celula
    restricoes.fill = GridBagConstraints.HORIZONTAL;

    // campo de codigo somente leitura, preenchido automaticamente pelo banco
    campoCodigo = new JTextField(10);
    campoCodigo.setEditable(false);

    // campo editavel para o nome da cidade
    campoNome = new JTextField(30);

    // campo editavel para a sigla do estado (UF), tamanho pequeno
    campoUF = new JTextField(5);

    // posiciona o label "Codigo" na coluna 0, linha 0 do grid
    restricoes.gridx = 0;
    restricoes.gridy = 0;
    painelFormulario.add(new JLabel("Código:"), restricoes);
    // posiciona o campo de codigo na coluna 1, mesma linha
    restricoes.gridx = 1;
    painelFormulario.add(campoCodigo, restricoes);

    // posiciona o label "Nome" na coluna 0, linha 1
    restricoes.gridx = 0;
    restricoes.gridy = 1;
    painelFormulario.add(new JLabel("Nome:"), restricoes);
    // posiciona o campo de nome na coluna 1, linha 1
    // weightx = 1 faz o campo expandir horizontalmente para ocupar o espaco disponivel
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoNome, restricoes);

    // posiciona o label "UF" na coluna 0, linha 2
    restricoes.gridx = 0;
    restricoes.gridy = 2;
    // reseta o peso para que o campo UF nao expanda como o campo nome
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("UF:"), restricoes);
    // posiciona o campo UF na coluna 1, linha 2
    restricoes.gridx = 1;
    painelFormulario.add(campoUF, restricoes);

    // painel com os tres botoes de acao centralizados com espaco entre eles
    JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    JButton botaoNovo = new JButton("Novo");
    JButton botaoSalvar = new JButton("Salvar");
    JButton botaoExcluir = new JButton("Excluir");

    // cada botao chama o metodo correspondente quando clicado
    botaoNovo.addActionListener(e -> novo());
    botaoSalvar.addActionListener(e -> salvar());
    botaoExcluir.addActionListener(e -> excluir());

    // adiciona os botoes ao painel de botoes
    painelBotoes.add(botaoNovo);
    painelBotoes.add(botaoSalvar);
    painelBotoes.add(botaoExcluir);

    // painel superior que agrupa o formulario e os botoes
    JPanel painelSuperior = new JPanel(new BorderLayout());
    // formulario ocupa o centro do painel superior
    painelSuperior.add(painelFormulario, BorderLayout.CENTER);
    // botoes ficam abaixo do formulario
    painelSuperior.add(painelBotoes, BorderLayout.SOUTH);
    // posiciona o painel superior na regiao norte da janela principal
    add(painelSuperior, BorderLayout.NORTH);

    // painel de pesquisa com campo de texto e botoes de busca
    JPanel painelPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
    // campo onde o usuario digita o nome para pesquisar
    campoPesquisa = new JTextField(25);
    JButton botaoPesquisar = new JButton("Pesquisar");
    JButton botaoListarTodos = new JButton("Listar Todos");

    // ao clicar em pesquisar, filtra as cidades pelo nome digitado
    botaoPesquisar.addActionListener(e -> pesquisar());
    // ao clicar em listar todos, remove o filtro e exibe todas as cidades
    botaoListarTodos.addActionListener(e -> carregarTabela(cidadeDAO.listarTodos()));
    // pressionar Enter no campo de pesquisa tambem dispara a busca
    campoPesquisa.addActionListener(e -> pesquisar());

    // adiciona os componentes de pesquisa ao painel
    painelPesquisa.add(new JLabel("Pesquisar por nome:"));
    painelPesquisa.add(campoPesquisa);
    painelPesquisa.add(botaoPesquisar);
    painelPesquisa.add(botaoListarTodos);

    // modelo da tabela define as colunas e armazena os dados das linhas
    // isCellEditable retornando false impede que o usuario edite celulas diretamente na tabela
    modeloTabela =
        new DefaultTableModel(new String[] {"Código", "Nome", "UF"}, 0) {
          @Override
          public boolean isCellEditable(int linha, int coluna) {
            // nenhuma celula pode ser editada diretamente na tabela
            return false;
          }
        };

    // cria a tabela com o modelo definido acima
    tabela = new JTable(modeloTabela);
    // permite selecionar apenas uma linha por vez
    tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    // quando o usuario clica em uma linha da tabela, preenche o formulario com os dados
    tabela
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              // getValueIsAdjusting evita que o evento dispare duas vezes durante a mesma selecao
              if (!e.getValueIsAdjusting()) selecionarDaTabela();
            });

    // coloca a tabela dentro de um painel com barra de rolagem para suportar muitos registros
    JScrollPane rolagem = new JScrollPane(tabela);

    // painel inferior que agrupa a barra de pesquisa e a tabela
    JPanel painelInferior = new JPanel(new BorderLayout());
    // barra de pesquisa no topo do painel inferior
    painelInferior.add(painelPesquisa, BorderLayout.NORTH);
    // tabela ocupa o restante do espaco no centro
    painelInferior.add(rolagem, BorderLayout.CENTER);

    // posiciona o painel inferior na regiao central da janela principal
    add(painelInferior, BorderLayout.CENTER);
  }

  // limpa o formulario e prepara a tela para um novo cadastro
  private void novo() {
    // null indica que nao ha cidade selecionada, entao o salvar vai inserir um novo registro
    cidadeAtual = null;
    // limpa todos os campos do formulario
    campoCodigo.setText("");
    campoNome.setText("");
    campoUF.setText("");
    // move o foco para o campo nome para facilitar a digitacao
    campoNome.requestFocus();
  }

  // salva ou atualiza uma cidade dependendo se cidadeAtual e null ou nao
  private void salvar() {
    // remove espacos extras nas pontas dos campos e converte UF para maiusculo
    String nome = campoNome.getText().trim();
    String uf = campoUF.getText().trim().toUpperCase();

    // valida se os campos obrigatorios foram preenchidos
    if (nome.isEmpty() || uf.isEmpty()) {
      JOptionPane.showMessageDialog(
          this, "Preencha Nome e UF.", "Atenção", JOptionPane.WARNING_MESSAGE);
      // interrompe o metodo para nao tentar salvar com dados invalidos
      return;
    }
    if (uf.length() != 2) {
      JOptionPane.showMessageDialog(
          this, "UF deve ter 2 caracteres.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (!Validador.ufValida(uf)) {
      JOptionPane.showMessageDialog(
          this, "UF inválida. Informe uma sigla de estado brasileiro (ex: SP, RJ, MG).",
          "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      // se cidadeAtual for null, e um cadastro novo
      if (cidadeAtual == null) {
        // cria um novo objeto Cidade com os dados do formulario e salva no banco
        cidadeDAO.salvar(new Cidade(nome, uf));
        JOptionPane.showMessageDialog(this, "Cidade cadastrada com sucesso!");
      } else {
        // se cidadeAtual tiver valor, e uma edicao do registro ja existente
        // atualiza os campos do objeto com os novos valores digitados
        cidadeAtual.setNome(nome);
        cidadeAtual.setUf(uf);
        // envia as alteracoes para o banco
        cidadeDAO.atualizar(cidadeAtual);
        JOptionPane.showMessageDialog(this, "Cidade atualizada com sucesso!");
      }
      // limpa o formulario apos salvar
      novo();
      // recarrega a tabela para mostrar o registro salvo ou atualizado
      carregarTabela(cidadeDAO.listarTodos());
    } catch (Exception e) {
      // exibe a mensagem de erro caso algo falhe no banco
      JOptionPane.showMessageDialog(
          this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

  // remove a cidade selecionada do banco de dados
  private void excluir() {
    // nao permite excluir se nenhuma cidade estiver selecionada no formulario
    if (cidadeAtual == null) {
      JOptionPane.showMessageDialog(
          this, "Selecione uma cidade para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    // bloqueia a exclusao se existirem clientes vinculados a esta cidade
    // isso evita erro de chave estrangeira no banco (integridade referencial)
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

    // pede confirmacao do usuario antes de excluir permanentemente
    int ok =
        JOptionPane.showConfirmDialog(
            this,
            "Deseja excluir a cidade '" + cidadeAtual.getNome() + "'?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION);

    // so executa a exclusao se o usuario confirmar clicando em "Sim"
    if (ok == JOptionPane.YES_OPTION) {
      try {
        // remove o registro do banco de dados
        cidadeDAO.excluir(cidadeAtual);
        JOptionPane.showMessageDialog(this, "Cidade excluída com sucesso!");
        // limpa o formulario apos excluir
        novo();
        // recarrega a tabela removendo o registro excluido da lista
        carregarTabela(cidadeDAO.listarTodos());
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  // filtra a tabela exibindo apenas as cidades cujo nome contenha o texto pesquisado
  private void pesquisar() {
    // pega o texto digitado no campo de pesquisa, sem espacos nas pontas
    String termo = campoPesquisa.getText().trim();
    // se o campo estiver vazio, lista todas as cidades sem filtro
    carregarTabela(termo.isEmpty() ? cidadeDAO.listarTodos() : cidadeDAO.pesquisarPorNome(termo));
  }

  // preenche a tabela com a lista de cidades recebida como parametro
  private void carregarTabela(List<Cidade> lista) {
    // remove todas as linhas existentes antes de adicionar os novos dados
    modeloTabela.setRowCount(0);
    // percorre a lista e adiciona cada cidade como uma nova linha na tabela
    for (Cidade c : lista) {
      // cada linha contem o codigo, o nome e a uf da cidade
      modeloTabela.addRow(new Object[] {c.getId(), c.getNome(), c.getUf()});
    }
  }

  // preenche o formulario com os dados da linha que o usuario clicou na tabela
  private void selecionarDaTabela() {
    // getSelectedRow retorna -1 se nenhuma linha estiver selecionada
    int linha = tabela.getSelectedRow();
    // se nenhuma linha estiver selecionada, sai do metodo sem fazer nada
    if (linha < 0) return;

    // pega o valor da coluna 0 (codigo) da linha selecionada
    Long id = (Long) modeloTabela.getValueAt(linha, 0);

    // busca o objeto Cidade completo no banco pelo id para ter todos os dados atualizados
    cidadeAtual = cidadeDAO.buscarPorId(id);

    // so preenche o formulario se o objeto foi encontrado no banco
    if (cidadeAtual != null) {
      // exibe o codigo no campo somente leitura
      campoCodigo.setText(String.valueOf(cidadeAtual.getId()));
      // exibe o nome no campo de texto editavel
      campoNome.setText(cidadeAtual.getNome());
      // exibe a uf no campo de texto editavel
      campoUF.setText(cidadeAtual.getUf());
    }
  }
}
