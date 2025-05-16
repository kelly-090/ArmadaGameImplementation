package armada.view;

import armada.model.Armada;
import armada.model.ArmadaException;
import armada.model.ArmadaSolver;
import armada.model.BoardSymbol;
import armada.model.Move;
import backtracker.Backtracker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ArmadaGUI extends Application{
    private Armada game;
    private Button[][] buttons;
    private Button resetButton;
    private Button hintButton;
    private Button solveButton;
    private Button nextButton;
    private Label statusLabel;
    private Label datafile;
    public static final Image WATER = new Image("file:data/armadablue.png");
    public static final Image SHIP = new Image("file:data/armadared.png");
    public static final Image BLANK = new Image("file:data/armadawhite.jpeg");

    public static int fileint = 10;
    private String[] filearray = 
    {
     "data/armada_10_in.txt", 
     "data/armada_09_in.txt", 
     "data/armada_08_in.txt", 
     "data/armada_07_in.txt",
     "data/armada_06_in.txt",
     "data/armada_05_in.txt",
     "data/armada_04_in.txt",
     "data/armada_03_in.txt",
     "data/armada_02_in.txt",
     "data/armada_01_in.txt",
     "data/armada_00_in.txt"
    };

    @Override
    public void start(Stage primaryStage){
        String filename = "data/armada_10_in.txt";
        game = new Armada(filename);
        game.registerObserver(move -> updateButton(move));

        HBox root = new HBox();
        VBox console = new VBox();

        resetButton = new Button("reset");
        resetButton.setOnAction(e -> {
            String currFile = filearray[fileint];
            game = new Armada(currFile);
            game.registerObserver(move -> updateButton(move));

            for(int r = 0; r < game.getSize(); r++){
                for(int c = 0; c < game.getSize(); c++){
                    Move move = new Move(r, c);
                    buttons[r][c].setOnAction(new MoveMaker(game, move, buttons[r][c], this));
                    buttons[r][c].setStyle("");
                }
            }
            resetBoard();
            setStatus("Game reset!");
        });

        hintButton = new Button("hint");
        hintButton.setOnAction(e -> {
            Move hint = game.getHintMove();
            if (hint != null) {
                // Highlight hint visually
                buttons[hint.getRow()][hint.getCol()].setStyle("-fx-border-color: green; -fx-border-width: 3px;");
                statusLabel.setText("Try placing a ship at: (" + hint.getRow() + ", " + hint.getCol() + ")");
            } else {
                statusLabel.setText("No valid moves left!");
            }
        });

        nextButton = new Button("Next");
        nextButton.setOnAction( e -> {
            game = new Armada();
            fileint -- ;
            game.setFilename(filearray[fileint]);
            game.registerObserver(move -> updateButton(move));
        });


        solveButton = new Button("Solve");
        solveButton.setOnAction(e -> {
            resetButton.setDisable(true);
            hintButton.setDisable(true);
            nextButton.setDisable(true);
            solveButton.setDisable(true);

            ArmadaSolver solver = new ArmadaSolver(game);

            new Thread(() -> {
                ArmadaSolver solution = solver.solve(game);
                if(solution == null || solution.getMoves().isEmpty()){
                    setStatus("No solution found.");
                }
                else{
                    for(Move move : solution.getMoves()){
                        updateButton(move);
                    }
                    setStatus("Solution found!");
                }

                resetButton.setDisable(false);
                hintButton.setDisable(false);
                nextButton.setDisable(false);
                solveButton.setDisable(false);


            }).start();
        });


        // // Solve button
        // Button solveButton = new Button("Solve");
        // solveButton.setFont(new Font("Courier New", 20));
        // solveButton.setPrefSize(100, 60);
        // solveButton.setOnAction((ActionEvent event) -> {game = new Armada(filename);} );


        // GridPane for game board
        GridPane grid = new GridPane();
        buttons = new Button[game.getSize()][game.getSize()];
        // buttons for game board
        for (int row = 0; row < game.getSize(); row++) {
            for (int col = 0; col < game.getSize(); col++) {
                buttons[row][col] = makeButton(row, col);
                grid.add(buttons[row][col], col, row);
            }
        }

            // Add row count labels at the end of each row
        for (int row = 0; row < game.getSize(); row++) {
            Label rowLabel = new Label(String.valueOf(game.getRowCounts()[row]));
            rowLabel.setPadding(new Insets(1));
            grid.add(rowLabel, game.getSize()+1, row);  // one column after the buttons
            GridPane.setHalignment(rowLabel, HPos.CENTER);
        }

        // Add column count labels below each column
        for (int col = 0; col < game.getSize(); col++) {
            Label colLabel = new Label(String.valueOf(game.getColCounts()[col]));
            colLabel.setPadding(new Insets(1));
            grid.add(colLabel, col, game.getSize()+1);  // one row below the buttons
            GridPane.setHalignment(colLabel, HPos.CENTER);
        }


        for (int r = 0; r < game.getSize(); r++) {
            for (int c = 0; c < game.getSize(); c++) {
                if (game.getSymbolAt(r, c) != BoardSymbol.BLANK) {
                    updateButton(new Move(r, c));
                }
            }
        }


        statusLabel = new Label("New Game has started!");

        console.getChildren().addAll(statusLabel,resetButton,hintButton,solveButton, nextButton);
        root.getChildren().addAll(grid,console);

        
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setTitle("Armada");
        primaryStage.show();
    }

    private Button makeButton(int row, int col){
        Button button = new Button();
        ImageView imageView = new ImageView();
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setImage(BLANK);

        Move move = new Move(row, col);

        button.setGraphic(imageView);
        button.setMinSize(100, 100);
        button.setOnAction(new MoveMaker(game, move, button, this));
        
        return button;
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }

    public void updateStatus(){
        if (game.getBoard().boardFull()){
            statusLabel.setText("You win!");
        }
    }

    public void updateButton(Move move){
        ImageView imageView = (ImageView) buttons[move.getRow()][move.getCol()].getGraphic();

        if (game.getSymbolAt(move.getRow(), move.getCol()) == BoardSymbol.SHIP){
            imageView.setImage(SHIP);
        }
        else if (game.getSymbolAt(move.getRow(), move.getCol()) == BoardSymbol.WATER){
            imageView.setImage(WATER);
        }
    }

    public void resetBoard() {
        for (int row = 0; row < game.getSize(); row++) {
            for (int col = 0; col < game.getSize(); col++) {
                BoardSymbol symbol = game.getSymbolAt(row, col);
                ImageView imageView = (ImageView) buttons[row][col].getGraphic();
    
                if (symbol == BoardSymbol.SHIP) {
                    imageView.setImage(SHIP);
                } else if (symbol == BoardSymbol.WATER) {
                    imageView.setImage(WATER);
                } else {
                    imageView.setImage(BLANK);
                }
            }
        }
        updateStatus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}