import java.util.Scanner;
import java.util.Random;
import java.time.LocalDate;

/**
 * Simulates forestry operations like adding, cutting, and reaping trees in a forest.
 */
public class ForestrySimulation {
    private static final Scanner scanner = new Scanner(System.in);
    private static Forest currentForest;
    private static final String FOREST_SAVE_EXTENSION = ".db";

    /**
     * Main method to start the simulation.
     * @param args Command-line arguments containing names of forests to initialize from
     */
    public static void main(String[] args) {
        System.out.println("Welcome to the Forestry Simulation");
        System.out.println("----------------------------------");

        for (String forestName : args) {
            System.out.println("Initializing from " + forestName);
            Forest forest = new Forest(forestName);
            if (forest.loadForest()) {
                currentForest = forest;
                simulateForest();
                currentForest = null; // Reset current forest to None
            }
        }

        System.out.println("Exiting the Forestry Simulation");
    }

    /**
     * Simulates operations in the current forest until the user chooses to exit.
     */
    private static void simulateForest() {
        String input;
        do {
            System.out.println("(P)rint, (A)dd, (C)ut, (G)row, (R)eap, (S)ave, (L)oad, (N)ext, e(X)it : ");
            input = scanner.nextLine().trim().toUpperCase();
            switch (input) {
                case "P":
                    currentForest.printForest();
                    break;
                case "A":
                    addTree();
                    break;
                case "C":
                    cutTree();
                    break;
                case "G":
                    currentForest.simulateYearlyGrowth();
                    break;
                case "R":
                    reapForest();
                    break;
                case "S":
                    currentForest.saveForest();
                    break;
                case "L":
                    loadForest();
                    break;
                case "N":
                    return; // Move to the next forest
                case "X":
                    return; // Exit the method and program
                default:
                    System.out.println("Invalid menu option, try again");
                    break;
            }
        } while (!input.equals("X"));
    }


    /**
     * Adds a new random tree to the current forest.
     */
    private static void addTree() {
        Random random = new Random();
        TreeSpecies randomSpecies = TreeSpecies.getRandomSpecies();
        int year = LocalDate.now().getYear() - random.nextInt(20);
        double height = random.nextDouble() * 100;
        double growthRate = random.nextDouble() * 20;
        Tree newTree = new Tree(randomSpecies, year, height, growthRate);
        currentForest.addTree(newTree);
        System.out.println("New random tree added successfully!");
    }

    /**
     * Cuts down a tree in the current forest based on user input.
     */
    private static void cutTree() {
        System.out.print("Index of tree to cut down? ");
        int index = getIntInput();
        if (index >= 0 && index < currentForest.getTrees().size()) {
            boolean success = currentForest.cutTree(index);
            if (success) {
                System.out.println("Tree at index " + index + " cut down successfully!");
            } else {
                System.out.println("Failed to cut down tree at index " + index + ".");
            }
        } else {
            System.out.println("Invalid tree index.");
        }
    }

    /**
     * Reaps trees in the current forest taller than a specified height.
     */
    private static void reapForest() {
        System.out.print("Enter the height threshold for reaping: ");
        double heightThreshold = getDoubleInput();
        currentForest.reapForest(heightThreshold);
        System.out.println("Forest reaped successfully!");
    }

    /**
     * Loads a forest from file based on user input.
     */
    private static void loadForest() {
        System.out.print("Enter the name of the forest to load: ");
        String forestName = scanner.nextLine();
        forestName += FOREST_SAVE_EXTENSION;
        Forest loadedForest = Forest.loadFromFile(forestName);
        if (loadedForest != null) {
            currentForest = loadedForest;
            System.out.println("Forest loaded successfully!");
        } else {
            System.out.println("Old forest retained.");
        }
    }

    // Utility methods for input handling

    private static int getIntInput() {
        System.out.print("Enter an integer: ");
        if (scanner.hasNextInt()) {
            int input = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
            return input;
        } else {
            System.out.println("Invalid input. ");
            scanner.nextLine(); // Consume invalid input
            return getIntInput(); // Calls function until valid input is provided
        }
    }

    private static double getDoubleInput() {
        System.out.print("Enter a number: ");
        if (scanner.hasNextDouble()) {
            double input = scanner.nextDouble();
            scanner.nextLine(); // Consume newline character
            return input;
        } else {
            System.out.println("Invalid input. ");
            scanner.nextLine(); // Consume invalid input
            return getDoubleInput(); // Recursive call until valid input is provided
        }
    }
}
