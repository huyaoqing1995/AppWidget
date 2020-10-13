package com.osapp.appwidget.wg.wgContact;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import androidx.core.content.ContextCompat;

import com.osapp.appwidget.R;

import java.io.InputStream;

public class ContactWidgetUtils {

    public static String getContactId(Context context,Uri uri){
        String contactID = null;
        Cursor cursorID = context.getContentResolver().query(uri,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID!=null&&cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
            cursorID.close();
        }else return contactID;

        return contactID;
    }


    public static String getContactNumber(Context context,String contactID){
        String contactNumber = "";
        Cursor cursorPhone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
                new String[]{contactID},
                null);

        if (cursorPhone!=null&&cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursorPhone.close();
        }
        return contactNumber+"";
    }

    public static String getContactName(Context context,Uri uri){
        String contactName = "";
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if(cursor!=null&&cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            cursor.close();
        }
        return contactName+"";
    }

    public static Bitmap getContactBitmap(Context context,String id) {
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,Long.parseLong(id)));

            if (inputStream != null) {
                Bitmap photo = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream), 256, 256, false);;
                Bitmap crop = getCircular(photo);
                inputStream.close();
                photo.recycle();
                return crop;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getBitmap(context);
    }

    public static Bitmap getCircular(Bitmap bm) {
        int w = bm.getWidth();
        int h = bm.getHeight();
        int radius = Math.min(w, h);
        w = radius;
        h = radius;
        Bitmap bmOut = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOut);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(0xff424242);
        Rect rect = new Rect(0, 0, w, h);
        RectF rectF = new RectF(rect);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(rectF.left + (rectF.width()/2), rectF.top + (rectF.height()/2), radius / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bm, rect, rect, paint);
        return bmOut;
    }

    private static Bitmap getBitmap(Context context) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_user);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

}
