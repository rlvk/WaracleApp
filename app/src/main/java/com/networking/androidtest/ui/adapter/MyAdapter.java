package com.networking.androidtest.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.networking.androidtest.R;
import com.networking.androidtest.datamodel.Cake;
import com.networking.androidtest.datasource.ImageController;
import com.networking.androidtest.utils.DeviceUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by rafalwesolowski on 15/07/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CakeViewHolder> {

    private final WeakReference<Context> mContext;
    private final List<Cake> mCakes;
    private final int mImageRequiredWidth;

    /**
     * Constructor
     *
     * @param context Context
     * @param cakes {@link List<Cake> }
     */
    public MyAdapter(Context context, List<Cake> cakes) {
        // Assume that thumbnail will not be bigger size than half of the display width.
        mImageRequiredWidth = DeviceUtils.getDisplayWidth(context) / 3;

        mContext = new WeakReference<>(context);
        mCakes = cakes;
    }

    @Override
    public CakeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_item_layout, viewGroup, false);
        return new CakeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CakeViewHolder holder, int position) {
            final Cake cake = mCakes.get(position);

            holder.title.setText(cake.getName());
            holder.desc.setText(cake.getDescription());

            String imageUrl = cake.getImageUrl();
            if (mContext != null && mContext.get() != null) {
                // Assume that thumbnail will not be bigger size than half of the display width.
                ImageController imageController = new ImageController(mContext.get(), imageUrl);
                imageController.loadBitmap(holder.imageView, mImageRequiredWidth, mImageRequiredWidth);
            }
    }

    @Override
    public int getItemCount() {
        return mCakes != null ? mCakes.size() : 0;
    }

    /**
     * Game ViewHolder.
     */
    protected class CakeViewHolder extends RecyclerView.ViewHolder {

        protected TextView title;
        protected TextView desc;
        protected ImageView imageView;

        /**
         * Constructor with the View.
         *
         * @param view view object
         */
        public CakeViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            desc = (TextView) view.findViewById(R.id.desc);
            imageView = (ImageView) view.findViewById(R.id.image);
        }
    }
}
