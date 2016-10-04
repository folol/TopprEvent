package com.topprevents.android;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by devansh on 9/24/16.
 */
public class CustomAdapter extends BaseAdapter {


    List<Website> events;
    Context mContext;
    int lowerLimit, upperLimit;

    public CustomAdapter(Context Context, List<Website> eventList, int cp) {

        events = new ArrayList<>();

        events = eventList;

        mContext = Context;

        upperLimit = cp * 4 - 1;

        lowerLimit = upperLimit - 3;

    }

    public void dataChanged(List<Website> list) {

        if (list != null) {
            events = list;
        }

    }

    public void changeCurrentPage(int cp) {

        upperLimit = cp * 4 - 1;

        lowerLimit = upperLimit - 3;



        this.notifyDataSetChanged();

       // events.clear();
       // events.addAll(list);




    }

    public void changeSortingMethod(int i){
        //0 - cat , 1 - fav

        if(i == 0) {

            Collections.sort(events, new Comparator<Website>() {
                public int compare(Website a, Website b) {
                    return a.getCategory().compareTo(b.getCategory());
                }
            });
        }
        else {

            Collections.sort(events, new Comparator<Website>() {
                public int compare(Website a, Website b) {
                    boolean first = false ,second = false;
                    if(Preferences.getAppPrefs().isEventFav(a.getId()))
                        first = true;
                    if(Preferences.getAppPrefs().isEventFav(b.getId()))
                        second = true;

                    if(first && second)
                        return 0;
                    else if(first && !second)
                        return -1;
                    else if(!first && second)
                            return 1;
                    else
                        return 0;

                }
            });

        }

        this.notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(events.size() < 4)
            return events.size();
        return 4;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated me
        // thod stub

        View listSingle;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = null;

        if (convertView == null) {
            listSingle = inflater.inflate(R.layout.list_item_layout, parent, false);


            TextView eventName = (TextView) listSingle.findViewById(R.id.eventName);
            TextView eventCat = (TextView) listSingle.findViewById(R.id.eventCat);
            ImageView eventImage = (ImageView) listSingle.findViewById(R.id.event_image);
            final ImageView star = (ImageView) listSingle.findViewById(R.id.star);
            ImageView expand = (ImageView) listSingle.findViewById(R.id.expand);





            position = position+lowerLimit;

            if(position <= upperLimit) {
                String s = events.get(position).getCategory();
                star.setTag(events.get(position).getId());
                expand.setTag(position);
                switch (s) {

                    case "HIRING":
                        eventImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.hiring));
                        break;
                    case "BOT":
                        eventImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.robot));
                        break;
                    case "HACKATHON":
                        eventImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.softtware_engineer));
                        break;


                }


                eventName.setText(events.get(position).getName());
                eventCat.setText(s);

                if(Preferences.getAppPrefs().isEventFav(events.get(position).getId())) {
                    Log.d("Toppr","checking - "+events.get(position).getId());
                    star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_on));
                }
                else {
                    star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star));
                }

                final int finalPosition = position;
                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int id = (int)view.getTag();

                        Log.d("Toppr","clicked "+id);
                        if(Preferences.getAppPrefs().isEventFav(id)){
                            Log.d("Toppr","clicked true");

                            Preferences.getAppPrefs().removeEvent(id);
                           // if(Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                star.setImageDrawable( mContext.getResources().getDrawable(R.drawable.star) );
//                            } else {
//                                star.setImageDrawable( mContext.getResources().getDrawable(R.drawable.star));
//                            }

                        }
                        else {
                            Log.d("Toppr","clicked false");

                            Preferences.getAppPrefs().saveEvent(events.get(finalPosition));
                           // if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                star.setImageDrawable( mContext.getResources().getDrawable(R.drawable.star_on) );
//                            } else {
//                                star.setImageDrawable( mContext.getResources().getDrawable(R.drawable.star_on));
//                            }

                        }

                        notifyDataSetChanged();
                    }
                });


                expand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int)view.getTag();
                        Intent i = new Intent(mContext , ViewEventActivity.class);
                        Website w = events.get(position);

                        //Correct way would be to make webstie parceable but it's
                        // that i had little time left when i was writing this

                        i.putExtra("event_id",w.getId());
                        i.putExtra("event_name",w.getName());
                        i.putExtra("event_cat",w.getCategory());
                        i.putExtra("event_desc",w.getDescription());
                        i.putExtra("event_image",w.getImage());
                        i.putExtra("event_exp",w.getExperience());

                        mContext.startActivity(i);

                    }
                });
            }

    }

    else

    {

                listSingle = convertView;

                TextView eventName = (TextView) listSingle.findViewById(R.id.eventName);
                TextView eventCat = (TextView) listSingle.findViewById(R.id.eventCat);
                ImageView eventImage = (ImageView) listSingle.findViewById(R.id.event_image);
        final ImageView star = (ImageView) listSingle.findViewById(R.id.star);



        position = position+lowerLimit;
        star.setTag(position);
        if(position <= upperLimit) {
            String s = events.get(position).getCategory();
            star.setTag(events.get(position).getId());

            switch (s) {

                case "HIRING":
                    eventImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.hiring));
                    break;
                case "BOT":
                    eventImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.robot));
                    break;
                case "HACKATHON":
                    eventImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.softtware_engineer));
                    break;


            }


            eventName.setText(events.get(position).getName());
            eventCat.setText(s);

            if(Preferences.getAppPrefs().isEventFav(events.get(position).getId()))
                star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star_on));

            final int finalPosition = position;
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int id = (int)view.getTag();

                    Log.d("Toppr","clicked "+id);
                    if(Preferences.getAppPrefs().isEventFav(id)){
                        Log.d("Toppr","clicked true");

                        Preferences.getAppPrefs().removeEvent(id);
                        // if(Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        star.setImageDrawable( mContext.getResources().getDrawable(R.drawable.star) );
//                            } else {
//                                star.setImageDrawable( mContext.getResources().getDrawable(R.drawable.star));
//                            }

                    }
                    else {
                        Log.d("Toppr","clicked false");

                        Preferences.getAppPrefs().saveEvent(events.get(finalPosition));
                        // if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        star.setImageDrawable( mContext.getResources().getDrawable(R.drawable.star_on) );
//                            } else {
//                                star.setImageDrawable( mContext.getResources().getDrawable(R.drawable.star_on));
//                            }

                    }

                    notifyDataSetChanged();
                }
            });
        }



        }

        return listSingle;

    }


    public void filterEvents(String s){




    }
}
