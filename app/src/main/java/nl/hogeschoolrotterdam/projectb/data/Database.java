package nl.hogeschoolrotterdam.projectb.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
@SuppressWarnings("unused")
public class Database {
    private static Database ourInstance = new Database();
    private final ArrayList<Memory> memories = new ArrayList<>();

    private Database() {
    }

    @NonNull
    public static Database getInstance() {
        return ourInstance;
    }


    @NonNull
    public ArrayList<Memory> getMemories() {
        return memories;
    }

    public void addMemory(Memory memory) {
        memories.add(memory);
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
