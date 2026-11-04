package br.com.controller;

import br.com.entidade.CadastroAcademicoEntity;
import java.sql.SQLException;
import java.util.List;

/** Faz a mediacao entre as telas Swing e o acesso aos dados. */
public class CadastroAcademicoController {
    private final CadastroAcademicoEntity dados = new CadastroAcademicoEntity();

    public List<Object[]> listarUsuarios() throws SQLException { return dados.listarUsuarios(); }
    public boolean criarUsuario(String nome, String cpf, String email, String senha) throws SQLException {
        return dados.cadastrarUsuario(nome, cpf, email, senha);
    }
    public boolean alterarUsuario(int id, String nome, String cpf, String email) throws SQLException {
        return dados.atualizarUsuario(id, nome, cpf, email);
    }
    public boolean excluirUsuario(int id) throws SQLException { return dados.excluirUsuario(id); }

    public List<Object[]> listarRetirantes() throws SQLException { return dados.listarRetirantes(); }
    public boolean criarRetirante(String nome, String documento, String telefone) throws SQLException {
        return dados.cadastrarRetirante(nome, documento, telefone);
    }
    public boolean alterarRetirante(int id, String nome, String documento, String telefone) throws SQLException {
        return dados.atualizarRetirante(id, nome, documento, telefone);
    }
    public boolean excluirRetirante(int id) throws SQLException { return dados.excluirRetirante(id); }

    public List<Object[]> listarReivindicacoes() throws SQLException { return dados.listarReivindicacoes(); }
    public boolean criarReivindicacao(int idRetirante, int idObjeto, String observacao) throws SQLException {
        return dados.cadastrarReivindicacao(idRetirante, idObjeto, observacao);
    }
    public boolean alterarReivindicacao(int id, String status, String observacao) throws SQLException {
        return dados.atualizarReivindicacao(id, status, observacao);
    }
    public boolean excluirReivindicacao(int id) throws SQLException { return dados.excluirReivindicacao(id); }
}
