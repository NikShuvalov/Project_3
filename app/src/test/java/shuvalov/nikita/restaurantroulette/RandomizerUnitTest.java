package shuvalov.nikita.restaurantroulette;

import org.junit.Test;

import java.util.List;

import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

import static org.junit.Assert.*;


/**
 * Created by NikitaShuvalov on 12/7/16.
 */

public class RandomizerUnitTest {
    @Test
    public void randomizerFilterCorrectly() throws Exception{
        TestMockData mockData = new TestMockData();
        List<Business> mockBusinesses = mockData.mockBusinessSetForRandomizer();

        Randomizer randomizer = new Randomizer(3,2);
        List<Business> postRandomizedList = randomizer.pickRandomFromList(mockBusinesses);
        assertEquals(3, postRandomizedList.size()); //Asserts that that size of the list is 3 after randomizing.


    }
}
