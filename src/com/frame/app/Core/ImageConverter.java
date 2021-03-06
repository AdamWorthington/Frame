package com.frame.app.Core;

import java.io.File;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class ImageConverter {
	
	private static int width = 300;
	private static int height = 300;
	private static int line = 17;
	private static int textSize = 10;
	private static int increment = 50;
	
	public static Bitmap textToImage(String str)
	{
		Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		Bitmap bmp = Bitmap.createBitmap(width, height, conf);
		Canvas can = new Canvas(bmp);
		can.drawColor(255);
		Paint paint = new Paint();
		paint.setColor(Color.WHITE); 
		paint.setTextSize(30);
		String str1;
		String str2;
		String strt;
		int printingPos = increment;
        if(str.length()>line) {
            str1 = str.substring(0, line);
            can.drawText(str1, 10, printingPos, paint);
            strt = str.substring(line, str.length());

            while (strt.length() > line) {
                printingPos += increment;
                str1 = strt.substring(0, line);
                can.drawText(str1, 10, printingPos, paint);

                //printingPos += increment;
                str2 = strt.substring(line, strt.length());

                if (str2.length() > line) {
                    strt = str2;
                } else {
                    printingPos += increment;

                    can.drawText(str2, 10, printingPos, paint);
                    break;
                }

            }
            if(strt.length() <= line){
                printingPos += increment;

                can.drawText(strt, 10, printingPos, paint);
            }
        }
        else{
            can.drawText(str, 10, 150, paint);
        }
		return bmp;
	}
}
