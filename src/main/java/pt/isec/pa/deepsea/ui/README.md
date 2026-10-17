# Package: pt.isec.pa.deepsea.ui

Este package contém toda a Interface Gráfica do projeto, desenvolvida em JavaFX.

### Objetivo e Responsabilidade
A principal responsabilidade deste package é a representação visual do estado do 
jogo e a captura das interações do utilizador (eventos de teclado e rato).
Em estrito cumprimento do padrão arquitetural **MVC@PA**, as classes deste 
package não efetuam cálculos de lógica de negócio. Elas comunicam exclusivamente 
com o `DeepSeaManager` (a Facade do modelo), enviando comandos de input e reagindo
de forma assíncrona a eventos de mudança de estado (`PropertyChangeEvents`).

### Componentes e Funcionalidades
Os componentes organizam-se nas seguintes categorias:
* **Estrutura Base e Layouts:** `MainJFX` (configuração da Janela/Stage), `RootPane` (layout principal), `GameView` (gestor de empilhamento de ecrãs) e `GameMenu`.
* **Superfícies de Desenho (Canvas):** Classes dedicadas a renderizar cada fase do jogo (ex: `SuperficieCanvas`, `FossoCanvas`, `FundoCanvas`, `PuzzleCanvas`, `OficinaCanvas`).
* **Painéis de Estado e Controlo:** Painéis interativos (`LeftPanelStats`, sub-classes de `RightPanel`, `DroneCardsBox`) que exibem as estatísticas da missão em tempo real e fornecem botões de ação (como comprar melhorias).
* **Logs Visuais:** `LogWindow` e `LogPane` para monitorização gráfica do histórico de eventos em tempo real.