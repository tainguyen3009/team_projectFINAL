package algonquin.cst2335.team_project;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class SavedTerms implements Parcelable {

    @ColumnInfo(name = "word")
    private String word;
    @ColumnInfo(name = "definition")
    private String definition;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    public SavedTerms(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    protected SavedTerms(Parcel in) {
        word = in.readString();
        definition = in.readString();
        id = in.readInt();
    }

    public static final Creator<SavedTerms> CREATOR = new Creator<SavedTerms>() {
        @Override
        public SavedTerms createFromParcel(Parcel in) {
            return new SavedTerms(in);
        }

        @Override
        public SavedTerms[] newArray(int size) {
            return new SavedTerms[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(word);
        dest.writeString(definition);
        dest.writeInt(id);
    }
}
