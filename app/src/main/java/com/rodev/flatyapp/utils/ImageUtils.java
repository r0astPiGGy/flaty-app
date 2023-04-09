package com.rodev.flatyapp.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class ImageUtils {

    public static byte[] downsizeBitmap(Bitmap fullBitmap) {
        int width = fullBitmap.getWidth();
        int height = fullBitmap.getHeight();

        boolean needsDownsize = bitmapNeedsDownsize(fullBitmap);

        if(!needsDownsize) {
            return convertBitmapToByteArray(fullBitmap);
        }

        Bitmap bitmap = fullBitmap;

        while(bitmapNeedsDownsize(bitmap)) {
            bitmap = convertByteArrayToBitmap(getDownsizedImageBytes(bitmap, width /= 2, height /= 2));
        }

        return convertBitmapToByteArray(bitmap);
    }

    public static boolean bitmapNeedsDownsize(Bitmap bitmap) {
        return bitmap.getByteCount() > 800 * 1024;
    }

    public static byte[] getDownsizedImageBytes(Bitmap fullBitmap, int scaleWidth, int scaleHeight) {

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(fullBitmap, scaleWidth, scaleHeight, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        return outputStream.toByteArray();
    }

    public static byte[] getByteArrayFromUri(ContentResolver resolver, Uri uri) {
        return convertBitmapToByteArray(Objects.requireNonNull(getBitmapFromUri(resolver, uri)));
    }

    public static byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        final int width = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().width() : drawable.getIntrinsicWidth();

        final int height = !drawable.getBounds().isEmpty() ? drawable
                .getBounds().height() : drawable.getIntrinsicHeight();

        final Bitmap bitmap = Bitmap.createBitmap(width <= 0 ? 1 : width,
                height <= 0 ? 1 : height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static byte[] resizeAndConvertDrawable(Drawable drawable) {
        return downsizeBitmap(getBitmapFromDrawable(drawable));
    }

    public static byte[] convertDrawableToByteArray(Drawable drawable){
        return convertBitmapToByteArray(getBitmapFromDrawable(drawable));
    }

    public static Bitmap convertByteArrayToBitmap(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static Bitmap getBitmapFromUri(ContentResolver contentResolver, Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(contentResolver, uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
