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

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Help");
        View dialogView = getLayoutInflater().inflate(R.layout.help_dialog_dictionary, null);
        builder.setView(dialogView);
        builder.setPositiveButton("OK", null);
        builder.show();
    }

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

    private String buildUrl(String word) {
        return String.format("https://api.dictionaryapi.dev/api/v2/entries/en/%s", word);
    }

    public class DefinitionAdapter extends RecyclerView.Adapter<DefinitionAdapter.DefinitionViewHolder> {
        private List<SavedTerms> definitions;
        private AdapterView.OnItemClickListener clickListener;

        public DefinitionAdapter() {
            definitions = new ArrayList<>();
        }

        public void addAllDefinitions(List<SavedTerms> newDefinitions) {
            definitions.clear();
            definitions.addAll(newDefinitions);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DefinitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_definition, parent, false);
            return new DefinitionViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DefinitionViewHolder holder, int position) {
            SavedTerms definition = definitions.get(position);
            holder.bind(definition);
        }

        @Override
        public int getItemCount() {
            return definitions.size();
        }

        class DefinitionViewHolder extends RecyclerView.ViewHolder {
            private TextView textViewDefinition;

            public DefinitionViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewDefinition = itemView.findViewById(R.id.textViewDefinition);
            }

            public void bind(SavedTerms definition) {
                textViewDefinition.setText(definition.getDefinition());
            }
        }
    }
}
