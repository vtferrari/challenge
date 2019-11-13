# Desafio Noverde - Engenheiro de Software Senior

## Introdução
A Noverde, sendo uma empresa de crédito pessoal, recebe diariamente milhares de solicitações de crédito. Cada solicitação passa por um processo de análise, um fluxo com várias etapas, onde o resultado de cada etapa (chamada de política de crédito) retorna um estado de "APROVADO" ou "NEGADO". Caso toda as políticas sejam aprovadas, o crédito é liberado.

## Desafio
Você como engenheiro de software Noverde, precisa desenvolver uma aplicação backend para processar as requisições de crédito, disponibilizando para tal uma API com os seguintes endpoints:

### **POST** `/loan`
Este endpoint é responsável por receber as requisições. Ao recebê-la, você deve armazear os dados enviados pelo cliente para processamento posterior, e gerar um ID (UUID) retornando-o imediatamente. O processamento dos dados enviados será executado em background (ex: Sidekiq em Ruby ou Celery em Python). As informações trocadas na requisição e na resposta, precisam ser formatadas em `json`, seguindo os schemas listados abaix

#### Request Schema

|atributo|tipo|descricao|
|-|-|-|
|name|string|Nome do cliente|
|cpf|string|CPF do cliente|
|birthdate|date|Data de nascimento do cliente|
|amount|decimal|Valor desejado, entre R$ 1.000,00 e R$ 4.000,00|
|terms|integer|Quantidade de parcelas desejadas. Valores disponíveis: 6, 9 ou 12|
|income|decimal|Renda mensal do cliente|


#### Success Response Schema
|atributo|tipo|descricao|
|-|-|-|
|id|string|UUID gerado para esta requisição|


#### Error Response Schema
|atributo|tipo|descricao|
|-|-|-|
|errors|array|Lista de erros de validação|

**ATENÇÃO** Todos os atributos são obrigatórios. As restrições de cada atributo deve ser validada no momento da requisição, retornando o status `400 - Bad Request` em caso de erro na validação.


#### Sucess Response Schema

### **GET** `/loan/:id`
Este endpoint irá retornar o status atual da solicitação. Caso o processamento em background já tiver sido concluído, deve-se exibir o status como `completed` e o resultado no atributo `result`, caso contrário o status estará como `processing`.

#### Response Schema
|atributo|tipo|descricao|
|-|-|-|
|id|string|UUID requisitado|
|status|string|Status atual da solicitação. Possíveis valores: `processing`, `completed`|
|result|string|Em caso de status *completed*, os valores podem ser `approved` ou `refused`, caso contrário será **null**|
|refused_policy|string|Em caso de result *refused*, os valores podem ser: `age` se foi recusado na política de idade, `score` na política de score ou `commitment` na política de comprometimento. Caso não tenha sido negado, será **null**. Mais detalhes em [Políticas de Crédito](#políticas-de-crédito)|
|amount|decimal|Montante liberado em caso de proposta aprovada. Caso contrário deve ser **null**|
|terms|integer|Quantidade de parcelas aprovadas na oferta. Caso a proposta tenha sido recusada, deve ser **null**|

## Workflow

1. Cliente envia um POST com as devidas informações para o endpoint  `/loan`;
2. O backend salva a informação para ser processada em background (motor de crédito), e gera um UUID retornando para o cliente.
3. O motor de crédito começa a processar os dados informados, seguindo a ordem das políticas de crédito descritas na seção `Políticas de Crédito`.
4. O cliente pesquisa o status da sua solicitação no endpoint `/loan/:id`.


## Políticas de Crédito
As informações enviadas pelo cliente, são processadas pelo motor de crédito, que nada mais é que um conjunto de etapas (pipelines) com regras que podem negar aquela solicitação.
Caso todas as etapas sejam processadas com sucesso, a solicitação será `aprovada`, caso contrário, `recusada`.
O motor de crédito deve seguir a ordem de processamento abaixo:

### 1. Política de Idade
Aqui, deve-se verificar a idade informada pelo cliente. Caso ele seja menor de 18 anos, esta política irá `recusar` a solicitação.

### 2. Política de Score
Cada cliente possui um `credit score`, um valor que vai de 0 a 1000, sendo 1000 o melhor score e 0 o pior score. A consulta do score se dá através do serviço de score (API desenvolvida apenas para este teste). Detalhes da API abaixo:

#### **POST** `https://testapi.noverde-dev.com/score`

#### REQUEST HEADER
Esta API utiliza autenticação via token. Você deve solicitar o token previamente através do e-mail dev@noverde.com.br

```
 Authorization: Bearer $token_informado_por_email
```

#### REQUEST BODY
```json
 {
     "cpf": "12345678901"
 }
```

#### RESPONSE BODY
```json
 {
     "score": 800
 }
```

Caso o score do cliente solicitado seja **MENOR QUE 600** esta política irá `recusar` a solicitação.


### 3. Política de Comprometimento
A idéia do comprometimento de renda está em saber se o cliente conseguirá honrar com o pagamento da parcela, mediante o uso atual da renda.
Exemplo: João possui uma renda de R$ 1.500,00/mês, porém, 80% dessa renda (R$ 1.200,00) já está comprometida com diversas contas, como luz, aluguel, carro, etc. Logo, se a parcela do empréstimo for maior que R$ 300,00, existe uma grande chance de João não honrar este compromisso.

A renda comprometida pode ser consultada na seguinte API (desenvolvida exclusivamente para este desafio), que irá retornar a renda comprometida, em decimal, entre 0 e 1.
#### **POST** `https://testapi.noverde-dev.com/income`

#### REQUEST HEADER
Esta API utiliza autenticação via token. Você deve solicitar o token previamente através do e-mail dev@noverde.com.br

```
 Authorization: Bearer $token_informado_por_email
```

#### REQUEST BODY
```json
 {
     "cpf": "12345678901"
 }
```

#### RESPONSE BODY
```json
 {
     "commitment": 0.8 // 80% da renda comprometida
 }
```

Após calcular o [valor da parcela](#cálculo-da-parcela), deve-se verificar se este é maior que a renda comprometida. Em caso positivo, um novo cálculo com um período maior deve ser efetuado, visando diminuir o valor da parcela. Em cada novo cálculo, deve-se verificar se o valor da parcela ultrapassa a renda comprometida. Se todos os cálculos forem concluídos (em 6x, 9x e 12x) e mesmo assim a parcela ainda ultrapassar a renda comprometida, esta política deve `recusar` a solicitação.

##### Exemplo
João solicitou um empréstimo de R$ 1.000,00 para pagar em 6 parcelas. A renda de João é de R$ 1.500,00, mas 80% desta renda está comprometida, sobrando R$ 300,00.
O sistema calculou a parcela de R$ 500,00. Visto que essa parcela ultrapassa os R$ 300,00, o sistema irá fazer um novo cálculo, porém com 9 parcelas ao invés de 6 como solicitado por João.
Ao efetuar este cálculo, chegou-se no valor da parcela de R$ 320,00.
Este valor ainda é maior que os R$ 300,00 que João consegue pagar. Logo, o sistema tenta um novo cálculo, porém agora com 12 parcelas, chegando a um valor de parcela de R$ 280,00. Como este valor é menor que R$ 300, esta será a oferta informada para João: R$ 1.000,00 de empréstimo a serem pagos em 12 vezes de R$ 280,00.


## Taxas de Juros
Abaixo estão as taxas de juros **mensais** praticadas para este desafio. Estas taxas são relacionadas ao score do cliente + a quantidade de parcelas da oferta.

|score|6 parcelas|9 parcelas|12 parcelas
|-|-|-|-|
|900 ou mais|3,9%|4,2%|4,5%
|800 a 899|4,7%|5,0%|5,3%
|700 a 799|5,5%|5,8%|6,1%
|600 a 699|6,4%|6,6%|6,9%

## Cálculo da Parcela
O cálculo da parcela (chamado de PMT) leva em consideração 3 variáveis:

* Valor Presente (PV) - é o valor solicitado pelo cliente
* Número de parcelas ou períodos (n) - quantidade de parcelas, neste desafio pode ser apenas 6, 9 ou 12.
* Taxa de juros (i) - a taxa de juros praticada de acordo com a tabela descrita em ["Taxas de Juros"](#taxas-de-juros).

### Fórmula
![](https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcTUYGunlS7w_Fja0QfY4xHO-MPQoRdL94OILEzOZzi7zJfEC91u)

## Considerações
Você deve implementar tratamento para os erros mais óbvios, mas pode decidir como este tratamento será feito. Iremos considerar especialmente a clareza da sua solução, conceitos de orientação a objetos e o uso de boas práticas de programação (como Unit Tests).

O código deve ser escrito em uma das seguintes linguagens: Python, Ruby ou Javascript - utilizamos Python na Noverde.

Como resposta, envie um repositório GIT (Github, Bitbucket ou outro de sua preferência) contendo sua solução e um arquivo README com as instruções de como executar o código.

## Bônus (opcional)
Você pode utilizar a stack Serverless da AWS (API Gateway + Lambda + DynamoDB) para implementar a solução. Caso siga por este caminho, adicione os detalhes de execução no seu arquivo README.

## Observações
Os endpoints criados pela Noverde para serem utilizados neste desafio, não representam valores reais, mas sim dados gerados randomicamente com o intuito único e exclusivo de execução deste desafio.