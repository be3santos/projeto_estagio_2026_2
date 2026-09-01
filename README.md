# 🎲 BoardGame Café — Sistema de Reservas

Sistema web para reserva de mesa num café de jogos de tabuleiro fictício. O visitante escolhe
categoria de jogo, data e horário pela página pública; o admin gerencia as reservas recebidas
num painel protegido por login.

## Stack

- **Java 17** + **Spring Boot 3.3**
- **Spring Web** + **Thymeleaf** (server-side rendering)
- **Spring Data JPA** + **H2** (banco em arquivo, sem setup externo)
- **Spring Security** (form login, sessão, proteção de rota)
- **Bootstrap 5** via CDN (sem build de CSS)

## Pré-requisitos

- Java 17 ou superior
- Maven (ou use o `mvnw` incluso no projeto — não precisa ter Maven instalado globalmente)

## Como rodar

```bash
./mvnw spring-boot:run
```

Windows (PowerShell):
```powershell
.\mvnw.cmd spring-boot:run
```

A aplicação sobe em **http://localhost:8080**.

O banco H2 é criado automaticamente como arquivo em `./data/boardgamecafe.mv.db` na primeira
execução — não precisa rodar migration nem seed manualmente.

## Usuário admin

O usuário administrador não fica salvo no banco: é criado em memória, a partir de variáveis de
ambiente, quando a aplicação sobe.

- Usuário padrão: `admin`
- Senha padrão: `admin123`

Para trocar as credenciais, defina as variáveis de ambiente antes de rodar:

```bash
export ADMIN_USUARIO=seu_usuario
export ADMIN_SENHA=sua_senha
./mvnw spring-boot:run
```

Veja `.env.example` para o formato esperado.

## Rotas principais

| Rota | O que é |
|---|---|
| `/` | Página pública com informações do café e formulário de reserva |
| `/login` | Login do admin |
| `/admin/painel` | Painel de gestão (protegido — redireciona pro login se não autenticado) |
| `/logout` | Encerra a sessão do admin |

## Estrutura do projeto