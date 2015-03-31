package JSON;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ImageConverter {
	
	private final int WIDTH = 400;
	private final int HEIGHT = 600;
	
	public Bitmap textToImage(String str)
	{
		Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		Bitmap bmp = Bitmap.createBitmap(WIDTH, HEIGHT, conf);
		Canvas can = new Canvas(bmp);
		can.drawColor(255);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE); 
		paint.setTextSize(10); 
		can.drawText(str, 50, 50, paint);
		
		return bmp;
	}
}
