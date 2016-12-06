package shuvalov.nikita.restaurantroulette;

import java.util.ArrayList;
import java.util.List;

import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

/**
 * Created by NikitaShuvalov on 12/1/16.
 */
public class RestaurantSearchHelper {
    private static List<Business> mSearchResults;

    //ToDo:Might need a second list reference to hold the random results as well.

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

    /**
     * Important that this method clears current list and then adds results to that list instead of creating new list
     * because that's the list the RecyclerAdapter has a reference to.
     *
     * @param searchResults
     */
    public void setmBusinessList(List<Business> searchResults){
        mSearchResults.clear();
        mSearchResults.addAll(searchResults);
    }
    public List<Business> getSearchResults(){
        return mSearchResults;
    }
    public Business getBusinessAtPosition(int position){
        return mSearchResults.get(position);
    }
    public void addBusinessToList(Business business) {
        mSearchResults.add(business);
    }
}
