package algonquin.cst2335.team_project;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
import java.util.List;

public class DictionaryViewModel extends ViewModel {

    private MutableLiveData<List<FavoriteWord>> favoriteWordsList = new MutableLiveData<>();

    public DictionaryViewModel() {
        // Initialize the MutableLiveData with an empty list
        favoriteWordsList.setValue(new ArrayList<>());
    }

    // Getter for observing the LiveData in the UI
    public MutableLiveData<List<FavoriteWord>> getFavoriteWordsList() {
        return favoriteWordsList;
    }

    // Method to add a FavoriteWord to the list
    public void addFavoriteWord(FavoriteWord favoriteWord) {
        List<FavoriteWord> currentList = favoriteWordsList.getValue();
        if (currentList != null) {
            currentList.add(favoriteWord);
            favoriteWordsList.setValue(currentList);
        }
    }

    // Method to remove a FavoriteWord from the list
    public void removeFavoriteWord(FavoriteWord favoriteWord) {
        List<FavoriteWord> currentList = favoriteWordsList.getValue();
        if (currentList != null) {
            currentList.remove(favoriteWord);
            favoriteWordsList.setValue(currentList);
        }
    }
}
