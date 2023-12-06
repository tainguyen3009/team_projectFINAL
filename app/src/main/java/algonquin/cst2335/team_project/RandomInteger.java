package algonquin.cst2335.team_project;
import java.util.Random;

public class RandomInteger {
    public int generateRandomInt() {
        // Create a Random object
        Random random = new Random();
        int randomNumber = random.nextInt(4) + 1;
        // Print the random integer
        return randomNumber;
    }
}
