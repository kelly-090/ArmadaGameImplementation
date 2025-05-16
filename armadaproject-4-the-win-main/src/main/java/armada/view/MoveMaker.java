package armada.view;

import armada.model.Armada;
import armada.model.BoardSymbol;
import armada.model.Move;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class MoveMaker implements EventHandler<ActionEvent>{
    private Armada game;
    private Move move;
    private Button button;
    private ArmadaGUI gui;

    public MoveMaker(Armada game, Move move, Button button, ArmadaGUI gui){
        this.game = game;
        this.move = move;
        this.button = button;
        this.gui = gui;
    }

    @Override
    public void handle(ActionEvent event) {
        if (game.getSymbolAt(move.getRow(), move.getCol()) != BoardSymbol.BLANK) {
            return; // Ignore clicks on already filled buttons
        }

            if(game.makeMove(move)){
                gui.setStatus("Move placed at (" + move.getRow() + ", " + move.getCol() + ")");
            }
            gui.updateStatus();
            gui.updateButton(move);
    }
    }


