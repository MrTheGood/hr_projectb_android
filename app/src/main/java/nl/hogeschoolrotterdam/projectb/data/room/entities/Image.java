package nl.hogeschoolrotterdam.projectb.data.room.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings({"WeakerAccess", "unused"})
public class Image extends Media {
    @NonNull
    private byte[] image;

    public Image(@NonNull byte[] image) {
        this.image = image;
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
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
