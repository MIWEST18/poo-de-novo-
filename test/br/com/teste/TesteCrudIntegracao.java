package br.com.teste;

import br.com.controller.CadastroAcademicoController;
import br.com.entidade.FuncionarioEntity;
import br.com.entidade.ObjetoEntity;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Teste de integracao CRUD - executar apenas sobre um banco descartavel.
 * Confere Create, Read, Update, Delete e JOIN.
 */
public final class TesteCrudIntegracao {
    private static final CadastroAcademicoController c = new CadastroAcademicoController();

    private static void exigir(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
        System.out.println("OK: " + mensagem);
    }

    private static int idUsuario(String email) throws SQLException {
        for (Object[] linha : c.listarUsuarios()) {
            if (email.equals(linha[3])) return ((Number)linha[0]).intValue();
        }
        throw new AssertionError("Usuario nao encontrado: " + email);
    }

    private static int idRetirante(String documento) throws SQLException {
        for (Object[] linha : c.listarRetirantes()) {
            if (documento.equals(linha[2])) return ((Number)linha[0]).intValue();
        }
        throw new AssertionError("Retirante nao encontrado: " + documento);
    }

    private static int idObjeto(String descricao) throws SQLException {
        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/achados_perdidos");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "");
        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement p = con.prepareStatement(
                     "SELECT ID_Objeto FROM Objeto WHERE Descricao_Generica = ?")) {
            p.setString(1, descricao);
            try (ResultSet r = p.executeQuery()) {
                if (!r.next()) throw new AssertionError("Objeto nao cadastrado");
                return r.getInt(1);
            }
        }
    }

    private static int idReivindicacao(int retirante, int objeto) throws SQLException {
        for (Object[] linha : c.listarReivindicacoes()) {
            if (((Number)linha[1]).intValue() == retirante
                    && ((Number)linha[3]).intValue() == objeto) {
                exigir(linha[2] != null && linha[4] != null, "JOIN retorna nomes de retirante e objeto");
                return ((Number)linha[0]).intValue();
            }
        }
        throw new AssertionError("Reivindicacao nao cadastrada");
    }

    public static void main(String[] args) throws Exception {
        // Rodar depois dos scripts banco.sql e inserts.sql em banco descartavel.
        String email = "crud.test@a.ucb.br";
        exigir(c.criarUsuario("Teste CRUD", "12345678901", email, "senha-teste"),
                "C: usuario");
        int usuario = idUsuario(email);
        exigir(usuario > 0, "R: usuario");
        exigir(c.alterarUsuario(usuario, "Teste CRUD Editado", "12345678901", email),
                "U: usuario");

        FuncionarioEntity f = new FuncionarioEntity();
        exigir(f.promover(usuario, "Assistente", "Teste"), "C: funcionario");
        exigir(f.listarUsuarios("crud.test").stream().anyMatch(u -> u.isFuncionario()),
                "R: funcionario");
        exigir(f.atualizarCargoSetor(usuario, "Coordenador", "Teste"), "U: funcionario");
        exigir(f.removerFuncionario(usuario), "D: funcionario");

        String doc = "TESTE-AUTOMATIZADO";
        exigir(c.criarRetirante("Retirante Teste", doc, "61999990000"), "C: retirante");
        int retirante = idRetirante(doc);
        exigir(retirante > 0, "R: retirante");
        exigir(c.alterarRetirante(retirante, "Retirante Editado", doc, "61999990001"),
                "U: retirante");

        String descricao = "Objeto de teste automatico CRUD";
        ObjetoEntity o = new ObjetoEntity();
        exigir(o.cadastrarObjeto(descricao, "", "Eletronicos", LocalDate.now(),
                LocalTime.of(10, 0), "Taguatinga-DF", "G", "Sala de testes",
                LocalDate.now().plusDays(90), usuario), "C: objeto");
        int objeto = idObjeto(descricao);
        exigir(objeto > 0, "R: objeto");
        exigir(o.atualizarObjeto(objeto, "Objeto teste editado", "Eletronicos", "Taguatinga-DF"),
                "U: objeto");

        exigir(c.criarReivindicacao(retirante, objeto, "Reivindicacao de teste"),
                "C: reivindicacao");
        int reivindicacao = idReivindicacao(retirante, objeto);
        exigir(reivindicacao > 0, "R: reivindicacao");
        exigir(c.alterarReivindicacao(reivindicacao, "Recusada", "Conferido"),
                "U: reivindicacao");
        exigir(c.excluirReivindicacao(reivindicacao), "D: reivindicacao");

        exigir(o.excluirObjeto(objeto), "D: objeto");
        exigir(c.excluirRetirante(retirante), "D: retirante");
        exigir(c.excluirUsuario(usuario), "D: usuario");

        System.out.println("TODOS OS 5 CRUDs BASICOS/CENTRAL e JOIN PASSARAM.");
    }
}
