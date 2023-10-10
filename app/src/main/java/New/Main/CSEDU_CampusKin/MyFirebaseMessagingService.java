package New.Main.CSEDU_CampusKin;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static final String channelId = "notification_channel";
    public static final String channelName= "New.Main.CSEDU_CampusKin";
    private RemoteViews getCustomDesign(String title, String message)
    {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.pushnotification);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.description, message);
        remoteViews.setImageViewResource(R.id.app_logo, R.drawable.logo);
        return remoteViews;
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        if(remoteMessage.getNotification()!=null){
            generateNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
        }
    }

    void generateNotification(String title, String message){
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        //channel id, channel name
        NotificationCompat.Builder builder= new NotificationCompat.Builder(getApplicationContext(),channelId)
                .setSmallIcon(R.drawable.logo)
                .setAutoCancel(false)
                .setVibrate(new long[]{1000, 1000, 1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder = builder.setContent(getCustomDesign(title, message));
        }
        else{
            builder = builder.setContentTitle(title).setContentText(message).setSmallIcon(R.drawable.logo);
        }
        //builder=builder.setContent(getRemoteView(title,message));

        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel= new NotificationChannel(channelId,channelName,notificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);

        }
        notificationManager.notify(0,builder.build());





    }
}
