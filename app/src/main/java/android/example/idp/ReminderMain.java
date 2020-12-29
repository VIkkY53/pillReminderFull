package android.example.idp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import java.util.Calendar;
public class ReminderMain {
    Context context;
    public ReminderMain(Context context,long time,boolean at12) {
        this.context = context;
        MainList mainList = new MainList(context);
        if (mainList.getUpcomingList() != null) {
            Calendar calendar = Calendar.getInstance();
            for (int i = 0; i < mainList.getUpcomingList().size(); i++) {
                long time1=((calendar.getTime().getHours()*60) + (calendar.getTime().getMinutes()))*60*1000;
                long time2=(mainList.getUpcomingList().get(i).getTime().getHours()*60 + mainList.getUpcomingList().get(i).getTime().getMinutes())*60*1000;
                if (time2>time1 && mainList.getUpcomingList().get(i).isActive()){
                    if (at12)
                        setReminder(mainList.getUpcomingList().get(i).getTime().getTime(), mainList.getUpcomingList().get(i), false);
                    else{
                        setReminder(time2-time1, mainList.getUpcomingList().get(i), false);
                    }
                }
            }
            setReminder(time,null,true);
        }
    }
    private void setReminder(long time,ListItemObject listItemObject,boolean isMain){
        AlarmManager alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent fullScreenIntent=new Intent(context,ReminderReceiver.class);
        fullScreenIntent.putExtra("isMain",isMain);
        if (listItemObject!=null){
            fullScreenIntent.putExtra("pill_name",listItemObject.getName());
            fullScreenIntent.putExtra("pill_image",listItemObject.getImageId());
        }
        Calendar c=Calendar.getInstance();
        long id= (long) (c.getTimeInMillis()*Math.random());
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context, (int) id,fullScreenIntent,0);
        if (Build.VERSION.SDK_INT>=23)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC,c.getTimeInMillis()+time,pendingIntent);
        else
            alarmManager.setExact(AlarmManager.RTC,c.getTimeInMillis()+time,pendingIntent);
    }
}
