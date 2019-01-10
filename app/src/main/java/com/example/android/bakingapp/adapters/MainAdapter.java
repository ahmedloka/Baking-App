package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.bakingapp.StepsActivity.TAG;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private List<Integer> mListImgRes ;
    private List<String> mListNames ;

    private final ClickHandler mClickHandler ;

    public interface ClickHandler{
        void onClick (int position);
    }

    public MainAdapter (List<Integer> imgRes,List<String> nameList , ClickHandler clickHandler){
        this.mListImgRes = imgRes ;
        this.mListNames = nameList ;
        this.mClickHandler = clickHandler ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        int layout = R.layout.item_view_main ;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layout,parent,false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


         //int width = Resources.getSystem().getDisplayMetrics().widthPixels;
         //int height = Resources.getSystem().getDisplayMetrics().heightPixels ;


        try {
            Picasso.get()
                    .load(mListImgRes.get(position))
                    .resize(600, 200)
                    .placeholder(R.drawable.loading_place_holder)
                    .error(R.drawable.error_place_holder)
                    .centerInside()
                    .into(holder.mImageView);
        }catch (IndexOutOfBoundsException e){
            Log.e(TAG, "onBindViewHolder: ",e );
        }

        holder.mTextView.setText(mListNames.get(position));
    }

    @Override
    public int getItemCount() {
        return mListNames.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView mImageView ;
        final TextView mTextView ;

        public ViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.iv_main);
            mTextView = itemView.findViewById(R.id.tv_main);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mClickHandler.onClick(position);
        }
    }
}
