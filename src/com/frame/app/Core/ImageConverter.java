package com.frame.app.Core;

import java.io.File;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ImageConverter {
	
	private static int width = 800;
	private static int height = 1200;
	
	public static Bitmap textToImage(String str)
	{
		Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		Bitmap bmp = Bitmap.createBitmap(width, height, conf);
		Canvas can = new Canvas(bmp);
		can.drawColor(255);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE); 
		paint.setTextSize(30);
        if(str.length() > 20){
            String str1;
            str1 = str.substring(0,20);
            can.drawText(str1, 50, 300, paint);
            String str2;
            str2 = str.substring(21,str.length());
            can.drawText(str2, 50, 330, paint);
        }
        else{
            can.drawText(str, 50, 300, paint);

        }
		return bmp;
	}
}
