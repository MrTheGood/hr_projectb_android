package nl.hogeschoolrotterdam.projectb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.hogeschoolrotterdam.projectb.MemoryDetailActivity;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.adapter.MemoriesAdapter;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.util.AnalyticsUtil;
import nl.hogeschoolrotterdam.projectb.util.SimpleTextWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Michaël van Asperen, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class SearchFragment extends Fragment {
    private List<Memory> memories;
    private MemoriesAdapter adapter;

    EditText input_search;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        memories = Database.getInstance().getMemories();
        adapter = new MemoriesAdapter(memories, new MemoriesAdapter.OnClickListener() {
            @Override
            public void onItemClick(View view, Memory obj, int pos) {
                Intent intent = new Intent(getContext(), MemoryDetailActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", obj.getId());
                view.getContext().startActivity(intent);
                AnalyticsUtil.selectContent(getContext(), "List");
            }

            @Override
            public void onItemLongClick(View view, Memory obj, int pos) {
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.memorylist);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        input_search = view.findViewById(R.id.input_search);
        input_search.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        memories = Database.getInstance().getMemories();
        adapter.setData(memories);
        if (!input_search.getText().toString().isEmpty())
            filter(input_search.getText().toString());
    }

    private void filter(String text) {
        ArrayList<Memory> filteredlist = new ArrayList<>();

        for (Memory item : memories) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())
                    || item.getDescription().toLowerCase().contains(text.toLowerCase())
                    || item.getCountryName(getContext()).toLowerCase().contains(text.toLowerCase())) {
                filteredlist.add(item);
            }
        }
        adapter.setData(filteredlist);
        AnalyticsUtil.search(getContext());
    }
}
