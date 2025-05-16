package armada.model;

import java.util.HashMap;
import java.util.Map;

public enum BoardSymbol {
    BLANK(' '),
    WATER('.'),
    SHIP('S');

    private char symbol;

    public static final Map<BoardSymbol, BoardSymbol> NEXT = new HashMap<>();

    static{
        NEXT.put(BLANK, SHIP);
        NEXT.put(SHIP, WATER);
        NEXT.put(WATER, BLANK);
    }
    
    private BoardSymbol(char symbol){
        this.symbol = symbol;
    }

    public char getSymbol(){
        return symbol;
    }
}
