package nl.hogeschoolrotterdam.projectb.data.room.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity
public class Video extends Media {
    @NonNull
    private String videoPath;

    public Video(@NonNull String memoryId, @NonNull String videoPath) {
        super(memoryId);
        this.videoPath = videoPath;
    }

    @NonNull
    public String getVideoPath() {
        return videoPath;
    }
}
