package shuvalov.nikita.restaurantroulette;

import java.util.ArrayList;
import java.util.List;

import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

/**
 * Created by justinwells on 12/5/16.
 */
public class RouletteHelper {
    private static RouletteHelper ourInstance;
    private static List<Business> mRandomResults;

    public static RouletteHelper getInstance() {
        if (ourInstance == null) {
            ourInstance = new RouletteHelper();
        }
        return ourInstance;
    }

    private RouletteHelper() {
        mRandomResults = new ArrayList<>();
    }

    public void setRandomList (List<Business>list) {
        mRandomResults.clear();
        mRandomResults.addAll(list);
    }

    public List<Business>getRandomList () {
        return mRandomResults;
    }

    public Business getBusinessAtPosition (int position) {
        return mRandomResults.get(position);
    }
}
