package com.topprevents.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<EventData> {

    Call<EventData> call;
    ProgressBar mProgressBar;
    ListView listView;
    EditText editText;
    CustomAdapter customAdapter;
    List<Website> eventList;
    TextView totalEvents , quota , tv_curr_page;
    ImageView cancelSearch;

    SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout pagindCont , sortCont , openFav , favLow , catLow;

    int currentPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);

        actionBar.setTitle(getResources().getString(R.string.app_name));

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        listView = (ListView) findViewById(R.id.list);

        totalEvents = (TextView) findViewById(R.id.totalEvents);
        quota = (TextView) findViewById(R.id.quota);

        tv_curr_page = (TextView) findViewById(R.id.curretPage);
        editText = (EditText) findViewById(R.id.search_box);

        cancelSearch = (ImageView) findViewById(R.id.cancel_search);

        pagindCont = (LinearLayout) findViewById(R.id.pagingCont);

        sortCont = (LinearLayout) findViewById(R.id.sort);

        favLow = (LinearLayout) findViewById(R.id.favLower);
        catLow = (LinearLayout) findViewById(R.id.catLower);

        openFav = (LinearLayout) findViewById(R.id.openFav);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.view);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                callApi();
            }
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence si, int start, int before, int count) {
                // TODO Auto-generated method stub

                String s = editText.getText().toString();
                if(customAdapter != null ){

                    List<Website> tempList = new ArrayList<>();
                    for(Website website: eventList){

                        if (website.getName().toLowerCase().contains(s.toLowerCase())) {
                            tempList.add(website);
                        }

                        if(website.getCategory().toLowerCase().contains(s.toLowerCase()))
                            tempList.add(website);

                    }


                    customAdapter.dataChanged(tempList);
                    customAdapter.changeCurrentPage(1);
                    //customAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editText.setText("");

            }
        });

        sortCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence items[] = {"Category","Favourites"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Sort by");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Create the File where the photo should go

                        if (items[which].equals("Category")) {

                            customAdapter.changeSortingMethod(0);

                        } else if (items[which].equals("Favourites")) {


                            customAdapter.changeSortingMethod(1);

                        } else {
                            dialog.dismiss();
                        }

                    }

                });
                dialog.show();


            }
        });

        catLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.changeSortingMethod(0);
            }
        });

        favLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customAdapter.changeSortingMethod(1);
            }
        });


        openFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this , ViewFavourites.class);
                startActivity(i);

            }
        });

        callApi();



    }


    private void callApi(){

        if(mProgressBar != null && mProgressBar.getVisibility() == View.GONE)
            mProgressBar.setVisibility(View.VISIBLE);

        GetEventService getEventService = ServiceGenerator.createService(GetEventService.class);
        call = getEventService.get("json" , "list_events");
        call.enqueue(this);




    }


    @Override
    public void onResponse(Call<EventData> call, Response<EventData> response) {

      //  Log.d("Toppr","or 0 "+response.isSuccessful() +" "+response.code());

        if(mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE)
            mProgressBar.setVisibility(View.GONE);

      //  Log.d("Toppr","or 1 "+response.isSuccessful() +" "+response.code());

        if(response.isSuccessful()){
           // Log.d("Toppr","or 2 ");

            if(response.code() == 200){


               // Log.d("Toppr","or 3 ");


                EventData eventData = response.body();

             //   Log.d("Toppr","size - "+eventData);

                addDataToView(eventData);


            }

        }

    }


    private void addDataToView(EventData eventData){


        if(eventData != null){

            eventList = eventData.getWebsites();

            float quotaValue = ((float)eventData.getQuote_available() / (float)eventData.getQuote_max()) * 100;



            quota.setText("API Quota:"+quotaValue+"%");

            totalEvents.setText(String.valueOf(eventList.size()));

         //   Log.d("Toppr","size - "+eventList.size());

            customAdapter = new CustomAdapter(this , eventList , currentPage);

            listView.setAdapter(customAdapter);

            setPaging();


        }

    }

    private void setPaging(){


        int pages = eventList.size() / 4;

        float weight = (float)10/pages;

        pagindCont.removeAllViews();

        for(int i= 0 ;i <pages ; i++){

            final Button b = new Button(this);
            b.setText(String.valueOf(i+1));
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT , weight);
            //params.setMargins(10,10,10, 10);
            b.setGravity(Gravity.CENTER);
            b.setLayoutParams(params);

            b.setBackgroundColor(getResources().getColor(R.color.transparent));

            if(i != 0 && i != pages-1) {
                if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    b.setBackgroundDrawable( getResources().getDrawable(R.drawable.page_button_base) );
                } else {
                    b.setBackground( getResources().getDrawable(R.drawable.page_button_base));
                }

            }

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s  = b.getText().toString();
                    tv_curr_page.setText(s);
                    int cp = Integer.parseInt(s);
                   // Log.d("Toppr","size ma -"+eventList.size());
                    customAdapter.changeCurrentPage(cp);
                    //customAdapter.notifyDataSetChanged();

                }
            });


            pagindCont.addView(b);



        }

        tv_curr_page.setText("Page 1");


    }

    @Override
    public void onFailure(Call<EventData> call, Throwable t) {

        if(mProgressBar != null && mProgressBar.getVisibility() == View.VISIBLE)
            mProgressBar.setVisibility(View.GONE);

       // Log.d("Toppr","of 1 "+t.getMessage()+" "+t.getCause());



    }


}
