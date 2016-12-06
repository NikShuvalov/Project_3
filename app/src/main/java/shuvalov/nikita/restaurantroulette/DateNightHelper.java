package shuvalov.nikita.restaurantroulette;

import android.support.annotation.Nullable;

import java.util.ArrayList;

import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

/**
 * Created by NikitaShuvalov on 12/5/16.
 */

public class DateNightHelper {
    private ArrayList<Business> mDateItinerary;

    private static DateNightHelper mDateNightHelper;

    public static DateNightHelper getInstance() {
        if(mDateNightHelper==null){
            mDateNightHelper= new DateNightHelper();
        }
        return mDateNightHelper;
    }
    private DateNightHelper(){
        mDateItinerary=new ArrayList<>();
    }

    public ArrayList<Business> getDateItinerary() {
        return mDateItinerary;
    }
    public void addBusinessToItinerary(Business business){
        mDateItinerary.add(business);
    }

    public void removeBusinessAtPosition(int position){
        mDateItinerary.remove(position);
    }
}
