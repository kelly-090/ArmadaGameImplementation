package armada.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import backtracker.Backtracker;
import backtracker.Configuration;

public class ArmadaSolver implements Configuration<ArmadaSolver>{
    private Armada armada;
    private Set<String> seen;
    private List<Move> moves;

    public ArmadaSolver(Armada armada){
        this(armada, generateAllMoves(armada.getSize()), new HashSet<>());
    }

    private ArmadaSolver(Armada armada, List<Move> remainingMoves, Set<String> seen){
        this.armada = armada;
        this.moves = remainingMoves;
        this.seen = seen;

    }

    private static List<Move> generateAllMoves(int size) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                moves.add(new Move(i, j));
            }
        }
        return moves;
    }

    public Collection<ArmadaSolver> getSuccessors(){
        Collection<ArmadaSolver> successors = new ArrayList<>();

        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            Armada newArmada = new Armada(armada); // deep copy
            if (!newArmada.makeMove(move)) continue;

            Set<String> newSeen = new HashSet<>(seen);
            newSeen.add(newArmada.getBoard().toString());
            List<Move> newMoves = new ArrayList<>();
                for (Move m : moves) {
                    if (!m.equals(move)) {
                        newMoves.add(m);
                    }
                }
            successors.add(new ArmadaSolver(newArmada, newMoves, newSeen));
            ArmadaSolver successor = new ArmadaSolver(newArmada, newMoves, newSeen);
            if (successor.isValid()) {
                successors.add(successor);
            }
                
                }

        return successors;
    }

    public boolean isValid(){

        String key = armada.getBoard().toString();
        return !seen.contains(key);
    }

    public boolean isGoal(){
        return armada.getBoard().boardFull();
    }

    public List<Move> getMoves(){
        return moves;
    }

    @Override
    public String toString(){
        return armada.getBoard().toString();
    }
    
    public static ArmadaSolver solve(Armada armada){
        Backtracker<ArmadaSolver> backtracker = new Backtracker<>(true);
        ArmadaSolver solution = backtracker.solve(new ArmadaSolver(armada));
        if(solution == null){
            System.out.println("No solution.");
        }
        else{
            System.out.println("Solved!");
        }
        return solution;
    }

public static void main(String[] args) {
    //testing
    Armada armada = new Armada("data/armada_08_in.txt");
    ArmadaSolver solution = solve(armada);
    System.out.println(solution);
}


}
