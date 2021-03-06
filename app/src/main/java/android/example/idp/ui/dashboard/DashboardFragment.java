package android.example.idp.ui.dashboard;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.example.idp.ListItemDetailView;
import android.example.idp.ListItemDialog;
import android.example.idp.ListItemObject;
import android.example.idp.MainList;
import android.example.idp.ReminderObject;
import android.example.idp.ui.home.HomeFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.example.idp.R;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.provider.MediaStore.Images.Media.getBitmap;

public class DashboardFragment extends Fragment {

    public  ArrayList<ListItemObject> MainList=new ArrayList<>();
    ViewGroup container;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        this.container=container;
        //Toast.makeText(getContext(),"Restarted",Toast.LENGTH_SHORT).show();
        MainList.add(new ListItemObject());
        View view=inflater.inflate(R.layout.fragment_dashboard,container,false);
        return updateList(inflater,container,savedInstanceState);
    }
    public void addNewItem(){
        Intent intent=new Intent(getContext(),ListItemDetailView.class);
        intent.putExtra("ifNew",true);
        startActivity(intent);
    }
    public View updateList(@NonNull LayoutInflater inflater,
                           ViewGroup container, Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_dashboard,container,false);
        FloatingActionButton floatingActionButton=view.findViewById(R.id.addNewItemButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewItem();
            }
        });
        ListView listView = (ListView) view.findViewById(R.id.upcomingListView);
        MainList mainList=new MainList(getContext());
        mainList.loadMainList();
        this.MainList=mainList.getMainList();

        if (MainList==null){
            MainList=new ArrayList<ListItemObject>();
        }
        else {
            Collections.sort(this.MainList, new Comparator<ListItemObject>() {
                @Override
                public int compare(ListItemObject o1, ListItemObject o2) {
                    boolean a=o1.isActive();
                    boolean b=o2.isActive();
                    if (a==true && b==false)
                        return -1;
                    else if(a==false && b==true)
                        return 1;
                    else
                        return 0;
                }
            });
        }
        customAdapter adapter=new customAdapter(getContext(),0,MainList);
        listView.setAdapter(adapter);
        listView.setClickable(true);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ListItemDialog listItemDialog=new ListItemDialog(MainList.get(position),position);
                listItemDialog.show(getParentFragmentManager(),"Dialog");
                return true;
            }
        });
        return view;
    }
    public void launchDetailItemView(Integer position){
        Intent intent=new Intent(getContext(), ListItemDetailView.class);
        intent.putExtra("ifNew",false);
        intent.putExtra("position",position);
        startActivity(intent);
    }
    private class customAdapter extends ArrayAdapter<ListItemObject> {
        public customAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, 0, objects);
        }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.mylistitem_view, parent, false);
            }
            ListItemObject currentObject = getItem(position);
            TextView textView = (TextView) view.findViewById(R.id.pill_name);
            ImageView imageView=(ImageView) view.findViewById(R.id.pill_image);
            TextView textView1=view.findViewById(R.id.pill_time);
            textView.setText(currentObject.getName().toString());
            File file=new File(currentObject.getImageId());
            if (file.exists()){
                Bitmap bitmap=BitmapFactory.decodeFile(currentObject.getImageId());
                imageView.setImageBitmap(bitmap);
            }
            else {
              imageView.setImageResource(R.mipmap.ic_defaultpillimage);
            }
            if(!currentObject.isActive()){
                ImageView imageView1=(ImageView)view.findViewById(R.id.mylistitem_active);
                imageView1.setImageResource(R.drawable.ic_inactive_check);
            }
            textView1.setText("Time : "+currentObject.getTime().toString());
            return view;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}