package br.com.entidade;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Acesso ao banco MySQL. As credenciais sao configuradas fora do codigo-fonte:
 * DB_URL, DB_USER e DB_PASSWORD (ou -Ddb.url, -Ddb.user, -Ddb.password).
 */
public class DAO {
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    public void abrirBanco() throws SQLException {
        String url = configuracao("db.url", "DB_URL", "jdbc:mysql://localhost:3306/achados_perdidos");
        String usuario = configuracao("db.user", "DB_USER", "root");
        String senha = configuracao("db.password", "DB_PASSWORD", "");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL nao encontrado. Adicione o Connector/J ao projeto.", e);
        }
        con = DriverManager.getConnection(url, usuario, senha);
    }

    private static String configuracao(String propriedade, String ambiente, String padrao) {
        String valor = System.getProperty(propriedade);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(ambiente);
        }
        return valor == null ? padrao : valor;
    }

    public void fecharBanco() throws SQLException {
        SQLException erro = null;
        try {
            if (rs != null && !rs.isClosed()) rs.close();
        } catch (SQLException e) { erro = e; }
        try {
            if (pst != null && !pst.isClosed()) pst.close();
        } catch (SQLException e) {
            if (erro == null) erro = e;
            else erro.addSuppressed(e);
        }
        try {
            if (con != null && !con.isClosed()) con.close();
        } catch (SQLException e) {
            if (erro == null) erro = e;
            else erro.addSuppressed(e);
        }
        rs = null;
        pst = null;
        con = null;
        if (erro != null) throw erro;
    }
}
