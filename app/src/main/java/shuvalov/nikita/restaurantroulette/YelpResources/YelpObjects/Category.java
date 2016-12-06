
package shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("alias")
    @Expose
    private String alias;

    public Category(String title, String alias) {
        this.title = title;
        this.alias = alias;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 
     * @param alias
     *     The alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

}
