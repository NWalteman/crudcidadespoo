package view;

import dao.CidadeDAO;
import dao.ClienteDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Cidade;
import model.Cliente;
import util.Validador;

// tela de cadastro de clientes com operacoes de incluir, alterar, excluir e pesquisar
public class TelaCliente extends JFrame {

  // dao responsavel pelas operacoes de cliente no banco de dados
  private final ClienteDAO clienteDAO = new ClienteDAO();

  // dao usado para carregar as cidades no combobox
  private final CidadeDAO cidadeDAO = new CidadeDAO();

  // campos de texto do formulario de dados do cliente
  private JTextField campoCodigo, campoNome, campoCpf, campoTelefone, campoEmail, campoPesquisa;

  // combobox que lista as cidades disponiveis para o usuario selecionar
  private JComboBox<Cidade> comboCidade;

  // tabela que exibe a lista de clientes cadastrados
  private JTable tabela;

  // modelo que gerencia os dados exibidos na tabela (linhas e colunas)
  private DefaultTableModel modeloTabela;

  // guarda o cliente selecionado na tabela, null quando for um novo cadastro
  private Cliente clienteAtual = null;

  public TelaCliente() {
    // titulo que aparece na barra da janela
    setTitle("Cadastro de Clientes");
    // tamanho da janela em pixels, maior que a de cidades por ter mais campos
    setSize(800, 560);
    // centraliza a janela no meio da tela
    setLocationRelativeTo(null);
    // ao fechar esta janela, apenas ela e fechada, a aplicacao continua rodando
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    // chama o metodo que cria e posiciona todos os componentes visuais
    iniciarComponentes();
    // popula o combobox de cidades com os registros do banco
    carregarCidades();
    // carrega todos os clientes do banco na tabela assim que a tela abre
    carregarTabela(clienteDAO.listarTodos());
  }

  // cria e organiza todos os componentes visuais da tela
  private void iniciarComponentes() {
    // BorderLayout divide a tela em cinco regioes: norte, sul, leste, oeste e centro
    setLayout(new BorderLayout(10, 10));

    // painel do formulario com titulo visivel na borda
    JPanel painelFormulario = new JPanel(new GridBagLayout());
    painelFormulario.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));

    // GridBagConstraints controla o posicionamento de cada componente no GridBagLayout
    GridBagConstraints restricoes = new GridBagConstraints();
    // espaco de 5 pixels em volta de cada componente
    restricoes.insets = new Insets(5, 5, 5, 5);
    // faz os componentes preencherem toda a largura da celula
    restricoes.fill = GridBagConstraints.HORIZONTAL;

    // campo de codigo somente leitura, preenchido automaticamente pelo banco
    campoCodigo = new JTextField(10);
    campoCodigo.setEditable(false);

    // campos editaveis com os dados do cliente
    campoNome = new JTextField(30);
    campoCpf = new JTextField(15);
    campoTelefone = new JTextField(15);
    campoEmail = new JTextField(25);

    // combobox que sera preenchido com as cidades cadastradas no banco
    comboCidade = new JComboBox<>();

    // posiciona o label "Codigo" na coluna 0, linha 0
    restricoes.gridx = 0;
    restricoes.gridy = 0;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("Código:"), restricoes);
    // posiciona o campo de codigo na coluna 1, linha 0
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoCodigo, restricoes);

    // posiciona o label "Nome" na coluna 0, linha 1
    restricoes.gridx = 0;
    restricoes.gridy = 1;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("Nome:"), restricoes);
    // posiciona o campo de nome na coluna 1, linha 1
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoNome, restricoes);

    // posiciona o label "CPF" na coluna 0, linha 2
    restricoes.gridx = 0;
    restricoes.gridy = 2;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("CPF:"), restricoes);
    // posiciona o campo de CPF na coluna 1, linha 2
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoCpf, restricoes);

    // posiciona o label "Telefone" na coluna 0, linha 3
    restricoes.gridx = 0;
    restricoes.gridy = 3;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("Telefone:"), restricoes);
    // posiciona o campo de telefone na coluna 1, linha 3
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoTelefone, restricoes);

    // posiciona o label "E-mail" na coluna 0, linha 4
    restricoes.gridx = 0;
    restricoes.gridy = 4;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("E-mail:"), restricoes);
    // posiciona o campo de email na coluna 1, linha 4
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(campoEmail, restricoes);

    // posiciona o label "Cidade" na coluna 0, linha 5
    restricoes.gridx = 0;
    restricoes.gridy = 5;
    restricoes.weightx = 0;
    painelFormulario.add(new JLabel("Cidade:"), restricoes);
    // posiciona o combobox de cidades na coluna 1, linha 5
    restricoes.gridx = 1;
    restricoes.weightx = 1;
    painelFormulario.add(comboCidade, restricoes);

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

    // ao clicar em pesquisar, filtra os clientes pelo nome digitado
    botaoPesquisar.addActionListener(e -> pesquisar());
    // ao clicar em listar todos, remove o filtro e exibe todos os clientes
    botaoListarTodos.addActionListener(e -> carregarTabela(clienteDAO.listarTodos()));
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
        new DefaultTableModel(
            new String[] {"Código", "Nome", "CPF", "Telefone", "E-mail", "Cidade"}, 0) {
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

  // limpa o combobox e recarrega as cidades do banco
  // chamado ao abrir a tela e ao clicar em Novo para refletir cidades adicionadas recentemente
  private void carregarCidades() {
    // remove todos os itens existentes no combobox antes de recarregar
    comboCidade.removeAllItems();
    // busca todas as cidades cadastradas no banco
    List<Cidade> cidades = cidadeDAO.listarTodos();
    // adiciona cada cidade como um item do combobox
    // o texto exibido em cada item vem do metodo toString() da classe Cidade
    for (Cidade c : cidades) {
      comboCidade.addItem(c);
    }
  }

  // limpa o formulario e prepara a tela para um novo cadastro
  private void novo() {
    // null indica que nao ha cliente selecionado, entao o salvar vai inserir um novo registro
    clienteAtual = null;
    // limpa todos os campos do formulario
    campoCodigo.setText("");
    campoNome.setText("");
    campoCpf.setText("");
    campoTelefone.setText("");
    campoEmail.setText("");
    // recarrega as cidades para refletir novos cadastros feitos na tela de cidades
    carregarCidades();
    // seleciona a primeira cidade da lista se houver alguma cadastrada
    if (comboCidade.getItemCount() > 0) comboCidade.setSelectedIndex(0);
    // move o foco para o campo nome para facilitar a digitacao
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

    if (nome.isEmpty()) {
      JOptionPane.showMessageDialog(
          this, "Preencha o nome do cliente.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (cidade == null) {
      JOptionPane.showMessageDialog(
          this,
          "Selecione uma cidade. Cadastre cidades primeiro.",
          "Atenção",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (!Validador.cpfValido(cpf)) {
      JOptionPane.showMessageDialog(
          this, "CPF inválido.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (!Validador.telefoneValido(telefone)) {
      JOptionPane.showMessageDialog(
          this,
          "Telefone inválido. Informe 10 dígitos (fixo) ou 11 dígitos (celular).",
          "Atenção",
          JOptionPane.WARNING_MESSAGE);
      return;
    }
    if (!Validador.emailValido(email)) {
      JOptionPane.showMessageDialog(
          this, "E-mail inválido.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      // se clienteAtual for null, e um cadastro novo
      if (clienteAtual == null) {
        // cria um novo objeto Cliente com os dados do formulario e salva no banco
        clienteDAO.salvar(new Cliente(nome, cpf, telefone, email, cidade));
        JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
      } else {
        // se clienteAtual tiver valor, e uma edicao do registro ja existente
        // atualiza cada campo do objeto com os novos valores digitados
        clienteAtual.setNome(nome);
        clienteAtual.setCpf(cpf);
        clienteAtual.setTelefone(telefone);
        clienteAtual.setEmail(email);
        // atualiza a cidade vinculada ao cliente com a selecionada no combobox
        clienteAtual.setCidade(cidade);
        // envia as alteracoes para o banco
        clienteDAO.atualizar(clienteAtual);
        JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
      }
      // limpa o formulario apos salvar
      novo();
      // recarrega a tabela para mostrar o registro salvo ou atualizado
      carregarTabela(clienteDAO.listarTodos());
    } catch (Exception e) {
      // exibe a mensagem de erro caso algo falhe no banco
      JOptionPane.showMessageDialog(
          this, "Erro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
    }
  }

  // remove o cliente selecionado do banco de dados
  private void excluir() {
    // nao permite excluir se nenhum cliente estiver selecionado no formulario
    if (clienteAtual == null) {
      JOptionPane.showMessageDialog(
          this, "Selecione um cliente para excluir.", "Atenção", JOptionPane.WARNING_MESSAGE);
      return;
    }

    // pede confirmacao do usuario antes de excluir permanentemente
    int ok =
        JOptionPane.showConfirmDialog(
            this,
            "Deseja excluir o cliente '" + clienteAtual.getNome() + "'?",
            "Confirmar exclusão",
            JOptionPane.YES_NO_OPTION);

    // so executa a exclusao se o usuario confirmar clicando em "Sim"
    if (ok == JOptionPane.YES_OPTION) {
      try {
        // remove o registro do banco de dados
        clienteDAO.excluir(clienteAtual);
        JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
        // limpa o formulario apos excluir
        novo();
        // recarrega a tabela removendo o registro excluido da lista
        carregarTabela(clienteDAO.listarTodos());
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
            this, "Erro ao excluir: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  // filtra a tabela exibindo apenas os clientes cujo nome contenha o texto pesquisado
  private void pesquisar() {
    // pega o texto digitado no campo de pesquisa, sem espacos nas pontas
    String termo = campoPesquisa.getText().trim();
    // se o campo estiver vazio, lista todos os clientes sem filtro
    carregarTabela(termo.isEmpty() ? clienteDAO.listarTodos() : clienteDAO.pesquisarPorNome(termo));
  }

  // preenche a tabela com a lista de clientes recebida como parametro
  private void carregarTabela(List<Cliente> lista) {
    // remove todas as linhas existentes antes de adicionar os novos dados
    modeloTabela.setRowCount(0);
    // percorre a lista e adiciona cada cliente como uma nova linha na tabela
    for (Cliente c : lista) {
      // cada linha contem codigo, nome, cpf, telefone, email e o nome da cidade
      modeloTabela.addRow(
          new Object[] {
            c.getId(),
            c.getNome(),
            // campos opcionais podem ser null, exibe string vazia nesse caso
            c.getCpf(),
            c.getTelefone(),
            c.getEmail(),
            // getCidade pode retornar null em casos extremos, exibe vazio para nao quebrar
            c.getCidade() != null ? c.getCidade().toString() : ""
          });
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

    // busca o objeto Cliente completo no banco pelo id para ter todos os dados atualizados
    clienteAtual = clienteDAO.buscarPorId(id);

    // so preenche o formulario se o objeto foi encontrado no banco
    if (clienteAtual != null) {
      // exibe o codigo no campo somente leitura
      campoCodigo.setText(String.valueOf(clienteAtual.getId()));
      // exibe o nome no campo de texto editavel
      campoNome.setText(clienteAtual.getNome());
      // campos opcionais podem ser null, por isso exibe string vazia se for o caso
      campoCpf.setText(clienteAtual.getCpf() != null ? clienteAtual.getCpf() : "");
      campoTelefone.setText(clienteAtual.getTelefone() != null ? clienteAtual.getTelefone() : "");
      campoEmail.setText(clienteAtual.getEmail() != null ? clienteAtual.getEmail() : "");

      // recarrega as cidades no combobox para garantir que estejam atualizadas
      carregarCidades();
      // percorre os itens do combobox para encontrar e selecionar a cidade do cliente
      if (clienteAtual.getCidade() != null) {
        for (int i = 0; i < comboCidade.getItemCount(); i++) {
          // compara pelo id para evitar problemas com equals mal implementado
          if (comboCidade.getItemAt(i).getId().equals(clienteAtual.getCidade().getId())) {
            // seleciona o item correspondente no combobox
            comboCidade.setSelectedIndex(i);
            // para o loop assim que encontrar a cidade correta
            break;
          }
        }
      }
    }
  }
}
