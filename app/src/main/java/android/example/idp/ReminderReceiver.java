package android.example.idp;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

import static android.content.Context.WINDOW_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static androidx.core.content.ContextCompat.getSystemServiceName;
import static androidx.core.content.ContextCompat.startActivity;
import static java.security.AccessController.getContext;
public class ReminderReceiver extends BroadcastReceiver  {
    private static final String CHANNEL_REMINDERS ="REMINDER_NOTIFICATIONS";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isMain=intent.getBooleanExtra("isMain",false);
        if (isMain){
            ReminderMain reminderMain=new ReminderMain(context,86400000,true);
        }
        else{
            final String CHANNEL_1 ="CHANNEL_1";
            PowerManager powerManager=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn=powerManager.isInteractive();
            if (isScreenOn){
                CharSequence channel1="TIMEPASS";
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel=new NotificationChannel(CHANNEL_1,channel1, NotificationManager.IMPORTANCE_HIGH);
                    NotificationManager notificationManager = getSystemService(context,NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                    channel.enableVibration(true);
                }
                Intent fullScreenIntent=new Intent(context,MainActivity.class);
                fullScreenIntent.putExtra("pill_name",intent.getCharSequenceExtra("pill_name"));
                fullScreenIntent.putExtra("pill_image",intent.getCharSequenceExtra("pill_image"));
                PendingIntent pendingIntent=PendingIntent.getActivity(context,1,fullScreenIntent,PendingIntent.FLAG_ONE_SHOT);
                NotificationCompat.Builder sample=new NotificationCompat.Builder(context, CHANNEL_1)
                        .setSmallIcon(R.mipmap.ic_defaultpillimage)
                        .setLargeIcon(BitmapFactory.decodeFile((String) intent.getCharSequenceExtra("pill_image")))
                        .setContentTitle("Medication Reminder")
                        .setContentText("Take " + intent.getCharSequenceExtra("pill_name"))
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setFullScreenIntent(pendingIntent,true)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(5, sample.build());
            }
            else {
                Intent fullScreenIntent=new Intent(context,ReminderActivity.class);
                fullScreenIntent.putExtra("pill_name",intent.getCharSequenceExtra("pill_name"));
                fullScreenIntent.putExtra("pill_image",intent.getCharSequenceExtra("pill_image"));
                fullScreenIntent.setFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(context,fullScreenIntent,null);
            }
        }
    }
}
