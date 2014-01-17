package com.example.myapplication8;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication8.dummy.ContactModel;

import java.util.List;

/**
 * Created by SBaar on 12/30/13.
 *///TODO all text broken sticky image
public class ContactAdapter extends ArrayAdapter<ContactModel>  {

    private final Context context;
    private final List<ContactModel> values;
    private final Bitmap icon;
    public ContactAdapter(Context context, List<ContactModel> values) {
        super(context, R.layout.contact_row, values);
        this.context = context;
        this.values = values;
         icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);

    }
    static class ContactHolder{
        protected TextView textView;
        protected ImageView imageView;
        protected Uri uri = null;
        protected boolean seen = false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = null;

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.contact_row, parent, false);
            final ContactHolder contactHolder = new ContactHolder();
            contactHolder.textView = (TextView) view.findViewById(R.id.contactNameView);
            contactHolder.imageView = (ImageView) view.findViewById(R.id.imageView);

            view.setTag(contactHolder);
            contactHolder.imageView.setTag(values.get(position));

           /* if (values.get(position).isAll == false){
                Uri uri = getPhotoUri(view.getContext(), Long.decode(values.get(position).id));
                contactHolder.uri = uri;
                if(uri != null) contactHolder.imageView.setImageURI(uri);
            }*/
        }
        else
        {
            view = convertView;
            ((ContactHolder) view.getTag()).imageView.setTag(values.get(position));
        }
        ContactHolder contactHolder = (ContactHolder) view.getTag();
        contactHolder.textView.setText(values.get(position).toString());
        if (values.get(position).isAll == false) contactHolder.uri = getPhotoUri(view.getContext(), Long.decode(values.get(position).id));
        else contactHolder.imageView.setImageBitmap(icon);
        Bitmap bitmap = null;
        {

            try{
                bitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), contactHolder.uri);
            }
            catch (Exception e) {}
            if(bitmap != null)
                contactHolder.imageView.setImageBitmap(bitmap);
            else{
                contactHolder.imageView.setImageBitmap(icon);
            }
        }
       /* if(contactHolder.uri != null) {
            Log.d("imageuri", contactHolder.uri.toString());
            contactHolder.imageView.setImageURI(contactHolder.uri);
        }
        else{
            int resID = view.getContext().getResources().getIdentifier("@drawable/ic_launcher", null,  view.getContext().getPackageName());
            contactHolder.imageView.setImageDrawable(view.getContext().getResources().getDrawable(resID));
            contactHolder.imageView.invalidate();
        }
        contactHolder.seen = true;
*/
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainActivity.class);
                i.putExtra("number", values.get(position).number);
                view.getContext().startActivity(i);
            }
        });
         if (position % 2 == 1)
            view.setBackgroundColor(Color.rgb(225,225,225));
        else
            view.setBackgroundColor(Color.rgb(255,255,255));
            return view;
    }

    public Uri getPhotoUri(Context c, long contactId) {
        ContentResolver contentResolver =  c.getContentResolver();

        try {
            Cursor cursor = contentResolver
                    .query(ContactsContract.Data.CONTENT_URI,
                            null,
                            ContactsContract.Data.CONTACT_ID
                                    + "="
                                    + contactId
                                    + " AND "

                                    + ContactsContract.Data.MIMETYPE
                                    + "='"
                                    + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE
                                    + "'", null, null);

            if (cursor != null) {
                if (!cursor.moveToFirst()) {
                    cursor.close();
                    return null; // no photo
                }
            } else {
                cursor.close();
                return null; // error in cursor process
            }
        cursor.close();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }

        Uri person = ContentUris.withAppendedId(
                ContactsContract.Contacts.CONTENT_URI, contactId);
        return Uri.withAppendedPath(person,
                ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
    }

}