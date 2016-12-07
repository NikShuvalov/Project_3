package shuvalov.nikita.restaurantroulette.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import shuvalov.nikita.restaurantroulette.Activities.DateNightActivity;
import shuvalov.nikita.restaurantroulette.Activities.DateNightSearchActivity;
import shuvalov.nikita.restaurantroulette.DateNightHelper;
import shuvalov.nikita.restaurantroulette.OurAppConstants;
import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPI;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpAPIConstants;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Location;

/**
 * Created by NikitaShuvalov on 12/5/16.
 */

public class DateNightRecyclerAdapter extends RecyclerView.Adapter {
    List<Business> mBusinessList;
    int mCategorySpinnerPosition;

    public DateNightRecyclerAdapter(List<Business> businesses){
        mBusinessList = businesses;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==0){//The first viewHolder is unique in that it asks for your location of first business.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_date_first_item, null);
            return new DateItemViewHolderFirst(view);
        }else if (viewType==1&& mBusinessList.size()>0){//The second view appears after we have an initial business.
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_date_consequent_item, null);
            return new DateItemViewHolderConsequent(view);
        } else if (viewType==2){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_search_form,null);
            return new SearchResultViewHolder(view);
        }
        return null;//If all is well, this shouldn't happen. If there are issues just make a new viewholder to put on the bottom.
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder.getItemViewType()<=1){
            Button searchButton = (Button) holder.itemView.findViewById(R.id.search_button);
            Spinner categorySpinner = (Spinner)holder.itemView.findViewById(R.id.category_spinner);
            mCategorySpinnerPosition = categorySpinner.getSelectedItemPosition();
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    mCategorySpinnerPosition = i;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        int itemViewType = holder.getItemViewType();

            //If nothing in DateNightList singleton at the moment, then we start off with the initial search Card.
        if (itemViewType==0) {
            DateItemViewHolderFirst specificHolder = (DateItemViewHolderFirst) holder;
            specificHolder.bindAdapterToSpinner();
            final EditText queryEntry = (EditText) specificHolder.itemView.findViewById(R.id.query_entry);
            final EditText additionalQueryEntry = (EditText) specificHolder.itemView.findViewById(R.id.additional_query_entry);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (queryEntry.getText().toString().isEmpty()) {
                        queryEntry.setError("Can't be empty");
                    } else {
                        String query = "";
                        if (!additionalQueryEntry.getText().toString().isEmpty()) {
                            query = additionalQueryEntry.getText().toString();
                        }
                        String zip = queryEntry.getText().toString();
                        Intent intent = new Intent(view.getContext(), DateNightSearchActivity.class);
                        List<String> categories = YelpAPIConstants.getCategoryList();
                        intent.putExtra(OurAppConstants.SEARCH_CATEGORY, categories.get(mCategorySpinnerPosition));
                        intent.putExtra(OurAppConstants.SEARCH_QUERY, query);
                        intent.putExtra(OurAppConstants.SEARCH_ZIP_VALUE, zip);
                        view.getContext().startActivity(intent);
                    }
                }
            });
        }
        //This adds cards to subsequent date location search
        else{
            DateItemViewHolderConsequent specificHolder = (DateItemViewHolderConsequent) holder;
            specificHolder.bindAdapterToSpinner();

            final EditText additionalQueryEntry = (EditText) specificHolder.itemView.findViewById(R.id.additional_query_entry);

            //Do Search based on location of previous area and category picked.

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Business lastBusiness = DateNightHelper.getInstance().getLastBusinessInList();
                    Location businessLocation = lastBusiness.getLocation();
                    String zipCode = businessLocation.getZipCode();//Search using the zip code of that business.
                    Log.d("Date Itinerary", "Search for business at zip code: "+zipCode);
                    String query = "";
                    if (!additionalQueryEntry.getText().toString().isEmpty()) {
                        query = additionalQueryEntry.getText().toString();
                    }
                    Intent intent = new Intent(view.getContext(), DateNightSearchActivity.class);
                    List<String> categories = YelpAPIConstants.getCategoryList();
                    intent.putExtra(OurAppConstants.SEARCH_CATEGORY, categories.get(mCategorySpinnerPosition));
                    intent.putExtra(OurAppConstants.SEARCH_QUERY, query);
                    intent.putExtra(OurAppConstants.SEARCH_ZIP_VALUE, zipCode);
                    view.getContext().startActivity(intent);
                }
            });

        }

        }else{
            Business business = mBusinessList.get(position);
            ((SearchResultViewHolder)holder).bindDataToView(business);
            ((SearchResultViewHolder) holder).mRemoveButton.setVisibility(View.VISIBLE);
            ((SearchResultViewHolder) holder).mRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DateNightHelper.getInstance().removeBusinessAtPosition(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    ((SearchResultViewHolder) holder).mRemoveButton.setVisibility(View.GONE);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mBusinessList.size()+1;//It's plus one because there's always one more card than there are datecards.
    }

    @Override
    public int getItemViewType(int position) {
        /*If looking at position of recycler that is less than size of business list then we have business data to populate it.
        So we use the searchViewHolder to display the information.
         */
        if(position<mBusinessList.size()){
            return 2;
        }else if(position==0){ //Otherwise, if we're in the first position use the initial querycard. Only happens if we have an empty list.
            return 0;
        }else{//Otherwise, we use the consequent querycards which only take a category and additional info.
            return 1;
        }
        //ToDo: Needs another itemViewType to display picked spots in list.
    }

}
class DateItemViewHolderFirst extends RecyclerView.ViewHolder{
    TextView mTextView;
    Spinner mCatSpinner;
    EditText mQueryInput, mAdditionalQuery;
    Button mButton;
    CardView mCardContainer;

    public DateItemViewHolderFirst(View itemView) {
        super(itemView);

        mTextView = (TextView)itemView.findViewById(R.id.prompt_text);
        mCatSpinner = (Spinner)itemView.findViewById(R.id.category_spinner);
        mQueryInput = (EditText)itemView.findViewById(R.id.query_entry);
        mButton = (Button)itemView.findViewById(R.id.search_button);
        mCardContainer = (CardView)itemView.findViewById(R.id.date_item_card_holder);
        mAdditionalQuery= (EditText)itemView.findViewById(R.id.additional_query_entry);
    }

    public void bindAdapterToSpinner(){
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(mCatSpinner.getContext(),R.array.categories,android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCatSpinner.setAdapter(categoryAdapter);
        mCatSpinner.setSelection(0);
    }
}

class DateItemViewHolderConsequent extends RecyclerView.ViewHolder{
    TextView mTextView;
    Spinner mCatSpinner;
    Button mButton;
    CardView mCardContainer;
    EditText mAdditionalQuery;

    public DateItemViewHolderConsequent(View itemView) {
        super(itemView);
        mTextView = (TextView)itemView.findViewById(R.id.prompt_text);
        mCatSpinner = (Spinner)itemView.findViewById(R.id.category_spinner);
        mButton = (Button)itemView.findViewById(R.id.search_button);
        mCardContainer = (CardView)itemView.findViewById(R.id.date_item_card_holder);
        mAdditionalQuery = (EditText)itemView.findViewById(R.id.additional_query_entry);
    }
    public void bindAdapterToSpinner(){
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(mCatSpinner.getContext(),R.array.categories,android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCatSpinner.setAdapter(categoryAdapter);
        int randomCat = new Random().nextInt(11)+1;//We have 12 choices, this picks a random one between 0 and 10 (11 choices) but adds plus 1 so that the default isn't restaurant.
        mCatSpinner.setSelection(randomCat);
    }
}





