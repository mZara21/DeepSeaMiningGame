package pt.isec.pa.deepsea.ui;

import javafx.application.Platform;
import javafx.scene.control.Button;
import pt.isec.pa.deepsea.model.DeepSeaManager;
import pt.isec.pa.deepsea.ui.res.SoundManager;

/**
 * Painel lateral direito utilizado na fase de Oficina.
 * <p>
 * Apresenta a interface de gestão e manutenção dos drones, incluindo
 * botões para abastecer, reparar a integridade e aplicar melhorias (upgrades)
 * ao tanque de combustível ou ao casco, além da opção de sair da oficina.
 * </p>
 */
public class RightPanelOficina extends RightPanel {
    private Button btnSair;
    private Button btnReparar;
    private Button btnMelhorCombustivel;
    private Button btnMelhorCasco;
    private Button btnAbastecer;


    public RightPanelOficina(DeepSeaManager m) {
        super(m);

        createViews();
        registerHandles();
    }

    private void createViews() {
        DroneCardsBox droneCardsBox = new DroneCardsBox(manager);

        btnAbastecer = new Button("ABASTECER");
        btnAbastecer.getStyleClass().addAll("botao-base","botao-destaque","botao-oficina");

        btnReparar = new Button("REPARAR");
        btnReparar.getStyleClass().addAll("botao-base","botao-destaque", "botao-oficina");

        btnMelhorCombustivel = new Button("MELHORAR TANQUE (" + manager.getCustoMelhoria() + ")");
        btnMelhorCombustivel.getStyleClass().addAll("botao-base","botao-destaque", "botao-oficina");

        btnMelhorCasco = new Button("MELHORAR CASCO (" + manager.getCustoMelhoria() + ")");
        btnMelhorCasco.getStyleClass().addAll("botao-base","botao-destaque", "botao-oficina");

        btnSair = new Button("SAIR DA OFICINA");
        btnSair.getStyleClass().addAll("botao-base","botao-sair", "botao-oficina");

        this.getChildren().addAll(droneCardsBox, btnAbastecer, btnReparar, btnMelhorCombustivel, btnMelhorCasco, btnSair);
    }

    private void registerHandles() {

        btnAbastecer.setOnAction(e -> {
            boolean sucesso = manager.abastecer();
            if(sucesso)
                SoundManager.getInstance().play(SoundEffect.ABASTECE_DRONE);
            else
                SoundManager.getInstance().play(SoundEffect.ERRO_OFICINA);

        });

        btnReparar.setOnAction(e -> {
            boolean sucesso = manager.reparar();
            if(sucesso)
                SoundManager.getInstance().play(SoundEffect.REPARA_DRONE);
            else
                SoundManager.getInstance().play(SoundEffect.ERRO_OFICINA);
        });

        btnMelhorCombustivel.setOnAction(e -> {
            boolean sucesso = manager.melhorar("COMBUSTIVEL");
            if(sucesso)
                SoundManager.getInstance().play(SoundEffect.MELHORA_DRONE);
            else
                SoundManager.getInstance().play(SoundEffect.ERRO_OFICINA);
        });

        btnMelhorCasco.setOnAction(e -> {
            boolean sucesso = manager.melhorar("CASCO");
            if(sucesso)
                SoundManager.getInstance().play(SoundEffect.MELHORA_DRONE);
            else
                SoundManager.getInstance().play(SoundEffect.ERRO_OFICINA);

        });

        // Botão para sair (mesma lógica do ESC no Canvas)
        btnSair.setOnAction(e -> manager.fechar());
    }
}