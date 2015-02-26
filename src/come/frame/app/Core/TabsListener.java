package come.frame.app.Core;

import com.frame.app.R;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;

@SuppressWarnings("deprecation")
public class TabsListener implements ActionBar.TabListener {

    private Fragment fragment;

    public TabsListener(Fragment fragment) {
        this.fragment = fragment;
    }

	@Override
	public void onTabReselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab arg0,
			android.support.v4.app.FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
}