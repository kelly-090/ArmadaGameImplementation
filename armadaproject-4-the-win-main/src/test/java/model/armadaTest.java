package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;

import armada.model.Armada;
import armada.model.ArmadaException;
import armada.model.Board;
import armada.model.BoardSymbol;
import armada.model.Move;

public class armadaTest {
    @Test
    public void testMove() {
        Move move = new Move(2, 3);
        assertEquals(move.getRow(), 2);
        assertEquals(move.getCol(), 3);
    }    

    @Test
    public void testMakeMove_InvalidMove() throws ArmadaException{
        Armada armada = new Armada("data/armada_10_in.txt");
        Move invalid = new Move(-1,-1);

        boolean execptionThrown = false;

        boolean actual = armada.makeMove(invalid);
        assertEquals(execptionThrown, actual,"Expected ArmadaException to be thrown.");
        
    }

    @Test
    public void testSetSymbolAt(){
        Board board = new Board("data/armada_09_in.txt");
        board.setSymbolAt(BoardSymbol.SHIP, 2, 2);
        assertEquals(BoardSymbol.SHIP, board.getSymbolAt(2, 2));
    }


    @Test
    public void testBoardInitialization(){
        Board board = new Board("data/armada_09_in.txt");
        assertNotNull(board);
    }

    @Test
    public void testBoardSize(){
        Board board = new Board("data/armada_09_in.txt");
        assertEquals(6, board.getSize());
    }
}
