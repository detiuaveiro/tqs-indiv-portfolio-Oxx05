<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-test</artifactId>
<scope>test</scope>
</dependency>

<plugin>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-maven-plugin</artifactId>
</plugin>

 <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.5.6</version>
        <relativePath/>
        <!-- lookup parent from repository -->
    </parent>
    
    
NOTAS: 

- Entidade - Informacao principal (exemplo do mealRequest) - conteudo armazenado na bd (h2 ou postsql. Guardado com o servico


- Repositorio - Métodos que são passados a querys e que vão à bd buscar e por
-- Tem restricoes como se fossem linhas de uma bd (not null, size...)

- Servico - Middle-man entre repositorio e boundary, usado para controlo e para possíveis implementaçóes de lógica

- Boundary - Indica os endpoints da app e redireciona as suas chamadas para o respetivo metodo do servico
-- PathVariable é um 'arg' no url enquanto que request body é um json no curl
-- 


3.1
- Entreprise app é uma app de larga escala, feita para muitos users.

- Frameworks conhecidas e mais usadas: Jakarta, Quarkus, SpringBoot, Micronaut, Helidon

-Spring boot usa os servlets do jakarta para correr alguns servicos como o tomcat. em relacao ao spring framework, o boot so o torna mais facil de usar

-Embedded Tomcat (or Jetty/Undertow), DataSource (HikariCP by default), JPA Repositories, Spring MVC (DispatcherServlet, RequestMappingHandlerMapping), Thymeleaf TemplateEngine and ViewResolver, Actuator endpoints (/health, /info, /metrics), Logging (Logback with console output), Cache (CacheManager for EhCache, Caffeine, Redis), Spring Security default login (auto-generated user/password), Jackson ObjectMapper for JSON serialization

-
    
    
3.2 
a
o springboot usa o tomcat internamente como servidor, uma vez que ele pussui servlets (jakarta) que permitem passar html a java e vice versa. aparece no teminal porque ele arranca autonomamente sem qque seja preciso instalar tomcat


b
a divisaon do codigo em camadas, nao so serve para uma melhor organizacao mas tambem para separacao de responsabilidades e independencia, ou seja, dado uma necessidade de alterar o codigo, ter que mexer em poucos ficheiros mas mantendo a integridade.

c
User e uma instancia que representa as informacoes da bd e e usado no userRepository como modelo dos tipos de pesquisa sql possiveis. o user controller mostra e recebe dados da bd e mostra os como sendo do tipo user e o service usa as informacoes do repository para apresentar dados

e
starters sao dependencias do spring boot que facilitam a gestao e compatibilidade das librarias. permitem muitas das funcoes, desde ferramentas para web app ate validacao.


f
TABELA
1 - spring-boot-starter-data-jpa (includes Spring Data JPA, Spring ORM)
2 - Hibernate (included transitively via spring-boot-starter-data-jpa)
3 - h2, postgresql


g 
os dados estao a ser guardados em memoria atraves da dependencia postgresql



3.4
a 
Vou usar o essquema normal de spring boot -> boundary, service e data, usando o codigo do 1.3 a comunicar com a boundary, atraves de curls para a api


@s
Entity
Interface
Service
Rest Controller 
Id
SpringBootApplication


