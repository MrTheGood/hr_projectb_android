package nl.hogeschoolrotterdam.projectb;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import nl.hogeschoolrotterdam.projectb.data.Database;
import nl.hogeschoolrotterdam.projectb.data.room.entities.Memory;
import nl.hogeschoolrotterdam.projectb.fragment.MemoriesFragment;

import java.util.ArrayList;
import java.util.List;

public class CheckedActivity extends AppCompatActivity {

    private TextView tvParent, tvChild;
    private List<Memory> memories;
    ArrayList<String> filtered_memories = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked);

        memories = Database.getInstance().getMemories();

        tvParent = findViewById(R.id.parent);
        tvChild = findViewById(R.id.child);

        ArrayList<String> filter_memory=new ArrayList<String>();

        for (int i = 0; i < MyCategoriesExpandableListAdapter.parentItems.size(); i++) {

            String isChecked = MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.IS_CHECKED);

            if (isChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                tvParent.setText(tvParent.getText() + MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME));
            }

            for (int j = 0; j < MyCategoriesExpandableListAdapter.childItems.get(i).size(); j++) {

                String isChildChecked = MyCategoriesExpandableListAdapter.childItems.get(i).get(j).get(ConstantManager.Parameter.IS_CHECKED);

                if (isChildChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                    filtered_memories.add(tvChild.toString());
                    tvChild.setText(tvChild.getText() + " , " + MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME) + " " + (j));
                    if((MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME).equals("Location"))){
                        filter_memory.add(memories.get(j).getLocation().toString());
                    } else if((MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME).equals("Year"))){
                        filter_memory.add(memories.get(j).getDateText().toString());
                    }
                }

            }


        }
        Intent intent = new Intent(this, MemoriesFragment.class);
        intent.putStringArrayListExtra("Filterd_memories", filter_memory);
        startActivity(intent);

    }
}
