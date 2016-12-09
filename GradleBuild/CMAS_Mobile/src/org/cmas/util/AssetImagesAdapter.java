package org.cmas.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import org.cmas.mobile.R;
import org.cmas.util.android.FileUtils;

import java.io.File;

/**
 * User: ABadretdinov
 * Date: 12.05.14
 * Time: 17:08
 */
public class AssetImagesAdapter extends BaseAdapter {
    private String[] fileNames;
    private String path;
    private LayoutInflater inflater;
    private Context context;
    private Bitmap defaultImage;
    private boolean firstNeeded=false;
    public AssetImagesAdapter(Context context,String path,Bitmap defaultImage) {
        this.context=context;
        this.defaultImage=defaultImage;
        this.inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.path=path;
        if(!StringUtil.isTrimmedEmpty(path)){
            fileNames= FileUtils.childrenFileNamesFromAssets(context,path);
        }
        if(defaultImage!=null){
            boolean isDefault=false;
            for(int i=0;!isDefault&&i<fileNames.length;i++){
                Bitmap currentDefault=getItem(i);
                if(ImageUtils.imagesAreEqual(defaultImage,currentDefault)){ //defaultImage.sameAs
                    isDefault=true;
                }
            }
            if(!isDefault){
                firstNeeded=true;
            }
        }
    }

    @Override
    public int getCount() {
        return firstNeeded?fileNames.length+1:fileNames.length;
    }

    @Override
    public Bitmap getItem(int position) {
        if(firstNeeded&&position==0){
            return defaultImage;
        }
        return FileUtils.getBitmapFromAsset(context,path+ File.separator+fileNames[firstNeeded?position-1:position]);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(vi==null){
            vi=inflater.inflate(R.layout.default_userpic_row,null,false);
        }
        ImageView imageView= (ImageView) vi.findViewById(R.id.img);
        Bitmap current=getItem(position);
        imageView.setImageBitmap(current);
        if(defaultImage!=null&&ImageUtils.imagesAreEqual(defaultImage,current)){//current.sameAs
            vi.findViewById(R.id.img_holder).setBackgroundColor(context.getResources().getColor(R.color.turquoise));
        }
        return vi;
    }
}
