package com.example.android.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>{

    private List<String> mListIngredient ;
    private List<String> mListMeasure ;
    private List<Double> mListQuantity ;

    public interface ClickHandler {
        void onClick(int position);
    }

    public IngredientsAdapter(List<String> mListIngredient, List<String> mListMeasure, List<Double> mListQuantity, ClickHandler mClickHandler) {
        this.mListIngredient = mListIngredient;
        this.mListMeasure = mListMeasure;
        this.mListQuantity = mListQuantity;
        ClickHandler mClickHandler1 = mClickHandler;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context mContext = parent.getContext();
        int layout = R.layout.item_view_ingredients ;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layout ,parent,false);

        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {

        holder.mIngredientTV.setText("Ingredient : " + mListIngredient.get(position));
        holder.mQuantityTV.setText("Quantity : "+mListQuantity.get(position));
        holder.mMeasureTV.setText("Measure : " + mListMeasure.get(position));
        holder.mPosition.setText(String.valueOf(++position));
    }

    @Override
    public int getItemCount() {
        return mListIngredient.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        private TextView mIngredientTV ;
        private TextView mMeasureTV ;
        private TextView mQuantityTV ;
        private TextView mPosition ;


        public IngredientsViewHolder(View itemView) {
            super(itemView);

            mIngredientTV = itemView.findViewById(R.id.tv_ingredient);
            mMeasureTV = itemView.findViewById(R.id.tv_measure);
            mQuantityTV = itemView.findViewById(R.id.tv_quantity);
            mPosition = itemView.findViewById(R.id.tv_position);

        }
    }
}
