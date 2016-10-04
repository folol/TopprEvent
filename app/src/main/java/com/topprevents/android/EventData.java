package com.topprevents.android;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devansh on 9/24/16.
 */
public class EventData {


    List<Website> websites  = new ArrayList<>();
    int quote_max , quote_available;


    public int getQuote_available() {
        return quote_available;
    }

    public void setQuote_available(int quote_available) {
        this.quote_available = quote_available;
    }

    public int getQuote_max() {
        return quote_max;
    }

    public void setQuote_max(int quote_max) {
        this.quote_max = quote_max;
    }

    public List<Website> getWebsites() {
        return websites;
    }

    public void setWebsites(List<Website> websites) {
        this.websites = websites;
    }
}
