package android.example.idp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.example.idp.ui.dashboard.DashboardFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;

public class ListItemDialog extends AppCompatDialogFragment {
    ListItemObject listItemObject;
    Integer position;
    public ListItemDialog(ListItemObject listItemObject,int position){
        this.listItemObject=listItemObject;
        this.position=position;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.list_item_dialog,null);
        builder.setView(view)
                .setTitle("Make changes")
                .setMessage(
                        "Details \n"+"Name : " + listItemObject.getName() + "\n"  +
                                "Time : "+listItemObject.getTime().toString() + "\n" +
                        "Reminder Frequency:" + listItemObject.getCount())
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteObject();
                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent(getContext(), ListItemDetailView.class);
                        intent.putExtra("ifNew",false);
                        intent.putExtra("position",position);
                        startActivity(intent);
                    }
                });
        return builder.create();
    }
    private void deleteObject(){
        MainList mainList=new MainList(getContext());
        mainList.loadMainList();
        ArrayList<ListItemObject> list=mainList.getMainList();
        Integer SizeofList=mainList.getMainList().size();
        File file=new File(listItemObject.getImageId());
        if (file.exists())
            file.delete();
        mainList.mainList=new ArrayList<ListItemObject>();
        int i=0;
        int y=0;
            for (i=0;i<SizeofList;i++){
                if (i!= position)
                    mainList.mainList.add(list.get(y));
                y++;
            }
        mainList.saveMainList();
    }
}
