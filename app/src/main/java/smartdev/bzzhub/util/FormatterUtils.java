package smartdev.bzzhub.util;

import android.annotation.SuppressLint;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import smartdev.bzzhub.repository.chatmodel.Users;

public class FormatterUtils {

    public static String getListTime(Timestamp timestamp) {
        String localTime = "";
        try {
            Calendar calendar = Calendar.getInstance();
            TimeZone timeZone = calendar.getTimeZone();
            Date calendarDate = Calendar.getInstance().getTime();
            /* date formatter in local timezone */
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatMMDD = new SimpleDateFormat("dd/MM,HH:mm");
 /*           @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatHHMM = new SimpleDateFormat("HH:mm");*/
            dateFormatMMDD.setTimeZone(timeZone);
       //     dateFormatHHMM.setTimeZone(timeZone);

            Date date = new Date(timestamp.toDate().getTime());
            calendar.setTime(date);
            localTime = dateFormatMMDD.format(date);
          /*  if (dateFormatMMDD.format(calendarDate).equals(dateFormatMMDD.format(date))) {
                localTime = dateFormatHHMM.format(date);
            } else {
                localTime = dateFormatMMDD.format(date);
            }*/
            return localTime;
        } catch (Exception e) {
            e.printStackTrace();
            return localTime;
        }
    }

    public static String getUserNameFromList(int id, int flag, ArrayList<Users> userList){
        String strName = "Unknown";
        if (userList != null && !userList.isEmpty()){
            for (int i = 0;i < userList.size(); i++){
                if (userList.get(i).getUserID() == id && userList.get(i).getUserFlag() == flag){
                    strName = userList.get(i).getUserName();

                }
            }
        }

        return strName;
    }

    public static String getUserImageFromList(int id, int flag, ArrayList<Users> userList){
        String strImage = "";
        if (userList != null && !userList.isEmpty()){
            for (int i = 0;i < userList.size(); i++){
                if (userList.get(i).getUserID() == id && userList.get(i).getUserFlag() == flag){
                    strImage = userList.get(i).getUserProfileURL();

                }
            }
        }

        return strImage;
    }

}
