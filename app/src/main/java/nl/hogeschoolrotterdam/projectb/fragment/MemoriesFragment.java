package nl.hogeschoolrotterdam.projectb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ExpandableListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import nl.hogeschoolrotterdam.projectb.CheckedActivity;
import nl.hogeschoolrotterdam.projectb.ConstantManager;
import nl.hogeschoolrotterdam.projectb.MyCategoriesExpandableListAdapter;
import nl.hogeschoolrotterdam.projectb.R;
import nl.hogeschoolrotterdam.projectb.adapter.MemoriesAdapter;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.model.DataItem;
import nl.hogeschoolrotterdam.projectb.model.SubCategoryItem;

import java.util.*;

/**
 * Created by maartendegoede on 20/03/2019.
 * Copyright © 2019 Anass El Mahdaoui, Hicham El Marzgioui, Wesley de Man, Maarten de Goede all rights reserved.
 */
public class MemoriesFragment extends Fragment {
    private List<Memory> memories;
    private MemoriesAdapter adapter;
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


        DrawerLayout mDrawerLayout = view.findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*listView = (ExpandableListView) view.findViewById(R.id.iVExp);
        checkData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listHash);
        listView.setAdapter(listAdapter);
        */

        Button btn = view.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckedActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        memories = Database.getInstance().getMemories();
        adapter.setData(memories);
        setupReferences();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_memory_listsort, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
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

    private void setupReferences() {

        ExpandableListView lvCategory = requireView().findViewById(R.id.lvCategory);
        ArrayList<DataItem> arCategory = new ArrayList<>();
        ArrayList<SubCategoryItem> arSubCategory;
        ArrayList<HashMap<String, String>> parentItems = new ArrayList<>();
        ArrayList<ArrayList<HashMap<String, String>>> childItems = new ArrayList<>();

        DataItem dataItem = new DataItem();
        dataItem.setCategoryId("1");
        dataItem.setCategoryName("Adventure");

        arSubCategory = new ArrayList<>();
        for (int i = 1; i < 6; i++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(i));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Adventure: " + i);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("2");
        dataItem.setCategoryName("Art");
        arSubCategory = new ArrayList<>();
        for (int j = 1; j < 6; j++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(j));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Art: " + j);
            arSubCategory.add(subCategoryItem);
        }
        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        dataItem = new DataItem();
        dataItem.setCategoryId("3");
        dataItem.setCategoryName("Cooking");
        arSubCategory = new ArrayList<>();
        for (int k = 1; k < 6; k++) {

            SubCategoryItem subCategoryItem = new SubCategoryItem();
            subCategoryItem.setCategoryId(String.valueOf(k));
            subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            subCategoryItem.setSubCategoryName("Cooking: " + k);
            arSubCategory.add(subCategoryItem);
        }

        dataItem.setSubCategory(arSubCategory);
        arCategory.add(dataItem);

        Log.d("TAG", "setupReferences: " + arCategory.size());

        for (DataItem data : arCategory) {
//                        Log.i("Item id",item.id);
            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<>();
            HashMap<String, String> mapParent = new HashMap<>();

            mapParent.put(ConstantManager.Parameter.CATEGORY_ID, data.getCategoryId());
            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME, data.getCategoryName());

            int countIsChecked = 0;
            for (SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<>();
                mapChild.put(ConstantManager.Parameter.SUB_ID, subCategoryItem.getSubId());
                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.CATEGORY_ID, subCategoryItem.getCategoryId());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED, subCategoryItem.getIsChecked());

                if (subCategoryItem.getIsChecked().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
            }

            if (countIsChecked == data.getSubCategory().size()) {

                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED, data.getIsChecked());
            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(requireActivity(), parentItems, childItems, false);
        lvCategory.setAdapter(myCategoriesExpandableListAdapter);
    }
}


