package android.example.idp;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
public class ReminderActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("came here");
        super.onCreate(savedInstanceState);
        setShowWhenLocked(true);
        PowerManager powerManager=(PowerManager) this.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock=powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP ,"TAG");
            wakeLock.acquire();
            setContentView(R.layout.activity_reminder);
            System.out.println("came here");
            String image,pillname;
            pillname=getIntent().getStringExtra("pill_name");
            image=getIntent().getStringExtra("pill_image");
            ImageView imageView=(ImageView) findViewById(R.id.reminder_pill_image);
            TextView textView=(TextView) findViewById(R.id.reminder_pill_name);
            textView.setText(pillname);
            if ((new File(image)).exists())
            imageView.setImageBitmap(BitmapFactory.decodeFile(image));
            Vibrator vibrator=(Vibrator)getSystemService(VIBRATOR_SERVICE);
            final MediaPlayer mediaPlayer=MediaPlayer.create(this, R.raw.reminder_sound);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
            Button stopSound=(Button) findViewById(R.id.reminder_stop);
            stopSound.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                }
            });
        Button done=(Button) findViewById(R.id.reminder_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
            }
        });
        Button skip=(Button) findViewById(R.id.reminder_skip);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                finish();
            }
        });
            wakeLock.release();
    }
}