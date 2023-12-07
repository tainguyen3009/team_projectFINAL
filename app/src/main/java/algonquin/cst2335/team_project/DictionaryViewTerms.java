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

/**
 * Activity for displaying and managing saved dictionary terms.
 */
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

    /**
     * RecyclerView Adapter for displaying saved dictionary terms.
     */
    class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.MyRowHolder> {

        /**
         *  Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return A new ViewHolder that holds a View of the given view type.
         */
        @NonNull
        @Override
        public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_definition_show, parent, false);
            return new MyRowHolder(itemView);
        }

        /**
         * Called by RecyclerView to display the data at the specified position.
         * @param holder   The ViewHolder which should be updated to represent the contents of the
         *                 item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            SavedTerms savedTerm = savedTerms.get(position);
            holder.word.setText(savedTerm.getWord());
            holder.definition.setText(savedTerm.getDefinition());

            // Set click listener for item
            holder.itemView.setOnClickListener(view -> deleteOrUpdate(position));
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {
            return savedTerms.size();
        }

        /**
         * ViewHolder class for holding the views of each item in the RecyclerView.
         */
        class MyRowHolder extends RecyclerView.ViewHolder {

            public TextView word;
            public TextView definition;

            /**
             * Constructor for MyRowHolder.
             *
             * @param itemView The view representing an item in the RecyclerView.
             */
            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                word = itemView.findViewById(R.id.wordText);
                definition = itemView.findViewById(R.id.definitionText);
            }
        }
    }

    /**
     * Displays an alert dialog for confirming deletion or update of a saved term.
     *
     *  @param position The position of the item in the list.
     */
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


    /**
     * Deletes a saved term from the list and the database.
     *
     * @param position The position of the item in the list.
     */
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
