package algonquin.cst2335.team_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.team_project.databinding.ActivityMainDictionaryBinding;

/**
 * MainActivityDictionary is the main activity for the dictionary feature of the application.
 * It allows users to search for word definitions and view/save the results.
 */
public class MainActivityDictionary extends AppCompatActivity {
    private EditText wordToFind;
    private Button buttonSearch;
    private Button saveButton;

    private String word;

    private DefinitionAdapter myAdapter;
    private List<SavedTerms> definitions;

    private ActivityMainDictionaryBinding binding;

    private SharedPreferences sharedPreferences;
    private static final String LAST_SEARCHED_WORD_KEY = "last_searched_word";

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainDictionaryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize UI components
        wordToFind = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        saveButton = findViewById(R.id.buttonViewSaved);

        // Initialize the RecyclerView and its adapter
        binding.recyclerViewDefinitions.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new DefinitionAdapter();
        binding.recyclerViewDefinitions.setAdapter(myAdapter);

        // Initialize the definitions list
        definitions = new ArrayList<>();

        if (buttonSearch != null) {
            buttonSearch.setOnClickListener(v -> {
                String word = wordToFind.getText().toString();
                // Add this line to check if fetchWord is called
                Log.d("MyDebug", "Button clicked. Fetching word: " + word);
                fetchWord(word);
            });
        }
        // Initialize SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Load the last searched word
        wordToFind.setText(sharedPreferences.getString(LAST_SEARCHED_WORD_KEY, ""));

        Button buttonSearch = findViewById(R.id.buttonSearch);
        if (buttonSearch != null) {
            String word = wordToFind.getText().toString();
            // Add this line to check if fetchWord is called
            fetchWord(word);
        }

        ImageButton btnHelp = findViewById(R.id.btnHelp);
        if (btnHelp != null) {
            btnHelp.setOnClickListener(v -> showHelpDialog());
        }

    }

    /**
     * Shows a help dialog providing information about the dictionary feature.
     */
    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help");
        View dialogView = getLayoutInflater().inflate(R.layout.help_dialog_dictionary, null);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    /**
     * Fetches the definition of a given word from the API and updates the UI accordingly.
     *
     * @param word The word to fetch the definition for.
     */
    private void fetchWord(String word) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String apiUrl = buildUrl(word);

        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            // Clear old definitions before adding new ones
                            definitions.clear();
                            Log.d("MyDebug", "Response received: " + response.toString());

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject wordObject = response.getJSONObject(i);
                                String word = wordObject.optString("word", ""); // Extract the word

                                JSONArray meaningsArray = wordObject.getJSONArray("meanings");

                                for (int j = 0; j < meaningsArray.length(); j++) {
                                    JSONObject meaningObject = meaningsArray.getJSONObject(j);
                                    String partOfSpeech = meaningObject.optString("partOfSpeech", "");
                                    JSONArray definitionsArray = meaningObject.getJSONArray("definitions");

                                    for (int k = 0; k < definitionsArray.length(); k++) {
                                        JSONObject definitionObject = definitionsArray.getJSONObject(k);
                                        String definitionText = definitionObject.optString("definition", "");

                                        // Create a SavedTerms object with the extracted data
                                        SavedTerms definition = new SavedTerms(word, definitionText);
                                        // Add the SavedTerms object to the list
                                        definitions.add(definition);
                                    }
                                }
                            }

                            // Update the RecyclerView with the definitions
                            myAdapter.addAllDefinitions(definitions);
                            // Save the word and definitions to be used in other places
                            MainActivityDictionary.this.word = word;

                            // Save the last searched word to SharedPreferences
                            sharedPreferences.edit().putString(LAST_SEARCHED_WORD_KEY, word).apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("MyDebug", "Volley Error: " + error.getMessage());
                        error.printStackTrace();

                        // Show a toast message for invalid word
                        if (error instanceof com.android.volley.NoConnectionError) {
                            Toast.makeText(MainActivityDictionary.this, "No internet connection", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivityDictionary.this, "Word not found. Please try another.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        // Add the request to the RequestQueue
        queue.add(jsonRequest);

        // Save the word and definitions to be used in other places
        this.word = word;

        saveButton.setOnClickListener(click -> {
            try {
                Intent goToSavePlace = new Intent(MainActivityDictionary.this, DictionaryViewTerms.class);
                goToSavePlace.putExtra("word", word);
                goToSavePlace.putExtra("definitions", new ArrayList<>(definitions));
                startActivity(goToSavePlace);
            } catch (Exception e) {
                Log.e("MyDebug", "Error starting DictionaryViewTerms activity: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    /**
     * Builds the API URL for fetching word definitions.
     *
     * @param word The word to include in the URL.
     * @return The constructed API URL.
     */
    private String buildUrl(String word) {
        return String.format("https://api.dictionaryapi.dev/api/v2/entries/en/%s", word);
    }

    /**
     * DefinitionAdapter is the RecyclerView adapter for displaying word definitions.
     */
    public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder> {
        private List<SavedTerms> definitions;
        private AdapterView.OnItemClickListener clickListener;

        /**
         * Constructor for DefinitionAdapter.
         */
        public DefinitionAdapter() {
            definitions = new ArrayList<>();
        }

        /**
         * Sets the definitions list with a new list of definitions and notifies the adapter of the change.
         *
         * @param newDefinitions The new list of definitions to set.
         */
        public void addAllDefinitions(List<SavedTerms> newDefinitions) {
            definitions.clear();
            definitions.addAll(newDefinitions);
            notifyDataSetChanged();
        }

        /**
         * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
         *
         * @param parent   The ViewGroup into which the new View will be added after it is bound to
         *                 an adapter position.
         * @param viewType The view type of the new View.
         * @return
         */
        @NonNull
        @Override
        public DefinitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_definition, parent, false);
            return new DefinitionViewHolder(view);
        }

        /**
         * Called by RecyclerView to display the data at the specified position.
         * @param holder   The ViewHolder which should be updated to represent the contents of the
         *                 item at the given position in the data set.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void onBindViewHolder(@NonNull DefinitionViewHolder holder, int position) {
            SavedTerms definition = definitions.get(position);
            holder.bind(definition);
        }

        /**
         * Returns the total number of items in the data set held by the adapter.
         *
         * @return The total number of items in this adapter.
         */
        @Override
        public int getItemCount() {
            return definitions.size();
        }

        /**
         * DefinitionViewHolder is the RecyclerView ViewHolder for individual word definitions.
         */
        class DefinitionViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewDefinition;

            /**
             * Constructor for DefinitionViewHolder.
             *
             * @param itemView The view representing an item in the RecyclerView.
             */
            public DefinitionViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewDefinition = itemView.findViewById(R.id.textViewDefinition);
            }

            /**
             * Binds the provided definition to the ViewHolder, updating the UI.
             *
             * @param definition The definition to bind.
             */
            public void bind(SavedTerms definition) {
                textViewDefinition.setText(definition.getDefinition());
            }
        }
    }
}
