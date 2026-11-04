package br.com.visao;

import br.com.controller.CadastroAcademicoController;
import br.com.controller.SessaoUsuario;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Complementa os cadastros existentes, sem substituir as telas originais.
 * Formulario academico Swing com abas de usuarios, retirantes e reivindicacoes.
 */
public class TelaCadastrosAcademicos extends JFrame {
    private final CadastroAcademicoController controller = new CadastroAcademicoController();
    private static final Color AZUL = new Color(10, 61, 163);

    public TelaCadastrosAcademicos() {
        super("Achados e Perdidos - Gerenciamento de cadastros");
        if (!SessaoUsuario.isAdministrador() && !SessaoUsuario.isFuncionario()) {
            throw new SecurityException("Acesso permitido apenas a funcionarios e administradores.");
        }
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1120, 680);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        if (SessaoUsuario.isAdministrador()) {
            tabs.addTab("Usuarios", new PainelCrud(Tipo.USUARIO));
        }
        tabs.addTab("Retirantes", new PainelCrud(Tipo.RETIRANTE));
        tabs.addTab("Reivindicacoes", new PainelCrud(Tipo.REIVINDICACAO));

        JLabel titulo = new JLabel("  Sistema de Achados e Perdidos - Cadastros");
        titulo.setOpaque(true);
        titulo.setBackground(AZUL);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 19));
        titulo.setPreferredSize(new Dimension(1100, 58));
        add(titulo, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }

    private enum Tipo { USUARIO, RETIRANTE, REIVINDICACAO }

    private class PainelCrud extends JPanel {
        private final Tipo tipo;
        private final JTable tabela = new JTable();
        private final DefaultTableModel modelo;
        private final JTextField[] campos;
        private final String[] rotulos;
        private final JComboBox<String> status = new JComboBox<>(
                new String[]{"Pendente", "Em analise", "Aprovada", "Recusada"});
        private int idSelecionado = -1;

        PainelCrud(Tipo tipo) {
            super(new BorderLayout(10, 10));
            this.tipo = tipo;
            setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
            if (tipo == Tipo.USUARIO) {
                rotulos = new String[]{"Nome", "CPF", "Email institucional", "Senha (somente novo cadastro)"};
                modelo = novoModelo("ID", "Nome", "CPF", "Email institucional");
            } else if (tipo == Tipo.RETIRANTE) {
                rotulos = new String[]{"Nome completo", "Documento", "Telefone"};
                modelo = novoModelo("ID", "Nome", "Documento", "Telefone");
            } else {
                rotulos = new String[]{"ID retirante", "ID objeto", "Observacao"};
                modelo = novoModelo("ID", "ID retirante", "Nome retirante", "ID objeto",
                        "Objeto", "Status", "Observacao", "Data");
            }
            campos = new JTextField[rotulos.length];
            tabela.setModel(modelo);
            tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tabela.setAutoCreateRowSorter(true);
            tabela.setRowHeight(25);
            tabela.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) preencher();
            });
            add(new JScrollPane(tabela), BorderLayout.CENTER);

            JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
            for (int i = 0; i < campos.length; i++) {
                formulario.add(new JLabel(rotulos[i]));
                campos[i] = new JTextField(26);
                formulario.add(campos[i]);
            }
            if (tipo == Tipo.REIVINDICACAO) {
                formulario.add(new JLabel("Status (edicao)"));
                formulario.add(status);
            }

            JPanel acoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
            botao(acoes, "Cadastrar", () -> executar("cadastrar"));
            botao(acoes, "Consultar / Atualizar tabela", this::carregar);
            botao(acoes, "Editar selecionado", () -> executar("editar"));
            botao(acoes, "Excluir selecionado", () -> executar("excluir"));
            botao(acoes, "Limpar campos", this::limpar);

            JPanel inferior = new JPanel(new BorderLayout(8, 8));
            inferior.add(formulario, BorderLayout.CENTER);
            inferior.add(acoes, BorderLayout.SOUTH);
            add(inferior, BorderLayout.SOUTH);
            carregar();
        }

        private DefaultTableModel novoModelo(String... colunas) {
            return new DefaultTableModel(colunas, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
        }

        private void botao(JPanel painel, String texto, Runnable acao) {
            JButton botao = new JButton(texto);
            botao.setBackground(AZUL);
            botao.setForeground(Color.WHITE);
            botao.setFocusPainted(false);
            botao.addActionListener(e -> acao.run());
            painel.add(botao);
        }

        private String campo(int indice) {
            return campos[indice].getText().trim();
        }

        private int numero(int indice) {
            try { return Integer.parseInt(campo(indice)); }
            catch (NumberFormatException e) {
                throw new IllegalArgumentException(rotulos[indice] + ": informe um numero inteiro.");
            }
        }

        private void carregar() {
            try {
                List<Object[]> dados = switch (tipo) {
                    case USUARIO -> controller.listarUsuarios();
                    case RETIRANTE -> controller.listarRetirantes();
                    case REIVINDICACAO -> controller.listarReivindicacoes();
                };
                modelo.setRowCount(0);
                for (Object[] linha : dados) modelo.addRow(linha);
                idSelecionado = -1;
            } catch (SQLException ex) { erro(ex); }
        }

        private void preencher() {
            int selecionada = tabela.getSelectedRow();
            if (selecionada < 0) return;
            int linha = tabela.convertRowIndexToModel(selecionada);
            idSelecionado = ((Number) modelo.getValueAt(linha, 0)).intValue();
            if (tipo == Tipo.USUARIO) {
                copiar(linha, 1, 0);
                copiar(linha, 2, 1);
                copiar(linha, 3, 2);
                campos[3].setText("");
            } else if (tipo == Tipo.RETIRANTE) {
                copiar(linha, 1, 0);
                copiar(linha, 2, 1);
                copiar(linha, 3, 2);
            } else {
                copiar(linha, 1, 0);
                copiar(linha, 3, 1);
                copiar(linha, 6, 2);
                status.setSelectedItem(modelo.getValueAt(linha, 5));
            }
        }

        private void copiar(int linha, int coluna, int indice) {
            Object v = modelo.getValueAt(linha, coluna);
            campos[indice].setText(v == null ? "" : v.toString());
        }

        private void limpar() {
            tabela.clearSelection();
            idSelecionado = -1;
            for (JTextField campo : campos) campo.setText("");
            status.setSelectedIndex(0);
        }

        private void executar(String operacao) {
            try {
                if (!operacao.equals("cadastrar") && idSelecionado < 1)
                    throw new IllegalArgumentException("Selecione um registro na tabela.");
                if (operacao.equals("excluir")) {
                    int resposta = JOptionPane.showConfirmDialog(this,
                            "Excluir o registro ID " + idSelecionado
                            + "? Registros relacionados impedem a exclusao.",
                            "Confirmar exclusao", JOptionPane.YES_NO_OPTION);
                    if (resposta != JOptionPane.YES_OPTION) return;
                }
                boolean sucesso;
                switch (tipo) {
                    case USUARIO -> sucesso = switch (operacao) {
                        case "cadastrar" -> controller.criarUsuario(campo(0), campo(1), campo(2), campo(3));
                        case "editar" -> controller.alterarUsuario(idSelecionado, campo(0), campo(1), campo(2));
                        default -> controller.excluirUsuario(idSelecionado);
                    };
                    case RETIRANTE -> sucesso = switch (operacao) {
                        case "cadastrar" -> controller.criarRetirante(campo(0), campo(1), campo(2));
                        case "editar" -> controller.alterarRetirante(idSelecionado, campo(0), campo(1), campo(2));
                        default -> controller.excluirRetirante(idSelecionado);
                    };
                    default -> sucesso = switch (operacao) {
                        case "cadastrar" -> controller.criarReivindicacao(numero(0), numero(1), campo(2));
                        case "editar" -> controller.alterarReivindicacao(
                                idSelecionado, status.getSelectedItem().toString(), campo(2));
                        default -> controller.excluirReivindicacao(idSelecionado);
                    };
                }
                JOptionPane.showMessageDialog(this, sucesso
                        ? "Operacao concluida com sucesso."
                        : "Nenhum registro alterado. Verifique se existe devolucao vinculada.");
                carregar();
                limpar();
            } catch (SQLException ex) {
                erro(ex);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Validacao", JOptionPane.WARNING_MESSAGE);
            }
        }

        private void erro(SQLException ex) {
            String aviso = ex.getSQLState() != null && ex.getSQLState().startsWith("23")
                    ? "Nao foi possivel concluir: registro duplicado ou vinculado a outro cadastro."
                    : "Erro de banco de dados: " + ex.getMessage();
            JOptionPane.showMessageDialog(this, aviso, "Banco de dados",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
