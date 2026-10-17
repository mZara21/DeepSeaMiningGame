package pt.isec.pa.deepsea.ui;

public enum SoundEffect {
    SHIP_MOVED("navio_anda.mp3", true),
    DRONE_MOVED("drone_anda.mp3", true),
    TRANSITION("transicao_oceano.mp3", false),
    EXPLOSAO("explosao.mp3", true),
    CACHOLOTE("cachalote.mp3", false),
    CORRENTE("corrente.mp3", false),
    MONSTRO("monstro.mp3", false),
    ARTEFACTO("artefacto_encontrado.mp3", true),
    MINERIO("minerio_encontrado.mp3", true),
    ABASTECE_DRONE("abastecimento.mp3", true),
    MELHORA_DRONE("melhoria.mp3", true),
    SELECIONA_DRONE("selecionar_drone.mp3", true),
    ERRO_OFICINA("erro_oficina.mp3", true),
    REPARA_DRONE("repara.mp3", true),
    SLIDE("slide_carta.mp3", false);

    private final String soundFile;
    private final boolean playImmediately;

    SoundEffect(String soundFile, boolean playImmediately){
        this.soundFile = soundFile;
        this.playImmediately = playImmediately;
    }

    public String getSoundFile(){ return soundFile; }
    public boolean isPlayImmediately(){ return playImmediately; }
}
