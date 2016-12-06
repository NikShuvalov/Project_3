package shuvalov.nikita.restaurantroulette.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import shuvalov.nikita.restaurantroulette.R;
import shuvalov.nikita.restaurantroulette.YelpResources.YelpObjects.Business;

/**
 * Created by NikitaShuvalov on 12/5/16.
 */

public class DateNightRecyclerAdapter extends RecyclerView.Adapter {
    List<Business> mBusinessList;

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
        }
        return null;//If all is well, this shouldn't happen. If there are issues just make a new viewholder to put on the bottom.
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = holder.getItemViewType();
        if (itemViewType==0){
            //Do Search with location that's based in the location field and category picked.
        }else{
            //Do Search based on location of previous area and category picked.
        }
    }

    @Override
    public int getItemCount() {
        return mBusinessList.size()+1;//It's plus one because there's always one more card than their are datecards.
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return 0;
        }else{
            return 1;
        }
    }
}
class DateItemViewHolderFirst extends RecyclerView.ViewHolder{
    TextView mTextView;
    Spinner mCatSpinner;
    EditText mQueryInput;
    Button mButton;
    CardView mCardContainer;
    ImageView mClose;

    public DateItemViewHolderFirst(View itemView) {
        super(itemView);

        mTextView = (TextView)itemView.findViewById(R.id.prompt_text);
        mCatSpinner = (Spinner)itemView.findViewById(R.id.category_spinner);
        mQueryInput = (EditText)itemView.findViewById(R.id.query_entry);
        mButton = (Button)itemView.findViewById(R.id.search_button);
        mCardContainer = (CardView)itemView.findViewById(R.id.date_item_card_holder);
        mClose = (ImageView)itemView.findViewById(R.id.close);

    }
}

class DateItemViewHolderConsequent extends RecyclerView.ViewHolder{
    TextView mTextView;
    Spinner mCatSpinner;
    Button mButton;
    CardView mCardContainer;
    ImageView mClose;

    public DateItemViewHolderConsequent(View itemView) {
        super(itemView);
        mTextView = (TextView)itemView.findViewById(R.id.prompt_text);
        mCatSpinner = (Spinner)itemView.findViewById(R.id.category_spinner);
        mButton = (Button)itemView.findViewById(R.id.search_button);
        mCardContainer = (CardView)itemView.findViewById(R.id.date_item_card_holder);
        mClose = (ImageView)itemView.findViewById(R.id.close);
    }
}

