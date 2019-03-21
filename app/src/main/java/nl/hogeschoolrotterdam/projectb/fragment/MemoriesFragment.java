package nl.hogeschoolrotterdam.projectb.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.Memory;

import java.util.List;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class MemoriesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memories, container, false);
        ((TextView) view.findViewById(R.id.textview)).setText(R.string.menu_memories);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getView() == null) return;

        // get list of memories
        List<Memory> memories = Database.getInstance().getMemories();

        // get random memory
        Memory memory = memories.get((int) (Math.random() * memories.size()));

        // set memory texts
        ((TextView) getView().findViewById(R.id.memory_date)).setText(memory.getDateText());
        ((TextView) getView().findViewById(R.id.memory_title)).setText(memory.getTitle());
        ((TextView) getView().findViewById(R.id.memory_description)).setText(memory.getDescription());
    }
}
