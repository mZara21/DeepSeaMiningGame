# Package: pt.isec.pa.deepsea

Este é o package principal (root) da aplicação Deep Sea Mining.

### Objetivo e Responsabilidade
Este package atua como o ponto de entrada principal do programa. A sua responsabilidade é inicializar o motor gráfico JavaFX e dar o arranque do ciclo de vida da aplicação.

### Componentes e Funcionalidades
Sendo o package de nível mais alto, ele contém apenas o essencial para iniciar a aplicação:
* **`DeepSeaApp`**: Contém o método genérico `main` que faz a chamada estática ao `Application.launch()`,iniciando arranque a interface gráfica definida no package de UI.