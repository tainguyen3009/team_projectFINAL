package algonquin.cst2335.team_project;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * The {@code SavedTerms} class represents a saved dictionary term with its word, definition, and unique identifier.
 * Implements the Parcelable interface to allow for object serialization and passing between components.
 */
@Entity
public class SavedTerms implements Parcelable {

    @ColumnInfo(name = "word")
    private String word;
    @ColumnInfo(name = "definition")
    private String definition;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    /**
     * Constructs a new instance of the {@code SavedTerms} class with the specified word and definition.
     *
     * @param word       The word of the dictionary term.
     * @param definition The definition of the dictionary term.
     */
    public SavedTerms(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    /**
     * Parcelable constructor used for reconstructing the object from a Parcel.
     *
     * @param in The Parcel containing the object data.
     */
    protected SavedTerms(Parcel in) {
        word = in.readString();
        definition = in.readString();
        id = in.readInt();
    }

    /**
     * Parcelable creator for generating instances of {@code SavedTerms} from a Parcel.
     */
    public static final Creator<SavedTerms> CREATOR = new Creator<SavedTerms>() {
        /**
         * Creates a new instance of the {@code SavedTerms} class, instantiating it from the given Parcel.
         *
         * @param in The Parcel to read the object's data from.
         * @return A new instance of {@code SavedTerms} created from the Parcel.
         */
        @Override
        public SavedTerms createFromParcel(Parcel in) {
            return new SavedTerms(in);
        }

        /**
         * Creates a new array of {@code SavedTerms} with the specified size.
         *
         * @param size Size of the array.
         * @return An array of {@code SavedTerms} with the specified size.
         */
        @Override
        public SavedTerms[] newArray(int size) {
            return new SavedTerms[size];
        }
    };

    /**
     * Gets the unique identifier of the saved term.
     *
     * @return The unique identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the saved term.
     *
     * @param id The unique identifier to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the word of the saved term.
     *
     * @return The word of the saved term.
     */
    public String getWord() {
        return word;
    }

    /**
     * Gets the definition of the saved term.
     *
     * @return The definition of the saved term.
     */
    public String getDefinition() {
        return definition;
    }

    /**
     * Describes the contents of the Parcelable object.
     *
     * @return A bitmask indicating the set of special object types marshaled by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the object data to a Parcel.
     *
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(definition);
        dest.writeInt(id);
    }
}
