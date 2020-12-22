package android.example.idp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

public class ListItemDetailView extends AppCompatActivity {
    String imagePath;
    ArrayList<ListItemObject> MainList;
    Button saveButton;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_detail_view);
        boolean isNew=getIntent().getBooleanExtra("ifNew",false);
        final Integer position=getIntent().getIntExtra("position",0);
        saveButton=(Button) findViewById(R.id.saveButton);
        editText=(EditText) findViewById(R.id.editText);
        setContent(isNew,position);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Time time=new Time(10);
                ListItemObject listItemObject=new ListItemObject();
                listItemObject.setName(editText.getText().toString());
                MainList.add(0,listItemObject);*/
           //     takePicture();
                setReminder();
                Toast.makeText(getApplicationContext(),"Reminder Set",Toast.LENGTH_SHORT).show();
              //  finish();
            }
        });
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
        else{
            Time time=new Time(10,0,0);
            MainList.add(new ListItemObject());
            editText.setText(MainList.get(i).getName());
            Toast.makeText(this,""+i,Toast.LENGTH_SHORT).show();
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
        ImageView imageView=findViewById(R.id.imageView);
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
}