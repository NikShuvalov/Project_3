
package shuvalov.nikita.restaurantroulette.UberResources.UberObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UberMainObject {

    @SerializedName("prices")
    @Expose
    private List<Price> prices = new ArrayList<Price>();

    /**
     * 
     * @return
     *     The prices
     */
    public List<Price> getPrices() {
        return prices;
    }

    /**
     * 
     * @param prices
     *     The prices
     */
    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

}
