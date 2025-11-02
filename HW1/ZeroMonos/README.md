
Projeto criado com SpringBoot Inicializr



*** Endpoints
POST   /api/requests                → cria nova recolha - devolve token
GET    /api/requests/{token}        → consulta por token
PUT    /api/requests                → atualiza/cancela
DELETE /api/requests/{token}        → remove (opcional)
GET    /api/municipalities          → lista de municípios
Post    /api/municipalities/update  → atualizar lista de municípios
GET    /api/staff/requests          → lista todas (filtrável)
PUT    /api/staff/requests/{token}  → muda estado



SonarQube key: sqp_bf2461136de46a2fcefc728bc3459ee745ca13fe
comando para rodar o sonar scanner:
mvn clean verify sonar:sonar \
-Dsonar.projectKey=ZeroMonos \
-Dsonar.projectName='ZeroMonos' \
-Dsonar.host.url=http://127.0.0.1:9000 \
-Dsonar.token=sqp_bf2461136de46a2fcefc728bc3459ee745ca13fe

77b91fac9dac06408457fd5a404bb951d8ffe992