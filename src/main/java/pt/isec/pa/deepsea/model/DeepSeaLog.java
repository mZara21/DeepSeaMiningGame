package pt.isec.pa.deepsea.model;

import static pt.isec.pa.deepsea.model.data.Constantes.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementação do padrão Singleton para registo (log) de eventos do jogo.
 * <p>
 * Regista todas as ações e transições realizadas sobre o modelo de dados,
 * anexando uma marca temporal (timestamp) automática a cada mensagem.
 * Permite a exportação do histórico para um ficheiro de texto.
 * </p>
 *
 * @author Miguel Zara
 */

public class DeepSeaLog {
    private static DeepSeaLog instance;
    private final List<String> logs;

    /**
     * Construtor privado para garantir o padrão Singleton.
     * Inicializa a lista de logs em memória.
     */
    private DeepSeaLog(){
        logs = new ArrayList<>();
    }

    /**
     * Obtém a instância única e global do sistema de Logs.
     * @return A instância atual de DeepSeaLog (cria uma nova se ainda não existir).
     */
    public static DeepSeaLog getInstance(){
        if (instance == null)
            instance = new DeepSeaLog();
        return instance;
    }

    /**
     * Adiciona uma nova mensagem ao histórico de eventos do jogo.
     * <p>
     * A mensagem é automaticamente prefixada com a data e hora atuais
     * no formato [yyyy-MM-dd HH:mm:ss] e impressa na consola para debug.
     * </p>
     *
     * @param msg A mensagem de texto descrevendo o evento a registar.
     */
    public void log(String msg){
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String linha = String.format("[%s] %s", timestamp, msg);
        logs.add(linha);

        // System.out.println(linha);
    }

    /**
     * Exporta todo o histórico de logs em memória para um ficheiro de texto físico.
     * O nome do ficheiro de destino é definido pelo utilizador.
     * Em caso de erro de I/O (Input/Output), imprime a mensagem de erro na consola de erros (System.err).
     */
    public void saveToTextFile(File file) {
        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {
            for (String line : logs) out.println(line);
        } catch (IOException e) {
            System.err.println("Erro ao gravar log: " + e.getMessage());
        }
    }
    /**
     * Limpa o histórico atual de logs em memória.
     * Útil quando se inicia um jogo novo ou se carrega um jogo previamente guardado.
     */
    public void clear() {
        logs.clear();
    }

    /**
     * Retorna uma cópia imutável do histórico de logs.
     * Garante que as classes externas podem ler os logs, mas não podem corromper a lista original.
     * @return Uma lista não modificável de Strings contendo os logs.
     */
    public List<String> getLogs() {
        return List.copyOf(logs);
    }
}
