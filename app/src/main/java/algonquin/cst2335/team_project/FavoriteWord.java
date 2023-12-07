package algonquin.cst2335.team_project;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Entity class representing a favorite word.
 * Stores information such as the word itself and the date when it was saved.
 */
@Entity
public class FavoriteWord {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "word")
    private String word;

    @ColumnInfo(name = "saveDate")
    private String saveDate;

    /**
     * Constructor for creating a FavoriteWord instance.
     *
     * @param word     The favorite word to be saved.
     * @param saveDate The date when the word was saved.
     */
    public FavoriteWord(String word, String saveDate) {
        this.word = word;
        this.saveDate = saveDate;
    }

    /**
     * Gets the unique identifier (ID) of the favorite word.
     *
     * @return The ID of the favorite word.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier (ID) of the favorite word.
     *
     * @param id The ID to set for the favorite word.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the word that is saved as a favorite.
     *
     * @return The favorite word.
     */
    public String getWord() {
        return word;
    }

    /**
     * Gets the date when the word was saved.
     *
     * @return The date when the word was saved.
     */
    public String getSaveDate() {
        return saveDate;
    }

    /**
     * Sets the word to be saved as a favorite.
     *
     * @param word The word to be saved.
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Sets the date when the word was saved.
     *
     * @param saveDate The date when the word was saved.
     */
    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }
}
