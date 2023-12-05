package com.vskub.certificate.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ScalingUtilities {

	    
	    public static  Bitmap decodeFile(String path, int dstWidth, int dstHeight,
	            ScalingLogic scalingLogic) {
	        Options options = new Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(path, options);
	        options.inJustDecodeBounds = false;
	        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
	                dstHeight, scalingLogic);
	        Bitmap unscaledBitmap = BitmapFactory.decodeFile(path, options);
	        return unscaledBitmap;
	    }

	    
	    public static  Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
	            ScalingLogic scalingLogic) {
	        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
	                dstWidth, dstHeight, scalingLogic);
	        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
	                dstWidth, dstHeight, scalingLogic);
	        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
	                Config.ARGB_8888);
	        Canvas canvas = new Canvas(scaledBitmap);
	        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

	        return scaledBitmap;
	    }

	    public  static enum ScalingLogic {
	        CROP, FIT
	    }
	    public static  int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
	            ScalingLogic scalingLogic) {
	        if (scalingLogic == ScalingLogic.FIT) {
	            final float srcAspect = (float)srcWidth / (float)srcHeight;
	            final float dstAspect = (float)dstWidth / (float)dstHeight;

	            if (srcAspect > dstAspect) {
	                return srcWidth / dstWidth;
	            } else {
	                return srcHeight / dstHeight;
	            }
	        } else {
	            final float srcAspect = (float)srcWidth / (float)srcHeight;
	            final float dstAspect = (float)dstWidth / (float)dstHeight;

	            if (srcAspect > dstAspect) {
	                return srcHeight / dstHeight;
	            } else {
	                return srcWidth / dstWidth;
	            }
	        }
	    }
	    public static  Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
	            ScalingLogic scalingLogic) {
	        if (scalingLogic == ScalingLogic.CROP) {
	            final float srcAspect = (float)srcWidth / (float)srcHeight;
	            final float dstAspect = (float)dstWidth / (float)dstHeight;

	            if (srcAspect > dstAspect) {
	                final int srcRectWidth = (int)(srcHeight * dstAspect);
	                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
	                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
	            } else {
	                final int srcRectHeight = (int)(srcWidth / dstAspect);
	                final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;
	                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
	            }
	        } else {
	            return new Rect(0, 0, srcWidth, srcHeight);
	        }
	    }

	   	
				
	 public static  Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
	            ScalingLogic scalingLogic) {
	        if (scalingLogic == ScalingLogic.FIT) {
	            final float srcAspect = (float)srcWidth / (float)srcHeight;
	            final float dstAspect = (float)dstWidth / (float)dstHeight;

	            if (srcAspect > dstAspect) {
	                return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
	            } else {
	                return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
	            }
	        } else {
	            return new Rect(0, 0, dstWidth, dstHeight);
	        }
	    }
	 
	 public static Bitmap getImage(String ur, int mode, int dstW, int dstH){
			try {
				if(mode == 2){
				URL url = new URL(ur);	
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				Options options = new Options();
				options.inJustDecodeBounds = true;
				InputStream input = connection.getInputStream();
				BitmapFactory.decodeStream(input,null,options);
				options.inJustDecodeBounds = false;
				options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstW,
			                dstH, ScalingLogic.FIT);
				Bitmap bitmap = BitmapFactory.decodeStream(input,null,options);
				if (!(bitmap.getWidth() <= dstW && bitmap
						.getHeight() <= dstH)) {
					Bitmap myBitmap = ScalingUtilities.createScaledBitmap(
							bitmap, dstW, dstH,
							ScalingLogic.FIT);
					return myBitmap;
				} else {
					Bitmap myBitmap = bitmap;
					return myBitmap;
				}
				}else{
					Options options = new Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(ur, options);
					options.inJustDecodeBounds = false;
					options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstW,
			                dstH, ScalingLogic.FIT);
					Bitmap bitmap = BitmapFactory.decodeFile(ur, options);
					if (!(bitmap.getWidth() <= dstW && bitmap.getHeight() <= dstH)) {
						Bitmap myBitmap = ScalingUtilities.createScaledBitmap(
								bitmap, dstW, dstH,
								ScalingLogic.FIT);
						return myBitmap;
					} else {
						Bitmap myBitmap = bitmap;
						return myBitmap;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	 
	 public static Bitmap getCropedBitmap(Bitmap bm, int dstW, int dstH){
		try{
			if (!(bm.getWidth() <= dstW && bm.getHeight() <= dstH)) {
				Bitmap myBitmap = ScalingUtilities.createScaledBitmap(bm, dstW, dstH, ScalingLogic.FIT);
				return myBitmap;
			} else {
				Bitmap myBitmap = bm;
				return myBitmap;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	 }
	 
	 
	 public static Bitmap getBlurredBitmap(Bitmap original, int radius)
	  {
	    if (radius < 1)
	      return null;

	    int width = original.getWidth();
	    int height = original.getHeight();
	    int wm = width - 1;
	    int hm = height - 1;
	    int wh = width * height;
	    int div = radius + radius + 1;
	    int r[] = new int[wh];
	    int g[] = new int[wh];
	    int b[] = new int[wh];
	    int rsum, gsum, bsum, x, y, i, p, p1, p2, yp, yi, yw;
	    int vmin[] = new int[Math.max(width, height)];
	    int vmax[] = new int[Math.max(width,height)];
	    int dv[] = new int[256 * div];
	    for (i=0; i<256*div; i++)
	      dv[i] = i / div;

	    int[] blurredBitmap = new int[wh];
	    original.getPixels(blurredBitmap, 0, width, 0, 0, width, height);

	    yw = 0;
	    yi = 0;

	    for (y=0; y<height; y++)
	    {
	      rsum = 0;
	      gsum = 0;
	      bsum = 0;
	      for(i=-radius; i<=radius; i++)
	      {
	        p = blurredBitmap[yi + Math.min(wm, Math.max(i,0))];
	        rsum += (p & 0xff0000) >> 16;
	        gsum += (p & 0x00ff00) >> 8;
	        bsum += p & 0x0000ff;
	      }
	      for (x=0; x<width; x++)
	      {
	        r[yi] = dv[rsum];
	        g[yi] = dv[gsum];
	        b[yi] = dv[bsum];

	        if(y==0)
	        {
	          vmin[x] = Math.min(x + radius+1,wm);
	          vmax[x] = Math.max(x - radius,0);
	        } 
	        p1 = blurredBitmap[yw + vmin[x]];
	        p2 = blurredBitmap[yw + vmax[x]];

	        rsum += ((p1 & 0xff0000)-(p2 & 0xff0000))>>16;
	        gsum += ((p1 & 0x00ff00)-(p2 & 0x00ff00))>>8;
	        bsum += (p1 & 0x0000ff)-(p2 & 0x0000ff);
	        yi ++;
	      }
	      yw += width;
	    }

	    for (x=0; x<width; x++)
	    {
	      rsum=gsum=bsum=0;
	      yp =- radius * width;
	      for(i=-radius; i<=radius; i++)
	      {
	        yi = Math.max(0,yp) + x;
	        rsum += r[yi];
	        gsum += g[yi];
	        bsum += b[yi];
	        yp += width;
	      }
	      yi = x;
	      for (y=0; y<height; y++)
	      {
	        blurredBitmap[yi] = 0xff000000 | (dv[rsum]<<16) | (dv[gsum]<<8) | dv[bsum];
	        if(x == 0)
	        {
	          vmin[y] = Math.min(y + radius + 1, hm) * width;
	          vmax[y] = Math.max(y - radius, 0) * width;
	        } 
	        p1 = x + vmin[y];
	        p2 = x + vmax[y];

	        rsum += r[p1] - r[p2];
	        gsum += g[p1] - g[p2];
	        bsum += b[p1] - b[p2];

	        yi += width;
	      }
	    }

	    return Bitmap.createBitmap(blurredBitmap, width, height, Config.RGB_565);
	  }

	}   
