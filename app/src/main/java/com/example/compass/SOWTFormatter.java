package com.example.compass;

import android.content.Context;

public class SOWTFormatter {
    private static final int[] sides={0, 45, 90, 135, 180, 225, 270, 315, 360};
    private static String[] names=null;
    public  SOWTFormatter(Context context)
    {
        initLocalizedNames(context);
    }
    public String format(float azimuth){
        int iAzimuth=(int)azimuth;
        int index=findClosestIndex(iAzimuth);
        return  iAzimuth +"° "+ names[index];
    }

    private void initLocalizedNames(Context context) {
        if(names==null){
            names=new  String[]{context.getString(R.string.sotw_north),
                    context.getString(R.string.sotw_northeast),
                    context.getString(R.string.sotw_east),
                    context.getString(R.string.sotw_southeast),
                    context.getString(R.string.sotw_south),
                    context.getString(R.string.sotw_southwest),
                    context.getString(R.string.sotw_west),
                    context.getString(R.string.sotw_northwest),
                    context.getString(R.string.sotw_north)

            };
        }
    }
    private static int findClosestIndex(int target) {

        int i=0,j=sides.length,mid=0;
        while (i<j){
            mid=(i+j)/2;
            if(target<sides[mid]){
                if(mid>0 && target>sides[mid-1]){
                    return getClosest(mid-1,mid,target);
                }
                j=mid;
            }
            else {
                if(mid<sides.length-1 && target<sides[mid+1]){
                    return getClosest(mid,mid+1,target);
                }
                i=mid+1;
            }
        }
        return mid;

    }

    private static int getClosest(int mid, int i, int target) {
        if(target-sides[mid]>=sides[i]-target){
            return i;
        }
        return mid;
    }

}
