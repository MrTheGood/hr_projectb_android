package nl.hogeschoolrotterdam.projectb.fragment;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.navigation.NavigationView;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.WhibApp;
import nl.hogeschoolrotterdam.projectb.adapter.MemoriesAdapter;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class MemoriesFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    private List<Memory> memories;
    private MemoriesAdapter adapter;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle mToggle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memories, container, false);
        setHasOptionsMenu(true);

        memories = Database.getInstance().getMemories();
        adapter = new MemoriesAdapter(memories);

        RecyclerView recyclerView = view.findViewById(R.id.memorylist);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        Toolbar tb = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(tb);



        mDrawerLayout=(DrawerLayout) view.findViewById(R.id.drawer);
        navigationView = view.findViewById(R.id.nav_view);
        mToggle= new ActionBarDrawerToggle(getActivity(),mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        memories = Database.getInstance().getMemories();
        adapter.setData(memories);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_memory_listsort, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        switch (item.getItemId()) {
            case R.id.Newest:
                Collections.sort(memories, new Comparator<Memory>() {
                    @Override
                    public int compare(Memory a, Memory b) {
                        if (b.getDate().before(a.getDate()))
                            return -1;
                        if (b.getDate().after(a.getDate()))
                            return 1;
                        return 0;
                    }
                });
                adapter.setData(memories);
                return true;
            case R.id.Oldest:
                Collections.sort(memories, new Comparator<Memory>() {
                    @Override
                    public int compare(Memory a, Memory b) {
                        if (b.getDate().before(a.getDate()))
                            return 1;
                        if (b.getDate().after(a.getDate()))
                            return -1;
                        return 0;
                    }
                });
                adapter.setData(memories);
                return true;
            case R.id.Alphabetical:
                Collections.sort(memories, new Comparator<Memory>() {
                    @Override
                    public int compare(Memory a, Memory b) {
                        return (a.getTitle().toLowerCase().compareTo(b.getTitle().toLowerCase()));
                    }
                });
                adapter.setData(memories);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:{
                break;
            }
            case R.id.setting:{
                break;
            }
            case R.id.log:{
                break;
            }
        }
        item.setChecked(true);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
