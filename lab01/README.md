aula 1

id - pt.ua.tqs - identificacao unica ou pelo menos muito restrita

artefacto - lab01_ex01 p.e.


usamos maven com dependencias que nos ajudam a testar o codigo

JUnit Jupiter - corre os testes desde que estejam anotados como @Test
tem funcoes importantes como:
@Disabled, @Before/After All, @Before/After Each...


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

<dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.13.4</version>
            <scope>test</scope>
        </dependency>


Jacoco - ferramenta que permite ver que partes do codigo estao cobertas e por cobrir, seja por linhas, branches...


<plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>0.8.13</version>
                <configuration>
                    <rules>
                        <rule>
                            <element>CLASS</element>
                            <excludes>
                                <exclude>**/*Test*</exclude>
                                <exclude>**/test/**</exclude>
                            </excludes>
                            <limits>
                                <limit>
                                    <counter>LINE</counter>
                                    <value>COVEREDRATIO</value>
                                    <minimum>0.90</minimum>
                                </limit>
                            </limits>
                        </rule>
                    </rules>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>prepare-package</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>jacoco-check</id>
                        <goals>
                            <goal>check</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>