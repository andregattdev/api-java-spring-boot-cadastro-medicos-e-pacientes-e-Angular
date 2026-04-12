# Sistema de Cadastro e Agendamento Médico (B2B/Clínico)

Este projeto é uma plataforma completa e moderna para agendamento e gerenciamento de consultas médicas. O sistema suporta um fluxo de trabalho corporativo B2B e clínico, permitindo diferentes níveis de acesso e gerenciamento para administradores, médicos, pacientes de empresas parceiras e pacientes particulares.

##  Principais Funcionalidades

- **Gerenciamento de Usuários e Controle de Acesso (RBAC):** Sistema seguro com JWT definindo perfis (Administrador, Médico, Funcionário/Paciente Empresa, Paciente Particular), adaptando e restringindo as interfaces dinamicamente de acordo com as permissões.
- **Fluxo Corporativo B2B:** Cadastro de Empresas parceiras e gestão de funcionários, permitindo que o sistema agrupe e gerencie o fluxo de agendamentos dos colaboradores corporativos.
- **Cadastro de Médicos e Especialidades:** Gerenciamento da equipe clínica da plataforma.
- **Agendamento de Consultas e Histórico:** Criação, controle e histórico de consultas dos pacientes vinculados às agendas dos médicos.
- **Interface Moderna e Responsiva:** Design limpo, dinâmico e amigável, focado na melhor experiência do usuário (UX), desenvolvido como uma Single Page Application em Angular.

## 🛠 Tecnologias Utilizadas

**Backend:**
- **Linguagem:** Java 21
- **Framework:** Spring Boot
- **Persistência / Banco de Dados:** Spring Data JPA, Hibernate e PostgreSQL
- **Segurança:** Spring Security com autenticação stateless baseada em JSON Web Tokens (JWT)
- **Build / Boilerplate:** Maven e Lombok

**Frontend:**
- **Linguagem:** TypeScript
- **Framework:** Angular 21
- **Estilização e Componentes:** Bootstrap 5, CSS3, e HTML5
- **Gerenciamento de Estados / Rotas:** RxJS e Angular Router

##  Como Executar o Projeto Localmente

### Pré-requisitos
- **Java 21**, **Node.js** e **npm** instalados.
- **PostgreSQL** instalado e ativo, com um schema/banco preparado de acordo com as configurações em seu application properties.

### 1. Configurando e iniciando o Backend (Spring Boot)
1. Abra um terminal e navegue até a pasta base: `cd backend/app`
2. Certifique-se de que a string de conexão, usuário e senha do PostgreSQL estão configurados corretamente em `src/main/resources/application.properties`.
3. Inicie o servidor: `mvn spring-boot:run` ou pela sua IDE. A API subirá por padrão na porta `8080` e criará a estrutura no banco de dados.

### 2. Configurando e iniciando o Frontend (Angular)
1. Abra outro terminal e vá até: `cd frontend/cadastro-medico-app`
2. Instale as dependências executando: `npm install`
3. Execute o servidor de testes/visualização: `npm start` (ou `ng serve`)
4. Acesse em seu navegador via: `http://localhost:4200/`

##  Acesso Inicial do Sistema (Usuário Mestre)

Para facilitar a reprodução e avaliação do sistema (por exemplo, durante entrevistas ou testes pela equipe de qualidade), já foi configurada a injeção (seeding via `DataInitializer`) de um **Usuário Administrador Mestre**. Logo que o backend sobe pela primeira vez, este usuário é criado caso não exista no banco.

Utilize as seguintes credenciais na tela inicial de Login:

- **E-mail (Login):** `admin@email.com`
- **Senha:** `Senha@123`

Com este login inicial de Administrador, você terá acesso ao painel de controle mestre, de onde poderá realizar o cadastro inicial das Empresas (Clientes B2B), Médicos, Funcionários e outros usuários essenciais para explorar e validar todo o fluxo e regras de negócio da aplicação.

---
*Este projeto demonstra conhecimento prático e real em desenvolvimento Fullstack, arquitetura de software limpa, padrão MVC, concepção de REST APIs robustas, persistência relacional e construção de client-sides modernos via SPA de navegação fluida.*
