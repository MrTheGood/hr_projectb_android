package nl.hogeschoolrotterdam.projectb.data.room.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public abstract class Media {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @NonNull
    private String memoryId;

    Media(@NonNull String memoryId) {
        this.memoryId = memoryId;
    }

    @NonNull
    public String getMemoryId() {
        return memoryId;
    }

    public void setMemoryId(@NonNull String memoryId) {
        this.memoryId = memoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
