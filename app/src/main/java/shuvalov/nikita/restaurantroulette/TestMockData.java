package shuvalov.nikita.restaurantroulette;

import java.util.ArrayList;
import java.util.List;

import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

/**
 * Created by NikitaShuvalov on 12/6/16.
 */

public class TestMockData {

    public List<Business> mockBusinessSetForRandomizer(){
        List<Business> mockBusinessList = new ArrayList<>();

        //Using a new Randomizer(context,3,"$$") should only return the noted businesses
        mockBusinessList.add(new Business("Adam's Apples",3.5,"$$"));//Return this
        mockBusinessList.add(new Business("Bethany's Beats",4.0,"$$"));//Return this
        mockBusinessList.add(new Business ("Carol's Carrots", 2.5,"$$"));
        mockBusinessList.add(new Business("Devin's Danishes", 4.5,"$$$"));
        mockBusinessList.add(new Business ("Emily's Edibles", 5.0, "$$$$"));
        mockBusinessList.add(new Business("Frank's franks",1.0,"$"));
        mockBusinessList.add(new Business("Gary's Gelato",3.5,"$"));//Return this
        return mockBusinessList;
    }
}
