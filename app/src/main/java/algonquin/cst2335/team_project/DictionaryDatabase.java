package algonquin.cst2335.team_project;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/**
 * Database class for the dictionary app.
 * Extends RoomDatabase and defines the entities and version.
 */
@Database(entities = {SavedTerms.class}, version = 1, exportSchema = false)
public abstract class DictionaryDatabase extends RoomDatabase {

    /**
     * Provides access to the Data Access Object (DAO) for interacting with the database.
     *
     * @return An instance of the DictionaryDao interface.
     */
    public abstract DictionaryDao dicDao();


}
