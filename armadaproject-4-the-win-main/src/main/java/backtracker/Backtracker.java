package backtracker;

import java.util.*;

/**
 * This class represents the classic recursive backtracking algorithm.
 * It has a solver that can take a valid configuration and return a
 * solution, if one exists.
 * 
 * @author GCCIS Faculty
 */
public class Backtracker<C extends Configuration<C>> {
    /*
     * Should debug output be enabled?
     */
    private boolean debug;
    private int totalConfigs;
    private Set<C> solutions;
    
    /**
     * Initialize a new backtracker
     * 
     * @param debug Is debugging output enabled?
     */
    public Backtracker(boolean debug) {
        this.debug = debug;
        if (this.debug) {
            System.out.println("backtracker.Backtracker debugging enabled...");
        }
        this.totalConfigs = 0;
        this.solutions = new HashSet<>();
    }

    public int getTotalConfigs() {
        return totalConfigs;
    }

    public int getSolutionCount() {
        return solutions.size();
    }

    public Set<C> getSolutions() {
        return solutions;
    }

    /**
     * A utility routine for printing out various debug messages.
     * 
     * @param msg The type of config being looked at (current, goal, 
     *  successor, e.g.)
     * @param config The config to display
     */
    private void debugPrint(String msg, C config) {
        if (this.debug) {
            System.out.println(msg + ":\n" + config);
        }
    }
    
    /**
     * Try find a solution, if one exists, for a given configuration.
     * 
     * @param config A valid configuration
     * @return A solution config, or null if no solution
     */
    public C solve(C config) {
        debugPrint("Current config", config);
        if (config.isGoal()) {
            debugPrint("\tGoal config", config);
            return config;
        } else {
            for (C child : config.getSuccessors()) {
                if (child.isValid()) {
                    debugPrint("\tValid successor", child);
                    this.totalConfigs++;
                    C sol = solve(child);
                    if(sol != null) {
                        return sol;
                    }
                } else {
                    debugPrint("\tInvalid successor", child);
                }
            }
            // implicit backtracking happens here
        } 
        return null;
    }

    /**
     * Try to find ALL solutions, if one exists, for a given configuration.
     * 
     * @param config A valid configuration
     */
    public void solveAll(C config) {
        debugPrint("Current config", config);
        if (config.isGoal()) {
            debugPrint("\tGoal config", config);
            this.solutions.add(config);
            System.out.print(".");
        } else {
            for (C child : config.getSuccessors()) {
                if (child.isValid()) {
                    debugPrint("\tValid successor", child);
                    this.totalConfigs++;
                    solveAll(child);
                    // C sol = solveAll(child);  Don't return so it finds all possible unique solutions
                    // if(sol != null) {
                    //     return sol;
                    // }
                } else {
                    debugPrint("\tInvalid successor", child);
                }
            }
            // implicit backtracking happens here
        }
    }
}
/*
 What state will your configuration need to keep track of as it attempts to find a solution?


How will you make successor configurations?


How will you determine if a configuration is invalid?
If it is impossible to find a solution from this point.

How will you determine if the configuration is the goal?
If it is the valid solution to the problem.


Do you need to be concerned with cycles?  If so, how will you avoid them?

*/