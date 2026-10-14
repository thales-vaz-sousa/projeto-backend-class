# GameStore

Sistema para loja de revenda de mídias físicas de jogos controlar o estoque, os clientes e as compras: o que tem à venda, quanto cada cliente tem de saldo e o que foi comprado.

Projeto Final da disciplina de Backend (Engenharia de Software, SATC). 

**Etapa 1: Arquitetura do backend.**

## Índice

- [Integrantes](#integrantes)
- [O problema](#o-problema)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Entidades](#entidades)
- [Rotas](#rotas)
- [Como executar](#como-executar)
- [Situação do projeto](#situação-do-projeto)
- [Documentação](#documentação)

## Integrantes

| Nome            | GitHub            |
| --------------- | ----------------- |
| Pedro Dal Ponte | @PedroDPavei      |
| José Dos Santos | @JooJ-E           |
| Thales Vaz      | @thales-vaz-sousa |

## O problema

**Tema:** Revenda de midia fisicas de jogos

*Descrição* : *Uma loja de revenda de jogos físicos (CDs, cartuchos, discos de videogames etc...) hoje controla seu estoque, seus clientes e suas vendas em planilhas,  anotações soltas e usa plataformas de marketplace para expor os produtos de forma descentralizada. Isso causa três problemas pertinentes: o dono não sabe ao certo quantas unidades de cada jogo ainda tem, pode induzir ao erro de vender um produto que já acabou, e não há um controle descentralizado da exposição do produto, assim como desconto e preço.*

*O sistema resolve isso centralizando o cadastro de mídias, clientes e carrinhos de compra. O dono cadastra as mídias com preço e quantidade em estoque e administra o saldo dos clientes. O cliente monta seu carrinho, consulta as mídias disponíveis e finaliza a compra, e o sistema desconta o saldo e baixa o estoque automaticamente, impedindo vendas inválidas.*

**Quem usa o sistema:**

| Quem           | O que faz no sistema                           |
| -------------- | ---------------------------------------------- |
| Dono E-Comerce | Cadastra midias fisicas, clientes e saldo      |
| Cliente        | Consulta os carrinho, midias e finaliza compra |

**Regras de negócio.** O grupo criou estas regras olhando para o problema citado.

| #    | Regra                                                          | Onde está no código           | Rota que usa    |
| ---- | -------------------------------------------------------------- | ----------------------------- | --------------- |
| `R1` | O cliente pode comprar somente produtos que tenham estoque > 0 | `CompraService`               | `POST /pagar`   |
| `R2` | O dono não pode criar cadastro de midias com campos em branco  | `ProdutoDto e ProdutoService` | `POST /produto` |
| `R3` | O cliente compra somente com saldo >= preço total              | `CompraService`               | `POST /compra`  |



**Elaborar integrações:** blablabla 

## Tecnologias

| O quê               | Qual                                        |
| ------------------- | ------------------------------------------- |
| Linguagem           | Java 25                                     |
| Framework           | Spring Boot 3.5 (Web, Validation, Data JPA) |
| Banco de dados      | PostgreSQL - Docker                         |
| Migrations          | Flyway                                      |
| Documentação da API | springdoc-openapi (Scalar UI)               |

## Arquitetura
A API é organizada em camadas. Uma requisição entra pelo *Controller*, passa pelo *Service*, que aplica as regras de negócio, e chega ao banco pelo *Repository*; a resposta faz o caminho de volta entregando o status ou a informação de retorno estruturada. O Mapper converte DTO em entidade e entidade em DTO.

**Mapa da estrutura:**

| Entidade | Migration                         | Model              | Repository                   | DTOs                                                     | Mapper              | Service                                       | Controller                                 |
| -------- | --------------------------------- | ------------------ | ---------------------------- | -------------------------------------------------------- | ------------------- | --------------------------------------------- | ------------------------------------------ |
| Cliente  | `V1__criar_clientes.sql`, pronta  | `Cliente`, pronta  | `ClienteRepository`, pronto  | `ClienteDTO`, `LivroResponseDTO`, prontos                | `???`, implementado | `ClienteService`, implementado                | `ClienteController`, implementado          |
| Produto  | `V2__criar_produtos.sql`, pronta  | `Produto`, pronta  | `ProdutoRepository`, pronto  | `ProdutoDTO`, `LeitorResponseDTO`, prontos               | `???`, implementado | `ProdutoService`, em parte: só o cadastro     | `ProdutoController`, em parte: só o `POST` |
| Carrinho | `V3__criar_carrinhos.sql`, pronta | `Carrinho`, pronta | `CarrinhoRepository`, pronto | `CarrinhoTransacaoDTO`, `EmprestimoResponseDTO`, prontos | `???`, implementado | `CarrinhoService`, implementado (R1, R2 e R3) | `CarrinhoController`, implementado         |

elaborar etc..

## Entidades

| Entidade | O que representa                                    | Como se relaciona                           |
| -------- | --------------------------------------------------- | ------------------------------------------- |
| Cliente  | Pessoa cadastrada na loja, com saldo                | Tem um carrinho e faz várias compras        |
| Produto  | Mídia física de jogo, com preço e estoque           | Aparece em vários itens de carrinho         |
| Carrinho | Conjunto de produtos que o cliente pretende comprar | Pertence a um cliente e tem vários produtos |

Atributos, tipos e o diagrama das tabelas estão específicados em: [docs/modelo-de-dominio.md](docs/modelo-de-dominio.md).

## Rotas

### As que já funcionam

| Verbo    | Caminho                  | O que faz                                                              | Regra        | Sucesso | Erros        |
| -------- | ------------------------ | ---------------------------------------------------------------------- | ------------ | ------- | ------------ |
| `POST`   | `/produto`               | Cadastra um produto                                                    | `R2`         | `201`   | `400`, `404` |
| `GET`    | `/produtos`              | Lista os produtos                                                      | Nenhuma      | `200`   | Nenhum       |
| `GET`    | `/produto/{id}`          | Busca um produto pelo id                                               | Nenhuma      | `200`   | `404`        |
| `DELETE` | `/produto/{id}`          | Deleta um produto pelo id                                              | Nenhuma      | `200`   | `404`        |
| `POST`   | `/cliente`               | Cadastra um cliente                                                    | `R2`         | `201`   | `400`        |
| `POST`   | `/compra`                | Registra um transação de compra e registra baixa de produto no estoque | `R1`  e `R3` | `200`   | `400`, `404` |
| `GET`    | `/carrinho/{idCarrinho}` | Lista de compra                                                        | Nenhuma      | `200`   | `400`, `404` |
| `PUT`    | `/saldo-cliente`         | Cadastra saldo cliente                                                 | Nenhuma      | `200`   | `400`, `404` |

Exemplo de requisição:

```http
POST /compra
Content-Type: application/json

{
  "carrinhoId": 1
}
```

Resposta, `200 Sucessfull`:

```json
{
  "id": 1,
  "clienteNome": "João",
  "produtoNome": "Fita Super Nintendo - Donkey Kong",
  "dataCompra": "2026-10-06"
}
```

### Erros

Todo erro sai no mesmo formato. Este é o `400` da regra R1 ou R3, quando alguém tenta comprar  um livro que não tem estoque ou não tem saldo suficiente para a transação:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R3: o Cliente {NomeCliente} não possui saldo suficiente para efetuar a compra",
  "caminho": "/compra",
  "timestamp": "2026-10-06T19:30:00",
}
```

### As planejadas

Estão no contrato, mas ainda não respondem.

| Recurso                               | O que vai fazer          |
| ------------------------------------- | ------------------------ |
| `GET /clientes` e `GET /cliente/{id}` | Listar e buscar clientes |

O contrato completo, com todas as rotas e os DTOs, está em [docs/contrato-da-api.md](docs/contrato-da-api.md).

## Como executar

Pré-requisitos: JDK 25, Docker Desktop aberto e Git.

```bash
cd "Aula 10-11/exemplo-biblioteca"

docker compose up -d          # sobe o PostgreSQL na porta 5438
./mvnw spring-boot:run        # Windows: .\mvnw.cmd spring-boot:run
```

Com a API no ar:

| Endereço | O que é |
|---|---|
| <http://localhost:8080/swagger-ui.html> | Swagger UI: as rotas, com **Try it out** para enviar requisições |
| <http://localhost:8080/v3/api-docs> | O contrato OpenAPI em JSON |

Para testar as rotas, use o Swagger UI ou o **Postman**. No Postman, importe todas as rotas de uma vez: **Import** → **Link** → `http://localhost:8080/v3/api-docs`.

Para ver as regras funcionando: cadastre um livro e um leitor, faça um empréstimo e tente emprestar o mesmo livro de novo (R2). Depois cadastre mais livros e faça empréstimos para o mesmo leitor até o quarto ser barrado (R1).

Para parar: `Ctrl+C` na API e `docker compose stop` no banco. Para apagar o banco e rodar as migrations do zero: `docker compose down -v` e suba de novo.

## Situação do projeto

ReadME em andamento

| Parte | Situação |
|---|---|
| Desenho | Completo: problema e regras neste README, modelo e contrato em `docs/` |
| Estrutura | As 3 entidades têm migration, entidade, Repository, DTOs, Mapper, Service e Controller |
| API e regras | Livros com as 3 rotas da API simples; cadastro de leitores; empréstimo e devolução com R1, R2 e R3 implementadas |

**Ainda não funciona:** `GET /leitores` e `GET /leitores/{id}`.

## Documentação

| Documento | O que tem |
|---|---|
| [Modelo de domínio](docs/modelo-de-dominio.md) | Entidades, relacionamentos, diagrama das tabelas, migrations |
| [Contrato da API](docs/contrato-da-api.md) | Todas as rotas, os DTOs, exemplos de JSON e erros |
| [Autoavaliação](docs/autoavaliacao.md) | Os critérios da revisão do código, marcados pelo grupo |
