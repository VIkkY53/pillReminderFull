package android.example.idp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.example.idp.ui.dashboard.DashboardFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ListItemDetailView extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    String imagePath=" ";
    ArrayList<ListItemObject> List;
    Button saveButton;
    ImageView imageView;
    Button timePicker;
    MainList mainList;
    long time;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_detail_view);
        final boolean isNew=getIntent().getBooleanExtra("ifNew",false);
        final Integer position=getIntent().getIntExtra("position",0);
        this.mainList=new MainList(this);
        mainList.loadMainList();
        List=mainList.getMainList();
        saveButton=(Button) findViewById(R.id.save);
        imageView=findViewById(R.id.imageView);
        timePicker=findViewById(R.id.TimePicker);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        setContent(isNew,position);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            saveContent(isNew,position);
         //   setReminder();
            }
        });
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment TimePicker =new TimePickerFragment();
                TimePicker.show(getSupportFragmentManager(),"Time Picker");
            }
        });
    }
    private void saveContent(boolean isNew,int position) {
        EditText pillName,pillCount;
        pillName=findViewById(R.id.pill_name);
        pillCount=findViewById(R.id.pill_count);
        if (pillName.getText().toString()=="" )
            pillName.setError("This is a mandatory field");
        ListItemObject listItemObject=new ListItemObject();
        listItemObject.setName(pillName.getText().toString());
        listItemObject.setImageId(imagePath);
        listItemObject.setTime(time-(5*60 + 30)*60*1000);
        if (isNew) {
            if(mainList.getMainList()==null){
                mainList.mainList=new ArrayList<ListItemObject>();
            }
            mainList.getMainList().add(listItemObject);
            mainList.SizeOfList++;
        }
        else
            mainList.getMainList().set(position,listItemObject);
            mainList.saveMainList();
    }
    private void setReminder() {
        AlarmManager alarmManager=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(this,ReminderReceiver.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,1,intent,0);
        Calendar c=Calendar.getInstance();
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis()+10000,pendingIntent);
    }
    public void setContent(boolean isNew, Integer i){
        if (isNew){

        }
        else {
            ListItemObject listItemObject=new ListItemObject();
            listItemObject=List.get(i);
            EditText pillName,pillCount;
            ImageView imageView=findViewById(R.id.imageView);
            pillName=findViewById(R.id.pill_name);
            pillCount=findViewById(R.id.pill_count);
            pillName.setText(listItemObject.getName());
            Bitmap image= BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(image);
        }
    }
    private void takePicture(){
        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File imageFile=null;
        try {
            imageFile=getImageFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        if (imageFile!=null){
            Uri imageUri= FileProvider.getUriForFile(this,"com.example.android.fileprovider",imageFile);
            i.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            startActivityForResult(i,100);
        }
    }
    private void storeImage(){
        Bitmap image= BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(image);
    }
    private File getImageFile()throws IOException {
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageName="pill_"+timeStamp+"_";
        File storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile=File.createTempFile(imageName,".jpg",storageDir);
        this.imagePath=imageFile.getAbsolutePath();
        return imageFile;
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==100){
            storeImage();
        }
    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        this.time=(hourOfDay*60*60*1000) + (minute*60*1000);
    }
}