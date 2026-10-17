# Package: pt.isec.pa.deepsea.ui.res

Este package funciona como o **Gestor de Recursos Estáticos (Assets)** da interface gráfica.

### Objetivo e Responsabilidade
A sua responsabilidade é centralizar o carregamento, cache e reprodução de ficheiros externos (Imagens, Áudio e CSS). O objetivo primordial é a **otimização de memória e desempenho**. Ao garantir que recursos pesados são lidos do disco apenas uma vez, previne-se o estrangulamento da *Application Thread* do JavaFX.

### Componentes e Funcionalidades
Os utilitários aqui presentes funcionam predominantemente através de métodos estáticos ou sob o padrão *Singleton*:
* **Carregadores com Cache:** `ImageLoader` e `SoundLoader`. Utilizam estruturas de dados em mapa (`HashMap`) para guardar e fornecer acesso rápido aos recursos multimédia.
* **Gestor de Áudio:** `SoundManager` (Singleton) que controla o `MediaPlayer`, mantendo uma fila de espera ordenada para evitar a sobreposição caótica de múltiplos efeitos sonoros.
* **Gestor de Estilos:** `CSSManager`, utilitário para a injeção limpa de folhas de estilo na estrutura de nós do JavaFX.