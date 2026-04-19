# Sistema de Cadastro e Agendamento Médico (B2B/Clínico)

Este projeto é uma plataforma Fullstack robusta e moderna voltada para o mercado de HealthTech (Tecnologia em Saúde). O sistema atua como o principal núcleo de gestão, suportando um modelo híbrido flexível: do atendimento direto à pessoa física (Pacientes Particulares) até um **fluxo de trabalho corporativo B2B**, focando na parceria com empresas que fornecem planos/convênios para seus funcionários.

---

## Principais Funcionalidades Adicionadas
Este não é apenas um sistema de agendamento visual, mas carrega extensas lógicas de negócio por debaixo dos panos:

- **Sistema B2B Integrado (Multi-Tenant):** Cadastro de Empresas parceiras. Funcionários registrados no sistema precisam pertencer obrigatoriamente a uma dessas empresas, e os acessos ficam encapsulados respeitando essa hierarquia.
- **Gestão Global de Usuários (RBAC + Soft Delete):** 
  - Controle de Acesso Baseado em Filas (Role-Based Access Control) englobando 4 esferas: Administrador Global, Médicos, Funcionários(Empresas parceiras) e Pacientes(Público-Geral). 
  - As interfaces e menus colapsam ou se revelam ativamente de acordo com os privilégios do token `JWT` logado.
  - Implementação do conceito avançado de deleção lógica (**Soft Delete**) em escala, evitando quebras drásticas de integridade de Banco de Dados nas chaves estrangeiras caso um usuário se desligue. 
- **Agendamento Avançado e Tracking Diário Clínico:**
  - O coração do paciente pode agendar, porém, com *UX Guard*: Se os perfis não estiverem 100% preenchidos, a UI barra e exige dados obrigatórios antes da gravação no JPA.
  - Painéis de acompanhamento com Fila Diária no Dashboard que rastreia dinamicamente e concede a capacidade administrativa de avançar o status (Confirmar Presença vs Cancelar) das consultas agendadas ao longo do dia, dando fluidez real para uma Recepção ou Corpo Clínico.
- **Micro-Interações e Responsividade Pura:** Todo o frontend foge dos padrões crus e integra loaders amigáveis de API, formulários dinâmicos que mudam de forma e opções de dropdowns baseados nas entradas ativas do usuário e Badges e cards coloridos baseados em lógicas.

---

## Tecnologias Utilizadas no Ecossistema

As escolhas arquiteturais simulam uma aplicação profissional apta para ser lançada em mercado.

**Backend Architecture:**
- **Core da Lógica:** Java 21 LTS 
- **Framework Ouro:** Spring Boot 3.x
- **Persistência de Alto Nível:** Hibernate e Spring Data JPA abstraindo o **PostgreSQL** relacional.
- **Camada de Blindagem e Autenticação:** Padrão de segurança State-less provido via **Spring Security + Filtros Customizados** decodificando e injetando **JSON Web Tokens (JWT)**.
- **Infra e Acoplamento:** Beans, Componentes e Injeção de dependências pesadas orquestradas pelo Maven, com DTOs limpos e construtores via Lombok.

**Frontend Interface:**
- **Tecnologia Padrão Ouro:** Angular 
- **Linguagem Restrita:** TypeScript
- **Estilização e Componentes:** SCSS/CSS3 Customizados e Bootstrap 5 sem depender de bibliotecas externas pesadas e datadas como Jquery. Componentização standalone nativas do ecossistema novo do Angular.
- **Assincronicidade Reativa:** Integração extrema usando RxJS (Pipes async, Observables) para evitar os problemas tradicionais de concorrência e gerenciamento de estado. Interceptadores HTTP interceptando os requests para embutir o Token JWT de autorização transparente.

---

## Como Executar o Projeto Localmente

Ideal para Headhunters, Tech Recruiters ou pares de desenvolvimento verificarem e testarem a aplicação rodando de fato:

### Pré-requisitos Básicos
- **Java 21**, **Node.js LTS** e **npx/npm** instalados na máquina.
- **PostgreSQL** instalado e respondendo na porta nativa, com um banco (schema) vazio correspondendo ao que consta em seu *application.properties*.

### 1. Levantando o Backend (API)
1. Abra um terminal e navegue até a raiz do backend: `cd backend/app`
2. Certifique-se de que a string de conexão (JDBC), usuário e senha em `/src/main/resources/application.properties` se alinhem aos seus credenciais Postgre.
3. Inicie o servidor via commando wrapper: `mvn spring-boot:run`
> *Nota: O Hibernate criará as tabelas sozinho imediatamente após a inicialização! A API ficará ouvindo na porta padrão `8080`.*

### 2. Levantando o Frontend App (Angular)
1. Sem desligar o terminal anterior, abra uma nova janela de prompt e navegue: `cd frontend/cadastro-medico-app`
2. Se for a primeira vez instalando a cópia, baixe os módulos: `npm install`
3. Inicie o servidor frontend: `ng serve`
4. Na tela que aparecerá o sucesso, acesse a porta: `http://localhost:4200/`

---

## Acesso Rápido para Avaliação (Master Root)

Um dos maiores problemas para quem avaia um sistema de controle de acesso é ter que cavar nos códigos ou "chutar" qual a porta de entrada. 
Isso foi abstraído aqui com um *Database Seeding Inicializador*. Uma semente (Usuário Master) se cria dinamicamente logo no milissegundo de carregamento número 1 da API para você explorar a ferramenta.

Na página bonita de login do Front, utilize:

- **E-mail:** `admin@email.com`
- **Senha:** `Senha@123`

De cara, esse token de administrador te abrirá as portas para cadastrar Empresas falsas, simular funcionários, doutores e ver a *pipeline* de consultas funcionando na aba do dia.

---
*Este repositório prova capacidades consistentes no design Clean Architecture de backend, criação de RESTful APIs polidas, persistências robustas em SQL relacional, bem como conhecimento extenso sobre as práticas mais modernas e ativas na confecção de client-sides profissionais e reativos via ecossistema Angular.*
