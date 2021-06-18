# produtos-vendas-cqrs

Projeto para demonstração de habilidades com Java BackEnd utilizando micro-serviços.


## Descrição

<p>
Nesse projeto é criado uma aplicação para gerenciar produtos e pedidos (vendas), sendo possível 
criar e alterar dados dos produtos, os adicionar em pedidos e depois alterar os itens existentes no pedido.
</p>

<p>
O desenvolvimento foi feito com Java 11 + SpringBoot e utilizando rabbitmq + axon para controle das 
mensagens e eventos
</p>

<p>
A execução do sistema é feita de através de scripts encontrado no próprio repositório.
</p>

<p>
**OBS**: O projeto foi criado e testado em Ubuntu, talvez seja necessária alguma customização para execução 
em outras plataformas.
</p>
 

### Pré-requisitos (Sistema)
- Java 11 ([openJdk11](https://openjdk.java.net/projects/jdk/11/))
- Maven 3.2.5+ ([maven](https://maven.apache.org/install.html))
- Docker 20.10.3+ ([docker](https://docs.docker.com/))
- Git 2.17.1+ ([git](https://git-scm.com/downloads))


### Pré-requisitos (Imagem)
- Java 11 ([docker-openJdk11](https://hub.docker.com/_/openjdk))
- Postgresql ([docker-postgres](https://hub.docker.com/_/postgres))
- Activemq ([docker-activemq](https://hub.docker.com/r/rmohr/activemq))
- Axon Server ([docker-axonserver](https://hub.docker.com/r/axoniq/axonserver/))


### Execução
O comando a seguir utiliza três etapas para inicialização do sistema:
```
reset && mvn clean install && ./build.sh && docker-compose up
```

1. Build utilizando maven
2. Execução do script "build.sh" (detalhado abaixo)
3. Execução do docker-compose com base nas configurações da no arquivo "docker-compose.yml"

**build.sh**
```
#!/bin/bash
set -e

docker build -t produtos-vendas-cqrs-app .
```

### Aplicação Rodando
<p>
A url http://localhost:8081/swagger-ui/#/ pode ser utilizada para acessar uma documentação técnica do 
formato dos JSONs, métodos utilizados e opções existentes.
</p>


## Exemplos de Uso

#### PUT /produto
Criação de produto
```sh
curl --location --request PUT 'localhost:8081/produto' \
--header 'Content-Type: application/json' \
--data-raw '{
    "nome": "produto 1",
    "preco": 20.00
}'
```
Retorno
```json
produto-3f517a96-1a9a-418a-a010-bfada09a75ab
```

#### POST /produto/{id}/preco
Alterar preço de um produto
```sh
curl --location --request POST 'localhost:8081/produto/produto-3f517a96-1a9a-418a-a010-bfada09a75ab/preco' \
--header 'Content-Type: application/json' \
--data-raw '{
    "preco": 55.00
}'
```
Retorno
```json
produto-3f517a96-1a9a-418a-a010-bfada09a75ab
```

#### POST /produto/{id}/nome
Alterar nome de um produto
```sh
curl --location --request POST 'localhost:8081/produto/produto-3f517a96-1a9a-418a-a010-bfada09a75ab/preco' \
--header 'Content-Type: application/json' \
--data-raw '{
    "nome": "produto 1 modificado"
}'
```
Retorno
```json
produto-3f517a96-1a9a-418a-a010-bfada09a75ab
```

#### GET /produto
Listar produtos cadastrados
```sh
curl --location --request GET 'localhost:8081/produto'
```
Retorno
```json
[
    {
        "id": "produto-3f517a96-1a9a-418a-a010-bfada09a75ab",
        "nome": "produto modificado",
        "preco": 55.00
    }
]
```

#### PUT /pedido
Criar pedido para venda de produtos
```sh
curl --location --request PUT 'localhost:8081/pedido' \
--header 'Content-Type: application/json' \
--data-raw '{
    "itens" : [
        {
            "produtoId": "produto-3f517a96-1a9a-418a-a010-bfada09a75ab",
            "quantidade": 2
        }
    ]
}
'
```
Retorno
```json
pedido-2a71727a-f92e-4c16-9b42-cf5eba0594cd
```

#### POST /pedido/{id}/item
Adiciona Item a um pedido
```sh
curl --location --request POST 'localhost:8081/pedido/pedido-2a71727a-f92e-4c16-9b42-cf5eba0594cd/item' \
--header 'Content-Type: application/json' \
--data-raw '{
    "produtoId": "produto-c1b62046-68c2-4675-b7b2-dac567448d88",
    "quantidade": 5
}'
```
Retorno
```json
pedido-2a71727a-f92e-4c16-9b42-cf5eba0594cd
```

#### POST /itempedido/{id}/quantidade
Altera a quantidade de um item no pedido
```sh
curl --location --request POST 'localhost:8081/itempedido/itemPedido-43156abc-4379-4a51-9e77-70dbe0b38099/quantidade' \
--header 'Content-Type: application/json' \
--data-raw '13'
```
Retorno
```json
itemPedido-43156abc-4379-4a51-9e77-70dbe0b38099
```

#### DELETE /itempedido/{id}
Remover item do pedido
```sh
curl --location --request DELETE 'localhost:8081/itempedido/itemPedido-186a903d-0e4f-446d-ba47-02e12759d793'
```
Retorno
```json
itemPedido-43156abc-4379-4a51-9e77-70dbe0b38099
```


#### GET /pedido
Listar pedidos existentes
```sh
curl --location --request GET 'localhost:8081/pedido'
```
Retorno
```json
[
    {
        "id": "pedido-629c2466-8974-4c95-90f0-f124bbb0dd10",
        "itensPedido": [
            {
                "id": "itemPedido-d7a619ee-c2e0-4d01-8ca0-3a1d6921e750",
                "produto": {
                    "id": "produto-c1b62046-68c2-4675-b7b2-dac567448d88",
                    "nome": "produto 2"
                },
                "precoUnitario": 25.00,
                "precoTotal": 125.00,
                "quantidade": 5
            },
            {
                "id": "itemPedido-43156abc-4379-4a51-9e77-70dbe0b38099",
                "produto": {
                    "id": "produto-3f517a96-1a9a-418a-a010-bfada09a75ab",
                    "nome": "produto modificado"
                },
                "precoUnitario": 20.00,
                "precoTotal": 260.00,
                "quantidade": 13
            }
        ]
    }
]
```