
Projeto criado com SpringBoot Inicializr



*** Endpoints
POST   /api/requests                → cria nova recolha - devolve token
GET    /api/requests/{token}        → consulta por token
PUT    /api/requests                → atualiza/cancela
DELETE /api/requests/{token}        → remove (opcional)
GET    /api/municipalities          → lista de municípios
GET    /api/staff/requests          → lista todas (filtrável)
PUT    /api/staff/requests/{token}  → muda estado

