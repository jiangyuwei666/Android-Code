package com.example.fragmentbestpractice;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/28.
 */

public class NewsTitleFragment extends Fragment {

    private boolean isTwoPane ;

    @Override
    public View onCreateView (LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState ) {

        View view = inflater.inflate( R.layout.news_title_frag , container , false ) ;
        return view ;
    }

    @Override
    public void onActivityCreated ( Bundle savedInstanceState ) {
        super.onActivityCreated( savedInstanceState ) ;
        if ( getActivity().findViewById( R.id.news_content_layout ))

    }
}
