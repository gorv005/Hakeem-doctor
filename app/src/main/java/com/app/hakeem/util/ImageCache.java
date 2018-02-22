package com.app.hakeem.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.util.Hashtable;


public class ImageCache
{
	private static Hashtable<Integer, Bitmap> htInt = new Hashtable<Integer, Bitmap>();
	private static Hashtable<String, Bitmap> htStr = new Hashtable<String, Bitmap>();

	private static Hashtable<Integer, Bitmap> ht = new Hashtable<Integer, Bitmap>();
	private static Hashtable<String, Bitmap> htScaled = new Hashtable<String, Bitmap>();
	private static Hashtable<Integer, Drawable> htd = new Hashtable<Integer, Drawable>();
	static Matrix matrix;
	
	public static Bitmap getBitmap(int id, Context c)
	{
		Bitmap b = htInt.get(id);
		if (b == null)
		{
			b=((BitmapDrawable) c.getResources().getDrawable(id)).getBitmap();
//			htInt.put(id, b);
		}
		return b;
	}


	
	public static Bitmap getBmp(int id, Resources resources)
	{
		Bitmap b = ht.get(id);
		if (b == null)
		{
			b = ((BitmapDrawable) resources.getDrawable(id)).getBitmap();
			ht.put(id, b);
		}
		return b;
	}

	public static Bitmap getBitmapforLevel(int id, int level, Bitmap srcBitmap, int boxWidth, int boxHeight)
	{
		String s_id = "" + id + "" + level;
		Bitmap b = htScaled.get(s_id);
		if (b == null)
		{
			b = Bitmap.createScaledBitmap(srcBitmap, boxWidth, boxHeight, true);
//			htScaled.put(s_id, b);
		}
		return b;
	}

	public static Bitmap getScaledBmp(int id, Bitmap srcBitmap, int boxWidth, int boxHeight)
	{
		String s_id = "" + id + "_" + boxWidth + "_" + boxHeight;
		Bitmap b = htScaled.get(s_id);
		if (b == null)
		{
			b = Bitmap.createScaledBitmap(srcBitmap, boxWidth, boxHeight, true);
//			htScaled.put(s_id, b);
		}
		return b;
	}


	
	public static BitmapDrawable getdrawable(int id, Resources resources)
	{
		BitmapDrawable d = (BitmapDrawable) htd.get(id);
		if (d == null)
		{
			d = (BitmapDrawable) resources.getDrawable(id);
//			htd.put(id, d);
		}
		return d;
	}
}
