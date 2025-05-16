package armada.model;

import java.util.HashMap;
import java.util.Map;

public class Armada{
    private Board board;
    private int moveCount;
    private Map<Move, Boolean> moveHistory;
    private ArmadaObserver observer;
    private String filename;

    public Armada(String filename){
        this.filename = filename;
        this.board = new Board(filename);
        this.moveCount = 0;
        this.moveHistory = new HashMap<>();
    }

    public Armada(){
        this.board = new Board(filename);
        this.moveCount = 0;
        this.moveHistory = new HashMap<>();
    }

    public Armada(Armada other) {
        this.board = new Board(other.board); // Assumes Board has a copy constructor
        this.moveCount = other.moveCount;
        this.moveHistory = new HashMap<>(other.moveHistory); // Deep copy of move history
        this.observer = null; // DO NOT copy observer
    }

    public Map<Move, Boolean> getMoveHis(){
        return moveHistory;
    }

    public void setFilename(String filename){
        this.filename = filename;
    }

    public void registerObserver(ArmadaObserver observer){
        this.observer = observer;
    }

    private void notifyObserver(Move move){
        if(observer != null){
            observer.updateBoard(move);
        }
    }
    // if we update the board, 
    // We should change its graphic according to its boardsymbol,,,
    

    public Board getBoard(){
        return board;
    }

    public void setMoveCount(int num){
        this.moveCount = num;
    }
    

    public int getMoveCount(){
        return moveCount;
    }

    public int getSize(){
        return board.getSize();
    } 

    public BoardSymbol getSymbolAt(int row, int col){
        return board.getSymbolAt(row, col);
    }

    public int[] getRowCounts(){
        return board.getRowCounts();
    }

    public int[] getColCounts(){
        return board.getColCounts();
    }

    public Move getHintMove() {
    for (int row = 0; row < board.getSize(); row++) {
        for (int col = 0; col < board.getSize(); col++) {
            Move move = new Move(row, col);
            if (isValidMove(move)) {
                return move;
            }
        }
    }
    return null; // No valid move found
    }

    // helper method to test move validity
    private boolean isValidMove(Move move) {
        int row = move.getRow();
        int col = move.getCol();

        if (row < 0 || row >= board.getSize() || col < 0 || col >= board.getSize()) return false;
        if (board.getSymbolAt(row, col) != BoardSymbol.BLANK) return false;

        int shipsInRow = 0;
        int shipsInCol = 0;
        for (int i = 0; i < board.getSize(); i++) {
            if (board.getSymbolAt(row, i) == BoardSymbol.SHIP) shipsInRow++;
            if (board.getSymbolAt(i, col) == BoardSymbol.SHIP) shipsInCol++;
        }

        if (shipsInRow >= board.getRowCount(row)) return false;
        if (shipsInCol >= board.getColCount(col)) return false;

        // Check diagonal adjacency
        int[] dRow = {-1, -1, 1, 1};
        int[] dCol = {-1, 1, -1, 1};
        for (int i = 0; i < 4; i++) {
            int newRow = row + dRow[i];
            int newCol = col + dCol[i];
        if (newRow >= 0 && newRow < board.getSize() && newCol >= 0 && newCol < board.getSize()) {
            if (board.getSymbolAt(newRow, newCol) == BoardSymbol.SHIP) return false;
        }
    }

       return true;
    }


    public void rotatePiece(Move move){
        int row = move.getRow();
        int col = move.getCol();
        board.setSymbolAt(BoardSymbol.NEXT.get(board.getSymbolAt(row, col)), row, col);
    }

    public void reset() {
        this.board = new Board(filename); // Reload the board from file
        this.moveCount = 0;
        this.moveHistory.clear();
        if (observer != null) {
            observer.updateBoard(null); // Notify full refresh
        }
    }


    public void autoFillWater() {
        for (int r = 0; r < board.getSize(); r++) {
            for (int c = 0; c < board.getSize(); c++) {
                if (board.getSymbolAt(r, c) == BoardSymbol.BLANK) { //refactoring autofillwater to fill spots with accurate number of ships in col AND row
                    int shipsInRow = 0;
                    int shipsInCol = 0;
                    
                    for (int i = 0; i < board.getSize(); i++) {
                        if (board.getSymbolAt(r, i) == BoardSymbol.SHIP) {
                            shipsInRow++;
                        }
                        if (board.getSymbolAt(i, c) == BoardSymbol.SHIP) {
                            shipsInCol++;
                        }
                    }
                    
                    if (shipsInRow == getRowCounts()[r] && shipsInCol == getColCounts()[c]) {
                        board.setSymbolAt(BoardSymbol.WATER, r, c);
                        notifyObserver(new Move(r, c));
                    }
                }
            }
        }
    }

    public boolean makeMove(Move move) {
        int row = move.getRow();
        int col = move.getCol();

        int shipsInRow = 0;
        int shipsInCol = 0;

        for(int i = 0; i < board.getSize(); i ++){
            if (board.getSymbolAt(row, i).equals(BoardSymbol.SHIP)){
                shipsInRow++;
            }
            if (board.getSymbolAt(i, col).equals(BoardSymbol.SHIP)){
                shipsInCol++;
            }
        }
        try{
            if (moveHistory.containsKey(move)) {
                throw new ArmadaException("Move at (" + row + ", " + col + ") has already been made.");
            }
            
            if (row < 0 || row >= board.getSize() || col < 0 || col >= board.getSize()) {
                throw new ArmadaException("Move out of bounds.");
            }
            
            if (board.getSymbolAt(row, col) != BoardSymbol.BLANK) {
                throw new ArmadaException("Space not empty.");
            }
            
            if (shipsInRow >= board.getRowCount(row)) {
                throw new ArmadaException("Too many ships in row " + row);
            }
            
            if (shipsInCol >=board.getColCount(col)) {
                throw new ArmadaException("Too many ships in column " + col);
            }

            // Check for diagonal adjacency
            int[] dRow = {-1, -1, 1, 1};
            int[] dCol = {-1, 1, -1, 1};
            for (int i = 0; i < 4; i++) {
                int newRow = row + dRow[i];
                int newCol = col + dCol[i];
                if (newRow >= 0 && newRow < board.getSize() && newCol >= 0 && newCol < board.getSize()) {
                    if (board.getSymbolAt(newRow, newCol) == BoardSymbol.SHIP) {
                        throw new ArmadaException("Cannot place a ship diagonally adjacent to another ship.");
                    }
                }
            }
            
            board.setSymbolAt(BoardSymbol.SHIP, row, col);
            moveHistory.put(move, true);
            autoFillWater();
            moveCount++;
            notifyObserver(move);
            return true;

            
        }
        catch(ArmadaException e){
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    

    public String toString(){
        return board.toString();
    }
}
