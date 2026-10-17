# Package: pt.isec.pa.deepsea.model.state

Este package implementa a **Máquina de Estados (Finite State Machine - FSM)** que controla o fluxo de jogo e a lógica comportamental do sistema Deep Sea.

### Objetivo e Responsabilidade
A principal responsabilidade deste package é o **controlo comportamental**. Ele serve como uma camada de abstração entre a Interface Gráfica (UI) e os Dados (Model Data).

O seu objetivo é garantir que as regras de negócio sejam respeitadas em cada fase da simulação (ex: não permitir que um drone se mova no fundo se o estado atual for a superfície). Ele gere as transições de estado de forma dinâmica, reagindo às ações do utilizador e aos eventos gerados pelos dados.

### Componentes e Funcionalidades
Este package utiliza o **State Pattern** e está organizado da seguinte forma:

* **Interface de Estado (`IDeepSeaState`):** Define o contrato de todas as ações possíveis no sistema. Todos os métodos possuem implementações *default* que retornam `false`, obrigando apenas os estados específicos a implementar a lógica que lhes faz sentido.
* **Enumerado de Estados (`DeepSeaState`):** Lista todos os estados válidos do sistema e funciona como uma *Factory*, sendo responsável por instanciar a classe de estado correspondente.
* **Contexto (`DeepSeaContext`):** Atua como o ponto central da FSM, mantendo a referência para o estado atual e servindo de ponte para o `DeepSeaManager`.
* **Adaptador Base (`DeepSeaStateAdapter`):** Uma classe abstrata que facilita a implementação de novos estados, fornecendo acesso direto aos dados (`DeepSeaData`) e ao contexto para facilitar as transições.
* **Estados Concretos (Sub-package `.states`):** Contém a implementação real da lógica para cada cenário:
    * `SuperficieState`: Gestão do navio e seleção de drones.
    * `FossoState`: Descida/Subida vertical e gestão de perigos.
    * `FundoState`: Exploração do leito marinho com *Fog of War*.
    * `PuzzleState`: Resolução do mini-game para recolha de artefactos.
    * `OficinaState`: Gestão de melhorias e combustível.
    * `FimDeJogoState`: Ecrã final e processamento de resultados.****