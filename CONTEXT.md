# SplitHome — Contexto Completo do Projeto

Vou desenvolver o backend de um projeto chamado **SplitHome** — uma aplicação de gestão e divisão de despesas para moradores de imóveis compartilhados (repúblicas, apartamentos com múltiplos moradores, imóveis com inquilinos etc).

---

## STACK

- Java 21
- Spring Boot 3.5.14
- Spring Security + JWT (autenticação)
- Spring Data JPA + Hibernate
- PostgreSQL
- Lombok
- Bean Validation
- Maven

---

## MODELO DE DADOS

```
USER
- id            UUID        PK
- name          VARCHAR
- email         VARCHAR     único
- password_hash VARCHAR
- created_at    TIMESTAMP

PROPERTY
- id            UUID        PK
- owner_id      UUID        FK → USER
- address       VARCHAR
- description   TEXT
- available     BOOLEAN
- created_at    TIMESTAMP

TENANCY
- id            UUID        PK
- property_id   UUID        FK → PROPERTY
- name          VARCHAR
- invite_code   VARCHAR     único, gerado automaticamente
- started_at    TIMESTAMP
- active        BOOLEAN

TENANCY_MEMBER
- id                UUID        PK
- tenancy_id        UUID        FK → TENANCY
- user_id           UUID        FK → USER
- is_head           BOOLEAN
- share_percentage  DECIMAL     nullable
- joined_at         TIMESTAMP

EXPENSE
- id                UUID        PK
- tenancy_id        UUID        FK → TENANCY
- paid_by           UUID        FK → USER
- title             VARCHAR
- category          ENUM        (UTILITY, RENT, INTERNAL, OTHER)
- amount            DECIMAL
- visible_to_owner  BOOLEAN
- date              DATE

EXPENSE_SPLIT
- id            UUID        PK
- expense_id    UUID        FK → EXPENSE
- user_id       UUID        FK → USER
- amount_owed   DECIMAL
- paid          BOOLEAN
- paid_at       TIMESTAMP   nullable
```

---

## REGRAS DE NEGÓCIO IMPORTANTES

### Usuário e papéis
- Não existe campo `role` no USER — o papel é determinado pelas relações
- O sistema descobre se é OWNER porque tem um PROPERTY com `owner_id` apontando pra ele
- O sistema descobre se é MEMBER porque tem um TENANCY_MEMBER com `user_id` apontando pra ele
- Um usuário pode ser OWNER e MEMBER ao mesmo tempo
- Usuário recém cadastrado não tem papel nenhum — no primeiro login vai pra tela de escolha

### Autenticação JWT
- Login retorna um JWT com os seguintes claims: `sub` (user id), `email`, `isOwner`, `isMember`, `tenancyId`
- O backend consulta as relações no momento do login pra montar esses claims
- Token expira em 15 minutos, com refresh token pra renovar automaticamente

### Property
- Pertence a um único OWNER
- Campo `available` indica se está pronto pra receber moradores
- Quando todos os moradores saem, `available` volta pra `true` automaticamente

### Tenancy
- Representa a ocupação ativa de um imóvel
- `invite_code` é gerado automaticamente na criação (código alfanumérico, ex: SPLIT-4X9K)
- Quando uma Tenancy encerra (`active = false`), o histórico de despesas é preservado
- Uma Tenancy nunca pode ficar sem HEAD_TENANT enquanto tiver membros

### Tenancy Member
- `is_head = true` indica o responsável pela moradia (HEAD_TENANT)
- Só pode existir um `is_head = true` por Tenancy
- Todo HEAD_TENANT obrigatoriamente é um TENANCY_MEMBER
- Quando o HEAD_TENANT quer sair, ele pode escolher o substituto manualmente ou o sistema promove automaticamente o membro com `joined_at` mais antigo
- Se não houver nenhum outro membro, o sistema bloqueia a saída — o owner deve encerrar a Tenancy inteira

### Expense
- `visible_to_owner = true` → owner consegue ver (água, luz, condomínio)
- `visible_to_owner = false` → só moradores veem (feira, netflix, etc)
- No MVP, divisão é igualitária entre membros quando `share_percentage` for null

### Expense Split
- Criado automaticamente pelo sistema quando uma Expense é lançada
- Um EXPENSE_SPLIT por membro da Tenancy
- `paid_at` é null enquanto não pago

---

## ESTRUTURA DE PACOTES

A organização é por módulo — cada domínio tem seu próprio pacote com controller, service, repository, entity e dto internos. Isso facilita a navegação em projetos grandes.

```
com.splithome.backend
│
├── auth                        ← módulo de autenticação
│   ├── controller              ← AuthController
│   ├── dto
│   │   ├── request             ← LoginRequest, RegisterRequest
│   │   └── response            ← AuthResponse (JWT + claims)
│   └── service                 ← AuthService (login, registro, JWT)
│
├── user                        ← módulo de usuário
│   ├── controller              ← UserController
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── entity                  ← User
│   ├── repository              ← UserRepository
│   └── service                 ← UserService
│
├── property                    ← módulo de imóvel
│   ├── controller              ← PropertyController
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── entity                  ← Property
│   ├── repository              ← PropertyRepository
│   └── service                 ← PropertyService
│
├── tenancy                     ← módulo de moradia
│   ├── controller              ← TenancyController
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── entity                  ← Tenancy, TenancyMember
│   ├── repository              ← TenancyRepository, TenancyMemberRepository
│   └── service                 ← TenancyService
│
├── expense                     ← módulo de despesas
│   ├── controller              ← ExpenseController
│   ├── dto
│   │   ├── request
│   │   └── response
│   ├── entity                  ← Expense, ExpenseSplit
│   ├── enums                   ← ExpenseCategory
│   ├── repository              ← ExpenseRepository, ExpenseSplitRepository
│   └── service                 ← ExpenseService
│
├── config                      ← configurações globais
│   ├── SecurityConfig          ← Spring Security + filtros
│   ├── JwtConfig               ← geração e validação do token
│   └── CorsConfig              ← configuração de CORS
│
├── exception                   ← exceções globais
│   ├── GlobalExceptionHandler  ← @ControllerAdvice
│   ├── BusinessException       ← exceção base de regra de negócio
│   └── NotFoundException       ← recurso não encontrado
│
└── util                        ← utilitários gerais
    └── InviteCodeGenerator     ← gerador de código alfanumérico (ex: SPLIT-4X9K)
```

---

## GIT FLOW

- `main` → código estável, pronto pra produção
- `develop` → integração das features prontas
- `feature/nome` → desenvolvimento de cada feature

Atualmente na branch `feature/user-auth`.

---

## ORDEM DE DESENVOLVIMENTO — MVP

1. `feature/user-auth` → módulos `auth` e `user` — entidade User, registro, login, JWT
2. `feature/property` → módulo `property` — CRUD do owner
3. `feature/tenancy` → módulo `tenancy` — invite code, regras de head tenant
4. `feature/expense` → módulo `expense` — lançamento e divisão automática

---

## O QUE FAZER AGORA

Estamos começando pela `feature/user-auth`. Preciso que você me ajude a construir:

1. A entidade `User` com JPA e Lombok
2. O `UserRepository`
3. O `UserService` com registro e busca
4. O `AuthController` com endpoints de registro e login
5. A configuração do JWT (geração e validação do token com os claims corretos)
6. A configuração do Spring Security

Pode começar!
