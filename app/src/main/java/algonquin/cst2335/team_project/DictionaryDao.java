package algonquin.cst2335.team_project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

/**
 * Data Access Object (DAO) for interacting with the dictionary database.
 * Defines methods for inserting, querying, and deleting saved terms.
 */
@Dao
public interface DictionaryDao {
    /**
     * Inserts a new saved term into the database or replaces an existing one.
     *
     *   @param savedTerms The SavedTerms object to be inserted or replaced.
     *   @return The row ID of the inserted or replaced term.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SavedTerms savedTerms);

    /**
     * Retrieves all saved terms from the database.
     *
     * @return A list of all saved terms in the database.
     */
    @Query("SELECT * FROM SavedTerms")
    List<SavedTerms>getAllSavedTerms();

    /**
     * Deletes a specific saved term from the database.
     *
     * @param savedTerm The SavedTerms object to be deleted.
     */
    @Delete
    void delete(SavedTerms savedTerm);
}
