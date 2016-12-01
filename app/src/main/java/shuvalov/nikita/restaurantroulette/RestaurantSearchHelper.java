package shuvalov.nikita.restaurantroulette;

/**
 * Created by NikitaShuvalov on 12/1/16.
 */
public class RestaurantSearchHelper {
    private static RestaurantSearchHelper ourInstance;


    public static RestaurantSearchHelper getInstance() {
        return ourInstance;
    }

    private RestaurantSearchHelper() {
        if (ourInstance ==null){
            ourInstance = new RestaurantSearchHelper();
        }
    }
}
