package nl.hogeschoolrotterdam.projectb.data.room.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;

@Entity
public class Image extends Media {
    @NonNull
    private String imagePath;

    public Image(@NonNull String memoryId, @NonNull String imagePath) {
        super(memoryId);
        this.imagePath = imagePath;
    }

    @NonNull
    public String getImagePath() {
        return imagePath;
    }

    /**
     * use this to get the image drawable.
     * usage:
     * ImageView imageView = findViewById(R.id.imageview)
     * Image image = memory.getThumbnail()
     * if (image != null) {
     * imageView.setImageBitmap(image.getImage())
     * }
     *
     * @return the image bitmap
     */
    @Nullable
    public Bitmap getImage() {
        return BitmapFactory.decodeFile(imagePath);
    }
}
