package shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justinwells on 12/2/16.
 */
public class FakeBusiness {
    private static FakeBusiness ourInstance = new FakeBusiness();

    public static Business getInstance() {
        return new Business("http://cdn2-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-8.jpg",
                "732-598-6556",
                "$$$",
                false,
                new Location("407 7th Ave", "America",null,"Asbury Park", "NJ",null,"07712"),
                "http://www.google.com",
                null,
                3,
                getCategories(),
                4.0,
                new Coordinates(40.228319, -74.00348300000002),
                5.0,
                "Joe's Pizza");
    }

    private FakeBusiness() {
    }



    public static List<Category> getCategories() {
        List<Category>categories = new ArrayList<>();
        Category category = new Category("Pizza", "pizza");
        categories.add(category);
        return categories;
    }
}
