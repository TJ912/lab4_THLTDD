package com.example.lab4thltdd;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class Utility {
    //Notifycation
    //Notifycation Id
    public static final int NOTIFICATION_ID=1;

    public static NotificationManagerCompat mNotificationManager;

    //create Notifycation

    public static void initNotification(String songTitle, Context mContext){
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = "my_channel";
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){


                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name, NotificationManager.IMPORTANCE_DEFAULT);
//                notificationChannel.enableLights(true);
//                notificationChannel.setLightColor(Color.RED);
//                notificationChannel.enableVibration(true);
//                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                NotificationManager manager= mContext.getSystemService(NotificationManager.class);
                manager.createNotificationChannel(notificationChannel);

            }
            mNotificationManager=NotificationManagerCompat.from(mContext);


            Intent notificationIntent=new Intent(mContext,MainActivity.class);
            PendingIntent contentIntent=PendingIntent.getActivity(mContext,0,notificationIntent,0)   ;

            NotificationCompat.Builder builder=new NotificationCompat.Builder(mContext,CHANNEL_ID )
                    .setSmallIcon(R.drawable.ic_baseline_star_24)
                    .setContentTitle("Intentions- Justin Bieber")
                    .setContentText("Lab 4_nhom4A")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(false);

            mNotificationManager.notify(NOTIFICATION_ID,builder.build());


    }
    public static int getProgressPercentage(long currentDuration,long totalDuration){
        Double percentage=(double) 0;
        long currentSeconds=(int) (currentDuration/1000);
        long totalSeconds=(int)(totalDuration/1000);

        percentage=((double) currentSeconds/totalSeconds)*100;
        return percentage.intValue();
    }

    public static int progressToTimer(int progress,int totalDuration){
        int currentDuration=0;
        totalDuration=(int) (totalDuration/1000);
        currentDuration=(int) ((((double)progress)/100)*totalDuration  );
       //return current duration in milliseconds
        return currentDuration*1000;
    }

    public static String milliSecondsToTimer(long milliSeconds){


        //Convert total duration into time
        int hours=(int) (milliSeconds/(1000*60*60));
        int minutes=(int) (milliSeconds%(1000*60*60)/(1000*60));
        int seconds=(int) ((milliSeconds%(1000*60*60)%(1000*60))/1000);


        return String.format("%02d:%02d:%02d",hours,minutes,seconds);

    }

}
