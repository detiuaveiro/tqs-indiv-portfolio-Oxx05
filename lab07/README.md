Resultados do SonarQube
======================

Security
0
Open issues
A
The Security rating is A when there are no issues above info severity with impact on the security of your software.

Reliability
0
Open issues
A
The Reliability rating is A when there are no issues above info severity with impact on the reliability of your software.

Maintainability
27
Open issues
A
The Maintainability rating is A if the code has a relatively lower level of technical debt when compared to the size of the codebase.

Accepted issues
0
Valid issues that were not fixed

Coverage
56.0%
On
72
lines to cover.

Duplications
0.0%
On
254
lines.

Security Hotspots
0
A

===========================

Passou no quality gate, não por passar nos requisitos mas sim por ser código base, uma vez que os 
requisitos só se aplicam a código novo ou modificado.

-----
External Analyzers

Checkstyle
Verifica se o código segue certos formatos: nomes de vars, tamanho de métodos, indentação...

PMD
Encontra más práticas no código, como variáveis não utilizadas... e possíveis bugs.

SpotBugs
Encontra possíveis bugs no código, como null pointer exceptions...

-----

ex2:

c) Take note of the technical debt found (Measures > Overview, Maintainability): debt 22min, debt ratio 0.2%
Deve se a testes nao cobertos e a metodos vazios. Em geral é o código que será mais difícil de manter no futuro.


______________________

LightHouse

Metrics:
FCP (First Contentful Paint)	
Momento em que o primeiro conteúdo visível (texto, imagem, ícone, etc.) aparece no ecrã.
2,360 ms

73
10%
SI (Speed Index)	
Mede quão rapidamente o conteúdo visível é preenchido no ecrã durante o carregamento.
3,211 ms

92
10%
LCP (Largest Contentful Paint)	
Tempo até o maior elemento visível (geralmente a imagem principal ou bloco de texto grande) ser renderizado.
3,477 ms

65
25%
TBT (Total Blocking Time)	
Soma do tempo em que o main thread ficou bloqueado por tarefas longas (>50 ms) após o First Paint e antes da interação ser possível.
188 ms

91
30%
CLS (Cumulative Layout Shift)	
Mede instabilidade visual — o quanto o layout "salta" durante o carregamento.
0.00

100
25%

_____

Melhorias de acessibilidade:

Document does not have a main landmark. - meter landmark

Background and foreground colors do not have a sufficient contrast ratio. - aumentar o contraste das cores


_______

Os resultados melhoraram ao usar lighthouse como plugin num site anonimo, isto deve se a fatores como:

Headless / sem GUI real
Ao correr terminal, o Chrome headless não tem interface gráfica, mas a renderização pode ser ligeiramente diferente, porque algumas otimizações visuais não são aplicadas.
Isso pode afetar FCP, LCP e SI.

Simulação de CPU e rede
O Lighthouse CLI(terminal) aplica throttling por padrão:
CPU 4x slowdown

Rede lenta (simulada)
Isso deixa os tempos mais “reais” para utilizadores móveis, mas aumenta os valores medidos.

Cache limpo ou parcial
Por padrão, o CLI inicia com cache limpo a cada execução, o que significa que todos os recursos têm de ser carregados do zero.
Na janela anónima do Chrome, o cache do navegador pode ainda ser usado se estiveres numa sessão ativa, ou se o Chrome não limpar completamente.

Extensões e interferências
No CLI não há extensões, então normalmente não atrapalham, mas a diferença de throttling e de render headless ainda causa resultados diferentes.


Cenário	                    Throttling	                Cache	                Resultado
CLI	                        CPU + rede (simulada)	    Limpo	                Base de comparação
Mobile no Chrome            CPU + rede (simulada)	    Limpo	                Quase igual ao CLI
Desktop normal	            Sem throttling	            Pode ter cache	        Mais rápido, valores diferentes


ua.pt analysis

Use efficient cache lifetimes
Impact: The website is not caching resources (like images, scripts, CSS) efficiently. This causes browsers to re-download the same resources on each visit, increasing load times and bandwidth usage.
Estimated savings: 4,657 KiB.

Render blocking requests
Impact: Critical resources (CSS, JS) are blocking the page from rendering quickly. Users see a blank page longer, reducing perceived performance.
Estimated savings: 3,040 ms.

Legacy JavaScript
Impact: Old JavaScript features are used which may not be optimized for modern browsers, increasing download and execution times.
Estimated savings: 126 KiB.

Font display
Impact: Custom fonts are not loaded efficiently. This can cause “invisible text” (FOIT) or layout shifts.
Estimated savings: 80 ms.

______________

Relation to Quality Assurance (QA)

Performance QA: Lighthouse identifies bottlenecks that affect user experience. QA teams use these metrics to ensure applications meet performance standards.

Regression Testing: If a new release increases load times or layout shifts, Lighthouse reports can help detect performance regressions.

User Experience QA: Metrics like LCP, layout shifts, and render-blocking requests directly relate to UX testing. QA ensures that the site is fast and stable.

Maintainability: Legacy JS and inefficient caching hint at maintainability problems. QA checks that code follows best practices to prevent future performance issues.
