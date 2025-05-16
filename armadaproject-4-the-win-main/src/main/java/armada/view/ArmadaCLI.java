package armada.view;

import java.util.Scanner;

import armada.model.Armada;
import armada.model.ArmadaException;
import armada.model.ArmadaSolver;
import armada.model.Move;

public class ArmadaCLI{

    public ArmadaCLI() throws ArmadaException{
        System.out.print("Puzzle filename: ");
        Scanner scan = new Scanner(System.in);
        String filename = scan.nextLine();
        
        Armada armada = new Armada(filename);
        while(true){
            System.out.println(armada.getBoard());
            System.out.println("Moves: " + armada.getMoveCount());
            System.out.print("Command : ");
            String input = scan.nextLine();
            String[] tokens = input.split(" ");
            if(tokens[0].equals("help")){
                System.out.println("""
                    help - this help menu
                    board - display current board
                    reset - reset current puzzle to the start
                    new <puzzle_filename> - start a new puzzle
                    move <row> <col> - place a ship piece at <row>, <col>
                    hint - get a valid move, if one exists
                    solve - solve the current game state
                    quit - quit
                """
                );
            }
            else if(tokens[0].equals("board")){
                System.out.println(armada.getBoard());
            }
            else if(tokens[0].equals("hint")){
                Move hint = new Move(0, 0);
                System.out.println("Try: " + hint);
            }
            else if(tokens[0].equals("reset")){
                armada = new Armada(filename);
            }
            else if(tokens[0].equals("new")){
                armada = new Armada(tokens[1]);
            }
            else if(tokens[0].equals("move")){
                int r = Integer.parseInt(tokens[1]);
                int c = Integer.parseInt(tokens[2]);
                Move move = new Move(r, c);
                Boolean moveapprove = armada.makeMove(move);
                if(moveapprove == true){
                    System.out.println("Moves: " + armada.getMoveCount());
                }
                if(armada.getBoard().boardFull()){
                    System.out.println("Congratulations, you have won!");
                }
                
            }
            else if(tokens[0].equals("quit")){
                System.out.println("Goodbye!");
                scan.close();
                break;
            }
            else if(tokens[0].equals("solve")){
                ArmadaSolver.solve(armada);   
            }
            else {
                System.out.println("Please input valid command.");
            }
        }
    }
    public static void main(String[] args) throws ArmadaException{
        
        ArmadaCLI arCLI = new ArmadaCLI();
    }
}
