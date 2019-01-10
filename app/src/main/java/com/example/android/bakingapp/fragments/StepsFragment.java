package com.example.android.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;

import java.util.ArrayList;
import java.util.List;


public class StepsFragment extends Fragment {

    private List<String> mListData = new ArrayList<>();


    private int mIndex;

    private TextView mTextViewPart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragemnt_steps_text, container, false);

        mTextViewPart = view.findViewById(R.id.item_text_view_ui);
        try {
            mTextViewPart.setText(mListData.get(mIndex));
        } catch (IndexOutOfBoundsException ignore) {

        }

        return view;

    }

    public void setListData(List<String> mListData) {
        this.mListData = mListData;
    }

    public List<String> getmListData() {
        return mListData;
    }

    public int getmIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }
}
