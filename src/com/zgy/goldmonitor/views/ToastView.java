package com.zgy.goldmonitor.views;

import android.content.Context;
import android.widget.Toast;

/**
 * 
 * @Author lixiangwei
 * @Date:2014-4-11
 * @Copyright (c) 2014, 方正电子 All Rights Reserved.
 * @version 
 * @since
 */
public class ToastView {  
	  
    private static String oldMsg;  
    protected static Toast toast   = null;  
    private static long oneTime=0;  
    private static long twoTime=0;  
    
    public static int LENGTH_SHORT = ToastView.LENGTH_SHORT;
    public static int LENGTH_LONG = ToastView.LENGTH_LONG;
      
    public static void showToast(Context context, String s,int length){      
        if(toast==null){   
            toast =Toast.makeText(context, s, length);  
            toast.show();  
            oneTime=System.currentTimeMillis();  
        }else{  
            twoTime=System.currentTimeMillis();  
            if(s.equals(oldMsg)){  
                if(twoTime-oneTime>length){  
                    toast.show();  
                }  
            }else{  
                oldMsg = s;  
                toast.setText(s);  
                toast.show();  
            }         
        }  
        oneTime=twoTime;  
    }  
      
      
    public static void showToast(Context context, int resId,int length){     
        showToast(context, context.getString(resId),length);  
    }  
  
}  
