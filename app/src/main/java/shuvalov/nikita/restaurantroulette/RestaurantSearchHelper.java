package shuvalov.nikita.restaurantroulette;

import java.util.ArrayList;
import java.util.List;

import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

/**
 * Created by NikitaShuvalov on 12/1/16.
 */
public class RestaurantSearchHelper {
    private static List<Business> mSearchResults;

    private static RestaurantSearchHelper ourInstance;


    public static RestaurantSearchHelper getInstance() {
        if(ourInstance == null) {
            ourInstance = new RestaurantSearchHelper();
        }
        return ourInstance;
    }

    private RestaurantSearchHelper() {
        mSearchResults = new ArrayList<>();
    }

    public void setmBusinessList(List<Business> searchResults){
        mSearchResults.clear();
        mSearchResults.addAll(searchResults);
    }
    public List<Business> getSearchResults(){
        return mSearchResults;
    }
}
