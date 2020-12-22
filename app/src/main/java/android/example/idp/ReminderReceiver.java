package android.example.idp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent=new Intent(this,MainActivity.class);

        Toast.makeText(context,"Broadcast Received",Toast.LENGTH_LONG).show();
    }
}
