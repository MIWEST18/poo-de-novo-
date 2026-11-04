# Sistema de Achados e Perdidos — trabalho de POO

Aplicação desktop em **Java 21**, **Swing/JFrame**, **MySQL**, **JDBC** e organização **MVC**. Esta é a cópia de trabalho da equipe na branch `guimaraes`, importada de `ludy-d3v/sistema-achados-perdidos` (branch `guimaraes`), com correções incrementais.

## Como abrir no computador (NetBeans)

1. Instale **JDK 21**, **Apache NetBeans** (com suporte a Java/Ant) e **MySQL Server**.
2. Baixe o ZIP da branch `guimaraes` e extraia todos os arquivos.
3. No MySQL Workbench execute `database/banco.sql`, depois `database/inserts.sql` **somente em banco de testes**. O segundo script apaga os registros de demonstração anteriores.
4. Baixe **MySQL Connector/J 8.0.33** da fonte oficial do MySQL e coloque o arquivo **`mysql-connector-j-8.0.33.jar`** dentro da pasta `lib/` (crie a pasta se necessário). Não suba o driver binário para o GitHub.
5. No NetBeans, **Arquivo > Abrir Projeto**, selecione a pasta extraída; abra as propriedades e confira a biblioteca `Absolute Layout` (presente no NetBeans) e o Connector/J em `Libraries`.
6. Configure usuário e senha do MySQL como variáveis de ambiente `DB_USER`, `DB_PASSWORD` e opcionalmente `DB_URL`. A URL padrão é `jdbc:mysql://localhost:3306/achados_perdidos`; o usuário padrão é `root` e a senha padrão é vazia. Use credenciais locais de teste e não coloque senhas no código.
7. Faça **Clean and Build**; execute a classe principal `br.com.visao.LoginView`.

> Se usar propriedades da JVM em vez de variáveis de ambiente, configure em **Project Properties > Run > VM Options**: `-Ddb.user=USUARIO -Ddb.password=SENHA`. Não compartilhe arquivos locais que contenham senhas.

## Demonstração

Os usuários de demonstração estão no script `database/inserts.sql`. Para testes locais, o arquivo contém contas com senha `123`, **não adequada para produção**. Consulte o próprio script para os logins.

## Checklist para avaliação

Conferir quatro CRUDs simples (Create/Read/Update/Delete), um CRUD principal de reivindicações, consulta com JOIN que mostre dados relacionados, relatório da rotina principal, navegação Swing, MVC e integridade referencial. O roteiro detalhado está em `docs/ROTEIRO_TESTES.md`.

**Importante:** os ajustes estão em andamento. A existência de código no GitHub **não significa** que todos os requisitos foram testados e aprovados no NetBeans/MySQL.
