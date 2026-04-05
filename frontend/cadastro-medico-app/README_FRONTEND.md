# Cadastro Médico - Frontend Angular 20 com MDB Bootstrap

Um sistema de gerenciamento médico construído com Angular 20 e MDB Bootstrap, que se conecta a uma API REST Spring Boot para gerenciar pacientes, doutores, consultas e usuários.

## Funcionalidades

- ✅ Autenticação com JWT
- ✅ Dashboard com resumo dos dados
- ✅ Gerenciamento de Pacientes (CRUD)
- ✅ Gerenciamento de Doutores (CRUD)
- ✅ Gerenciamento de Consultas (CRUD)
- ✅ Gerenciamento de Usuários (CRUD)
- ✅ Interface moderna com MDB Bootstrap
- ✅ Proteção de rotas com AuthGuard
- ✅ Interceptor JWT automático

## Tecnologias Utilizadas

- **Angular** 20.x
- **MDB Bootstrap** para UI
- **TypeScript**
- **RxJS** para programação reativa
- **Bootstrap** 5
- **Font Awesome** 6

## Pré-requisitos

- Node.js 18.x ou superior
- npm 9.x ou superior
- Backend API rodando em `http://localhost:8080`

## Instalação

1. Navegue até a pasta do projeto:
```bash
cd cadastro-medico-app
```

2. Instale as dependências:
```bash
npm install
```

## Execução

Para iniciar o servidor de desenvolvimento:

```bash
npm start
```

A aplicação estará disponível em `http://localhost:4200`

## Estrutura do Projeto

```
src/
├── app/
│   ├── components/
│   │   ├── login/
│   │   ├── dashboard/
│   │   ├── pacientes/
│   │   ├── doutores/
│   │   ├── consultas/
│   │   └── usuarios/
│   ├── services/
│   │   ├── auth.service.ts
│   │   ├── usuario.service.ts
│   │   ├── paciente.service.ts
│   │   ├── doutor.service.ts
│   │   └── consulta.service.ts
│   ├── models/
│   │   ├── auth.model.ts
│   │   ├── usuario.model.ts
│   │   ├── paciente.model.ts
│   │   ├── doutor.model.ts
│   │   └── consulta.model.ts
│   ├── guards/
│   │   └── auth.guard.ts
│   ├── interceptors/
│   │   └── jwt.interceptor.ts
│   ├── app.routes.ts
│   └── app.config.ts
├── assets/
├── styles.css
└── main.ts
```

## Configuração da API

A aplicação está configurada para conectar à API em `http://localhost:8080`. Para alterar o endpoint da API, edite as URLs nos serviços localizados em `src/app/services/`.

### Endpoints esperados da API

- `POST /api/auth/login` - Login
- `GET/POST/PUT/DELETE /api/usuarios` - Gerenciar usuários
- `GET/POST/PUT/DELETE /api/pacientes` - Gerenciar pacientes
- `GET/POST/PUT/DELETE /api/doutores` - Gerenciar doutores
- `GET/POST/PUT/DELETE /api/consultas` - Gerenciar consultas

## Autenticação

A autenticação é feita via JWT (JSON Web Token). O token é armazenado no `localStorage` e automaticamente incluído em todas as requisições HTTP através do `JwtInterceptor`.

### Fluxo de Login

1. Usuário acessa a página de login
2. Insere suas credenciais (email e senha)
3. Sistema valida as credenciais com o backend
4. Token JWT é armazenado no localStorage
5. Usuário é redirecionado para o dashboard

## Componentes Principais

### Login
Tela de autenticação com validação de email e senha.

### Dashboard
Painel principal com resumo dos dados (qty de pacientes, doutores, consultas e usuários).

### Pacientes
Lista, cria, edita e deleta pacientes. Campos: nome, CPF, data de nascimento, telefone, endereço.

### Doutores
Lista, cria, edita e deleta doutores. Campos: nome, CRM, especialidade, telefone.

### Consultas
Lista, cria, edita e deleta consultas. Campos: data, hora, doutor, paciente, descrição, status.

### Usuários
Lista, cria, edita e deleta usuários. Campos: nome, email, tipo, status.

## Build para Produção

```bash
npm run build
```

Os arquivos compilados estarão na pasta `dist/`.

## Scripts Disponíveis

- `npm start` - Inicia o servidor de desenvolvimento
- `npm run build` - Faz build para produção
- `npm test` - Executa os testes unitários
- `npm run lint` - Verifica o código com linter

## Próximos Passos

- [ ] Adicionar dropdowns dinâmicos em Consultas (carregar Doutores e Pacientes)
- [ ] Implementar busca e filtros nas listas
- [ ] Adicionar paginação
- [ ] Implementar formulário de registro
- [ ] Adicionar validações mais robustas
- [ ] Melhorar tratamento de erros
- [ ] Adicionar notificações (toast/snackbar)
- [ ] Implementar relatórios

## Licença

Este projeto está sob a licença MIT.
