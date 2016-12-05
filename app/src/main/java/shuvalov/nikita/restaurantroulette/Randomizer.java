package shuvalov.nikita.restaurantroulette;


import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

/**
 * Created by NikitaShuvalov on 11/30/16.
 */

public class Randomizer {
    private String mMaxPricy;
    private double mMinRating;
    private Context mContext;
    /**
     * Decided to make this into an object that way we can create one with passed parameters, default parameters, or sharedPreferences
     * @param minRating value between 1.0-5.0
     * @param maxExpensiveness "$", "$$", "$$$", "$$$$"
     * @param context Takes context to create toasts or sharedPreferences if necessary.
     */
    public Randomizer(Context context, float minRating, String maxExpensiveness){
        mMinRating = minRating;
        mMaxPricy = maxExpensiveness;
        mContext = context;
    }

    /**
     *Optional constructor if user doesn't explicitly state rating and expensiveness
     * @param context Takes context to get sharedPreferences, if there's no shared Preferences pass null
     */
    public Randomizer(Context context){
        if (context!= null){
            SharedPreferences sharedPreferences = context.getSharedPreferences(OurAppConstants.USER_PREFERENCES, Context.MODE_PRIVATE);
            float rateVal = sharedPreferences.get(OurAppConstants.SHARED_PREF_RATING, (float) -1);
            if(rateVal!=-1){
                mMinRating=rateVal;//If rating preference exist, set that preference.
            }else{
                mMinRating=3.5;//If rating preference doesn't exist, set to a default value of 3.5
            }
            String priceVal = sharedPreferences.getString(OurAppConstants.SHARED_PREF_PRICING,"");
            if(priceVal.length()!=0){
                mMaxPricy = priceVal;
            }else{
                mMaxPricy = "$$"; //Set default to "$$"
            }

        }else{
            mMaxPricy="$$";
            mMinRating=3.5;
        }

    }

    /**
     * Picks N Random businesses from a list based on parameters.
     *
     * @Param List<Business>, int numberToPick
     */

    public List<Business> pickRandomFromList(List<Business> businessList, int numberToPick) {
        List<Business> randomPicks = new ArrayList<>();
        Random picker = new Random();
        for (int i = 0; i < numberToPick; i++) {
            if (businessList.size() == 0) {
                Toast.makeText(mContext, "Not enough results to pick from", Toast.LENGTH_SHORT).show();
                break;
            }

            int randomIndex = picker.nextInt(businessList.size());
            Business randomBusiness = businessList.get(randomIndex);
            if (randomBusiness.getRating() < mMinRating || randomBusiness.getPrice() != null && randomBusiness.getPrice().length() >= mMaxPricy.length()) {
                i--;
            } else {
                randomPicks.add(randomBusiness);
            }
            businessList.remove(randomIndex); //Removes randomly picked business to prevent duplicates showing up in random selection
        }
            return randomPicks;

    }
}
