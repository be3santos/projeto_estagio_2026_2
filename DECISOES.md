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
- **Horário livre vs. horário fixo**: comecei com um campo de horário livre (`<input
  type="time">`), mas ao implementar o limite de mesas por horário percebi um problema: como a
  contagem é por horário exato, alguém reservando às 18:01 escapava completamente do limite
  aplicado às 18:00, mesmo sendo, na prática, a mesma "rodada" de mesas. Resolvi trocando por
  um `<select>` com horários fixos (14:00, 16:00, 18:00, 20:00, 22:00) — assim a capacidade por
  horário passa a fazer sentido de verdade, e também simplifica a experiência de quem reserva.
- **Como o visitante sabe se a reserva foi confirmada ou cancelada**: como decidi não enviar
  email real, o visitante não teria nenhuma forma de acompanhar o próprio pedido depois de
  enviado. Resolvi com uma página pública de consulta (`/consultar`), onde a pessoa digita o
  email usado na reserva e vê o status atual de todas as reservas feitas com aquele email —
  sem precisar de login nem de infraestrutura de envio de email.

## Além do pedido

1. **Admin pode confirmar/cancelar a reserva direto no painel** (não foi pedido — só listar).
   Sem isso, o admin veria a lista mas não teria como agir sobre ela, o que não é usável numa
   segunda-feira real.
2. **Filtro por status** no painel (Todas / Pendentes / Confirmados / Cancelados) — útil
   quando a lista cresce.
3. **Limite de mesas por horário**: defini uma capacidade máxima (6 reservas simultâneas por
   horário) para evitar overbooking. Ao tentar reservar um horário já lotado, o visitante
   recebe um aviso e precisa escolher outro horário ou data. Reservas canceladas liberam a vaga
   de volta (a contagem ignora status `CANCELADO`).
4. **Consulta pública de status** (`/consultar`): o visitante digita o email usado na reserva e
   vê o status atualizado de todas as suas reservas, sem precisar de login. Resolve uma lacuna
   real que percebi depois de decidir não implementar email — sem essa página, o cliente não
   teria absolutamente nenhuma forma de saber o que aconteceu com o pedido dele.

## O que decidi não fazer

- **Cancelamento pelo visitante**: exigiria autenticação ou link mágico por email — fora do
  escopo do teste. A consulta de status (item acima) cobre a necessidade de acompanhamento sem
  precisar desse nível de complexidade.
- **Notificação por email real**: simulei a confirmação como mensagem visual na própria
  página, e criei a consulta pública de status como alternativa — enviar email de verdade
  exigiria configurar um provedor SMTP, que não agrega ao que está sendo avaliado aqui.
- **Múltiplos usuários admin**: um único admin em memória resolve o requisito sem complexidade
  extra.
- **Trava de concorrência na checagem de capacidade**: a verificação de vagas e o salvamento da
  reserva são dois passos separados no código — em teoria, duas pessoas enviando o formulário
  no mesmo instante para o mesmo horário poderiam ambas passar pela checagem antes de qualquer
  uma salvar, excedendo a capacidade em casos raros de concorrência. Resolver isso de forma
  robusta exigiria uma constraint de banco ou lock transacional, o que julguei fora do escopo
  deste teste.

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