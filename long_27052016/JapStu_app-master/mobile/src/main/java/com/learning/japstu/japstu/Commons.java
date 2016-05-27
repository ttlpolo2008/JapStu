package com.learning.japstu.japstu;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class Commons {
    // random the direction for learning.
    public static char[] chooseDirection4Learning(){
        char[] direction = new char[6];
        for (int i = 0; i < 6; i++){
            direction[i] = chooseNextPart(direction, i);
        }
        return direction;
    }

    public static char chooseNextPart(char[] current, int index){
        Random rand = new Random();
        if(index == 0){
            return (char)(rand.nextInt(6) + 49);
        }
        char result = '0';
        boolean cont = true;
        while(cont) {
            int a = rand.nextInt(6) + 49;
            result = (char)(a);
            for (int i = 0; i < index; i++) {
                if(result == current[i] || result == '0'){
                    cont = true;
                    break;
                }
                cont = false;
            }
        }
        return result;
    }


    public static void playSoundTrack(byte[] mp3SoundByteArray, final Context context){

        try {
            File tempMp3;
            System.out.println(mp3SoundByteArray.length + "    " + (300*1024));
            if(mp3SoundByteArray.length < 300*1024) {
                tempMp3 = File.createTempFile("tempSoundTrack", "mp3", context.getCacheDir());
            }else{
                tempMp3 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                    + File.separator + "tempSoundTrack" +".mp3");
            }
            tempMp3.deleteOnExit();
            FileOutputStream fos = new FileOutputStream(tempMp3);

            fos.write(mp3SoundByteArray);
            fos.flush();
            fos.close();
            if(!tempMp3.isFile() || !tempMp3.exists()) {
                System.out.println("cannot save file sound");
                return;
            }


            MediaPlayer mediaPlayer = new MediaPlayer(){
                ProgressDialog dialog;
                @Override
                public void start() throws IllegalStateException {
                    super.start();
                }

                @Override
                public void setScreenOnWhilePlaying(boolean screenOn) {
                    super.setScreenOnWhilePlaying(screenOn);
                }

                @Override
                public void stop() throws IllegalStateException {
//                    dialog.dismiss();
                    super.stop();
                }
            };

            FileInputStream fis = new FileInputStream(tempMp3);
            mediaPlayer.setDataSource(fis.getFD());

            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void showAlertDialog(String msg, Context context){
        String dialog_message = msg;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(dialog_message)
                .setTitle("Warning");


        AlertDialog dialog = builder.create();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
