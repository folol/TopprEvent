package com.topprevents.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewFavourites extends AppCompatActivity {

    TextView totalFav;
    ListView favList;
    ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_favourites);

        mProgressbar = (ProgressBar) findViewById(R.id.progressBar);

        totalFav = (TextView) findViewById(R.id.totalFav);
        favList = (ListView) findViewById(R.id.favlist);

        mProgressbar.setVisibility(View.VISIBLE);

        int[] totalFavId = Preferences.getAppPrefs().getAllIds();

        totalFav.setText("Total Favourites: "+totalFavId.length);

        ArrayList<Website> favWeb = new ArrayList<>();

        for(int i = 0 ; i <totalFavId.length ; i++){

            Website w = Preferences.getAppPrefs().getEvent(totalFavId[i]);

            favWeb.add(w);


        }

        CustomAdapter2 customAdapter2 = new CustomAdapter2(this , favWeb);

        mProgressbar.setVisibility(View.GONE);

        favList.setAdapter(customAdapter2);




    }
}
