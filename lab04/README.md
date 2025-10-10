Aula 4:

Teste do framework Springboot


3.1 - Diferenca entre @Mock e @MockBeam - mock e a simulacao normal de uma classe para que seja criada uma instancia que use metodos simples e basicos. Mockbeam tambem faz isso, no entanto identifica a como uma classe cujo ciclo de vida e controlado pelo SpringBoot.

aplication-integration-propreties sao configuracoes usadas durante um teste de integracao. ex: confs de postgres


3.3
a)
a metodologia top down vai ao encontro do tdd no sentido em que se comeca pelos testes com dependencias de outros, obrigando a usar mockups ao inves de implementar o codigo. permite tambem definir o comportamento do codigo desde o inicio.

b)
Para garantir que a funcionalidade “encontrar um carro semelhante” está correta e confiável, é importante testar todos os níveis da aplicação — repositório, 
serviço e controlador — e depois validar a integração total.
Assim, os testes principais seriam:

🔹 1. Testes de Unidade (3 níveis)

1. Repositório (Repository Layer)
Objetivo: verificar se as consultas de pesquisa por características (segmento, tipo de motor, disponibilidade, etc.) funcionam corretamente.
Testar métodos como findBySegmentAndMotorTypeAndAvailableTrue().
Utilizar uma base de dados em memória (ex: H2) e dados de teste.
Verificar se apenas os carros que correspondem aos critérios são retornados.

2. Serviço (Service Layer)
Objetivo: garantir que a lógica de negócio para encontrar um carro semelhante funciona corretamente.
Testar que o serviço obtém as características do carro original (segmento, motor, etc.) e as usa para consultar o repositório.
Aqui os repositórios podem ser mockados (com Mockito) para isolar a lógica.
Validar que o serviço lida corretamente com casos em que não há substitutos disponíveis.

3. Controlador (Controller Layer)
Objetivo: confirmar que os endpoints REST expostos funcionam como esperado.
Testar endpoints como POST /cars (criação de carros) e GET /cars/{id}/similar.
Usar MockMvc para simular requisições HTTP e verificar os códigos de estado e o formato do JSON retornado.
Garantir que a camada REST chama corretamente o serviço e retorna os resultados esperados.

🔹 2. Teste de Integração
Por fim, um teste de integração completo deve verificar o fluxo total — sem mocks:
Subir o contexto completo do Spring Boot (com banco H2 real).
Inserir alguns carros de teste via o repositório ou endpoint.
Fazer uma chamada HTTP real (por exemplo, GET /cars/{id}/similar).
Confirmar que o resultado inclui apenas carros equivalentes e disponíveis.
Este teste garante que as camadas repository → service → controller interagem corretamente e que a aplicação cumpre o requisito de negócio como um todo.


-REST-assure api - when (http get) then (assure smth) -> pode ser usado para todos os objetos ou so para um. ex:

- verifica se o objeto com id 5 tem winners 23 e 54
body("lotto.lottoId", equalTo(5),
"lotto.winners.winnerId", hasItems(23, 54)); 

- verifica se o 23 2 54 ganharam alguma lotto
body("lotto.winners.winnerId", hasItems(23, 54)); 

