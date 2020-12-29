package android.example.idp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;

public class MainList {
    ArrayList<ListItemObject> mainList;
    Integer SizeOfList=0;
    Context getContext;
    public MainList(Context context){
        this.getContext=context;
        mainList=new ArrayList<ListItemObject>();
        getSize(context);
        System.out.println("size is :"+SizeOfList);
      /*for (int i=0;i<SizeOfList;i++){
            mainList.add(new ListItemObject());
        }*/
        loadMainList();
        Time time=new Time(0);
    }
    public void saveMainList(){
        SharedPreferences sharedPreferences =getContext.getSharedPreferences("MainListNameNew8", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.mainList);
        editor.putString("MainList", json);
        editor.putInt("SizeOfList1",SizeOfList);
        editor.apply();
    }
    public void loadMainList(){
        SharedPreferences sharedPreferences = getContext.getSharedPreferences("MainListNameNew8", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("MainList", null);
        Type type = new TypeToken<ArrayList<ListItemObject>>() {}.getType();
        mainList = gson.fromJson(json, type);
    }
    public ArrayList<ListItemObject> getMainList(){
        return mainList;
    }
    public void getSize(Context context){
        SharedPreferences sharedPreferences = getContext.getSharedPreferences("MainListName", MODE_PRIVATE);
        this.SizeOfList=sharedPreferences.getInt("SizeOfList1",0);
    }
    public Integer getSizeOfList() {
        return SizeOfList;
    }
    public ArrayList<ListItemObject> getUpcomingList(){
        ArrayList<ListItemObject> UpcomingList=new ArrayList<ListItemObject>();
        UpcomingList=this.mainList;
        if (UpcomingList!=null){
            Collections.sort(UpcomingList, new Comparator<ListItemObject>() {
                @Override
                public int compare(ListItemObject o1, ListItemObject o2) {
                    long a=o1.getTime().getTime();
                    long b=o2.getTime().getTime();
                    if (a>b)
                        return 1;
                    else if(a<b)
                        return -1;
                    else
                        return 0;
                }
            });
            return UpcomingList;
        }
        return new ArrayList<ListItemObject>();
    }
}


