package edu.umw.cpsc.donahue.james.assignmentthree;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataManager {

    private static final String TAG = "DataManager";

    public static void writeToFile(String data, boolean sent, Context context) {
        /*try {
            FileOutputStream fos;
            if (sent) {
                fos = context.openFileOutput("sent", Context.MODE_APPEND);
            } else {
                fos = context.openFileOutput("received", Context.MODE_APPEND);
            }
            fos.write(data.getBytes());
            fos.flush();
            Toast toast = Toast.makeText(context, sent ? "Location Sent" : "Location received", Toast.LENGTH_SHORT);
            toast.show();
        } catch (FileNotFoundException e) {
            Log.e("DATA_MANAGER", "File not found");
        } catch (IOException e) {
            Log.e("DATA_MANAGER", "IOException");
        }*/

        try {
            File file = new File(context.getFilesDir(), sent ? "sent" : "received");
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(data);
            bw.close();
            /*Toast toast = Toast.makeText(context, sent ? "Location Sent" : "Location received", Toast.LENGTH_SHORT);
            toast.show();*/
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        }
    }

    public static ArrayList<MyLocation> readFromFile(boolean sent, Context context) {
        ArrayList<MyLocation> locations = new ArrayList<>();
        File file = new File(context.getFilesDir(), sent ? "sent" : "received");

        try {
            Scanner io = new Scanner(file);

            while (io.hasNext()) {
                String location = io.nextLine();
                locations.add(new MyLocation(location.split(":")[0], Double.valueOf(location.split(":")[1]),
                        Double.valueOf(location.split(":")[2])));
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
        } finally {
            return locations;
        }
    }

    public static void saveUsername(Context context, String username) {
        try {
            File file = new File(context.getFilesDir(), "username");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(username);
            bw.close();
        } catch (IOException e) {
            Log.e(TAG, "IOException");
        }
    }

    public static String readUsername(Context context) {
        String username = "";
        try {
            File file = new File(context.getFilesDir(), "username");
            Scanner io = new Scanner(file);
            username = io.nextLine();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "FileNotFoundException");
        } finally {
            return username;
        }
    }
}
