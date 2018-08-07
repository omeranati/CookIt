package com.example.cookit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.cookit.Model.GetImageListener;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.GenericListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Utils {

    static public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            FeedActivity.mMemoryCache.put(key, bitmap);
        }
    }

    static public Bitmap getBitmapFromMemCache(String key) {
        return FeedActivity.mMemoryCache.get(key);
    }

    static public void displayPicture(final ImageView recipeImageView, final Bitmap imageBitmap, final double scale, final ProgressBar pb) {

        class DisplayPictureAsyncTask extends AsyncTask<String, String, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap newImageBitmap = imageBitmap;
                if (scale < 1) {
                    newImageBitmap = Bitmap.createScaledBitmap(imageBitmap, (int) (imageBitmap.getWidth() * scale), (int) (imageBitmap.getHeight() * scale), false);
                }
                return newImageBitmap;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                recipeImageView.setImageBitmap(result);

                if (pb!=null) {
                    pb.setVisibility(View.INVISIBLE);
                }
            }
        }

        DisplayPictureAsyncTask task = new DisplayPictureAsyncTask();
        task.execute();
    }

    static public void putPicture(final String imageName, final Context context, final ProgressBar pb, final GenericListener<Bitmap> listener) {
        Bitmap bitmap = getBitmapFromMemCache(imageName);

        if (bitmap == null) {
            if (pb != null){
                pb.setVisibility(View.VISIBLE);
            }
            class PutPictureAsyncTask extends AsyncTask<String, String, Recipe> {
                @Override
                protected Recipe doInBackground(String... strings) {
                    Model.getInstance().getImage(imageName, new GetImageListener() {
                        @Override
                        public void onDone(Bitmap imageBitmap) {
                            if (imageBitmap != null) {
                                addBitmapToMemoryCache(imageName,imageBitmap);
                                listener.onComplete(imageBitmap);
                            }
                        }
                    }, context);

                    return null;
                }
            }

            PutPictureAsyncTask task = new PutPictureAsyncTask();
            task.execute();
        }
        else {
            listener.onComplete(bitmap);
        }
    }

    static public void cropCenterAndCreateCircle(final Bitmap bitmap, final GenericListener<Bitmap> listener) {

        class CropCenterAndCreateCircleAsyncTask extends AsyncTask<String, String, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap newBitmap = bitmap;

                if (newBitmap != null) {
                    if (newBitmap.getWidth() >= newBitmap.getHeight()) {

                        newBitmap = Bitmap.createBitmap(
                                newBitmap,
                                newBitmap.getWidth() / 2 - newBitmap.getHeight() / 2,
                                0,
                                newBitmap.getHeight(),
                                newBitmap.getHeight()
                        );

                    } else {

                        newBitmap = Bitmap.createBitmap(
                                newBitmap,
                                0,
                                newBitmap.getHeight() / 2 - newBitmap.getWidth() / 2,
                                newBitmap.getWidth(),
                                newBitmap.getWidth()
                        );
                    }

                    newBitmap = ImageHelper.getRoundedCornerBitmap(newBitmap, newBitmap.getWidth() / 2);
                    listener.onComplete(newBitmap);
                }

                return newBitmap;
            }
        }

        CropCenterAndCreateCircleAsyncTask task = new CropCenterAndCreateCircleAsyncTask();
        task.execute();
    }

    public static byte[] getDataFromImageView(ImageView imageView) {
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.2),(int)(bitmap.getHeight()*0.2),false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        return (baos.toByteArray());
    }

    public static void showErrorAlert(int messageId, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(messageId).setPositiveButton("Ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void showDynamicErrorAlert(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setPositiveButton("Ok", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static Bitmap rotateImageIfNeeded(Bitmap bitmap, String photoPath) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }

        return rotatedBitmap;
    }

    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
