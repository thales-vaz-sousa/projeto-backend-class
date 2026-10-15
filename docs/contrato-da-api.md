# Contrato da API

## Convenções

| Item | Valor |
|---|---|
| Endereço base | `http://localhost:8080` |
| Formato | JSON, na requisição e na resposta |
| Dinheiro | Número com até 2 casas decimais (`89.90`), em reais |
| Erros | Sempre no formato `ErroDTO` (ver [Erros](#erros)) |
| Documentação viva | `http://localhost:8080/swagger-ui.html` |

## Rotas

Coluna **Funciona**: `sim` para as rotas que já respondem nesta etapa. Todas as rotas abaixo estão implementadas.

### Produtos

| Verbo | Caminho | O que faz | Regra | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|---|
| `POST` | `/produto` | Cadastra uma mídia física | R2 | `ProdutoRequestDTO` | `ProdutoResponseDTO` | `201` | `400` | sim |
| `GET` | `/produtos` | Lista os produtos (lista vazia se não houver nenhum) | Nenhuma | Nenhuma | lista de `ProdutoResponseDTO` | `200` | Nenhum | sim |
| `GET` | `/produto/{id}` | Busca um produto pelo id | Nenhuma | Nenhuma | `ProdutoResponseDTO` | `200` | `404` | sim |
| `DELETE` | `/produto/{id}` | Apaga um produto. Não apaga se ele já está em algum carrinho | Nenhuma | Nenhuma | Nenhuma (corpo vazio) | `200` | `400`, `404` | sim |

### Clientes

| Verbo | Caminho | O que faz | Regra | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|---|
| `POST` | `/cliente` | Cadastra um cliente. O saldo nasce zerado | Nenhuma | `ClienteRequestDTO` | `ClienteResponseDTO` | `201` | `400` | sim |
| `GET` | `/clientes` | Lista os clientes (lista vazia se não houver nenhum) | Nenhuma | Nenhuma | lista de `ClienteResponseDTO` | `200` | Nenhum | sim |
| `GET` | `/cliente/{id}` | Busca um cliente pelo id | Nenhuma | Nenhuma | `ClienteResponseDTO` | `200` | `404` | sim |
| `PUT` | `/saldo-cliente` | Carrega saldo: o valor é **somado** ao saldo atual | Nenhuma | `SaldoClienteRequestDTO` | `ClienteResponseDTO` | `200` | `400`, `404` | sim |

### Carrinhos

| Verbo | Caminho | O que faz | Regra | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|---|
| `POST` | `/carrinho` | Abre um carrinho vazio para um cliente | Nenhuma | `CarrinhoRequestDTO` | `CarrinhoResponseDTO` | `201` | `400`, `404` | sim |
| `POST` | `/carrinho/{idCarrinho}/itens` | Coloca um produto no carrinho. Repetir o produto soma a quantidade | R1 | `ItemCarrinhoRequestDTO` | `CarrinhoResponseDTO` | `200` | `400`, `404` | sim |
| `GET` | `/carrinho/{idCarrinho}` | Lista o carrinho, com itens e total | Nenhuma | Nenhuma | `CarrinhoResponseDTO` | `200` | `404` | sim |

### Compras

| Verbo | Caminho | O que faz | Regra | Entrada | Saída | Sucesso | Erros | Funciona |
|---|---|---|---|---|---|---|---|---|
| `POST` | `/compra` | Finaliza a compra de um carrinho: desconta o saldo do cliente, baixa o estoque e fecha o carrinho | R1 e R3 | `CompraRequestDTO` | `CompraResponseDTO` | `200` | `400`, `404` | sim |

## Regras de negócio nas rotas

| Regra | O que exige | Onde é conferida | Resposta quando violada |
|---|---|---|---|
| `R1` | O produto precisa ter estoque > 0 e suficiente para a quantidade pedida | `POST /carrinho/{id}/itens` (soma o que já está no carrinho) e de novo em `POST /compra`, porque o estoque pode mudar entre pôr no carrinho e pagar | `400` com a mensagem começando em `R1:` |
| `R2` | O cadastro de mídia não pode ter campos em branco | `POST /produto` (validação do DTO e conferência no Service) | `400` com a lista `campos` dizendo o que corrigir |
| `R3` | O cliente só compra com saldo >= total do carrinho | `POST /compra` | `400` com a mensagem começando em `R3:` |

## DTOs

| DTO | Direção | Campos | Validações |
|---|---|---|---|
| `ProdutoRequestDTO` | entrada | `nome`, `preco`, `estoque` | `nome` obrigatório, até 255 caracteres. `preco` obrigatório, maior que zero, até 2 casas decimais. `estoque` obrigatório, zero ou mais (R2) |
| `ProdutoResponseDTO` | saída | `id`, `nome`, `preco`, `estoque` | Nenhuma |
| `ClienteRequestDTO` | entrada | `nome`, `email` | os dois obrigatórios, até 255 caracteres; `email` em formato válido |
| `ClienteResponseDTO` | saída | `id`, `nome`, `email`, `saldo` | Nenhuma |
| `SaldoClienteRequestDTO` | entrada | `clienteId`, `valor` | os dois obrigatórios; `valor` maior que zero, até 2 casas decimais |
| `CarrinhoRequestDTO` | entrada | `clienteId` | obrigatório |
| `CarrinhoResponseDTO` | saída | `id`, `clienteId`, `clienteNome`, `dataCriacao`, `itens`, `total`, `finalizado` | Nenhuma. `itens` é lista de `ItemCarrinhoResponseDTO` (vazia se não há itens). `total` é a soma dos subtotais |
| `ItemCarrinhoRequestDTO` | entrada | `produtoId`, `quantidade` | os dois obrigatórios; `quantidade` maior que zero |
| `ItemCarrinhoResponseDTO` | saída | `produtoId`, `produtoNome`, `precoUnitario`, `quantidade`, `subtotal` | Nenhuma. `subtotal` = `precoUnitario` x `quantidade` |
| `CompraRequestDTO` | entrada | `carrinhoId` | obrigatório |
| `CompraResponseDTO` | saída | `id`, `clienteNome`, `itens`, `valorTotal`, `dataCompra` | Nenhuma. `itens` é lista de `ItemCarrinhoResponseDTO` |
| `ErroDTO` | saída | `status`, `erro`, `mensagem`, `caminho`, `timestamp`, `campos` | Nenhuma. `campos` é lista de `CampoErroDTO` |
| `CampoErroDTO` | saída | `campo`, `mensagem` | Nenhuma |

Relacionamento entra como id (`"clienteId": 1`) e sai também como id e nome (`"clienteId": 1, "clienteNome": "João"`).

## Exemplos

### `POST /produto` (funciona)

Requisição:

```json
{
  "nome": "Fita Super Nintendo - Donkey Kong",
  "preco": 89.90,
  "estoque": 5
}
```

Resposta, `201 Created`:

```json
{
  "id": 1,
  "nome": "Fita Super Nintendo - Donkey Kong",
  "preco": 89.90,
  "estoque": 5
}
```

### `GET /produtos` (funciona)

Resposta, `200 OK`:

```json
[
  {
    "id": 1,
    "nome": "Fita Super Nintendo - Donkey Kong",
    "preco": 89.90,
    "estoque": 5
  }
]
```

### `DELETE /produto/{id}` (funciona)

Resposta, `200 OK`, sem corpo. Se o produto já está em algum carrinho, responde `400`:

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "O produto 1 está em carrinhos e não pode ser apagado",
  "caminho": "/produto/1",
  "timestamp": "2026-10-06T19:30:00",
  "campos": []
}
```

### `POST /cliente` (funciona)

Requisição:

```json
{
  "nome": "João",
  "email": "joao@exemplo.com"
}
```

Resposta, `201 Created`:

```json
{
  "id": 1,
  "nome": "João",
  "email": "joao@exemplo.com",
  "saldo": 0
}
```

### `PUT /saldo-cliente` (funciona)

O valor é somado ao saldo atual.

Requisição:

```json
{
  "clienteId": 1,
  "valor": 200.00
}
```

Resposta, `200 OK`:

```json
{
  "id": 1,
  "nome": "João",
  "email": "joao@exemplo.com",
  "saldo": 200.00
}
```

### `POST /carrinho` (funciona)

Requisição:

```json
{
  "clienteId": 1
}
```

Resposta, `201 Created`:

```json
{
  "id": 1,
  "clienteId": 1,
  "clienteNome": "João",
  "dataCriacao": "2026-10-06",
  "itens": [],
  "total": 0,
  "finalizado": false
}
```

### `POST /carrinho/{idCarrinho}/itens` (funciona)

Requisição para `POST /carrinho/1/itens`:

```json
{
  "produtoId": 1,
  "quantidade": 2
}
```

Resposta, `200 OK`, com o carrinho atualizado:

```json
{
  "id": 1,
  "clienteId": 1,
  "clienteNome": "João",
  "dataCriacao": "2026-10-06",
  "itens": [
    {
      "produtoId": 1,
      "produtoNome": "Fita Super Nintendo - Donkey Kong",
      "precoUnitario": 89.90,
      "quantidade": 2,
      "subtotal": 179.80
    }
  ],
  "total": 179.80,
  "finalizado": false
}
```

### `GET /carrinho/{idCarrinho}` (funciona)

Resposta, `200 OK`: o mesmo formato do exemplo acima.

### `POST /compra` (funciona)

Requisição:

```json
{
  "carrinhoId": 1
}
```

Resposta, `200 OK`:

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

Depois da compra, o saldo do cliente cai em 179.80, o estoque do produto cai em 2 unidades e o carrinho passa a `finalizado: true`.

## Erros

Todo erro sai no mesmo formato. Este é o `404` de `GET /produto/999`:

```json
{
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Produto não encontrado: 999",
  "caminho": "/produto/999",
  "timestamp": "2026-10-06T19:30:00",
  "campos": []
}
```

No `400` de validação, a lista `campos` traz um item por campo inválido. Este é o de `POST /produto` com o nome em branco (R2):

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Dados inválidos: confira a lista de campos",
  "caminho": "/produto",
  "timestamp": "2026-10-06T19:30:00",
  "campos": [
    { "campo": "nome", "mensagem": "R2: o nome é obrigatório" }
  ]
}
```

Quando uma regra de negócio barra o pedido, o `400` vem com a regra na `mensagem` e `campos` vazio. Este é o de `POST /compra` com saldo insuficiente (R3):

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R3: o cliente João não possui saldo suficiente para efetuar a compra (saldo: 0.00, total: 179.80)",
  "caminho": "/compra",
  "timestamp": "2026-10-06T19:30:00",
  "campos": []
}
```

E este é o de `POST /carrinho/1/itens` com produto sem estoque suficiente (R1):

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "R1: o produto Fita Super Nintendo - Donkey Kong não tem estoque suficiente (disponível: 5)",
  "caminho": "/carrinho/1/itens",
  "timestamp": "2026-10-06T19:30:00",
  "campos": []
}
```

| Status | Quando acontece neste projeto |
|---|---|
| `400` | JSON ilegível ou campo inválido no DTO de entrada (a lista `campos` diz qual) |
| `400` | Regra R1 ou R3 violada. A `mensagem` explica a regra |
| `400` | Item colocado em carrinho já finalizado, compra de carrinho vazio ou já finalizado, ou `DELETE` de produto que já está em carrinhos |
| `404` | Id de produto, cliente ou carrinho que não existe |
 