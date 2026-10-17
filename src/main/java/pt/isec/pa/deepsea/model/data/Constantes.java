package pt.isec.pa.deepsea.model.data;

/**
 * Centraliza todas as configurações e valores fixos do jogo.
 * <p>
 * Esta classe facilita a manutenção e o ajuste do
 * equilíbrio do jogo (consumos, danos e limites).
 * </p>
 * @author Guilherme Eça, Miguel Zara e Luana Rabello
 * @version 1.0
 */
public final class Constantes {
    private Constantes() {}

    /** Ativa ou desativa comportamentos específicos para defesa. */
    public static boolean MODO_DEFESA = false;

    // GRELHAS
    /** Número de linhas da grelha de superfície. */
    public static final int SUPERFICIE_LINHAS  = 10;
    /** Número de colunas da grelha de superfície. */
    public static final int SUPERFICIE_COLUNAS = 10;

    /** Número de linhas do fosso vertical. */
    public static final int FOSSO_LINHAS  = 10;
    /** Número de colunas do fosso vertical. */
    public static final int FOSSO_COLUNAS = 10;

    /** Linha de entrada do drone no fosso. */
    public static final int FOSSO_DRONE_INICIO_LINHAS  = 0;
    /** Coluna de entrada do drone no fosso (centro). */
    public static final int FOSSO_DRONE_INICIO_COLUNAS = FOSSO_COLUNAS / 2;

    /** Número de linhas do fundo. */
    public static final int FUNDO_LINHAS  = 10;
    /** Número de colunas do fundo. */
    public static final int FUNDO_COLUNAS = 10;

    // RECURSOS DO NAVIO E DRONES
    /** Capacidade máxima do tanque de um drone. */
    public static final double COMBUSTIVEL_MAXIMO_DRONE = 250.00;
    /** Nível de combustível com que o drone sai do Navio. */
    public static final double COMBUSTIVEL_INICIAL_DRONE = 100.0;
    /** Resistência máxima inicial do casco do drone. */
    public static final double INTEGRIDADE_MAXIMA_DRONE = 300.0;

    /** Quantidade total de drones disponíveis na frota inicial. */
    public static final int N_DRONES = 3;
    /** Combustível total disponível no Navio. */
    public static final double COMBUSTIVEL_NAVIO = COMBUSTIVEL_MAXIMO_DRONE * (N_DRONES + 2);

    //MELHORIAS DO DRONE
    /** Custo em minérios para aplicar qualquer melhoria. */
    public static final int CUSTO_MELHORIA_MINERIOS = 15;
    /** Valor acrescido à capacidade do tanque após upgrade. */
    public static final double VALOR_UPGRADE_TANQUE = 25.0;
    /** Valor acrescido à integridade máxima após upgrade. */
    public static final double VALOR_UPGRADE_CASCO = 50.0;

    // CONSUMOS E PENALIZAÇÕES
    /** Custo de combustível por cada movimento do Navio. */
    public static final double CONSUMO_MOVIMENTO_NAVIO = 2.0;
    /** Custo de combustível base por cada movimento do Drone. */
    public static final double CONSUMO_MOVIMENTO_DRONE = 1.0;
    /** Custo de combustível do navio para reparar a integridade de um drone. */
    public static final double CONSUMO_REPARACAO_CASCO = 5.0;
    /** Multiplicador de consumo quando o drone enfrenta uma corrente forte. */
    public static final double MULTIPLICADOR_CORRENTE_FORTE = 2.0;

    /** Dano causado ao casco por animais marinhos no fosso. */
    public static final double DANO_ANIMAL_MARINHO = 5.0;
    /** Dano causado ao casco por monstros no fundo. */
    public static final double DANO_MONSTRO = 10.0;

    // ITENS DE EXPLORAÇÃO
    /** Total de artefactos escondidos no mundo. */
    public static final int N_ARTEFACTOS = 3;
    /** Quantidade mínima de minério por fundo. */
    public static final int MIN_MINERIOS = 5;
    /** Quantidade máxima de minério por fundo. */
    public static final int MAX_MINERIOS = 15;


    // LIMITES DE OBSTACULOS E INIMIGOS
    /** Limite de rochas e perigos no fosso. */
    public static final int MAX_OBSTACULOS_FOSSO = 15;
    /** Limite de monstros errantes no fundo. */
    public static final int MAX_MONSTROS_FUNDO   = 5;
    /** ID base para geração de elementos no mapa. */
    public static final int ID_INICIAL_OBSTACULOS = 1001;

    // PUZZLE
    /** Lado da grelha quadrada do puzzle do artefacto. */
    public static final int DIMENSAO_PUZZLE = 4;
    /** Limite de jogadas antes da falha energética do artefacto. */
    public static final int MAX_MOVIMENTOS_PUZZLE = 50;
    /** Número de trocas aleatórias para baralhar o puzzle. */
    public static final int SHUFFLES_PUZZLE = 20;

    //Ficheiros
    /** Nome do ficheiro onde serão registados os ficheiros recentes. */
    public static final String NOME_FICHEIRO_GAME_FILES = "deepsea_games.txt";
}