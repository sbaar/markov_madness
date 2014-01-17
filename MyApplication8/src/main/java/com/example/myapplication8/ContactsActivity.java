package com.example.myapplication8;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.myapplication8.dummy.ContactModel;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class ContactsActivity extends ActionBarActivity {
    ArrayList<ContactModel> contactList = new ArrayList<ContactModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        ListView lv =(ListView) findViewById(R.id.contactsView);
        if (savedInstanceState == null) {

            //getAllSms();
            LinkedHashSet<String> distinct = getDistinct();
            ContactModel all = new ContactModel("All texts", "-1", "-1");
            all.isAll = true;
            contactList.add(all);
            getContacts(distinct);


            lv.setAdapter(new ContactAdapter(this, contactList));

        }
    }

    private LinkedHashSet<String> getDistinct() {
        LinkedHashSet<String> lhs = new LinkedHashSet<String>();

        Uri uri = Uri.parse("content://sms/");
        String[] projection = new String[] {"address"};
        Cursor c =getContentResolver().query(uri, projection,null,null,null);
        while (c.moveToNext()) lhs.add(c.getString(c.getColumnIndex("address")));

        return lhs;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getContacts(Set<String> distinct) {
        Log.d("distinct no", Integer.toString(distinct.size()));
        ContentResolver cr  = getContentResolver();
        Cursor c;
        Uri uri;
        for (String phoneNumber: distinct){
            uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));


        String[] projection = new String[] { ContactsContract.PhoneLookup._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup.NUMBER};
        c = cr.query(uri,projection,null,null,null);
            while (c.moveToNext()) {

                String displayName = c.getString(c
                        .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                String number = c.getString(c
                        .getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
                String id = c.getString(c
                        .getColumnIndex(ContactsContract.PhoneLookup._ID));
                ContactModel cm = new ContactModel(displayName, number, id);
                Log.d("contact ts", cm.toString());
                contactList.add(new ContactModel(displayName, number, id));
            }
            Log.d("distinct no cl", Integer.toString(contactList.size()));
        }
        }


}

