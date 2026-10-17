# Package: pt.isec.pa.deepsea.model.data

Este package representa o **Coração de Dados (Domain Model)** do projeto Deep Sea. É aqui que toda a informação persistente e as regras fundamentais da simulação residem.

### Objetivo e Responsabilidade
O objetivo principal deste package é a **gestão do estado e da integridade dos dados**. Ele é responsável por:
* Manter o registo exato de todos os recursos (combustível, integridade, minérios e artefactos).
* Gerir a localização física das entidades (Navio e Drones) nas diferentes camadas do oceano.
* Validar e executar a lógica física dos movimentos, colisões e interações com o ambiente.
* Isolar a lógica de dados da lógica de controlo (FSM) e da interface visual (UI).

### Classes, Interfaces e Funcionalidades
Dentro deste package, as funcionalidades estão divididas em três pilares principais:

1. **Entidades Ativas:**
    * `Navio`: Atua como a "Base de Operações", gerindo a frota global, o combustível principal e o porão de recursos.
    * `Drone`: Representa a unidade de exploração, com lógica própria para consumo de energia, danos no casco e gestão de carga individual.

2. **Estrutura do Mundo (Grelhas):**
    * Classes que representam as camadas do oceano: `Superficie` (navegação horizontal), `Fosso` (navegação vertical) e `Fundo` (exploração 2D com névoa de guerra).
    * `Celula`: A unidade básica do mapa, que pode conter diferentes elementos como rochas, monstros, minérios ou artefactos.

3. **Utilitários e Regras Fixas:**
    * `Constantes`: Classe centralizadora de todos os parâmetros do jogo. Permite ajustar o equilíbrio (balancing) do jogo num único local (ex: custos de movimento, danos sofridos, limites das grelhas).
    * `DeepSeaData`: A classe agregadora que orquestra a interação entre todas as peças de dados, servindo de interface principal para o restante sistema.