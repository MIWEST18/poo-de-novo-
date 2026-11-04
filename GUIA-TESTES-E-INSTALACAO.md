# Como executar o Sistema de Achados e Perdidos (POO / MVC / MySQL)

Este projeto conserva a estrutura Java 21, Swing/JFrame, NetBeans Ant, JDBC e MySQL criada pela equipe.

## Preparar ambiente

1. Instale **JDK 21**, **Apache NetBeans** e um servidor **MySQL**.
2. Execute `database/banco.sql` e depois `database/inserts.sql` no MySQL. Use banco de testes: os scripts podem criar estruturas e registros.
3. Baixe o **MySQL Connector/J 8.0.33** (driver JDBC) do site oficial da Oracle/MySQL e coloque o arquivo com nome `mysql-connector-j-8.0.33.jar` na pasta `lib/` do projeto. O JAR não é incluído no repositório; seu download é separado.
4. Abra o projeto pelo NetBeans (Arquivo > Abrir Projeto). Se necessário, ajuste a biblioteca `Absolute Layout` usando a configuração de bibliotecas do NetBeans.
5. Configure as credenciais do banco **no seu computador**, sem publicá-las no GitHub:

   - `DB_URL`: padrão `jdbc:mysql://localhost:3306/achados_perdidos`
   - `DB_USER`: padrão `root`
   - `DB_PASSWORD`: sua senha MySQL (o padrão no código é vazio, não uma senha fixa)

   Alternativamente, em **Propriedades do projeto > Executar > Argumentos da VM**:
   `-Ddb.user=SEU_USUARIO -Ddb.password=SUA_SENHA`

6. Execute a classe principal `br.com.visao.LoginView`.

**Não suba senhas reais nem o arquivo de configuração do seu computador ao GitHub.**

## Checagem de aceitação conforme o enunciado

- [ ] Compila sem erros em um computador com Java 21
- [ ] Todas as telas abrem e navegam corretamente
- [ ] Login e cadastro de usuários
- [ ] CRUD completo 1: Objeto (criar, consultar, atualizar, excluir)
- [ ] CRUD completo 2: Funcionário (criar, consultar, atualizar, excluir)
- [ ] CRUD completo 3: Usuário (criar, consultar, atualizar, excluir)
- [ ] CRUD completo 4: Retirante (criar, consultar, atualizar, excluir)
- [ ] CRUD principal: Reivindicação (criar, consultar, atualizar, excluir, com relacionamentos)
- [ ] JOIN exibindo informações úteis das tabelas relacionadas
- [ ] Relatório da rotina principal
- [ ] Dados demonstrativos no banco e script SQL
- [ ] Repetir os testes em um segundo computador

Os itens acima **não estão marcados como concluídos**. A presença de código não comprova execução.

## Mudanças iniciais da branch `guimaraes`

- Credenciais MySQL removidas do código Java e passadas para propriedades Java/variáveis de ambiente.
- Fechamento de `ResultSet`, `PreparedStatement` e `Connection` no DAO.
- Registro de devolução e atualização de status do objeto em uma mesma transação (commit/rollback).
- Referência ao Connector/J alterada para `lib/mysql-connector-j-8.0.33.jar`.

## Limitações

A compilação e os testes com MySQL/NetBeans **ainda não foram executados** nesta revisão. Não trate esta branch como entrega final até executar os testes de aceitação acima. Esta branch foi copiada de `ludy-d3v/sistema-achados-perdidos` no ponto de partida de `guimaraes`, não necessariamente do último commit de `ludmilla`.

As imagens de `src/ICON/` devem ser comparadas com o repositório original: a importação pode não estar completa.
