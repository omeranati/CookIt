package com.example.cookit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.example.cookit.Model.GetImageListener;
import com.example.cookit.Model.Model;
import com.example.cookit.Model.RecipeAsyncDaoListener;

import java.io.ByteArrayOutputStream;

public class Utils {

    static public void displayPicture(final ImageView recipeImageView, final Bitmap imageBitmap, final double scale) {

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
            }
        }

        DisplayPictureAsyncTask task = new DisplayPictureAsyncTask();
        task.execute();
    }
    static public void putPicture(final String imageName, final Context context, final RecipeAsyncDaoListener<Bitmap> listener) {
        class PutPictureAsyncTask extends AsyncTask<String, String, Recipe> {
            @Override
            protected Recipe doInBackground(String... strings) {
                Model.getInstance().getImage(imageName, new GetImageListener() {
                    @Override
                    public void onDone(Bitmap imageBitmap) {
                        if (imageBitmap != null) {

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
}
