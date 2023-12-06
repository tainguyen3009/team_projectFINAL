package algonquin.cst2335.team_project;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;
public class DictionaryViewModel extends ViewModel {
    public MutableLiveData<ArrayList<FavoriteLocation>> savedTerms = new MutableLiveData<>();
}
