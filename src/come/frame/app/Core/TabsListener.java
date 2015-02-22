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

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        ft.add(R.id.fragment_container, fragment, null);
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // some people needed this line as well to make it work: 
        ft.remove(fragment);
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