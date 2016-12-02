package shuvalov.nikita.restaurantroulette;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by NikitaShuvalov on 12/2/16.
 */

public class PicassoImageManager {
    private Context mContext;
    private ImageView mImageView;
    private RequestCreator mRequestCreator;


    public PicassoImageManager(Context context, ImageView imageView){
        mContext = context;
        mImageView = imageView;
    }

    public void setImageFromUrl(final String imageUrl){

        AsyncTask asyncTask = new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                mRequestCreator = Picasso.with(mContext).load(imageUrl);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mRequestCreator.into(mImageView);
            }
        }.execute();


    }

}
