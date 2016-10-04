package com.topprevents.android;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

public class ViewEventActivity extends AppCompatActivity implements ImageLoadingListener{


    TextView eventName , eventExp , eventCTC , eventDesc;
    Website w;

    ImageView imageView ;
    Button share;

    ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        eventName = (TextView) findViewById(R.id.eventName);
        eventCTC = (TextView) findViewById(R.id.event_ctc);
        eventExp  = (TextView) findViewById(R.id.event_exp);

        eventDesc = (TextView) findViewById(R.id.event_desc);

        mProgressbar = (ProgressBar) findViewById(R.id.progressBar);

        imageView = (ImageView) findViewById(R.id.eventImage);

        share = (Button) findViewById(R.id.shareButton);

        w = new Website();
        Intent i = getIntent();
        if(i != null){

            w.setId(i.getIntExtra("event_id",-1));
            w.setDescription(i.getStringExtra("event_desc"));
            w.setImage(i.getStringExtra("event_image"));
            w.setName(i.getStringExtra("event_name"));
            w.setCategory(i.getStringExtra("event_cat"));
            w.setExperience(i.getStringExtra("event_exp"));

        }




        eventName.setText(w.getName());
        eventExp.setText("Experience: "+w.getExperience()+" years");

        eventCTC.setText("CTC: ");

        eventDesc.setText(w.getDescription());

        String url = w.getImage();

        ImageLoader imageLoader = ApplicaitonContext.imageLoader;

        mProgressbar.setVisibility(View.VISIBLE);

        if(url != null) {
            File file = imageLoader.getDiskCache().get(url);
            if (file == null || !file.exists()) {
                //  Log.d("ImageLoader","inside loading 1 ");
                //  Log.d("CommonUtils", "Load image from URL : " + url);
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheOnDisk(true)
                        .cacheInMemory(true)
                        .build();

                imageLoader.loadImage(url, this);

            } else {
                //  Log.d("ImageLoader","inside loading 2");
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                this.onLoadingComplete(file.getAbsolutePath(), imageView, bitmap);
                //  Log.d("Parameters","url2  - "+url+" "+bitmap);
            }
        }


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Event at Toppr- "+w.getName()+" in "+w.getCategory()+" .Exp required- "+w.getExperience()+" years");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));

            }
        });








    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.home_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) { //app icon in action bar clicked; go back
            //do something
//            if(!navDrawer.isDrawerOpen())
//                navDrawer.openDrawer();
//            else
//                navDrawer.closeDrawer();
//            return true;
            onBackPressed();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLoadingStarted(String imageUri, View view) {
        // Empty implementation
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
        // Empty implementation
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        // Empty implementation
        imageView.setImageBitmap(loadedImage);
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {
        // Empty implementation
        mProgressbar.setVisibility(View.GONE);
    }
}
