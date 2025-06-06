package armada.model;

public class Move {
    private int row;
    private int col;

    public Move(int row, int col){
        this.row = row;
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public String toString(){
        return "Move: R-" + row + ", C-" + col;
    }

}