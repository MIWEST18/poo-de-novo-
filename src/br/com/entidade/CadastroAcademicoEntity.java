package br.com.entidade;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Operacoes complementares de CRUD para a atividade de POO.
 * Usa JDBC com PreparedStatement e a mesma base de dados do projeto original.
 * A interface nao executa SQL; apenas chama estes metodos por um Controller.
 */
public class CadastroAcademicoEntity {

    public List<Object[]> listarUsuarios() throws SQLException {
        List<Object[]> linhas = new ArrayList<>();
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "SELECT ID_Usuario, Nome, CPF, Email_Institucional FROM Usuario ORDER BY Nome");
                 ResultSet r = p.executeQuery()) {
                while (r.next()) linhas.add(new Object[]{
                    r.getInt(1), r.getString(2), r.getString(3), r.getString(4)
                });
            }
            return linhas;
        } finally { dao.fecharBanco(); }
    }

    public boolean cadastrarUsuario(String nome, String cpf, String email, String senha) throws SQLException {
        if (vazio(nome) || vazio(cpf) || vazio(email) || vazio(senha))
            throw new IllegalArgumentException("Preencha nome, CPF, e-mail e senha.");
        if (!emailValido(email))
            throw new IllegalArgumentException("Use e-mail institucional @a.ucb.br ou @p.ucb.br.");
        if (!cpf.matches("\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}"))
            throw new IllegalArgumentException("Informe um CPF com 11 algarismos.");
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "INSERT INTO Usuario (Nome, CPF, Email_Institucional, Senha) VALUES (?, ?, ?, ?)")) {
                p.setString(1, nome.trim());
                p.setString(2, cpf.trim());
                p.setString(3, email.trim().toLowerCase());
                p.setString(4, senha);
                return p.executeUpdate() == 1;
            }
        } finally { dao.fecharBanco(); }
    }

    public boolean atualizarUsuario(int id, String nome, String cpf, String email) throws SQLException {
        if (id <= 0 || vazio(nome) || vazio(cpf) || !emailValido(email))
            throw new IllegalArgumentException("Informe ID valido, nome, CPF e e-mail institucional.");
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "UPDATE Usuario SET Nome=?, CPF=?, Email_Institucional=? WHERE ID_Usuario=?")) {
                p.setString(1, nome.trim());
                p.setString(2, cpf.trim());
                p.setString(3, email.trim().toLowerCase());
                p.setInt(4, id);
                return p.executeUpdate() == 1;
            }
        } finally { dao.fecharBanco(); }
    }

    public boolean excluirUsuario(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Selecione um usuario.");
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "DELETE FROM Usuario WHERE ID_Usuario=?")) {
                p.setInt(1, id);
                return p.executeUpdate() == 1;
            }
        } finally { dao.fecharBanco(); }
    }

    public List<Object[]> listarRetirantes() throws SQLException {
        List<Object[]> linhas = new ArrayList<>();
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "SELECT ID_Retirante, Nome_Completo, Documento_Identificacao, Telefone FROM Retirante ORDER BY Nome_Completo");
                 ResultSet r = p.executeQuery()) {
                while (r.next()) linhas.add(new Object[]{
                    r.getInt(1), r.getString(2), r.getString(3), r.getString(4)
                });
            }
            return linhas;
        } finally { dao.fecharBanco(); }
    }

    public boolean cadastrarRetirante(String nome, String documento, String telefone) throws SQLException {
        if (vazio(nome) || vazio(documento))
            throw new IllegalArgumentException("Nome e documento sao obrigatorios.");
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "INSERT INTO Retirante (Nome_Completo, Documento_Identificacao, Telefone) VALUES (?, ?, ?)")) {
                p.setString(1, nome.trim());
                p.setString(2, documento.trim());
                p.setString(3, telefone.trim());
                return p.executeUpdate() == 1;
            }
        } finally { dao.fecharBanco(); }
    }

    public boolean atualizarRetirante(int id, String nome, String documento, String telefone) throws SQLException {
        if (id <= 0 || vazio(nome) || vazio(documento))
            throw new IllegalArgumentException("Selecione um retirante e preencha nome e documento.");
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "UPDATE Retirante SET Nome_Completo=?, Documento_Identificacao=?, Telefone=? WHERE ID_Retirante=?")) {
                p.setString(1, nome.trim());
                p.setString(2, documento.trim());
                p.setString(3, telefone.trim());
                p.setInt(4, id);
                return p.executeUpdate() == 1;
            }
        } finally { dao.fecharBanco(); }
    }

    public boolean excluirRetirante(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Selecione um retirante.");
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "DELETE FROM Retirante WHERE ID_Retirante=?")) {
                p.setInt(1, id);
                return p.executeUpdate() == 1;
            }
        } finally { dao.fecharBanco(); }
    }

    public List<Object[]> listarReivindicacoes() throws SQLException {
        List<Object[]> linhas = new ArrayList<>();
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            String sql = "SELECT r.ID_Reivindicacao, r.ID_Retirante, t.Nome_Completo, "
                    + "r.ID_Objeto, o.Descricao_Generica, r.Status_Reivindicacao, "
                    + "r.Observacao, r.Data_Reivindicacao "
                    + "FROM Reivindicacao r "
                    + "INNER JOIN Retirante t ON t.ID_Retirante = r.ID_Retirante "
                    + "INNER JOIN Objeto o ON o.ID_Objeto = r.ID_Objeto "
                    + "ORDER BY r.ID_Reivindicacao DESC";
            try (PreparedStatement p = dao.con.prepareStatement(sql);
                 ResultSet r = p.executeQuery()) {
                while (r.next()) linhas.add(new Object[]{
                    r.getInt(1), r.getInt(2), r.getString(3), r.getInt(4),
                    r.getString(5), r.getString(6), r.getString(7), r.getTimestamp(8)
                });
            }
            return linhas;
        } finally { dao.fecharBanco(); }
    }

    public boolean cadastrarReivindicacao(int idRetirante, int idObjeto, String observacao)
            throws SQLException {
        if (idRetirante <= 0 || idObjeto <= 0)
            throw new IllegalArgumentException("Informe IDs validos do retirante e do objeto.");
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "INSERT INTO Reivindicacao (Data_Reivindicacao, Status_Reivindicacao, "
                    + "Observacao, ID_Retirante, ID_Objeto) VALUES (NOW(), 'Pendente', ?, ?, ?)")) {
                p.setString(1, observacao.trim());
                p.setInt(2, idRetirante);
                p.setInt(3, idObjeto);
                return p.executeUpdate() == 1;
            }
        } finally { dao.fecharBanco(); }
    }

    public boolean atualizarReivindicacao(int id, String status, String observacao)
            throws SQLException {
        if (id <= 0 || !(status.equals("Pendente") || status.equals("Em analise")
                || status.equals("Aprovada") || status.equals("Recusada")))
            throw new IllegalArgumentException("Selecione uma reivindicacao e um status valido.");
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "UPDATE Reivindicacao SET Status_Reivindicacao=?, Observacao=? "
                    + "WHERE ID_Reivindicacao=? AND ID_Reivindicacao NOT IN "
                    + "(SELECT ID_Reivindicacao FROM Devolucao)")) {
                p.setString(1, status);
                p.setString(2, observacao.trim());
                p.setInt(3, id);
                return p.executeUpdate() == 1;
            }
        } finally { dao.fecharBanco(); }
    }

    public boolean excluirReivindicacao(int id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("Selecione uma reivindicacao.");
        DAO dao = new DAO();
        try {
            dao.abrirBanco();
            try (PreparedStatement p = dao.con.prepareStatement(
                    "DELETE FROM Reivindicacao WHERE ID_Reivindicacao=? "
                    + "AND ID_Reivindicacao NOT IN (SELECT ID_Reivindicacao FROM Devolucao)")) {
                p.setInt(1, id);
                return p.executeUpdate() == 1;
            }
        } finally { dao.fecharBanco(); }
    }

    private static boolean vazio(String value) {
        return value == null || value.isBlank();
    }

    private static boolean emailValido(String email) {
        if (vazio(email)) return false;
        String normalizado = email.trim().toLowerCase();
        return normalizado.endsWith("@a.ucb.br") || normalizado.endsWith("@p.ucb.br");
    }
}
