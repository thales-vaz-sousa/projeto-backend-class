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

| #   | Regra                                                                                      | Onde está no código                   | Rota que usa                                  |
| --- | ------------------------------------------------------------------------------------------ | ------------------------------------- | --------------------------------------------- |
| R1  | Só se compra produto com estoque maior que zero e suficiente para a quantidade pedida      | `CarrinhoService`                     | `POST` /carrinho/{id}/itens` e `POST` /compra |
| R2  | O cadastro de uma mídia não pode ter campos em branco ou inválidos (nome, preço e estoque) | `ProdutoRequestDTO`, `ProdutoService` | `POST /produto`                               |
| R3  | O cliente só compra se o saldo for maior ou igual ao valor total do carrinho               | `CarrinhoService`                     | `POST /compra`                                |


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

|Entidade|Migration|Model|Repository|DTOs|Mapper|Service|Controller|
|---|---|---|---|---|---|---|---|
|Cliente|`V1__criar_clientes.sql`, pronta|`Cliente`, pronta|`ClienteRepository`, pronto|`ClienteRequestDTO`, `ClienteResponseDTO`, `SaldoClienteRequestDTO`, prontos|`ClienteMapper`, implementado|`ClienteService`, implementado|`ClienteController`, implementado|
|Produto|`V2__criar_produtos.sql`, pronta|`Produto`, pronta|`ProdutoRepository`, pronto|`ProdutoRequestDTO`, `ProdutoResponseDTO`, prontos|`ProdutoMapper`, implementado|`ProdutoService`, implementado (R2)|`ProdutoController`, implementado|
|Carrinho|`V3__criar_carrinhos.sql`, pronta|`Carrinho`, pronta|`CarrinhoRepository`, pronto|`CarrinhoRequestDTO`, `CarrinhoResponseDTO`, `CompraRequestDTO`, `CompraResponseDTO`, prontos|`CarrinhoMapper`, implementado|`CarrinhoService`, implementado (R1 e R3)|`CarrinhoController`, implementado|
|ItemCarrinho|`V3__criar_carrinhos.sql`, pronta|`ItemCarrinho`, pronta|`ItemCarrinhoRepository`, pronto|`ItemCarrinhoRequestDTO`, `ItemCarrinhoResponseDTO`, prontos|`ItemCarrinhoMapper`, implementado|`ItemCarrinhoService`, montada, sem lógica|`ItemCarrinhoController`, montado, sem rotas|

Os erros usam `ErroDTO` e `CampoErroDTO`, também em `dto/`.
## Entidades

|Entidade|O que representa|Como se relaciona|
|---|---|---|
|Cliente|Pessoa cadastrada na loja, com saldo|Tem vários carrinhos (os finalizados são suas compras)|
|Produto|Mídia física de jogo, com preço e estoque|Aparece em vários itens de carrinho|
|Carrinho|Conjunto de produtos que o cliente pretende comprar|Pertence a um cliente e tem vários itens|
|ItemCarrinho|Um produto dentro de um carrinho, com a quantidade|Liga um carrinho a um produto (relação N:N)|

Atributos, tipos e o diagrama das tabelas estão em docs/modelo-de-dominio.md.

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
  "itens": [
    {
      "produtoId": 1,
      "produtoNome": "Fita Super Nintendo - Donkey Kong",
      "precoUnitario": 89.90,
      "quantidade": 2,
      "subtotal": 179.80
    }
  ],
  "valorTotal": 179.80,
  "dataCompra": "2026-10-06"
}
```

### Erros

Todo erro sai no mesmo formato. Este é o `400` da regra R3, quando o cliente não tem saldo suficiente (a R1 sai igual, com a mensagem começando em `R1:`):

```json
{ "status": 400, "erro": "Bad Request", 
"mensagem": "R3: o cliente João não possui saldo suficiente para efetuar a compra (saldo: 0.00, total: 179.80)", 
"caminho": "/compra", 
"timestamp": "2026-10-06T19:30:00", 
"campos": [] 
}
```

### As planejadas

Estão no contrato, mas ainda não respondem.

| Recurso                               | O que vai fazer          |
| ------------------------------------- | ------------------------ |
| `GET /clientes` e `GET /cliente/{id}` | Listar e buscar clientes |

O contrato completo, com todas as rotas, DTOs e JSON, está em [docs/contrato-da-api.md](docs/contrato-da-api.md).

## Como executar

Pré-requisitos: JDK 25, Docker Desktop aberto e Git.

```bash
cd gamestore

docker compose up -d          # sobe o PostgreSQL na porta 5438
./mvnw spring-boot:run        # Windows: .\mvnw.cmd spring-boot:run
```

Para ver as regras funcionando: cadastre um produto com estoque 1 e um cliente com saldo baixo,
monte um carrinho e chame `POST /compra` para ver a R3 barrar. Depois dê saldo
com `PUT /saldo-cliente`, compre (estoque vai a 0) e tente comprar de novo para ver a R1 barrar.

Com a API no ar:

| Endereço | O que é |
|---|---|
| <http://localhost:8080/swagger-ui.html> | Swagger UI: as rotas, com **Try it out** para enviar requisições |
| <http://localhost:8080/v3/api-docs> | O contrato OpenAPI em JSON |

Para testar as rotas, use o Swagger UI ou o **Postman**. No Postman, importe todas as rotas de uma vez: **Import** → **Link** → `http://localhost:8080/v3/api-docs`.

Para ver as regras funcionando ao rodar:

1. Cadastre um produto com `estoque` 1 (`POST /produto`) e um cliente (`POST /cliente`, saldo 0).
2. Abra um carrinho (`POST /carrinho`) e coloque o produto nele (`POST /carrinho/1/itens`). Com `quantidade` 2, a **R1** barra.
3. Com `quantidade` 1, chame `POST /compra`: a **R3** barra, porque o saldo é zero.
4. Dê saldo com `PUT /saldo-cliente` e compre de novo. O estoque vai a 0.
5. Abra outro carrinho e tente pôr o mesmo produto: a **R1** barra de novo.
6. Para a **R2**, envie `POST /produto` com o `nome` em branco.

Para parar: `Ctrl+C` na API e `docker compose stop` no banco. Para apagar o banco e rodar as migrations do zero: `docker compose down -v` e suba de novo.

## Situação do projeto

| Parte        | Situação                                                                                                           |
| ------------ | ------------------------------------------------------------------------------------------------------------------ |
| Desenho      | Problema e regras neste README; modelo e contrato em `docs/`                                                       |
| Estrutura    | As 4 entidades têm migration, entidade, Repository, DTOs, Mapper, Service e Controller (`ItemCarrinho` só montada) |
| API e regras | Todas as rotas do contrato funcionam; R1, R2 e R3 implementadas                                                    |

**Ainda não funciona:** nada do que está no contrato. Ficam para as próximas etapas as rotas que nenhuma regra usa (como `PUT` de produto) e a autenticação.

## Documentação

| Documento | O que tem |
|---|---|
| [Modelo de domínio](docs/modelo-de-dominio.md) | Entidades, relacionamentos, diagrama das tabelas, migrations |
| [Contrato da API](docs/contrato-da-api.md) | Todas as rotas, os DTOs, exemplos de JSON e erros |
| [Autoavaliação](docs/autoavaliacao.md) | Os critérios da revisão do código, marcados pelo grupo |
