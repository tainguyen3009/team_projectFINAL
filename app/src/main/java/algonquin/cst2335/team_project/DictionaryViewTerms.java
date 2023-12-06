package algonquin.cst2335.team_project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.team_project.databinding.ActivityDictionaryViewTermsBinding;


public class DictionaryViewTerms extends AppCompatActivity {


    private ActivityDictionaryViewTermsBinding binding;


    private DictionaryDao dicDao;
    private List<SavedTerms> savedTerms = new ArrayList<>();
    private DictionaryAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDictionaryViewTermsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Room database and DAO
        DictionaryDatabase db = Room.databaseBuilder(getApplicationContext(), DictionaryDatabase.class, "dictionaryData")
                .fallbackToDestructiveMigration()
                .build();
        dicDao = db.dicDao();

        // Initialize RecyclerView and its adapter
        binding.myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new DictionaryAdapter();
        binding.myRecyclerView.setAdapter(myAdapter);

        // Retrieve data from the intent
        String word = getIntent().getStringExtra("word");
        ArrayList<SavedTerms> definitions = (ArrayList<SavedTerms>) getIntent().getSerializableExtra("definitions");

        // Update savedTerms list with received data
        savedTerms.clear();
        savedTerms.addAll(definitions);

        // Update RecyclerView adapter
        myAdapter.notifyDataSetChanged();

        // Save the received data to the database
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            for (SavedTerms savedTerm : definitions) {
                dicDao.insert(savedTerm);
            }

            // Fetch all SavedTerms from the database
            List<SavedTerms> allTermsSaved = dicDao.getAllSavedTerms();
            Log.d("MyDebug", "All Terms Saved size: " + allTermsSaved.size());
            runOnUiThread(() -> {
                // Update the UI on the main thread
                savedTerms.clear();  // Clear the existing list
                savedTerms.addAll(allTermsSaved);
                myAdapter.notifyDataSetChanged();
            });
        });
    }

    // Your existing DictionaryAdapter class
    class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MyRowHolder> {

        @NonNull
        @Override
        public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_definition_show, parent, false);
            return new MyRowHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            SavedTerms savedTerm = savedTerms.get(position);
            holder.word.setText(savedTerm.getWord());
            holder.definition.setText(savedTerm.getDefinition());

            // Set click listener for item
            holder.itemView.setOnClickListener(view -> deleteOrUpdate(position));
        }

        @Override
        public int getItemCount() {
            return savedTerms.size();
        }

        class MyRowHolder extends RecyclerView.ViewHolder {

            public TextView word;
            public TextView definition;

            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                word = itemView.findViewById(R.id.wordText);
                definition = itemView.findViewById(R.id.definitionText);
            }
        }
    }

    private void deleteOrUpdate(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.DeleteUpdatelocationTitle));
        builder.setMessage(R.string.DeleteUpdatelocationQuestion);
        builder.setPositiveButton((getString(R.string.Delete)), (dialog, which) -> {
            // Delete message from list and database
            delete(position);
        });
        builder.setNegativeButton(("No"), (dialog, which) -> {
            // Handle 'No' action if needed
        });
        builder.show();
    }

    private void delete(int position) {
        SavedTerms deletedTerm = savedTerms.get(position); // Make a copy
        savedTerms.remove(position);
        myAdapter.notifyItemRemoved(position);

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            dicDao.delete(deletedTerm);
        });
    }
}
