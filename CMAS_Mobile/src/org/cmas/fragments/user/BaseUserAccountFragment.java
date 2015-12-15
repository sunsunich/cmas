package org.cmas.fragments.user;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import org.cmas.R;
import org.cmas.fragments.BaseResultViewFragment;
import org.cmas.util.AssetImagesAdapter;

import java.io.IOException;

public class BaseUserAccountFragment extends BaseResultViewFragment {

    protected static final int PICK_PHOTO_ACTION = 113;
    protected static final int MAKE_PHOTO_ACTION = 114;

    protected TextView firstNameText;
    protected TextView lastNameText;
    protected TextView dobText;
    protected ImageView mUserUserpicImageView;

    protected Button changePassButton;
    

    public BaseUserAccountFragment() {
        super(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.account_info_icon);
        actionBar.setDisplayShowHomeEnabled(true);

        //mCheck = 1;

        View fragmentView = getView();

        firstNameText = (TextView) fragmentView.findViewById(R.id.user_account_first_name);
        lastNameText = (TextView) fragmentView.findViewById(R.id.user_account_last_name);
        dobText = (TextView) fragmentView.findViewById(R.id.user_account_dob);
        
        Button mSelectUserpicButton = (Button) fragmentView.findViewById(R.id.select_userpic_btn);
        mSelectUserpicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentActivity context = getActivity();
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.userpic_select_dialog);
                GridView defaultUserpicsGrid= (GridView) dialog.findViewById(R.id.default_userpics);
                Drawable userImage=mUserUserpicImageView.getDrawable();
                final AssetImagesAdapter adapter=new AssetImagesAdapter(context,"userpics",userImage!=null?((BitmapDrawable)userImage).getBitmap():null);
                defaultUserpicsGrid.setAdapter(adapter);
                defaultUserpicsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Bitmap current=adapter.getItem(position);
                        mUserUserpicImageView.setImageBitmap(current);
                        dialog.dismiss();
                    }
                });
                Button fromCamera = (Button) dialog.findViewById(R.id.btn1);
                fromCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, MAKE_PHOTO_ACTION);
                        dialog.dismiss();
                    }
                });
                Button fromGallery = (Button) dialog.findViewById(R.id.btn2);
                fromGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                        }
                        startActivityForResult(photoPickerIntent, PICK_PHOTO_ACTION);
                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        
        mUserUserpicImageView = (ImageView) fragmentView.findViewById(R.id.user_userpic_img);
        mUserUserpicImageView.setImageDrawable(getResources().getDrawable(R.drawable.userpic_icon));

        changePassButton = (Button) fragmentView.findViewById(R.id.change_pass_btn);

    }

    protected void collectProfileData() {


//        Drawable drawable = mUserUserpicImageView.getDrawable();
//        if (drawable == null) {
//            user.setUserpicBytes(null);
//        } else {
//            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//            if (bitmap == null) {
//                user.setUserpicBytes(null);
//            } else {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(CompressFormat.PNG, 100, stream);
//                user.setUserpicBytes(stream.toByteArray());
//            }
//        }
        /*Toast.makeText(getActivity(), "Profile data: " +
                user.getName() + ", " +
                user.getRelationship() + ", " + user.getSex(), Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MAKE_PHOTO_ACTION:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap mImageBitmap = (Bitmap) extras.get("data");
                    mUserUserpicImageView.setImageBitmap(mImageBitmap);
                    //mUserpicDelete.setVisibility(View.VISIBLE);
                    //mDefaultUserpic.setVisibility(View.GONE);
                }
                break;
            case PICK_PHOTO_ACTION:
                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    Uri selectedImageUri = data.getData();
                    String[] imageInfo = {MediaStore.MediaColumns.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, imageInfo, null, null, null);
                    String filePath = null;
                    if (cursor != null && cursor.moveToFirst()) {
                        filePath = cursor.getString(0);
                    }

                    Matrix matrix = new Matrix();
                    float rotation = rotationForImage(getActivity(), selectedImageUri);
                    if (rotation != 0.0f) {
                        matrix.preRotate(rotation);
                    }

                    Bitmap rawBitmap = decodeSampledBitmapFromResource(filePath, 100, 100);/*BitmapFactory.decodeFile(filePath);*/
                    Bitmap resizedBitmap = Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.getWidth(), rawBitmap.getHeight(), matrix, true);
                    //resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, (int)(resizedBitmap.getWidth() * 0.10),
                    //      (int)(resizedBitmap.getHeight() * 0.10), true);
                    mUserUserpicImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mUserUserpicImageView.setImageBitmap(resizedBitmap);
                    //mUserpicDelete.setVisibility(View.VISIBLE);
                   // mDefaultUserpic.setVisibility(View.GONE);
                    //if (!rawBitmap.isRecycled()) rawBitmap.recycle();
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            int halfHeight = height / 2;
            int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight
                    && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static float rotationForImage(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor cursor = context.getContentResolver().query(
                    uri, projection, null, null, null);
            if (cursor.moveToFirst()) {
                return (float) cursor.getInt(0);
            }
        } else if (uri.getScheme().equals("file")) {
            try {
                ExifInterface exif = new ExifInterface(uri.getPath());
                return (int) exifOrientationToDegrees(
                        exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL));
            } catch (IOException e) {
                Log.e("HB", "Error checking exif", e);
            }
        }
        return 0f;
    }

    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        }
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        }
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    public static Bitmap decodeSampledBitmapFromResource(String filePath, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    private void setUserpic(Bitmap bitmap) {
        mUserUserpicImageView.setImageBitmap(bitmap);
    }
}
