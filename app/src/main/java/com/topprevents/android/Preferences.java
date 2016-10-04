package com.topprevents.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by devansh on 9/24/16.
 */
public class Preferences {

    private static Preferences prefObj = new Preferences();


    public static Preferences getAppPrefs() {
        return prefObj;
    }

    private Preferences() {

    }

    public void saveEvent(Website w){
        Context appC = ApplicaitonContext.getAppContext();

        saveEventId(w.getId());


        // Save in the preferences
        if(appC != null) {
            SharedPreferences prefs = appC.getSharedPreferences("save_event_"+w.getId(), appC.MODE_PRIVATE);
            prefs.edit().putString("event_name",w.getName()).apply();
            prefs.edit().putInt("event_id",w.getId()).apply();
            prefs.edit().putString("event_cat",w.getCategory()).apply();
            prefs.edit().putString("event_desc",w.getDescription()).apply();
            prefs.edit().putString("event_image",w.getImage()).apply();
            Log.d("Event saved","save_event_"+w.getId());

        }
        else {

            Log.d("Event saved", "not save_event_" +appC);
        }


    }

    public void saveEventId(int id){
        Context appC = ApplicaitonContext.getAppContext();


        // Save in the preferences
        if(appC != null) {
            SharedPreferences prefs = appC.getSharedPreferences("save_all_event_ids", appC.MODE_PRIVATE);

            String ids = prefs.getString("all_id", "");
            StringTokenizer st = new StringTokenizer(ids, ",");

            Log.d("SaveFav","ttb "+st.countTokens());

            int counts = st.countTokens();
            //ArrayList<String> savedList = new ArrayList<>();
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < counts; i++) {
                str.append(st.nextToken()).append(",");
            }


            str.append(String.valueOf(id)).append(",");
            Log.d("SaveFav","sei "+str.toString()+" ");
            prefs.edit().putString("all_id", str.toString()).commit();


        }

    }

    public int[] getAllIds(){

        Context appC = ApplicaitonContext.getAppContext();


        // Save in the preferences
        if(appC != null) {
            SharedPreferences prefs = appC.getSharedPreferences("save_all_event_ids", appC.MODE_PRIVATE);
            String ids = prefs.getString("all_id", "");
            Log.d("SaveFav","gai "+ids);
            StringTokenizer st = new StringTokenizer(ids, ",");

            int counts = st.countTokens();
            int[] savedList = new int[counts];
            for (int i = 0; i < counts; i++) {
                savedList[i] = Integer.parseInt(st.nextToken());
            }

            return savedList;
        }
        return null;

    }

    public void removeIdfromAllIds(int id){

        String idInString = String.valueOf(id);

        Context appC = ApplicaitonContext.getAppContext();


        // Save in the preferences
        if(appC != null) {
            SharedPreferences prefs = appC.getSharedPreferences("save_all_event_ids", appC.MODE_PRIVATE);
            String ids = prefs.getString("all_id", "");
            Log.d("SaveFav","rifai "+ids+"itr "+idInString);
            StringTokenizer st = new StringTokenizer(ids, ",");

            int counts = st.countTokens();
            StringBuilder str = new StringBuilder();
            int[] savedList = new int[counts];
            for (int i = 0; i < counts; i++) {
                String s = st.nextToken();
                if(!idInString.equals(s)) {
                    str.append(s).append(",");
                    Log.d("SaveFav","not removed "+s);
                }
            }

            prefs.edit().putString("all_id", str.toString()).commit();


        }


    }

    public Website getEvent(int id){

        Context appC = ApplicaitonContext.getAppContext();


        // Save in the preferences
        if(appC != null) {
            SharedPreferences prefs = appC.getSharedPreferences("save_event_"+id, appC.MODE_PRIVATE);
            String name =  prefs.getString("event_name", null);
            int eid = prefs.getInt("event_id",0);
            String cat =  prefs.getString("event_cat", null);
            String desc =  prefs.getString("event_desc", null);
            String image =  prefs.getString("event_image", null);
            Website w = new Website();
            w.setCategory(cat);
            w.setDescription(desc);
            w.setId(eid);
            w.setName(name);
            w.setImage(image);
            return w;

        }
        else{
            return null;
        }

    }

    public void removeEvent(int id){

        Context appC = ApplicaitonContext.getAppContext();

        if(appC != null) {

            SharedPreferences prefs = appC.getSharedPreferences("save_event_"+id, appC.MODE_PRIVATE);
            if(prefs != null) {
                prefs.edit().clear().commit();
                removeIdfromAllIds(id);
            }
            Log.d("Toppr","Event removed");
        }

    }

    public boolean isEventFav(int w){

        Context appC = ApplicaitonContext.getAppContext();


        // Save in the preferences
        if(appC != null) {
            SharedPreferences prefs = appC.getSharedPreferences("save_event_" + w, appC.MODE_PRIVATE);
            if(prefs != null) {

                int id = prefs.getInt("event_id",-1);
                if(id == -1)
                    return false;

                Log.d("Toppr","event present "+"save_event_" + w);
                return true;
            }
            else
                return false;
        }

        return false;


    }

}
