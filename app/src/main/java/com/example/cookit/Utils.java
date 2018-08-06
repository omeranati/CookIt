package com.example.cookit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class Utils {

    public static byte[] getDataFromImageView(ImageView imageView) {
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(bitmap.getWidth()*0.4),(int)(bitmap.getHeight()*0.4),false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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
