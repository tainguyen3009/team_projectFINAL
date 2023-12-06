package algonquin.cst2335.team_project;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {SavedTerms.class}, version = 1, exportSchema = false)
public abstract class DictionaryDatabase extends RoomDatabase {

    public abstract DictionaryDao dicDao();


}
