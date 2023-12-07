package algonquin.cst2335.team_project;
import java.util.Random;

/**
 * The {@code RandomInteger} class generates random integers within a specified range.
 */
public class RandomInteger {
    /**
     * Generates a random integer between 1 and 4 (inclusive).
     *
     * @return A random integer between 1 and 4.
     */
    public int generateRandomInt() {
        // Create a Random object
        Random random = new Random();
        int randomNumber = random.nextInt(4) + 1;
        // Print the random integer
        return randomNumber;
    }
}
