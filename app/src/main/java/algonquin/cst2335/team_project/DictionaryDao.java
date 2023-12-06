package algonquin.cst2335.team_project;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;
@Dao
public interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(SavedTerms savedTerms);

    @Query("SELECT * FROM SavedTerms")
    List<SavedTerms>getAllSavedTerms();

    @Delete
    void delete(SavedTerms savedTerm);
}
