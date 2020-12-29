package android.example.idp.ui.home;

import android.content.SharedPreferences;
import android.example.idp.ListItemObject;
import android.example.idp.MainList;
import android.example.idp.ReminderObject;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.idp.R;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    ArrayList<ListItemObject> updatedList;
    ArrayList<ListItemObject> objects;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.upcomingrecyclerview);
        this.objects= new MainList(getContext()).getUpcomingList();
        updatedList=new ArrayList<ListItemObject>();
        updatedList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new adapter(updatedList));
        return view;
    }
    private void updatedList(){
        Calendar calendar=Calendar.getInstance();
        long time=((calendar.getTime().getHours()*60) + (calendar.getTime().getMinutes()))*60*1000;
        for (int i=0;i<objects.size();i++){
            ListItemObject listItemObject=objects.get(i);
            long time1=(listItemObject.getTime().getHours()*60 + listItemObject.getTime().getMinutes())*60*1000;
            if (time1>time){
                updatedList.add(listItemObject);
            }
        }
    }
    public ArrayList<ListItemObject> loadMainList(ArrayList<ListItemObject> MainList){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MainList", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MainList", null);
        Type type = new TypeToken<ArrayList<ListItemObject>>() {}.getType();
        MainList = gson.fromJson(json, type);
        return MainList;
    }
    private  class viewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        TextView textTime ;
        ImageView imageView;
                ArrayList<ListItemObject> list;
        public viewholder(LayoutInflater inflater, ViewGroup parent,ArrayList<ListItemObject> list) {

            super(inflater.inflate(R.layout.reminderitem_view,parent,false));
            this.list=list;
            textView=(TextView)itemView.findViewById(R.id.textView);
            textTime = (TextView) itemView.findViewById(R.id.texttime);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }
        public void bind(int i){
            ListItemObject current=list.get(i);
            textView.setText(current.getName());
            imageView=itemView.findViewById(R.id.image);
            File file=new File(current.getImageId());
            if (file.exists())
            imageView.setImageBitmap(BitmapFactory.decodeFile(current.getImageId()));
            else {
                imageView.setImageResource(R.mipmap.ic_defaultpillimage);
            }
            textTime.setText("Reminder at " + current.getTime().toString());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();
        }
    }
    private class adapter extends RecyclerView.Adapter<viewholder>{
        ArrayList<ListItemObject> list;
        public adapter(ArrayList<ListItemObject> list){
            this.list=list;
        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater=LayoutInflater.from(getActivity());
            return new viewholder(inflater,parent,list);
        }
        @Override
        public void onBindViewHolder(@NonNull viewholder holder, int position) {
            System.out.println(position);
            holder.bind(position);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}