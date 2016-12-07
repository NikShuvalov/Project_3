
package shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Business {

    public Business(List<Category> categories, Coordinates coordinates, Double distance, String id, String imageUrl, Boolean isClosed, Location location, String name, String phone, String price, Double rating, Integer reviewCount, String url) {
        this.categories = categories;
        this.coordinates = coordinates;
        this.distance = distance;
        this.id = id;
        this.imageUrl = imageUrl;
        this.isClosed = isClosed;
        this.location = location;
        this.name = name;
        this.phone = phone;
        this.price = price;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.url = url;
    }

    //Constructor used for Randomizer testing
    public Business(String name, Double rating,String price){
        this.name=name;
        this.rating = rating;
        this.price = price;
    }

    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("is_closed")
    @Expose
    private Boolean isClosed;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("review_count")
    @Expose
    private Integer reviewCount;
    @SerializedName("categories")
    @Expose
    private List<Category> categories = new ArrayList<Category>();
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("coordinates")
    @Expose
    private Coordinates coordinates;
    @SerializedName("distance")
    @Expose
    private Double distance;
    @SerializedName("name")
    @Expose
    private String name;

    public Business(String imageUrl, String phone, String price, Boolean isClosed, Location location,
                    String url, String id, Integer reviewCount, List<Category> categories, Double rating,
                    Coordinates coordinates, Double distance, String name) {
        this.imageUrl = imageUrl;
        this.phone = phone;
        this.price = price;
        this.isClosed = isClosed;
        this.location = location;
        this.url = url;
        this.id = id;
        this.reviewCount = reviewCount;
        this.categories = categories;
        this.rating = rating;
        this.coordinates = coordinates;
        this.distance = distance;
        this.name = name;
    }

    /**
     * 
     * @return
     *     The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 
     * @param imageUrl
     *     The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 
     * @return
     *     The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone
     *     The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 
     * @return
     *     The price
     */
    public String getPrice() {
        return price;
    }

    /**
     * 
     * @param price
     *     The price
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * 
     * @return
     *     The isClosed
     */
    public Boolean getIsClosed() {
        return isClosed;
    }

    /**
     * 
     * @param isClosed
     *     The is_closed
     */
    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    /**
     * 
     * @return
     *     The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * 
     * @param location
     *     The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The reviewCount
     */
    public Integer getReviewCount() {
        return reviewCount;
    }

    /**
     * 
     * @param reviewCount
     *     The review_count
     */
    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    /**
     * 
     * @return
     *     The categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * 
     * @param categories
     *     The categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    /**
     * 
     * @return
     *     The rating
     */
    public Double getRating() {
        return rating;
    }

    /**
     * 
     * @param rating
     *     The rating
     */
    public void setRating(Double rating) {
        this.rating = rating;
    }

    /**
     * 
     * @return
     *     The coordinates
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * 
     * @param coordinates
     *     The coordinates
     */
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * 
     * @return
     *     The distance
     */
    public Double getDistance() {
        return distance;
    }

    /**
     * 
     * @param distance
     *     The distance
     */
    public void setDistance(Double distance) {
        this.distance = distance;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

}
