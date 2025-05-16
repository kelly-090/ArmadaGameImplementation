package armada.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Board {
    private int size;
    private BoardSymbol[][] board ;
    private int[] rowCounts ;
    private int[] colCounts;

    public Board(String filename){
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            int i =0;
            while( scanner.hasNext()){
                String input = scanner.nextLine();
            // does " " recognize next line symbol?
                if(i==0){
                    String[] tokens = input.split(" ");
                    size = Integer.parseInt(tokens[0]);
                    rowCounts = new int[size];
                    colCounts = new int[size];
                    board = new BoardSymbol[size][size];
                }
                else if(i==1){
                    String[] tokens = input.split(" ");
                    for(int j =0; j< tokens.length; j++){
                        rowCounts[j] = Integer.parseInt(tokens[j]);
                    }
                }
                else if(i==2){
                    String[] tokens = input.split(" ");
                    for(int j =0; j< tokens.length; j++){
                        colCounts[j] = Integer.parseInt(tokens[j]);
                    }
                }
                else if(i>=3){
                    int k =0;
                    for ( char c : input.toCharArray() ){
                        if ( c == '.'){
                            board[i-3][k] = BoardSymbol.WATER ;
                        }
                        else if ( c == 'S'){
                            board[i-3][k] = BoardSymbol.SHIP;
                        }
                        else {
                            board[i-3][k] = BoardSymbol.BLANK ;
                        }
                        k++;
                    }
                    
                    //( k<this.size){
                    //    if(input.charAt(k) == '.'){
                    //        board[i-3][k] = BoardSymbol.WATER ;
                    //    } else if(input.charAt(k) == 'S'){
                    //        board[i-3][k] = BoardSymbol.SHIP;
                    //    }
                    //    else {
                    //        board[i-3][k] = BoardSymbol.BLANK ;
                    //    }
                    //    k++;
                    //}
                }
                i++;
            }
            scanner.close();
        } catch (FileNotFoundException e){
            System.out.println("File not found: " + e.getMessage());
        }
    };

    public Board(Board other) {
    this.size = other.size;
    this.board = new BoardSymbol[other.size][other.size];
    for (int r = 0; r < size; r++) {
        for (int c = 0; c < size; c++) {
            this.board[r][c] = other.board[r][c]; // copy symbols
        }
    }
    this.rowCounts = Arrays.copyOf(other.rowCounts, other.rowCounts.length);
    this.colCounts = Arrays.copyOf(other.colCounts, other.colCounts.length);
}

    public int getSize(){
        return size;
    }

    public int getRowCount(int r){
        return rowCounts[r];
    }

    public int[] getRowCounts(){
        return rowCounts;
    }

    public int getColCount(int c){
        return colCounts[c];
    }

    public int[] getColCounts(){
        return colCounts;
    }
    
    public BoardSymbol getSymbolAt(int r, int c){
        return board[r][c];
    }

    public boolean boardFull(){
        for ( int i =0; i<board.length; i++){
            for( int j=0; j<board[0].length; j++){
                if ( board[i][j] == BoardSymbol.BLANK){
                    return false;
                }
            }
        }
        return true;
    }

    public void setSymbolAt(BoardSymbol symbol, int r, int c){
        board[r][c] = symbol;
    }


    @Override
    public String toString(){
        String str = "";
        for ( int i =0; i<board.length; i++){
            for( int j=0; j<board[0].length; j++){
                str += board[i][j].getSymbol()+ " " ;
            }
            str += rowCounts[i];
            str += "\n";
        }

        for ( int k=0; k<colCounts.length; k++){
            str += colCounts[k] + " ";
        }
        return str;
    }

    public static void main(String[] args) {
        Board board = new Board("./data/armada_01_in.txt");

        board.setSymbolAt(BoardSymbol.WATER, 0, 0);
        board.setSymbolAt(BoardSymbol.SHIP, 0, 1);
        board.setSymbolAt(BoardSymbol.BLANK, 0, 2);
        board.setSymbolAt(BoardSymbol.WATER, 1, 0);
        board.setSymbolAt(BoardSymbol.SHIP, 1, 1);
        board.setSymbolAt(BoardSymbol.BLANK, 1, 2);
        board.setSymbolAt(BoardSymbol.WATER, 2, 0);
        board.setSymbolAt(BoardSymbol.SHIP, 2, 1);
        board.setSymbolAt(BoardSymbol.BLANK, 2, 2);

        System.out.println(board);
    }
}
