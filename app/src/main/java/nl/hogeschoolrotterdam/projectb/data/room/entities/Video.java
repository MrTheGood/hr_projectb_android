package nl.hogeschoolrotterdam.projectb.data.room.entities;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    public Bitmap getThumbnail() {
        return ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Video.Thumbnails.MINI_KIND);
    }
}
