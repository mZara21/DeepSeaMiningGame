package pt.isec.pa.deepsea.ui.res;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import pt.isec.pa.deepsea.model.DeepSeaLog;
import pt.isec.pa.deepsea.ui.SoundEffect;

import java.util.ArrayDeque;
import java.util.Queue;
/**
 * Gestor global de reprodução de efeitos sonoros da aplicação (Singleton).
 * <p>
 * Mantém uma fila de reprodução (Queue) para garantir que sons secundários
 * não se sobrepõem de forma indesejada. Permite também ligar/desligar
 * os sons globalmente através de uma propriedade observável (BooleanProperty)
 * interligada à Interface Gráfica.
 * </p>
 */
public class SoundManager {

    private static SoundManager instance;
    private final Queue<String> soundQueue;
    private MediaPlayer mediaPlayer;
    private final BooleanProperty soundEnabled =  new SimpleBooleanProperty(false);
    private boolean playing = false;

    private SoundManager() {
        soundQueue = new ArrayDeque<>();
    }


    public static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();
        return instance;
    }

    public BooleanProperty soundEnabledProperty() {
        return soundEnabled;
    }


    public void setSoundEnabled(boolean value) {
        soundEnabled.set(value);

        if (!value) {
            stop();
        }
    }

    public void play(SoundEffect sound) {
        if (!soundEnabled.get())
            return;

        if(sound.isPlayImmediately())
            stop();

        soundQueue.add(sound.getSoundFile());
        playNext();
    }

    private void stop() {
        soundQueue.clear();
        cleanupPlayer();
    }

    private void playNext() {

        if (!soundEnabled.get() || playing || soundQueue.isEmpty())
            return;

        String filename = soundQueue.poll();

        try {

            Media sound = SoundLoader.getSound(filename);

            if (sound == null) {
                playNext();
                return;
            }

            mediaPlayer = new MediaPlayer(sound);

            playing = true;

            mediaPlayer.setOnEndOfMedia(() -> Platform.runLater(() -> {
                cleanupPlayer();
                playNext();
            }));

            mediaPlayer.setOnError(() -> Platform.runLater(() -> {
                DeepSeaLog.getInstance().log("Erro ao tocar som: " + filename);

                cleanupPlayer();
                playNext();
            }));

            mediaPlayer.play();

        } catch (Exception e) {

            DeepSeaLog.getInstance().log(
                    "Erro ao inicializar som: " + filename
            );

            cleanupPlayer();
            playNext();
        }
    }

    private void cleanupPlayer() {

        if (mediaPlayer != null) {

            mediaPlayer.stop();
            mediaPlayer.dispose();

            mediaPlayer = null;
        }

        playing = false;
    }
}