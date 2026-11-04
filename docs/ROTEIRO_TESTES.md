# Roteiro de testes para a apresentação — POO

Base: atividade prática do professor: Java, POO, Swing/JFrame, MVC, JDBC, MySQL, **4 CRUDs simples completos**, **1 CRUD principal completo**, **JOIN** e **relatório principal**.

## Antes da apresentação

- [ ] JDK 21, NetBeans e MySQL instalados.
- [ ] Biblioteca Absolute Layout funcionando no NetBeans.
- [ ] JAR MySQL Connector/J 8.0.33 em `lib/`.
- [ ] Banco `database/banco.sql` criado e `database/inserts.sql` executado **em ambiente de teste**.
- [ ] Login como aluno, funcionário e administrador.
- [ ] Cadastro de objeto e consulta mostrando objeto inserido.
- [ ] Alteração de objeto refletida em nova consulta.
- [ ] Exclusão de objeto sem vínculos (ou mensagem clara quando houver chave estrangeira).
- [ ] Repetir operações C/R/U/D para **funcionário, usuário e retirante**.
- [ ] Reivindicação: criar e consultar; editar situação/observação; excluir quando permitido, observando vínculos com devoluções.
- [ ] Uma consulta com JOIN mostrando nome do retirante e descrição do objeto, não só IDs.
- [ ] Relatório de reivindicações por situação e período.
- [ ] Devolução registra tabela Devolucao e muda objeto para Devolvido; em erro, nenhuma das duas ações fica parcialmente gravada.
- [ ] Teste em segundo computador / ambiente com senha MySQL diferente.

## Registro de resultados

| Caso | Resultado | Observações |
| --- | --- | --- |
| Login e navegação | Não testado | |
| CRUD Objeto | Não testado | |
| CRUD Funcionario | Não testado | |
| CRUD Usuario | Não testado | |
| CRUD Retirante | Não testado | |
| CRUD Reivindicacao | Não testado | |
| JOIN | Não testado | |
| Relatório | Não testado | |
| Devolução atômica | Não testado | |
| Outro computador | Não testado | |

A equipe deve preencher a tabela somente depois de executar os testes. Não apagar banco real para rodar inserts de exemplo.
