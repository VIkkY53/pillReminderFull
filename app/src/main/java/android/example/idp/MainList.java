package android.example.idp;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;

public class MainList {
    ArrayList<ListItemObject> mainList;
    ArrayList<ListItemObject> upcomingList;
    Integer SizeOfList=0;
    Context getContext;
    public MainList(Context context){
        this.getContext=context;
        mainList=new ArrayList<>();
        getSize(context);
        for (int i=0;i<SizeOfList;i++){
            mainList.add(new ListItemObject());
        }
        loadMainList();
        Time time=new Time(50);
    }
    public void saveMainList(){
        SharedPreferences sharedPreferences =getContext.getSharedPreferences("MainListName", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.mainList);
        editor.putString("MainList", json);
        editor.putInt("SizeOfList",SizeOfList);
        editor.apply();
    }
    public void loadMainList(){
        SharedPreferences sharedPreferences = getContext.getSharedPreferences("MainListName", MODE_PRIVATE);
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
        SizeOfList=sharedPreferences.getInt("SizeOfList",0);
    }
    public ArrayList<ListItemObject> getUpcomingList(){

        return upcomingList;
    }
}


