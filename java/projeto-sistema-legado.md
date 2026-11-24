# Projeto: Evolução e Modernização de um Sistema Legado

![Logotipo da instituição](img/logo.png)

## Etapa 1: Escolha e Descrição do Sistema Legado (Peso: 1,0 ponto)

### Formação das Equipes e Regras de Colaboração

#### Apresentação da Equipe

| Nome                | Matrícula | E-mail                    | Usuário GitHub |
| ------------------- | --------- | ------------------------- | -------------- |
| Sérgio Lopes Júnior | 200026817 | sergio.lopes@ucsal.edu.br | sergiolj       |
| Manuel Bahamonde Boulhosa Neto | 200031998 | manuelbahamonde.neto@ucsal.edu.br | noclick11 |

#### Origem do Sistema Legado

O sistema legado escolhido faz parte do acervo de projetos para refatoração criados pela consultora independente e desenvolvedora de software Emily Bache. Emily é graduada em Engenharia pela Universidade de Cambridge no Reino Unido e  trabalha com consultoria com foco em refatoração contínua e técnicas como TDD (Test Driven Design).

Um dos algoritmos de refatoração disponíveis no github da autora (https://github.com/emilybache/) foi escolhido pela equipe para o desenvolvimento deste trabalho. Trata-se do algoritmo SupermarketReceipt-Refactoring-Kata (https://github.com/emilybache/SupermarketReceipt-Refactoring-Kata). O algoritmo possui versões em diversas linguagens de programação como cpp, csharp, php, Kotlin, Python e Java, entre outras.

A linguagem de programação escolhida foi o Java.

#### Ficha Técnica do sistema

- **Nome:** SupermarketReceipt-Refactoring-Kata
- **Fonte:** https://github.com/emilybache/SupermarketReceipt-Refactoring-Kata
- **Objetivo:** Simular um sistema de supermercado com grande rotatividade de ofertas de produtos além de grande variedade de promoções baseadas em diferentes tipos de ofertas como percentuais de desconto no valor dos produtos, promoções de compre três pelo preço de dois ou compre cinco produtos por um valor determinado, entre outras que devem ser criadas como novos recursos.
- **Tecnologias:** Java 17 e Junit 5.11.4
- **Justificativa:** O projeto foi desenvolvido com uma arquitetura procedural disfarçada de orientação a objetos. Embora possua objetos característicos de OO esses possuem forte acoplamento, métodos longos e com vários if-else encadeados, assim como baixo encapsulamento e vários objetos apenas como records ou classes anêmicas e sem inteligência, concentrando toda a regra de negócio em umas poucas classes. Além disso usa variáveis primitivas como double para representar valores monetários, o que dificulta a precisão e testes e cria erros de arredondamento. Além disso, os nomes de métodos e atributos são pouco expressivos e estão em língua inglesa o que dificulta a compreensão do fluxo do código.
- **Exemplos de code smell**
  - Método **ShoppingCart.handle.Offers** concentra toda a lógica do cálculo de promoções em um único bloco if/else if criando uma alta complexidade ciclomática
  - Atributo double **unitPrice** primitivo para representar e manipular dinheiro criando erros de arredondamento
  - Atributo double **quantity** primitivo para representar tanto unidades inteiras como representações de peso
  - Classes anêmicas como **Product, ProductQuantity, Offer e Discount** que não possuem nenhuma inteligência ou regra de negócio.

#### Mapeamento das funcionalidades atuais

O sistema não possui interface nem ponto de entrada (`main`); suas funcionalidades são expostas como API de classes Java e exercitadas pelos testes automatizados. O mapeamento abaixo foi feito a partir da leitura do código-fonte e dos testes.

| #    | Funcionalidade                  | Descrição                                                                                                                         | Onde está implementada                        |
| --- | ------------------------------- | --------------------------------------------------------------------------------------------------------------------------------- | --------------------------------------------- |
| F-01 | Catálogo de produtos            | Cadastra produtos com preço unitário e consulta o preço de um produto. Interface sem implementação de produção (só `FakeCatalog` nos testes). | `SupermarketCatalog`, `FakeCatalog` (teste)   |
| F-02 | Tipos de unidade de produto     | Produto vendido por unidade (`EACH`, discreto) ou por peso (`KILO`, fracionado).                                                  | `Product`, `ProductUnit`                      |
| F-03 | Carrinho de compras             | Adiciona um produto (1 unidade) ou uma quantidade qualquer; mantém cada linha adicionada e a quantidade acumulada por produto.    | `ShoppingCart`, `ProductQuantity`             |
| F-04 | Cadastro de ofertas especiais   | A caixa registra uma oferta por produto (tipo + valor/quantidade); uma nova oferta para o mesmo produto substitui a anterior.     | `Teller.addSpecialOffer`, `Offer`             |
| F-05 | Oferta 10% de desconto          | Percentual (valor configurável) aplicado sobre o total do produto, mesmo para uma única unidade.                                  | `ShoppingCart.handleOffers`                   |
| F-06 | Oferta leve 3, pague 2          | A cada 3 unidades, 1 é gratuita; só se aplica com 3 ou mais unidades.                                                             | `ShoppingCart.handleOffers`                   |
| F-07 | Oferta 2 por valor fixo         | A cada 2 unidades, preço fixo definido na oferta; só se aplica com 2 ou mais unidades.                                            | `ShoppingCart.handleOffers`                   |
| F-08 | Oferta 5 por valor fixo         | A cada 5 unidades, preço fixo definido na oferta; só se aplica com 5 ou mais unidades.                                            | `ShoppingCart.handleOffers`                   |
| F-09 | Fechamento da compra (checkout) | Gera a nota fiscal: uma linha por item adicionado, com preço unitário e total, e aplica as ofertas sobre as quantidades acumuladas. | `Teller.checksOutArticlesFrom`                |
| F-10 | Nota fiscal                     | Armazena itens e descontos e calcula o total (soma dos itens + descontos, que são valores negativos).                             | `Receipt`, `ReceiptItem`, `Discount`          |
| F-11 | Impressão da nota               | Gera o texto da nota em colunas: itens, linha de quantidade, descontos e total (quantidade inteira para `EACH`, 3 casas para `KILO`). | `ReceiptPrinter` (movido de `src/test` para `src/main` na Etapa 3) |
| F-12 | Oferta de pacote (*bundle*) | **Nova (Etapa 3).** Ao levar uma unidade de cada produto do conjunto, desconto percentual sobre o total do conjunto; só pacotes completos contam e soma-se às ofertas por produto. | `BundleOffer`, `SpecialOffers`, `Teller.addBundleOffer` |

**Fora do escopo atual (não implementado):** persistência, interface de usuário e catálogo real.

**Regras de negócio observadas (confirmadas por testes de caracterização):** descontos são registrados como valores negativos; as ofertas consideram a soma das quantidades do mesmo produto no carrinho, mesmo adicionado em linhas separadas; produtos sem oferta ou abaixo do mínimo da oferta pagam o preço cheio.

## Etapa 2: Engenharia Reversa e Diagnóstico Técnico (Peso: 2,0 pontos)

### Mapeamento da arquitetura atual e identificação de módulos/componentes.

Segundo uma pesquisa usando o [Claude.ai](http://Claude.ai) existem diversas técnicas disponíveis para o mapeamento de arquitetura e identificação de módulos em softwares legados. Alguns métodos, voltados para sistemas maiores utilizam ferramentas de software específicas como o SonarQube/SonarLint, PMD e CK (no ambiente Java), essas ferramentas medem a complexidade ciclomática e cognitiva através de métodos e métricas próprias e geram relatórios quantitativos além de permitirem a comparação do antes e depois da refatoração, o problema, nesse caso, é que nosso projeto é pequeno com algumas poucas classes e linhas de código o que irá gerar resultados pouco expressivos com a maioria das classes aparecendo como de baixa complexidade e umas poucas de alta, fato que pode ser percebido apenas com a leitura do código e análise estática manual, que é uma das técnicas mais básicas.

A técnica adotada deve ser adequada ao tamanho do problema. Sendo assim foram consideradas as seguintes abordagens: 1- Leitura de código e análise estática manual, 2- Recuperação de arquitetura por modelos (usando diagramas UML via engenharia reversa), 3- Cartões CRC(Class-Responsibility-Collaborator), 4- Detecção de code smells (catálogo de Fowler - Refactoring) e 5- Análise Dinâmica de testes de caracterização.

Existem diversas outras técnicas, mas as listadas acima parecem se adequar mais ao tamanho do problema apresentado. Dessas as abordagens adotadas foram a 1, 2, 3 e a 4 (Relatório de Débito Técnico) que foi incluída como parte do passo 5 da etapa 1, além da técnica 5 será executada diretamente no repositório a ser refaturado para dar segurança a integridade do algoritmo durante a refatoração.

#### 3.1 Leitura do código e análise manual

Para a leitura manual do código poder ser rastreável é importante a definição de um roteiro a ser seguido com passos definidos e claros. O roteiro escolhido abrange cinco passos com a produção de artefatos específicos a cada passo assim caracterizados:

**Passo 1:**

**Preparação do roteiro** - O repositório original foi clonado e foram executados os testes automatizados disponíveis para verificar seu funcionamento, a seguir foi feito o levantamento bruto dos arquivos.

**Registro:**

Versão Java, branch main, commit c72d68abd77a417f84193b72a3b8d14177336066

Repositório possui:

- 11 classes em apenas um package model (src/main/java/dojo.supermarket.model)
- 1 classe de teste (src/test/java/dojo.supermarket.model)
- 1 classe de implementação de interface (src/test/java/dojo.supermarket.model)
- 1 classe de funcionalidade de impressão (src/test/java/dojo.supermarket)
- 1 classe de relatório sem funcionalidade ((src/test/java/dojo.supermarket)
- Número total de linhas de código: 345

**Passo 2:**

**Leitura panorâmica** - Visualização e registro inicial das funcionalidades de cada classe e seu nível de complexidade ou primeiras impressões

**Registro:**

> **Legenda:** 🟦 record · 🟥 enum · 🟨 classe · 🟪 interface · 🟩 classe de teste
> *(no documento original, as linhas são coloridas por tipo)*

| **Classe**           | **Tipo**        | **Funcionalidade**                                                                     | **Primeiras impressões**                                                    |
| -------------------- | --------------- | -------------------------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| `Discount`           | 🟦 record        | armazena produto e desconto                                                            | Simples e sem responsabilidades                                             |
| `Offer`              | 🟦 record        | armazena produto e tipo de oferta                                                      | Simples e sem responsabilidades                                             |
| `Product`            | 🟦 record        | armazena produto e tipo de unidade                                                     | Simples e sem responsabilidades                                             |
| `ProductQuantity`    | 🟦 record        | armazena produto e quantidade                                                          | Simples e sem responsabilidades                                             |
| `ProductUnit`        | 🟥 enum          | classifica o tipo da unidade                                                           | Simples e sem responsabilidades                                             |
| `Receipt`            | 🟨 classe        | armazena itens e descontos e calcula totais da nota fiscal                             | Média complexidade com responsabilidades confusas                           |
| `ReceiptItem`        | 🟦 record        | armazena produto quantidade, preço unitário e total                                    | Simples e sem responsabilidades                                             |
| `ShoppingCart`       | 🟨 classe        | processa lista de produtos e descontos                                                 | Alta complexidade com responsabilidades confusas                            |
| `SpecialOfferType`   | 🟥 enum          | classifica tipos de ofertas                                                            | Simples e sem responsabilidades                                             |
| `SuperMarketCatalog` | 🟪 interface     | define métodos para um catálogo de produtos                                            | Simples                                                                     |
| `Teller`             | 🟨 classe        | recebe um catálogo de produtos e emite uma nota fiscal                                 | Média complexidade e com responsabilidades confusas                         |
| `PackageSettings`    | 🟨 classe        | arquivo de configuração para um tipo de relatório de testes                            | Simples e sem implementação                                                 |
| `ReceiptPrinter`     | 🟨 classe        | processa a nota fiscal para gerar uma saída através de métodos encadeados              | Alta complexidade pelo encadeamento de métodos (Method Chaining code smell) |
| `FakeCatalog`        | 🟩 classe (test) | implementador da interface SuperMarketCatalog usado para os testes                     | Simples                                                                     |
| `SuperMarketTest`    | 🟩 classe (test) | testa o resultado da nota fiscal após os produtos serem processados por outras classes | Média complexidade e com apenas um teste de oferta implementado.            |

**Passo 3:**

**Encontrar o ponto de entrada -** Compreender como se dá o fluxo de dados no algoritmo, onde se inicia.

**Registro:**

No algoritmo observado não existe um main e os testes concentram o que se pode observar do fluxo esperado para o sistema, mais especificamente a classe SupermarketTest.

**Passo 4:**

**Leitura guiada de um cenário concreto** - Como no exemplo existe apenas um teste implementado e não há outras classes que implementam fluxo o registro irá se concentrar nesse teste.

<table>
  <thead>
    <tr><th rowspan="2">#</th><th colspan="2">Processo acompanhado</th><th rowspan="2">Observação</th></tr>
    <tr><th>Onde acontece (Classe.Método)</th><th>O que acontece</th></tr>
  </thead>
  <tbody>
    <tr><td>1</td><td><code>SupermarketTest.tenPercentDiscount</code></td><td>cria um catálogo através da classe FakeCatalog(), cria dois produtos: pasta de dente preço 0.99 e maçãs com preço 1.99 e adiciona ambos ao catálogo</td><td></td></tr>
    <tr><td>2</td><td><code>Teller</code></td><td>cria uma caixa registradora passando como argumento o catálogo com os produtos</td><td></td></tr>
    <tr><td>3</td><td><code>Teller.addSpecialOffer</code></td><td>adiciona à caixa registradora uma oferta especial com tipo da oferta, o produto em oferta e um argument (double) que são adicionados em uma interface Map HashMap</td><td>O nome do atributo argument ficou confuso. Não fica claro sua função.</td></tr>
    <tr><td>3</td><td><code>ShoppingCart</code></td><td>cria o carrinho de compras e adiciona o produto maçã com a quantidade (double 2.5)</td><td></td></tr>
    <tr><td>4</td><td><code>Receipt</code></td><td>cria a nota fiscal passando como referência o método da caixa registradora que processa o carrinho de compras</td><td></td></tr>
    <tr><td>4.1</td><td><code>teller.checkOutArticlesFrom(ShoppingCart)</code></td><td>a classe nota fiscal irá receber a referência da  nota fiscal de retorno do método da caixa registradora tendo como base o carrinho de compras</td><td></td></tr>
    <tr><td>4.2</td><td><code>theCart.handleOffers(Receipt, Map&lt;Product, Offers&gt;, SupermarketCatalog)</code></td><td>O carrinho de compras recebe a nota fiscal, a lista de ofertas e o catálogo de produtos para processamento</td><td>O carrinho de compras é que vai processar as ofertas através de if/else encadeados fazendo alterações diretamente na nota fiscal violando princípio da responsabilidade única.</td></tr>
    <tr><td>4.3</td><td><code>teller.checkOutArticlesFrom(ShoppingCart)</code></td><td>a classe nota fiscal recebe a referência da nota fiscal de retorno processada pela classe carrinho</td><td></td></tr>
    <tr><td>5</td><td><code>assertEquals</code> <code>receipt.getTotalPrice</code></td><td>testa o valor calculado do preço total sem descontos</td><td>double para valor dificulta o teste de arredondamento e demanda o uso de um valor de arredondamento</td></tr>
    <tr><td>6</td><td><code>ReceiptPrinter.print Receipt(Receipt)</code></td><td>não aparece no teste mas a finalização do fluxo seria com a impressão da nota fiscal</td><td></td></tr>
  </tbody>
</table>

**Passo 5:**

**Leitura aprofundada de cada classe e método** - O método mais complexo do algoritmo está na classe ShoppingCart que processa as ofertas, chamado de ShoppingCart.handleOffers, mas todas as classes foram avaliadas para verificar alguma inconsistência que posteriormente foi categorizada com base no Refactoring de Fowler.

| **ID** | **Local**                                       | **Achado**                                                                                                                                                                                   | **Categoria**             | **Confirmar com**                                                   |
| ------ | ----------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------- | ------------------------------------------------------------------- |
| L-01   | `Offer` double argument                         | `argument`: nome do atributo não deixa claro o que representa                                                                                                                                | Nome Misterioso           | Rename Variable, RenameField                                        |
| L-02   | `ShoppingCart.addItem` (linha 18)               | Método específico para adicionar um produto com apenas uma unidade (não parece fácil identificar o que faz)                                                                                  | Generalidade Especulativa | Remove Dead Code                                                    |
| L-03   | `ShoppingCart.addItemQuantity` (linha 26)       | quantidade de produtos com primitiva em double tanto para unidade quanto para volume                                                                                                         | Obsessão por Primitivos   | Replace Primitive with Object                                       |
| L-04   | `ShoppingCart.handleOffers` (linha 35)          | Método longo com muitos if/else encadeados                                                                                                                                                   | Função Longa              | Extract Function, Decompose Conditional, Replace Loop with Pipeline |
| L-05   | `ShoppingCart.handleOffers` (linha 41)          | casting da quantidade em double para int                                                                                                                                                     | Obsessão por Primitivos   | Replace Primitive with Object                                       |
| L-06   | `ShoppingCart.handleOffers` (linha 43)          | atributo com nome genérico primitiva com o nome “x”                                                                                                                                          | Nome Misterioso           | Rename Variable, RenameField                                        |
| L-07   | `ShoppingCart.handleOffers` (linha 44-45/55-56) | processamento da oferta THREE_FOR_TWO FIVE_FOR_AMOUNT de difícil leitura, aparentam não estar implementadas                                                                                  | Defeito Pontencial        | Extract Method, Verificar funcionalidade                            |
| L-08   | `ShoppingCart.handleOffers` (linha 50)          | atributo com nome argument é usado para determinar o total do valor dos produtos                                                                                                             | Nome Misterioso           | Rename Variable, RenameField                                        |
| L-09   | `ShoppingCart.handleOffers` (linha 50)          | `quantityAsInt` / `x`: nomes de atributos não deixam claro o que representam                                                                                                                 | Nome Misterioso           | Rename Variable, RenameField                                        |
| L-10   | `ShoppingCart.handleOffers` (linha 52)          | pelo modo de concatenação percebe-se que o atributo argument parece ser um valor fixo passado na oferta                                                                                      | Nome Misterioso           | Rename Variable, RenameField                                        |
| L-11   | `ShoppingCart.handleOffers` (linha 52)          | Atributo com nome genérico numberOfXs                                                                                                                                                        | Nome Misterioso           | Rename Variable, RenameField                                        |
| L-12   | `ShoppingCart.handleOffers` (linha 70)          | Se o desconto não for nulo a nota fiscal recebe a informação sobre o desconto se não ela recebe um objeto nulo?                                                                              | Defeito Potencial         | Verificar                                                           |
| L-13   | `Product` e `ProductQuantity`                   | Não parece fazer sentido esses dois records para a mesma coisa                                                                                                                               | Data Class                | Move Method                                                         |
| L-14   | `Receipt` (linha 9)                             | Porque a classe de objetos que representa os produtos na nota fiscal se chama ReceiptItem ao invés de Product?                                                                               | Data Class                | Move Method                                                         |
| L-15   | `Receipt.getTotalPrice()` (linha 12-15)         | Porque o preço total é o resultado da soma de todos os produtos “mais” a soma de todos os descontos? O desconto é um valor negativo?                                                         | Defeito Pontencial        | Erro de lógica? Resultado pelo teste é correto.                     |
| L-16   | `Receipt.addProduct()` (linha 17-19)            | O método recebe muitos atributos que são convertidos para criar um novo objeto da classe não seria mais fácil receber um ReceitpItem?                                                        | Long Parameter Listetros  | Verificar                                                           |
| L-17   | `ReceiptItem`                                   | importe desnecessário                                                                                                                                                                        | Long Parameter List       | Verificar                                                           |
| L-18   | `Teller.addSpecialOffer` (linhas 16-17)         | A caixa tem que receber a informação de que existe uma oferta de um produto, mas isso não deveria ser parte do catálogo, porque assim cada caixa tem que receber todas as ofertas uma a uma. | Large Class               | Extract Class                                                       |

**Atualização dos achados (código da `main` no commit `9aa8f7e`, após os testes de caracterização):**

| ID   | Situação | Observação |
| ---- | -------- | ---------- |
| L-01 | Resolvido | O campo `argument` foi renomeado para `fixedValueOrQuantity` em `Offer`. |
| L-07 | Reclassificado | Os testes de caracterização provam que `THREE_FOR_TWO` e `FIVE_FOR_AMOUNT` **estão implementadas** e funcionam (3 por 2 com 4 e 6 itens; 5 por valor com 5, 6 e 10 itens). O problema real é de legibilidade: o cálculo está espalhado em blocos `if` separados que testam o mesmo tipo de oferta mais de uma vez. |
| L-13 | Pendente | `Product` e `ProductQuantity` continuam como dois records na `main`. |
| L-03 | Parcial | O dinheiro em `double` foi trocado pelo tipo `Money` (PR #30); a quantidade em `double` continua pendente (Grupo 2). |
| L-16 | Resolvido (Grupo 3) | `Receipt.addProduct` passou a receber três parâmetros, sem o total, que agora é calculado. |
| L-17 | Resolvido (Grupo 3) | O import sem uso de `ReceiptItem` foi removido. |

**Novos achados da revisão com testes de caracterização:**

| ID   | Local | Achado | Categoria | Refatoração sugerida |
| ---- | ----- | ------ | --------- | -------------------- |
| L-19 | `ShoppingCart.handleOffers` | O bloco de `FIVE_FOR_AMOUNT` começa com `} if (` logo após o `else if` anterior, sem `else`: a estrutura sugere uma cadeia exclusiva, mas os blocos seguintes são `if` independentes. | Defeito Potencial / Legibilidade | Decompose Conditional, Replace Conditional with Polymorphism |
| L-20 | `ShoppingCart.handleOffers` | O método lê dados de `Offer`, consulta o preço no catálogo e altera `Receipt` (`addDiscount`): usa mais dados de outras classes do que da própria. | Feature Envy | Move Method |
| L-21 | `handleOffers` (`x`, `% 2`, `% 3`, `% 5`) | Os números 2, 3 e 5 aparecem repetidos, ora na variável `x`, ora fixos nas contas de resto, e podem ficar fora de sincronia. | Números Mágicos / Duplicação | Replace Magic Literal, Extract Variable |
| L-22 | `Receipt.getDiscounts()` | Devolve a lista interna mutável, ao contrário de `getItems()`, que devolve uma visão imutável. | Encapsulamento fraco | Encapsulate Collection |
| L-23 | `ReceiptPrinter` | A classe de apresentação da nota está em `src/test`, fora do código de produção. | Estrutura do projeto | Move Class |
| L-24 | `THREE_FOR_TWO` / `Offer.fixedValueOrQuantity` | O valor da oferta é ignorado no 3 por 2, mas é obrigatório informar algum (o teste original passa o preço unitário). A API é ambígua. | Nome Misterioso / API enganosa | Introduce Parameter Object, Replace Parameter with Explicit Methods |
| L-25 | `SupermarketCatalog.getUnitPrice` | O contrato não define o que acontece com produto inexistente (o `FakeCatalog` lança `NullPointerException` por unboxing de `null`). | Tratamento de erro ausente | Introduce Special Case, lançar exceção de domínio |
| L-26 | `java/bin/pom.xml` | Arquivo gerado duplicado versionado no repositório. | Lixo no repositório | Remove Dead Code |

#### 3.2 Recuperação de arquitetura por modelos

Foi utilizado o recurso de exibição de diagramas de classes no Intellij para representar todas as classes, seus atributos e métodos.

![Diagrama de classes do sistema legado](supermarket.png)

**Análise do diagrama e módulos identificados.** O sistema tem **uma única camada física**: todas as 11 classes de produção (202 linhas) ficam no pacote `dojo.supermarket.model`, mais a classe de impressão (`ReceiptPrinter`, 79 linhas) em `src/test`. Do ponto de vista lógico, é possível identificar cinco componentes:

| Componente | Classes | Papel |
| ---------- | ------- | ----- |
| Dados de domínio | `Product`, `ProductUnit`, `ProductQuantity`, `Offer`, `SpecialOfferType`, `Discount`, `ReceiptItem` | Records e enums sem comportamento. |
| Contrato de catálogo | `SupermarketCatalog` | Única interface do sistema; a implementação de produção não existe. |
| Orquestração | `Teller` | Guarda as ofertas, usa o catálogo e produz a nota. |
| Regras de promoção | `ShoppingCart.handleOffers` | Concentra todo o cálculo de descontos dentro do carrinho. |
| Nota fiscal e apresentação | `Receipt`, `ReceiptPrinter` | Acumula itens e descontos; formata o texto de saída. |

Dependências observadas (seta = "depende de"):

```
Teller ──► ShoppingCart ──► Receipt ──► ReceiptItem, Discount
  │             │  └──────► Offer, SpecialOfferType, Discount, ProductQuantity
  └──► SupermarketCatalog ◄─┘
ReceiptPrinter ──► Receipt, ReceiptItem, Discount, ProductUnit
```

A leitura do diagrama mostra que `ShoppingCart` é o ponto de acoplamento do sistema: depende de sete tipos (`Product`, `ProductQuantity`, `Receipt`, `Offer`, `Discount`, `SpecialOfferType` e `SupermarketCatalog`) e é a única classe que decide regras de negócio. O `Teller` também depende de sete tipos, mas apenas repassa dados. As demais classes são folhas que não dependem de ninguém (exceto `Receipt`, `ReceiptItem` e `Discount`, que se referem a `Product`). Não há dependências circulares.



#### 3.3 Cartões CRC (Class-Responsibility-Collaborator)

Os cartões CRC foram criados por Kent Beck e Ward Cunningham em 1989 e servem para complementar os diagramas de UML. Cada cartão mostra o que a classe faz e não apenas suas estruturas de atributos e métodos assim como as interações entre elas. Cada classe deve ser representada por um cartão com três campos: o nome, as responsabilidades (o que ela sabe e faz) e os colaboradores (as classes de que precisa para cumprir essas responsabilidades).

Embora a técnica tenha sido criada para projetar sistemas, ela pode ser usada no sentido inverso para representar o estado atual do código, auxiliando na compreensão de sua estrutura e expondo acoplamentos e comportamentos diversos do que os esperados para cada classe.

Ao olhar para cada cartão você enxerga visualmente o problema arquitetural sem precisar ler o código: ShoppingCart tem colaboradores demais e responsabilidades visivelmente misturadas, enquanto Offer não tem responsabilidade nenhuma.

| **Teller - Responsabilidades**                                        | **Colaboradores**                         |
| --------------------------------------------------------------------- | ----------------------------------------- |
| Armazena ofertas especiais cadastradas                                | Offer, Product                            |
| Armazena o catálogo de produtos do supermercado                       | SupermarketCatalog                        |
| Orquestra o processo de checkout de um carrinho e gera um nota fiscal | SupermarketCatalog, ShoppingCart, Receipt |

| **Discount - Responsabilidades** | **Colaboradores** |
| -------------------------------- | ----------------- |
| Estrutura dados de descontos     | Product           |

| **Offer - Responsabilidades** | **Colaboradores**         |
| ----------------------------- | ------------------------- |
| Estrutura dados de ofertas    | SpecialOfferType, Product |

| **Product - Responsabilidades** | **Colaboradores** |
| ------------------------------- | ----------------- |
| Estrutura dados de produtos     | ProductUnit       |

| **ProductQuantity - Responsabilidades**   | **Colaboradores** |
| ----------------------------------------- | ----------------- |
| Estrutura dados de quantidade de produtos | Product           |

| **ProductUnit - Responsabilidades**      | **Colaboradores** |
| ---------------------------------------- | ----------------- |
| Classifica dados de unidades de produtos | Nenhum            |

| **Receipt - Responsabilidades**                                            | **Colaboradores**     |
| -------------------------------------------------------------------------- | --------------------- |
| Armazena lista de descontos                                                | Discount              |
| Armazena lista de produtos da nota fiscal                                  | ReceiptItem           |
| Calcula a soma de preços de produtos e descontos e retorna o total da nota | ReceiptItem, Discount |

| **ReceiptItem - Responsabilidades**           | **Colaboradores** |
| --------------------------------------------- | ----------------- |
| Estrutura os dados de produtos da nota fiscal | Product           |

| **ShoppingCart - Responsabilidades**                   | **Colaboradores**                                                |
| ------------------------------------------------------ | ---------------------------------------------------------------- |
| Armazena lista com quantidade de produtos              | ProductQuantity                                                  |
| Armazena lista de produtos e suas quantidades          | Product                                                          |
| Possui a regra de negócio para a aplicação das ofertas | Receipt, SupermarketCatalog, SpecialOfferType, Discount, Product |

| **SpecialOfferType - Responsabilidades** | **Colaboradores** |
| ---------------------------------------- | ----------------- |
| Classifica tipos de ofertas              | Nenhum            |

| **SupermarketCatalog - Responsabilidades**                    | **Colaboradores** |
| ------------------------------------------------------------- | ----------------- |
| Define a interface para uma estrutura de catálogo de produtos | Nenhum            |

| **ReceiptPrinter - Responsabilidades** | **Colaboradores**                           |
| -------------------------------------- | ------------------------------------------- |
| Imprime a nota fiscal                  | Receipt, Discount, ReceiptItem, ProductUnit |
| Imprime um produto                     | ReceiptItem                                 |
| Imprime o desconto                     | Discount                                    |
| Imprime o total da nota                | Receipt                                     |

### Relatório de Débito Técnico

O diagnóstico abaixo consolida os achados L-01 a L-26 nas cinco dimensões exigidas. As métricas foram obtidas por **contagem manual** sobre o código da `main`, o que é adequado ao tamanho do sistema (cerca de 280 linhas); ferramentas como SonarQube, PMD ou CK poderão ser usadas na Etapa 3 para comparar o antes e o depois.

#### Acoplamento

- `ShoppingCart` depende de 7 tipos e é o único lugar com regra de negócio; `Teller` depende de 7 tipos (L-20).
- O carrinho altera diretamente a nota fiscal (`receipt.addDiscount`) e consulta o catálogo: *Feature Envy* e violação do princípio da responsabilidade única.
- `Teller` recebe as ofertas, uma a uma, em cada caixa, e as repassa ao carrinho a cada checkout (L-18).
- Não há dependências circulares, mas também não há abstrações entre as classes: tudo é concreto, exceto o catálogo.

#### Duplicação

- O mesmo tipo de oferta é testado em mais de um `if` do `handleOffers`: `THREE_FOR_TWO` e `FIVE_FOR_AMOUNT` aparecem duas vezes cada (L-07).
- A fórmula de desconto `unitPrice * quantity - total` e a criação de `Discount` com sinal negativo se repetem em quatro blocos.
- As constantes 2, 3 e 5 aparecem como variável `x` e como literais nas contas de resto (L-21).
- Nos testes originais, os seis cenários repetem o mesmo preparo (catálogo, produto, caixa, carrinho). Os novos testes de caracterização usam um `setUp` comum para reduzir isso.

#### Complexidade ciclomática

Contagem manual de V(G) = pontos de decisão + 1:

| Método | Decisões | V(G) | Avaliação |
| ------ | -------- | ---- | --------- |
| `ShoppingCart.handleOffers` | 12 (laço, `containsKey`, 6 testes do tipo de oferta, 3 limites mínimos de quantidade, teste de desconto nulo; cada `&&` conta como uma decisão) | **13** | Alta (acima de 10) |
| `ReceiptPrinter.printReceipt` | 2 laços | 3 | Baixa |
| `ShoppingCart.addItemQuantity` | 1 | 2 | Baixa |
| `Teller.addItensToReceipt` | 1 laço | 2 | Baixa |
| `ReceiptPrinter.presentReceiptItem` e `presentQuantity` | 1 cada | 2 | Baixa |
| Demais métodos | 0 | 1 | Baixa |

Um único método concentra a complexidade do sistema. Cada nova oferta (como o *bundle* pedido no kata) acrescenta mais ramos a ele, o que faz V(G) crescer a cada evolução.

#### Falta de documentação

- Nenhum Javadoc nas classes de produção (`src/main`); os únicos comentários `/**` estão em arquivos de teste.
- Não existe documento de arquitetura nem de regras de negócio; as regras das ofertas só podem ser descobertas lendo `handleOffers`.
- Nomes em inglês com documentação em português, e nomes pouco expressivos (`x`, `numberOfXs`, `quantityAsInt`, `fixedValueOrQuantity`) (L-06, L-09, L-11, L-24).
- Os testes de caracterização escritos nesta etapa passam a ser a única documentação executável do comportamento.

#### Manutenibilidade

- **Extensão:** acrescentar um tipo de oferta exige alterar o enum e editar `handleOffers` (viola o princípio aberto/fechado).
- **Precisão numérica:** valores monetários em `double` (L-03), com erros de arredondamento que obrigam a usar tolerância (`DELTA`) nos testes.
- **Quantidade:** um único `double` representa unidades e peso, com *cast* para `int` dentro das regras (L-05).
- **Encapsulamento:** classes anêmicas e coleção mutável exposta (L-13, L-22).
- **Estrutura:** pacote único e classe de apresentação em `src/test` (L-23).
- **Rede de segurança:** o legado começou com um único teste efetivo; agora há 37 testes (6 originais e 31 de caracterização) cobrindo os quatro tipos de oferta, o carrinho, a nota e a impressão. Esse conjunto reduz o risco da refatoração (FEATHERS, 2013), embora a cobertura percentual ainda não tenha sido medida.

#### Síntese e priorização

| Prioridade | Débito | Achados | Impacto | Ação planejada (Etapa 3) |
| ---------- | ------ | ------- | ------- | ------------------------ |
| Alta | Cadeia condicional e regra de ofertas dentro do carrinho | L-04, L-07, L-19, L-20, L-21 | Complexidade 13, custo de evolução alto | Extrair uma classe por tipo de oferta (Strategy) |
| Alta | Dinheiro em `double` | L-03, L-05 | Erros de arredondamento | Introduzir tipo `Money` / `BigDecimal` |
| Média | Classes anêmicas e nomes confusos | L-06, L-09, L-11, L-13, L-14, L-24 | Legibilidade | Unificar records, renomear campos |
| Média | Encapsulamento e estrutura de pacotes | L-18, L-22, L-23 | Acoplamento | Mover `ReceiptPrinter`, separar pacotes |
| Baixa | Documentação e limpeza | L-16, L-17, L-25, L-26 | Manutenção | Javadoc, remover arquivos sobrando |

## Etapa 3: Refatoração Prática e Evolução Aplicada (Obrigatória) (Peso: 3,0 pontos)

- **Entregáveis:**
  - **Execução Obrigatória da Refatoração:** Repositório refatorado funcional com contribuições registradas de todos os membros no GitHub.
  - Plano de atualização tecnológica e estratégias para evolução futura (ex: quebra de monolito, migração de banco, etc.).
  - Plano de modularização e melhorias de legibilidade/complexidade.
  - **Evidências de Código:** Trechos de código comparativos no padrão **"Antes" vs. "Depois"** com a devida explicação técnica das melhorias e testes automatizados.

### Grupo 1: Ofertas e caixa (Manuel Bahamonde Boulhosa Neto)

**Escopo:** `Teller`, `Offer`, `SpecialOfferType` e a lógica de promoções que estava dentro de `ShoppingCart.handleOffers`. O trabalho foi integrado à `main` pelos PRs #9, #13, #14, #16 e #30 (este último, o `Money`, compartilhado com os demais grupos). Ao final, a suíte do projeto tem **110 testes, todos passando**, com **99,0% de cobertura de linhas e 100% de branches** (JaCoCo).

**Método:** cada passo foi feito em um commit próprio, com a suíte verde antes e depois. Antes de mexer no código, foram escritos testes de caracterização para registrar o comportamento do legado (FEATHERS, 2013). A complexidade ciclomática foi contada manualmente, e a cobertura medida com JaCoCo, adicionado ao `pom.xml` para ter a linha de base antes da refatoração (98,9% das linhas e 100% dos branches).

#### Testes de caracterização adicionados

| Commit | Teste | Comportamento registrado |
| ------ | ----- | ------------------------ |
| `1a83388` | `OfferCharacterizationTest` (16 testes) | Cada oferta abaixo do mínimo (sem desconto), com múltiplos do grupo (3 por 2 com 4 e 6 itens; 2 por valor com 4; 5 por valor com 4 e 10), percentual em produto por peso, quantidades somadas quando o mesmo produto entra em linhas separadas, última oferta cadastrada vence, vários produtos com ofertas diferentes. |
| `73f11e6` | `ReceiptTest` | O total é a soma dos itens mais os descontos (valores negativos); lista de itens imutável. |
| `2529418` | `ShoppingCartTest` | Acúmulo de quantidade por produto mantendo cada linha; coleções imutáveis. |
| `eb49fa5` | `ReceiptPrinterTest` | Formatação da nota: item unitário, quantidade inteira e em quilos, linha de desconto, nome maior que a coluna. |

#### Refatorações aplicadas

| Commit | Refatoração (referência) | Problema no legado | Resultado |
| ------ | ------------------------ | ------------------ | --------- |
| `e281d76`, `b339e07`, `85e77fc` | *Replace Conditional with Polymorphism* (FOWLER, 2020) e padrão *Strategy* (GAMMA et al., 2000) | `handleOffers` concentrava todas as regras em uma cadeia de `if`, com complexidade ciclomática 13 e o mesmo tipo de oferta testado mais de uma vez (L-04, L-07, L-19, L-21). | Interface `OfferStrategy` e três implementações (`PercentDiscountOffer`, `ThreeForTwoOffer`, `QuantityForAmountOffer`, esta atende 2 por valor e 5 por valor). Maior complexidade: 4. |
| `856131a` | Fábrica | A API pública usava `SpecialOfferType` mais um número solto. | `OfferStrategies.from(tipo, valor)`: `Teller.addSpecialOffer(tipo, produto, valor)` não mudou. |
| `67f0f48`, `d9e8c66` | *Extract Class* e *Move Function* (FOWLER, 2020) | O `Teller` guardava as ofertas (L-18) e o carrinho manipulava a nota fiscal (*Feature Envy*, L-20). | `SpecialOffers` guarda e aplica as ofertas; o `Teller` só delega. |
| `526cae2` | *Remove Dead Code* (FOWLER, 2020) | `handleOffers` e o record `Offer`, anêmico, ficaram sem uso. | Removidos; o `ShoppingCart` passou a depender de 2 tipos em vez de 7. |
| `1e32c45` | Teste de extensão | Provar que ofertas novas não exigem alterar as classes existentes. | `Teller.addSpecialOffer(produto, estrategia)` aceita uma lambda. |
| `5c173f4`, `0eb12b5`, `abf6d57` | *Funcionalidade nova do kata* | Oferta de pacote (*bundle*) inexistente. | `BundleOffer`: desconto percentual só sobre pacotes completos; soma-se às ofertas por produto. |
| `2880220`, `4ccf920` | *Replace Primitive with Object* (FOWLER, 2020) | Valores monetários em `double` em todo o domínio (obsessão por primitivos, L-03 em parte). | Tipo `Money` (`BigDecimal`, duas casas, `HALF_UP`) usado por catálogo, ofertas, descontos, itens e total. Mudança compartilhada com os Grupos 2 e 3. |

#### Evidências "Antes" vs. "Depois"

**1. Regra da oferta "3 por 2"**

Antes (`ShoppingCart.handleOffers`, trecho):

```java
int x = 1;
if (offer.offerType() == SpecialOfferType.THREE_FOR_TWO) {
    x = 3;
} else if (offer.offerType() == SpecialOfferType.TWO_FOR_AMOUNT) {
    x = 2;
    if (quantityAsInt >= 2) { /* ... calcula e cria o Discount ... */ }
} if (offer.offerType() == SpecialOfferType.FIVE_FOR_AMOUNT) {
    x = 5;
}
int numberOfXs = quantityAsInt / x;
if (offer.offerType() == SpecialOfferType.THREE_FOR_TWO && quantityAsInt > 2) {
    double discountAmount = quantity * unitPrice
        - ((numberOfXs * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
    discount = new Discount(p, "3 for 2", -discountAmount);
}
```

Depois (`ThreeForTwoOffer`):

```java
public record ThreeForTwoOffer() implements OfferStrategy {
    private static final int GROUP_SIZE = 3;
    private static final int PAID_PER_GROUP = 2;

    @Override
    public Optional<Discount> discountFor(Product product, double quantity, Money unitPrice) {
        int units = (int) quantity;
        if (units < GROUP_SIZE) {
            return Optional.empty();
        }
        int completeGroups = units / GROUP_SIZE;
        Money fullPrice = unitPrice.times(quantity);
        Money payable = unitPrice.times(completeGroups * PAID_PER_GROUP + units % GROUP_SIZE);
        return Optional.of(new Discount(product, "3 for 2", payable.minus(fullPrice)));
    }
}
```

Melhorias: sem variáveis `x` e `numberOfXs`, constantes nomeadas, cada oferta em sua classe, dinheiro em `Money`. Cada estratégia tem complexidade 1 ou 2 e é testada isoladamente.

**2. Papel do `Teller` e do carrinho**

Antes:

```java
private final Map<Product, Offer> offers = new HashMap<>();
...
theCart.handleOffers(receipt, offers, catalog);   // o carrinho altera a nota
```

Depois:

```java
private final SpecialOffers offers = new SpecialOffers();
...
offers.applyTo(theCart.productQuantities(), catalog, receipt);
```

**3. Dinheiro**

Antes: `double unitPrice`, `double discountAmount`, com `DELTA = 0.001` nos testes para tolerar o erro de arredondamento binário.

Depois: `Money unitPrice`, `Money discountAmount`, comparados com `assertEquals(Money.of("-0.99"), discount.discountAmount())`, sem tolerância.

**4. Extensibilidade (nova oferta sem editar código existente)**

```java
OfferStrategy oneOffFromTwoUnits = (product, quantity, unitPrice) ->
        quantity >= 2 ? Optional.of(new Discount(product, "1.00 off", Money.of("-1.00"))) : Optional.empty();
teller.addSpecialOffer(rice, oneOffFromTwoUnits);
```

#### Resultados medidos

| Indicador | Antes | Depois |
| --------- | ----- | ------ |
| Maior complexidade ciclomática de um método | 13 (`handleOffers`) | 4 (`OfferStrategies.from`) |
| Cadeias condicionais com regra de oferta | 1 | 0 |
| Dependências de `ShoppingCart` | 7 | 2 |
| Dependências de `Teller` | 7 | 11 (**piorou**: delega por `SpecialOffers`, `OfferStrategies` e `OfferStrategy`, e passou a usar `BundleOffer` e `Money`; a responsabilidade diminuiu, mas a contagem de tipos aumentou) |
| Testes (projeto inteiro) | 6 | 110 |
| Cobertura de linhas / branches | 98,9% / 100% (linha de base) | 99,0% / 100% |

#### Mudança de comportamento intencional

O `Money` arredonda ao centavo (`HALF_UP`), então alguns valores mudaram, e os testes registram isso: 10% de uma escova de 0,99 passou de 0,099 para **0,10**; o desconto de pacote de 10% sobre 2,78 passou de 0,278 para **0,28**; e as descrições mostram duas casas (`2 for 1.50`, antes `2 for 1.5`). O total da nota continua sendo a soma de valores já em centavos, sem diferença entre o que a linha mostra e o que é somado.

#### Situação dos achados do Grupo 1

| Achado | Situação |
| ------ | -------- |
| L-01 | Resolvido: campo `argument` renomeado e depois eliminado com o record `Offer`. |
| L-03 | **Parcial:** dinheiro resolvido pelo `Money`; a quantidade em `double` continua pendente (Grupo 2). |
| L-04, L-07, L-19, L-20, L-21 | Resolvidos pelo Strategy. |
| L-18 | Resolvido: `SpecialOffers` extraído do `Teller`. |
| L-24 | Parcial: o argumento ignorado do "3 por 2" continua na API pública `addSpecialOffer(tipo, produto, valor)`, por compatibilidade. |
| L-25 | Pendente: o contrato do catálogo não define o que acontece com produto inexistente. |
| L-26 | Pendente: `java/bin/pom.xml` continua versionado. |

#### Pendências e evolução futura

- Dividir o código em pacotes (`offer`, `checkout`): adiado porque criaria dependência circular enquanto `Teller` e `ShoppingCart` estiverem no mesmo pacote que as estratégias.
- O desconto do pacote aparece na nota com o nome do primeiro produto, porque `Discount` guarda um único produto.
- `Teller.addSpecialOffer(tipo, produto, double)` ainda recebe o valor da oferta como `double`; ele converte para `Money` internamente.
- Javadoc em `Teller` e `SpecialOfferType`, análise estática (Checkstyle ou PMD) e Maven Wrapper (planos do Grupo 1).

### Grupo 3: Nota fiscal e impressão (Sérgio Lopes Júnior)

**Escopo:** `Receipt`, `ReceiptItem`, `Discount`, `ReceiptPrinter` e `SupermarketCatalog`/`FakeCatalog`. O trabalho foi integrado à `main` pelos PRs #18 a #22, em 23/11. Ao final, a suíte tem **80 testes, todos passando**.

**Método:** cada refatoração foi feita em um commit próprio, com a suíte verde antes e depois. Antes de alterar o `ReceiptPrinter`, foram escritos testes de caracterização para registrar o comportamento atual, inclusive os defeitos do legado (FEATHERS, 2013).

#### Testes de caracterização adicionados

| Commit | Teste | Comportamento registrado |
| ------ | ----- | ------------------------ |
| `6df4392` | `printsSingleReceiptItemWithoutDiscountWithColumnArgument` | Impressão com largura de colunas informada no construtor. |
| `6df4392` | `printsSingleReceiptItemWithoutDiscountDefautlColumnArgument` | Construtor padrão usa 40 colunas (protege a troca do número mágico `40` por constante). |
| `6df4392` | `printsOneKiloItemHasNoMultiplicationLine` | Item com quantidade exatamente 1 não imprime a linha `preço * quantidade`. |
| `5c8abf6` / `ae7cdbc` | `printsLessThanOneKilo` | Quantidade menor que 1 (0,5 kg) **imprime** a linha de quantidade. |
| `ae7cdbc` | `printsProductDiscreteWithFractionQuantityBugInQuantityLine` | **Defeito legado registrado:** produto `EACH` com quantidade fracionada (2,5) imprime `0.99 * 2`, mas cobra 2,48. |
| `81b2dbb` | `discountsListIsUnmodifiable` | A lista de descontos da nota não pode ser alterada por quem a consulta. |

#### Refatorações aplicadas

| Commit | Refatoração (referência) | Problema no legado | Resultado |
| ------ | ------------------------ | ------------------ | --------- |
| `8a73523` | *Move Class* (FOWLER, 2020) | `ReceiptPrinter` era código de produção dentro de `src/test` (L-23). | Classe em `src/main/java/dojo/supermarket/model`; o teste acompanha o pacote. |
| `f388a85` | *Inline Variable* (FOWLER, 2020) | Variáveis temporárias em `printReceipt` só repassavam o valor. | `result.append(presentReceiptItem(item))` direto. |
| `2be4b82`, `8357b04` | *Replace Magic Literal* (MARTIN, 2011, G25) | Literais `40`, `"Total: "`, `"%.2f"`, `"%.3f"` e `"%d"` espalhados. | Constantes `DEFAULT_COLUMNS_WIDTH`, `TOTAL_LABEL`, `PRICE_FORMAT`, `WEIGHT_FORMAT` e `DISCRETE_UNIT_FORMAT`. O método de alinhamento foi renomeado para `formatLineLeftAndRightAlignment` (*Rename Function*). |
| `d7621e2` | Consistência de construção de texto | `presentReceiptItem` concatenava `String` com `+=`, enquanto o resto da classe usava `StringBuilder`. | `StringBuilder` em toda a classe. |
| `87bf455`, `c87c217` | *Decompose Conditional* (FOWLER, 2020) | Condição `item.quantity() != 1` sem nome de intenção. | Método `hasQuantityLine(item)`. |
| `f47f832` | *Move Function* para o dado (FOWLER, 2020) | O printer comparava `ProductUnit.EACH.equals(...)` para decidir o formato da quantidade. | Usa `unit().isDiscrete()`: a regra fica no `ProductUnit`. |
| `ea052bf`, `5b7296e` | *Replace Derived Variable with Query* (FOWLER, 2020) | `ReceiptItem` guardava `totalPrice`, que é sempre `quantity * price`; `Receipt.addProduct` recebia quatro parâmetros (L-16). | `ReceiptItem(product, quantity, price)` com o método `totalPrice()`; `addProduct` com três parâmetros. O `import java.util.Objects` sem uso saiu (L-17). |
| `81b2dbb` | *Encapsulate Collection* (FOWLER, 2020) | `getDiscounts()` devolvia a lista interna mutável (L-22). | Devolve `Collections.unmodifiableList`, como `getItems()`. |
| `5b7296e` | Limpeza de comentários (MARTIN, 2011, cap. 4) | Código comentado e comentários desatualizados nos testes. | Comentários removidos. |

#### Evidências "Antes" vs. "Depois"

**1. Linha de quantidade e formato por tipo de unidade (`ReceiptPrinter`)**

Antes:

```java
String line = formatLineWithWhitespace(name, totalPricePresentation);
if (item.quantity() != 1) {
    line += "  " + presentPrice(item.price()) + " * " + presentQuantity(item) + "\n";
}
...
return ProductUnit.EACH.equals(item.product().unit())
        ? String.format("%d", (int)item.quantity())
        : String.format(Locale.UK, "%.3f", item.quantity());
```

Depois:

```java
StringBuilder line = new StringBuilder(formatLineLeftAndRightAlignment(name, totalPricePresentation));
if (hasQuantityLine(item)) {
    line.append("  " + presentPrice(item.price()) + " * " + presentQuantity(item) + "\n");
}
...
private boolean hasQuantityLine(ReceiptItem item) {
    return item.quantity() != 1;
}
...
return item.product().unit().isDiscrete()
        ? String.format(DISCRETE_UNIT_FORMAT, (int) item.quantity())
        : String.format(Locale.UK, WEIGHT_FORMAT, item.quantity());
```

A condição ganhou nome de intenção, os formatos viraram constantes e a decisão "quantidade inteira ou fracionada" passou a ser do `ProductUnit`.

**2. Total do item calculado, não informado (`ReceiptItem` e `Receipt`)**

Antes:

```java
public record ReceiptItem(Product product, double quantity, double price, double totalPrice) { }

public void addProduct(Product p, double quantity, double price, double totalPrice) {
    items.add(new ReceiptItem(p, quantity, price, totalPrice));
}
```

Depois:

```java
public record ReceiptItem(Product product, double quantity, double price) {
    public double totalPrice() {
        return this.price * quantity;
    }
}

public void addProduct(Product p, double quantity, double price) {
    items.add(new ReceiptItem(p, quantity, price));
}
```

O total deixa de ser um dado que pode ficar incoerente com a quantidade e o preço.

**3. Coleção de descontos encapsulada (`Receipt`)**

Antes: `return discounts;`. Qualquer código podia apagar os descontos da nota com `receipt.getDiscounts().clear()`.

Depois: `return Collections.unmodifiableList(discounts);`. A tentativa de alteração lança `UnsupportedOperationException`, coberta pelo teste `discountsListIsUnmodifiable`.

#### Defeito introduzido e corrigido durante a refatoração

No commit `87bf455`, a condição `item.quantity() != 1` foi substituída por `item.quantity() > 1`. As duas não são equivalentes: quantidades menores que 1 (por exemplo, 0,5 kg) deixaram de imprimir a linha de quantidade. A suíte continuou verde porque nenhum teste usava quantidade abaixo de 1. O defeito foi encontrado na revisão, o teste `printsLessThanOneKilo` foi escrito para reproduzi-lo (`5c8abf6`) e a condição original foi restaurada (`c87c217`). O episódio mostra que uma suíte verde só protege o que ela cobre: os casos-limite precisam ser testados antes de alterar uma condição.

#### Mudança de comportamento intencional

Ao derivar `totalPrice` (`ea052bf`), três testes do printer tiveram os valores esperados alterados (por exemplo, de 2,50 para 3,13). Os testes antigos informavam um total incoerente com a quantidade e o preço (2,5 × 1,25 = 3,125, mas o total informado era 2,50), e a `Receipt` aceitava o valor sem conferir. Agora o total é sempre calculado. Isso é uma mudança de comportamento, não uma refatoração pura, e está registrada aqui por esse motivo.

#### Situação dos achados do Grupo 3

| ID | Situação | Observação |
| -- | -------- | ---------- |
| L-16 | Resolvido | `Receipt.addProduct` recebe três parâmetros; o total é derivado. |
| L-17 | Resolvido | `ReceiptItem` não importa mais `java.util.Objects`. |
| L-22 | Resolvido | `getDiscounts()` devolve visão imutável. |
| L-23 | Resolvido | `ReceiptPrinter` está em `src/main`. |
| L-25 | Pendente | `FakeCatalog.getUnitPrice` ainda lança `NullPointerException` para produto fora do catálogo; o mapa `products` é preenchido e nunca lido. |

#### Pendências e evolução futura

- **Arredondamento por linha (L-03):** cada linha é arredondada separadamente para duas casas. Com um item de 1,00 e um desconto de −0,995, a nota imprime `1.00`, `-1.00` e `Total: 0.01`: as linhas não somam o total. A correção depende de um tipo `Money` com regra de arredondamento única, previsto no plano de atualização tecnológica.
- **Quantidade fracionada em produto `EACH`:** defeito legado registrado por teste e mantido de propósito durante a refatoração; a correção (validar a quantidade ou mudar o formato) deve ser decidida pela equipe e feita em commit `fix` separado.
- **Novos formatos de saída:** a interface `ReceiptFormatter` (*Extract Interface*, padrão *Strategy*), que permitirá uma saída em HTML sem alterar o printer de texto, está no branch `feat/ReceiptInterface` e ainda não foi integrada à `main`.

## Etapa 4: Relatório Técnico Formal (Peso: 2,0 pontos)

- **Diretrizes Estritas:**
  1. Formatação seguindo as normas da ABNT.
  2. Extensão mínima de **8 páginas** com linguagem técnica, formal e objetiva.
- **Estrutura Obrigatória:**
  1. Capa e Introdução
  2. Fundamentação Teórica (Conceitos de evolução, refatoração, etc.)
  3. Desenvolvimento (Diagnóstico do sistema + Refatoração aplicada)
  4. Conclusão e Referências

## Etapa 5: Apresentação Final e Demonstração (Peso: 1,0 ponto)

- **Formato:** Presencial.
- **Tempo Limite:** Entre 10 e 15 minutos.
- **Requisito Obrigatório:** Demonstração prática do código refatorado rodando em comparação com a versão legada inicial (este pode ser em video)

## 4. Matriz de Avaliação (Resumo)

A nota final será calculada estritamente com base nos seguintes critérios:

| **Critério de Avaliação**          | **Foco Principal**                                                                                      | **Pontuação Máxima** |
| ---------------------------------- | ------------------------------------------------------------------------------------------------------- | -------------------- |
| Escolha e Análise do Sistema       | Contextualização e Justificativa do Legado                                                              | 1,0                  |
| Engenharia Reversa e Diagnóstico   | Identificação de Problemas Estruturais - Analise de requisitos do sistema legado                        | 2,0                  |
| Refatoração Obrigatória e Evolução | Qualidade, Execução Prática e Registro de Contribuição no GitHub por todos os membros                   | 3,0                  |
| Viabilidade e Análise Crítica      | Visão de Negócio, Prazo, Riscos                                                                         | 1,0                  |
| Relatório Técnico                  | Rigor Acadêmico (ABNT), escrita e estrutura (Mín. 8 Páginas) - Mostrando o que foi alterado e melhorado | 2,0                  |
| Apresentação e Demonstração        | Postura, Clareza, Gestão de Tempo e Demonstração Prática                                                | 1,0                  |
| **TOTAL**                          |                                                                                                         | **10,0**             |

## Referências (sugestão)

- FOWLER, Martin. *Refatoração: Aperfeiçoando o Design de Código Existente*. 2. ed. São Paulo: Novatec Editora, 2020.
  - *Uso no projeto:* Base conceitual para a Etapa 3 (técnicas de refatoração, padronização "Antes vs. Depois" e catálogo de refatorações).
- MARTIN, Robert C. *Código Limpo: Habilidades Práticas do Agile Software*. Rio de Janeiro: Alta Books, 2011.
  - *Uso no projeto:* Identificação de *code smells*, nomenclatura, funções pequenas e boas práticas de legibilidade no desenvolvimento.
- FEATHERS, Michael. *Trabalho Eficaz com Código Legado*. Porto Alegre: Bookman, 2013.
  - *Uso no projeto:* Estratégias para cobrir código antigo com testes de regressão antes de refatorar (essencial para a Etapa 3 e 4).
- SOMMERVILLE, Ian. *Engenharia de Software*. 10. ed. São Paulo: Pearson Education do Brasil, 2019.
  - *Uso no projeto:* Fundamentação teórica da Etapa 4 sobre evolução de software, reengenharia, manutenção preventiva e débito técnico.
  - Link: Pearson Brasil
