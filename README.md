# API Portal Juventude Junqueiro (Ouvidoria e Contato)

API RESTful desenvolvida em Java com Spring Boot para gerenciamento de formulários de contato, sistema de ouvidoria pública (propostas e apoios) e autenticação de usuários (Login Social via OAuth2 e JWT). Desenvolvida pelos alunos da disciplina de Programação Orientada a Objetos no curso de Bacharelado em Ciência da Computação (UFAL):

* Adryan Victor Gomes - 2025027518 | adryan.gomes@arapiraca.ufal.br
* Elias Sales de Magalhães - 2025027204 | elias.magalhaes@arapiraca.ufal.br
* Saulo Firmo do Nascimento - 2025027320 | saulo.nacimento@arapira.ufal.br

![Projeto_em_UML](uml_projeto.png)

## Padrões de Projeto
Visando as boas práticas de Programação Orientada a Objetos, o projeto faz uso de diversos padrões:
* **Observer Pattern & Event-Driven:** Utilizado no acompanhamento de propostas. Ao atingir 20 apoios, a proposta emite um evento notificando a secretaria por e-mail automaticamente. Também utilizado na exclusão de usuários (`UsuarioExcluidoEvent`) para deletar dados em cascata de forma desacoplada.
* **Strategy Pattern:** Implementado na autenticação social (`OAuth2UserInfo`, `GoogleUserInfo`, `FacebookUserInfo`) para padronizar o recebimento de dados independente do provedor.
* **Facade Pattern:** Utilizado no `GlobalExceptionHandler` para centralizar e simplificar o tratamento de exceções da API.

## Tecnologias Utilizadas

Este projeto foi construído utilizando as seguintes tecnologias e dependências principais:

- **Linguagem:** Java 21
- **Framework:** Spring Boot (`3.x.x`)
- **Gerenciador de Dependências:** Apache Maven (com Maven Wrapper incluído)
- **Web & Validação:** Spring Web MVC & Spring Validation
- **Banco de Dados:** Spring Data JPA & PostgreSQL (integrado ao Supabase) + H2 Database (para testes em memória)
- **Segurança & Autenticação:**
  * Spring Security
  * OAuth2 Client (Login Social com Google e Facebook)
  * JJWT (`0.12.6`) para geração e validação de tokens JWT
- **Serviço de E-mail:** Spring Boot Starter Mail
- **Testes:** JUnit 5 & Mockito

## Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas em sua máquina:

- **JDK 21** (Java Development Kit) configurado nas variáveis de ambiente (`JAVA_HOME`).
- **Git** para clonar o repositório.
- Uma instância do **PostgreSQL** (local ou via Supabase).
- Credenciais de um servidor **SMTP** (para envio de e-mails) e chaves **OAuth2** (Google/Facebook).

*(Nota: Não é necessário ter o Maven instalado globalmente, pois o projeto conta com o Maven Wrapper `./mvnw`).*

## Instalação e Configuração

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/adryangomesdev/api-juventude-junqueiro
   cd api-juventude-junqueiro
   ```

2. **Configure as variáveis de ambiente (.env):**
   O projeto está configurado para ler automaticamente um arquivo `.env` na raiz. Crie um arquivo `.env` na pasta principal do projeto contendo as suas credenciais:

   ```env
   # Banco de Dados (PostgreSQL / Supabase)
   DATABASE_URL=jdbc:postgresql://<seu-host>:5432/<seu-banco>
   DATABASE_USER=seu_usuario
   DATABASE_PASS=sua_senha

   # Configuração JWT
   JWT_SECRET=sua-chave-secreta-jwt-muito-segura-aqui

   # OAuth2 (Google)
   GOOGLE_CLIENT_ID=seu-google-client-id
   GOOGLE_CLIENT_SECRET=seu-google-client-secret

   # OAuth2 (Facebook)
   FACEBOOK_CLIENT_ID=seu-facebook-client-id
   FACEBOOK_CLIENT_SECRET=seu-facebook-client-secret
   ```

## Como Executar o Projeto

Você pode rodar a aplicação diretamente pelo terminal utilizando o **Maven Wrapper**:

- **No Linux / macOS:**
  ```bash
  ./mvnw spring-boot:run
  ```

- **No Windows (CMD / PowerShell):**
  ```cmd
  mvnw.cmd spring-boot:run
  ```

A API estará disponível por padrão em: `http://localhost:8080`

### Executando os Testes

Para rodar a suíte de testes automatizados (que utiliza banco em memória H2):

```bash
./mvnw test
```

## Principais Funcionalidades / Endpoints

| Método | Endpoint | Descrição | Autenticação | 
| ----- | ----- | ----- | ----- | 
| `POST` | `/api/contato` | Envia uma mensagem e dispara e-mail para a moderação | Pública | 
| `GET` | `/oauth2/authorization/{provider}` | Inicia o fluxo de login social (google ou facebook) | Pública | 
| `GET` | `/api/propostas` | Lista todas as propostas disponíveis | Pública |
| `POST` | `/api/propostas` | Cria uma nova proposta na ouvidoria | Requer JWT |
| `POST` | `/api/propostas/{id}/apoiar` | Apoia (vota) em uma proposta | Requer JWT |
| `GET` | `/api/usuarios/perfil` | Retorna os dados do perfil logado | Requer JWT |
| `DELETE` | `/api/usuarios/conta` | Exclui a conta e apaga dados (propostas/apoios) em cascata | Requer JWT |

## Licença

Este projeto está sob a licença [MIT](LICENSE).
