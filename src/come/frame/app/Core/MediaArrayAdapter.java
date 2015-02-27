package come.frame.app.Core;

import java.util.ArrayList;
import java.util.HashMap;

import com.frame.app.Model.MediaContent;

import android.content.Context;
import android.widget.ArrayAdapter;

public class MediaArrayAdapter extends ArrayAdapter<MediaContent>
{
	HashMap<MediaContent, Integer> mIdMap = new HashMap<MediaContent, Integer>();
	
	public MediaArrayAdapter(Context context, int resource, ArrayList<MediaContent> objects) 
	{
		super(context, resource, objects);
		
		for(int i = 0; i < objects.size(); i++)
		{
			mIdMap.put(objects.get(i), i);
		}
	}
	
	@Override
    public long getItemId(int position) 
	{
		MediaContent item = getItem(position);
		return mIdMap.get(item);
    }
	
	@Override
    public boolean hasStableIds() 
	{
      return true;
    }

}
