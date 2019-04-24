package nl.hogeschoolrotterdam.projectb.data.room.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public abstract class Media {

    @PrimaryKey
    private int id;

    @NonNull
    private String memoryId;

    Media(int id, @NonNull String memoryId) {
        this.id = id;
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
