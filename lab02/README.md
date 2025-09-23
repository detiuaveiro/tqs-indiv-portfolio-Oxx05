# tqs-indiv-portfolio-Oxx05



alinea f - Comentarios sobre os mocks para http

foi relativamente facil de fazer a transicao, so retirei codigo e verifiquei se os valores que vinham da api batiam certo. De resto, trocar uma implementacao do http client por uma nova nao deu trabalho nenhum tambem.

Ou seja, se o cliente http fosse criado dentro do codigo do servico eu nao poderia ter trocado as implementacoes sem mudar codigo. Como esta no construtor, so tenho de mudar na parte dos testes.

alinea g - Notas sobre as diferencas entre:
$ mvn test - testes unitarios correm separadamente um a um

$ mvn package - igual ao test mas cria um jar no fim se passar (para facilitar correr e partilhar o codigo)

$ mvn package -DskipTests=true - skipa testar e so faz o jar. funciona mesmo que os testes estejam mal

$ mvn failsafe:integration-test - apenas corre testes e so de integracao. para tal o teste deve ter o sufixo IT (integration-test por incrivel que pareca)

$ mvn install - basicamente uma mistura de tudo. corre testes e it e faz um jar no fim



Novidade:

--mockito - ferramenta para testar se uma funcao foi cahamada, usando um mock de uma classe que apenas devolve um valor default. Nao funciona e serve apenas para verificar.

import static org.mockito.BDDMockito.given;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

pode ser usado com

@ExtendWith(MockitoExtension.class) antes da classe

@Mock
IStockmarketService stockmarketService; para mockar um obleto
OU
IStockmarketService stockmarketService = Mockito.mock(IStockmarketService.calss);


--hamcrest/AssertJ - asserts nomeadamente assertThat, serve para asserts mais legiveis

AssertJ
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <version>3.26.0</version>
    <scope>test</scope>
</dependency>

import static org.assertj.core.api.Assertions.assertThat;
.
.
.

Hamcrest
<dependency>
    <groupId>org.hamcrest</groupId>
    <artifactId>hamcrest-all</artifactId>
    <version>1.3</version>
</dependency>
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
.
.
.


dependencia:
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>${mockito-junit-jupiter.version}</version>
    <scope>test</scope>
</dependency>





-- http - comunicacao com api
<!--    apache http tools -->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.13</version>
        </dependency>

--failsafe para testes de integracao
<plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-failsafe-plugin</artifactId>
                <version>${maven-surefire-plugin.version}</version>
                <goals>
                    <goal>integration-test</goal>
                    <goal>verify</goal>
                </goals>
            </plugin>






Imports ate agora:

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

    @InjectMocks

    @Mock
    LoginService loginService;
    
    
Dependencias totais:


    <properties>
        <slf4j.version>2.0.17</slf4j.version>
        <logback.version>1.5.18</logback.version>

        <junit-jupiter.version>5.13.4</junit-jupiter.version>
        <mockito-junit-jupiter.version>5.20.0</mockito-junit-jupiter.version>

        <maven-surefire-plugin.version>3.5.4</maven-surefire-plugin.version>

        <java.version>17</java.version>
        <maven.compiler.target>${java.version}</maven.compiler.target>
        <maven.compiler.source>${java.version}</maven.compiler.source>

        <project.encondig>UTF-8</project.encondig>
        <project.build.sourceEncoding>${project.encondig}</project.build.sourceEncoding>
        <project.reporting.outputEncoding>${project.encondig}</project.reporting.outputEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>${logback.version}</version>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit-jupiter.version}</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.mockito</groupId>
            <artifactId>mockito-junit-jupiter</artifactId>
            <version>${mockito-junit-jupiter.version}</version>
            <scope>test</scope>
        </dependency>
        
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${maven-surefire-plugin.version}</version>
            </plugin>
        </plugins>
    </build>

</project>
