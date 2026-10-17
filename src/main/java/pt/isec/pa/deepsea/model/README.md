# Package: pt.isec.pa.deepsea.model

Este package funciona como a **Fachada (Facade)** e o ponto de entrada principal para toda a lógica do ecossistema Deep Sea.

### Objetivo e Responsabilidade
A principal responsabilidade deste package é a **orquestração e simplificação**. Ele serve de ponte entre a Interface Gráfica (UI) e a complexidade interna do jogo (Dados e Máquina de Estados).

O seu objetivo é:
* Expor uma interface simplificada para que a UI não precise de conhecer os detalhes da FSM ou das estruturas de dados internas.
* Centralizar a gestão de logs e eventos do sistema.
* Providenciar "snapshots" imutáveis dos dados para garantir que a UI apenas lê o estado sem o conseguir corromper.

### Componentes e Funcionalidades
Os componentes presentes neste package são de alto nível e coordenam os sub-packages `data` e `state`:

* **Gestor Principal (`DeepSeaManager`):** A classeue implementa o padrão *Facade*. É através dela que a UI solicita ações (mover, abastecer, etc.) e obtém informações sobre o estado atual do jogo.
* **Sistema de Logs (`DeepSeaLog`):** Um *Singleton* responsável por registar todos os eventos relevantes da simulação, permitindo a persistência de mensagens e a consulta de histórico de ações.
* **Objetos de Transferência de Dados (Records):** Como o `DroneRec`, que são estruturas leves e imutáveis usadas para enviar informações detalhadas das entidades para a interface sem expor as referências reais dos objetos do motor de dados.