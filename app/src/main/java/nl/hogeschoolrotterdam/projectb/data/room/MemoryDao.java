package nl.hogeschoolrotterdam.projectb.data.room;

import androidx.room.*;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Image;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Media;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maartendegoede on 23/04/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Wesley de Man, Maarten de Goede all rights reserved.
 */
@Dao
public abstract class MemoryDao {
    @Transaction
    @Query("SELECT * from memory")
    abstract List<Memory> _getAll();

    // memory

    @Insert
    abstract void _insertMemory(Memory memory);

    @Update
    abstract void _updateMemory(Memory memory);

    @Delete
    abstract void _deleteMemory(Memory memory);

    // endregion memory
    // region imageList

    @Query("SELECT * FROM image WHERE memoryId =:memoryId")
    abstract List<Image> _getImageList(String memoryId);

    @Insert
    abstract void _insertImageList(List<Image> images);

    @Delete
    abstract void _deleteImageList(List<Image> images);

    // endregion imageList
    // region videoList

    @Query("SELECT * FROM video WHERE memoryId =:memoryId")
    abstract List<Video> _getVideoList(String memoryId);

    @Insert
    abstract void _insertVideoList(List<Video> videos);

    @Delete
    abstract void _deleteVideoList(List<Video> videos);

    // endregion videoList
    // region utility

    private List<Video> getVideoListFor(Memory memory) {
        ArrayList<Video> videos = new ArrayList<>();
        for (Media m : memory.getMedia())
            if (m instanceof Video) videos.add((Video) m);
        return videos;
    }

    private List<Image> getImagesListFor(Memory memory) {
        ArrayList<Image> images = new ArrayList<>();
        for (Media m : memory.getMedia())
            if (m instanceof Image) images.add((Image) m);
        return images;
    }

    // endregion utility

    public void insertMemoryWithMedia(Memory memory) {
        List<Media> media = memory.getMedia();
        for (Media m : media)
            m.setMemoryId(memory.getId());

        _insertImageList(getImagesListFor(memory));
        _insertVideoList(getVideoListFor(memory));
        _insertMemory(memory);
    }

    public List<Memory> getAllMemoriesWithMedia() {
        List<Memory> memories = _getAll();
        for (Memory m : memories) {
            ArrayList<Media> media = new ArrayList<>();
            media.addAll(_getImageList(m.getId()));
            media.addAll(_getVideoList(m.getId()));
            m._initializeMedia(media);
        }
        return memories;
    }

    public void updateMemoryWithMedia(Memory memory) {
        List<Media> media = memory.getMedia();
        for (Media m : media)
            m.setMemoryId(memory.getId());

        // delete old media
        _deleteVideoList(_getVideoList(memory.getId()));
        _deleteImageList(_getImageList(memory.getId()));

        // insert new media
        _insertImageList(getImagesListFor(memory));
        _insertVideoList(getVideoListFor(memory));

        _updateMemory(memory);
    }

    public void deleteMemoryWithMedia(Memory memory) {
        _deleteImageList(getImagesListFor(memory));
        _deleteVideoList(getVideoListFor(memory));
        _deleteMemory(memory);
    }
}