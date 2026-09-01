# Decisões

## Tema

BoardGame Café: o visitante reserva uma mesa escolhendo categoria de jogo, jogo específico
(opcional), número de pessoas, data e horário. O admin gerencia as reservas recebidas.

Decidi que "jogo específico" é opcional e "categoria" é obrigatória: nem todo cliente sabe
exatamente qual jogo quer jogar ao reservar, mas o café precisa saber ao menos o tipo pra
separar a mesa e sugerir opções. Isso resolve a ambiguidade "reserva de mesa + jogo" sem
travar quem ainda não decidiu.

## Stack

Java + Spring Boot, com Thymeleaf (server-side) em vez de uma SPA separada. Ganho: é a stack
que eu já domino, então o tempo foi gasto modelando o domínio e o painel, não aprendendo
framework novo. Perda: menos "moderno" que uma SPA em React — mas o teste pede pra usar o que
eu já sei, não pra impressionar com stack nova.

Banco H2 em arquivo em vez de Postgres/MySQL: zero setup externo pra quem for rodar o projeto.
Perda: não reflete um banco de produção real, mas pro escopo de um teste técnico o ganho de
simplicidade compensa.

Autenticação: Spring Security com usuário em memória (via variável de ambiente), em vez de uma
tabela de usuários no banco. Ganho: simples de explicar e de trocar credencial sem migration.
Perda: não escala pra múltiplos admins — pra isso eu criaria uma entidade `Usuario` com senha
hasheada no banco.

## Ambiguidades que percebi

- **Painel vazio**: tratei com uma mensagem amigável ("Nenhuma reserva por aqui ainda") em vez
  de deixar uma tabela vazia sem contexto.
- **Limite de data**: a especificação não diz até quando uma reserva pode ser feita no futuro.
  Sem limite, alguém poderia reservar pra daqui a 10 anos, o que não faz sentido operacional
  pra um café. Limitei a 1 ano a partir de hoje, usando `min`/`max` no próprio campo de data —
  o navegador já bloqueia visualmente qualquer data fora desse intervalo.
- **Ordenação**: "por data" ficou por data + horário juntos, senão duas reservas no mesmo dia
  apareceriam em ordem arbitrária.

## Além do pedido

1. **Admin pode confirmar/cancelar a reserva direto no painel** (não foi pedido — só listar).
   Sem isso, o admin veria a lista mas não teria como agir sobre ela, o que não é usável numa
   segunda-feira real.
2. **Filtro por status** no painel (Todas / Pendentes / Confirmados / Cancelados) — útil
   quando a lista cresce.
3. **Contadores** (pendentes/confirmados/cancelados) no topo do painel, pra visão rápida sem
   precisar ler a tabela inteira.

## O que decidi não fazer

- **Cancelamento pelo visitante**: exigiria autenticação ou link mágico por email — fora do
  escopo do teste.
- **Notificação por email real**: simulei a confirmação como mensagem visual na própria
  página; enviar email de verdade exigiria configurar um provedor SMTP, que não agrega ao que
  está sendo avaliado aqui.
- **Múltiplos usuários admin**: um único admin em memória resolve o requisito sem complexidade
  extra.

## Uso de IA

Usei IA como par de programação: ela sugeria a estrutura e o código de cada peça (entidade,
repositório, controllers, segurança, templates), e eu revisava, digitava e testava cada parte
antes de seguir pra próxima — não gerei o projeto inteiro de uma vez. As decisões de tema,
modelagem de campos e escopo (o que entrar, o que cortar) foram minhas, discutidas antes do
código ser escrito.

Um ponto em que a sugestão inicial da IA não fazia sentido: numa primeira versão do
`SecurityConfig`, a sugestão era desabilitar CSRF globalmente "pra simplificar". Isso é uma
prática ruim mesmo em projeto pequeno — o Thymeleaf com `thymeleaf-extras-springsecurity6` já
injeta o token de CSRF automaticamente nos formulários com `th:action`, então não havia motivo
real pra desabilitar. Mantive CSRF ativo.

Decisão tomada contra a sugestão padrão: optei por autenticação em memória (variável de
ambiente) em vez de uma entidade `Usuario` persistida no banco, que seria o caminho "mais
robusto". Pro escopo de um único admin fixo, isso é complexidade desnecessária — prefiro a
versão mais simples que eu sei explicar de cabeça.