package shuvalov.nikita.restaurantroulette;

/**
 * Created by NikitaShuvalov on 11/30/16.
 */

public class UberAPI {

    /**
     * Uber API Logic goes in here
     */
    public static void getEstimates(Float startLong, Float startLat, Float endLong, Float endLat, boolean returnAsString){
        /**
         * Getting estimate logic here, figure out which cartype most likely:
         * Localized_display_name==UberX
         */

        //Once logic is retrieved
        if(returnAsString){
            //Pass it the actual Estimate
            estimateAsString("Estimate");
        }else{
            //Pass it high_estimate, low_estimate as average or range?
            estimateAsInt(0,123);
        }
    }

    public static String estimateAsString(String estimate){return estimate;}
    public static int estimateAsInt(int highEst, int lowEst) {
        /**
         * Logic to clean up text
         */
        return 123123123;
    }
}
