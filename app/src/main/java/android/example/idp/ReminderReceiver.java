package android.example.idp;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import static java.security.AccessController.getContext;

public class ReminderReceiver extends BroadcastReceiver {
    Intent intent1;
    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager powerManager=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock=powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP ,"TAG");
        wakeLock.acquire();


        Toast.makeText(context,"Broadcast Received",Toast.LENGTH_LONG).show();
        wakeLock.release();
    }
}
