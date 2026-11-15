# API Contato - Juventude

API RESTful desenvolvida em Java com Spring Boot para gerenciamento de formulários de contato, envio de e-mails e autenticação de usuários (incluindo login social e JWT). Desenvolvida pelos alunos da disciplina de Programação Orientada a Objetos no curso de Bacharel em Ciência da Computação:

Adryan Victor Gomes - 2025027518 | adryan.gomes@arapiraca.ufal.br
Elias Sales de Magalhães - 2025027204 | elias.magalhaes@arapiraca.ufal.br
Saulo Firmo do Nascimento - 2025027320 | saulo.nacimento@arapira.ufal.br

![Projeto_em_UML](uml_projeto.png)

## Tecnologias Utilizadas

Este projeto foi construído utilizando as seguintes tecnologias e dependências principais:

* **Linguagem:** Java 21

* **Framework:** Spring Boot (`4.1.1`)

* **Gerenciador de Dependências:** Apache Maven (com Maven Wrapper incluído)

* **Web & Validação:** Spring Web MVC & Spring Validation

* **Banco de Dados:** Spring Data JPA & PostgreSQL (integrado ao Supabase) + H2 Database (para testes)

* **Segurança & Autenticação:**

  * Spring Security

  * OAuth2 Client (Login Social com Google e Facebook)

  * JJWT (`0.12.6`) para geração e validação de tokens JWT

* **Serviço de E-mail:** Spring Boot Starter Mail

## Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

* **JDK 21** (Java Development Kit) configurado nas variáveis de ambiente (`JAVA_HOME`).

* **Git** para clonar o repositório.

* Uma instância do **PostgreSQL** (local ou via Supabase).

* Credenciais de um servidor **SMTP** (para envio de e-mails) e chaves **OAuth2** (Google/Facebook), caso utilize esses recursos localmente.

*(Nota: Não é necessário ter o Maven instalado globalmente, pois o projeto conta com o Maven Wrapper `./mvnw`).*

## Instalação e Configuração

1. **Clone o repositório:**

   ```
   git clone https://github.com/adryangomesdev/api-juventude-junqueiro
   cd api-contato
   
   ```

2. **Configure as variáveis de ambiente e propriedades locais:**
   Por questões de segurança, arquivos sensíveis como `.env`, `application-local.properties` e `application-dev.properties` são ignorados pelo Git.

   Crie um arquivo `src/main/resources/application-local.properties` (ou configure suas variáveis de ambiente no sistema) com as suas credenciais:

   ```
   # Banco de Dados (PostgreSQL / Supabase)
   spring.datasource.url=jdbc:postgresql://seu-host:5432/seu-banco
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   
   # Configuração de E-mail (SMTP)
   spring.mail.host=smtp.exemplo.com
   spring.mail.port=587
   spring.mail.username=seu-email@exemplo.com
   spring.mail.password=sua-senha-de-app
   
   # Segurança / JWT
   api.security.token.secret=sua-chave-secreta-jwt
   
   # OAuth2 (Google / Facebook)
   spring.security.oauth2.client.registration.google.client-id=seu-google-client-id
   spring.security.oauth2.client.registration.google.client-secret=seu-google-client-secret
   
   ```

## Como Executar o Projeto

Você pode rodar a aplicação diretamente pelo terminal utilizando o **Maven Wrapper**:

* **No Linux / macOS:**

  ```
  ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
  
  ```

* **No Windows (CMD / PowerShell):**

  ```
  mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
  
  ```

A API estará disponível por padrão em: `http://localhost:8080`

### Executando os Testes

Para rodar a suíte de testes automatizados (que utiliza banco em memória H2):

```
./mvnw test

```

## Principais Funcionalidades / Endpoints

*(Sugestão: Atualize esta tabela conforme as rotas criadas nos seus Controllers)*

| Método | Endpoint | Descrição | Autenticação | 
| ----- | ----- | ----- | ----- | 
| `POST` | `/api/contato` | Envia uma nova mensagem de contato e dispara e-mail | Pública | 
| `POST` | `/api/auth/login` | Autentica o usuário e retorna um token JWT | Pública | 
| `GET` | `/oauth2/authorization/google` | Inicia o fluxo de login social com o Google | Pública | 
| `GET` | `/api/mensagens` | Lista mensagens recebidas | Requer Token JWT | 

## Licença

Este projeto está sob a licença [MIT](LICENSE) *(ou especifique a licença adotada pelo projeto)*.