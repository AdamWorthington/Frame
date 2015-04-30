package com.frame.app.View;

import com.frame.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

public class PostPage extends Fragment
{
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.fragment_post, container, false);
		
		return root;
	}

    /** Text button called */
    public void changePage(View view){
        Intent intent;
        intent = new Intent(getActivity(),Text_post.class);
        getActivity().startActivity(intent);

    }
    
	@Override
	public void onPrepareOptionsMenu(Menu menu) 
	{
	    menu.findItem(R.id.action_search).setVisible(false);
	    menu.findItem(R.id.action_top).setVisible(false);
	    menu.findItem(R.id.action_new).setVisible(false);
	    super.onPrepareOptionsMenu(menu);
	}
    
    public void takeMediaContent(View view){
        Intent intent;
        intent = new Intent(getActivity(), MediaContentPost.class);
        getActivity().startActivity(intent);

    }
}
