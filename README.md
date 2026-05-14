# 🏠 SplitHome — Backend

> API RESTful para gestão e divisão de despesas entre moradores de imóveis compartilhados.

---

## 📌 Sobre o Projeto

O **SplitHome** é um sistema desenvolvido com Java e Spring Boot para repúblicas, apartamentos divididos e imóveis com múltiplos inquilinos. Permite que moradores registrem despesas compartilhadas e acompanhem a divisão de forma automática e centralizada.

- 🔐 **Módulo Auth** — Registro, login e autenticação via JWT
- 🏠 **Módulo Property** — Gestão de imóveis com controle de disponibilidade
- 🤝 **Módulo Tenancy** — Moradia ativa com sistema de convite por código
- 💸 **Módulo Expense** — Registro e divisão automática de despesas

---

## ✨ Funcionalidades

### 🔐 Autenticação
- ✅ Registro de usuário com senha hasheada em BCrypt
- ✅ Login com geração de token JWT
- ✅ Claims customizados no token (isOwner, isMember, isHead, tenancyId)
- ✅ Rotas públicas e protegidas configuradas via Spring Security
- ✅ Filtro de autenticação interceptando todas as requisições

### 🏠 Imóveis
- ✅ Cadastro de imóvel vinculado ao usuário autenticado
- ✅ Listagem dos imóveis do proprietário
- ✅ Remoção com validação de ownership
- ✅ Controle de disponibilidade (libera o imóvel para receber uma tenancy)

### 🤝 Tenancy
- ✅ Criação de moradia ativa para um imóvel disponível
- ✅ Geração automática de invite code único
- ✅ Entrada na moradia via invite code
- ✅ Promoção automática do primeiro membro a Head Tenant
- ✅ Novo JWT retornado a cada mudança de papel do usuário

### 💸 Despesas
- ✅ Registro de despesa por qualquer membro da tenancy
- ✅ Divisão automática e igualitária entre todos os membros ativos
- ✅ Listagem de despesas da tenancy
- ✅ Controle de acesso — apenas membros da tenancy podem visualizar

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Descrição |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 3.5 | Framework para criação da API REST |
| Spring Security | Controle de autenticação e autorização |
| JWT | Autenticação stateless com claims customizados |
| Spring Data JPA | Abstração de acesso a dados |
| PostgreSQL | Banco de dados relacional |
| BCrypt | Hash de senhas |
| Maven | Gerenciador de dependências |
| Docker | Containerização do banco de dados |

---

## 📁 Estrutura do Projeto

src/main/java/com/splithome/backend/
├── auth/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── user/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── property/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── tenancy/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── expense/
│   ├── controller/
│   ├── dto/
│   ├── entity/
│   ├── repository/
│   └── service/
├── config/
├── exception/
└── BackendApplication.java

---

## 📡 Endpoints

### 🔐 Auth

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/auth/register` | Registra um novo usuário |
| `POST` | `/auth/login` | Autentica e retorna o JWT |

### 🏠 Property

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/property` | Cria um novo imóvel |
| `GET` | `/property` | Lista os imóveis do usuário autenticado |
| `DELETE` | `/property/{id}` | Remove um imóvel (apenas o owner) |
| `PATCH` | `/property/{id}/availability` | Altera a disponibilidade do imóvel |

### 🤝 Tenancy

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/tenancy` | Cria uma tenancy para um imóvel disponível |
| `POST` | `/tenancy/join/{inviteCode}` | Entra em uma tenancy via invite code |
| `GET` | `/tenancy/{id}/invite-code` | Resgata o invite code da tenancy |

### 💸 Expense

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/expense` | Registra uma nova despesa |
| `GET` | `/expense?id={tenancyId}` | Lista todas as despesas da tenancy |

---

## 🔄 Fluxo Principal

1- Registro → Login → JWT
2- Criar imóvel → JWT atualizado (isOwner: true)
3- Marcar imóvel como disponível
4- Criar tenancy → invite code + JWT atualizado (isMember: true)
5- Compartilhar invite code com moradores
6- Moradores entram via /tenancy/join/{inviteCode} → Primeiro vira HEAD, demais entram como membros
7- Qualquer membro lança despesas → Splits gerados automaticamente para todos os membros

---

## ⚙️ Configuração

Configure o arquivo `src/main/resources/application.properties` com suas credenciais:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/splithome
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 🚀 Como Rodar o Projeto

### Pré-requisitos
- Java 21 ou superior
- PostgreSQL instalado e rodando
- Maven instalado (ou usar o Maven Wrapper incluso)

### Passo a passo

**1️⃣ Clone o repositório**
```bash
git clone git@github.com:pedrohcvf/split-home-backend.git
cd split-home-backend
```

**2️⃣ Crie o banco de dados**
```sql
CREATE DATABASE splithome;
```

**3️⃣ Configure suas credenciais**

Edite o arquivo `src/main/resources/application.properties` com seu usuário e senha do PostgreSQL.

**4️⃣ Execute a aplicação**

Com Maven Wrapper:
```bash
./mvnw spring-boot:run
```

Ou com Maven instalado:
```bash
mvn spring-boot:run
```

**5️⃣ Acesse a API**

http://localhost:8080

---

## 📦 Exemplos de Requisição

**POST** `/auth/register` — Registrar usuário:
```json
{
  "name": "Pedro Carvalho",
  "email": "pedro@email.com",
  "password": "minhasenha123"
}
```

**POST** `/auth/login` — Login:
```json
{
  "email": "pedro@email.com",
  "password": "minhasenha123"
}
```

**POST** `/property` — Criar imóvel:
```json
{
  "address": "Rua das Flores, 123 — Boa Viagem, Recife",
  "description": "Apartamento 3 quartos"
}
```

**POST** `/tenancy` — Criar tenancy:
```json
{
  "propertyId": "uuid-do-imovel",
  "name": "República das Flores"
}
```

**POST** `/expense` — Registrar despesa:
```json
{
  "tenancyId": "uuid-da-tenancy",
  "description": "Conta de luz",
  "amount": 180.00
}
```

---

## 👤 Autor

**Pedro Carvalho**

[![GitHub](https://img.shields.io/badge/GitHub-pedrohcvf-181717?style=flat&logo=github)](https://github.com/pedrohcvf)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-pcarvalhof-0A66C2?style=flat&logo=linkedin)](https://linkedin.com/in/pcarvalhof)
