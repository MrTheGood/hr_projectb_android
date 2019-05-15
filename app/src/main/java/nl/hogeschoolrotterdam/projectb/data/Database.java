package nl.hogeschoolrotterdam.projectb.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;
import nl.hogeschoolrotterdam.projectb.WhibApp;
import nl.hogeschoolrotterdam.projectb.data.room.AppDatabase;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class Database {
    private static Database ourInstance = new Database();
    private final ArrayList<Memory> memories = new ArrayList<>();
    private AppDatabase database;

    private Database() {
        database = Room.databaseBuilder(WhibApp.getInstance(), AppDatabase.class, "whib-database").build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                memories.addAll(database.memoryDao().getAllMemoriesWithMedia());
            }
        }).start();
    }

    public String newId() {
        return String.valueOf(System.currentTimeMillis());
    }

    @NonNull
    public static Database getInstance() {
        return ourInstance;
    }


    @NonNull
    public List<Memory> getMemories() {
        return memories;
    }

    public void addMemory(final Memory memory) {
        memories.add(memory);
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.memoryDao().insertMemoryWithMedia(memory);
            }
        }).start();
    }

    public void updateMemory(final Memory memory) {
        memories.set(memories.indexOf(memory), memory);
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.memoryDao().updateMemoryWithMedia(memory);
            }
        }).start();
    }

    public void deleteMemory(final Memory memory) {
        memories.remove(memory);
        new Thread(new Runnable() {
            @Override
            public void run() {
                database.memoryDao().deleteMemoryWithMedia(memory);
            }
        }).start();
    }

    /**
     * Use this to find a single memory, for instance when opening the detail page.
     *
     * @param memoryId memory to find
     * @return the memory with id memoryId, or null if none found.
     */
    @Nullable
    public Memory findMemory(String memoryId) {
        for (Memory memory : memories) {
            if (memory.getId().equals(memoryId))
                return memory;
        }
        return null;
    }
}
