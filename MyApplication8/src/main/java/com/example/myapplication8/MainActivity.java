package com.example.myapplication8;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {

            getAllSms();
            getContacts();
        }
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
    public void getContacts(){
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);

        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name      = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Log.d("contact", contactId + " " + name);         }
        cursor.close();
    }
    public  void getAllSms(){
        Uri uri = Uri.parse("content://sms/sent");
        Cursor c=getContentResolver().query(uri, null, null, null, null);
        startManagingCursor(c);

        String[] body = new String[c.getCount()];
        String[] number = new String[c.getCount()];
        int[] person = new int[c.getCount()];
       // Log.d("cols: ", Arrays.toString(c.getColumnNames()));
        //Log.d("tag,", String.valueOf(c.getCount()));
        if(c.moveToFirst()){
            final Markov m = new Markov();

            for(int i=0;i<c.getCount();i++){
                body[i]= c.getString(c.getColumnIndexOrThrow("body")).toString();
                number[i]=c.getString(c.getColumnIndexOrThrow("address")).toString();

                   person[i]=c.getInt(c.getColumnIndexOrThrow("person"));
                //Log.d("messages", body[i] + " " + person[i]);
                m.addSentence(body[i], 2);
                c.moveToNext();

            }
            final List<QuoteModel> genArray = m.generate(20, 10, 20);
            ListView lv = (ListView)(findViewById(R.id.listView));
            final QuoteAdapter adapter = new QuoteAdapter(this, genArray, m);
            lv.setAdapter(adapter);
            lv.setOnScrollListener(new EndlessScrollListener(genArray, m) {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    Log.d("time end", "");
                    //arr.addAll(m.generate(1, 10, 20));
                    //adapter.notifyDataSetChanged();
                    GenTask task = new GenTask(m, genArray, adapter);
                    task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    Log.d("time end", "");
                }
            });

        }
        c.close();
    }
    public class GenTask extends AsyncTask<String, Void, String> {
        private Markov m;
        private List<QuoteModel> arr;
        private QuoteAdapter adapter;
        private List<QuoteModel> newList;
        public GenTask(Markov markov, List<QuoteModel> list, QuoteAdapter a){
            m = markov;
            arr = list;
            adapter = a;
        }
        @Override
        protected String doInBackground(String ... urls) {
            newList = (m.generate(1, 10, 20));
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            arr.addAll(newList);
            adapter.notifyDataSetChanged();
        }
    }


    public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 20;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;
        protected List<QuoteModel> arr;
        private Markov m;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }
        public EndlessScrollListener(List<QuoteModel> backing, Markov markov){
            arr = backing;
            m = markov;
        }

        public EndlessScrollListener(int visibleThreshold, int startPage) {
            this.visibleThreshold = visibleThreshold;
            this.startingPageIndex = startPage;
            this.currentPage = startPage;
        }
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            // If there are no items in the list, assume that initial items are loading
            if (!loading && (totalItemCount < previousTotalItemCount)) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) { this.loading = true; }
            }

            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading) {
                if (totalItemCount > previousTotalItemCount) {
                    loading = false;
                    previousTotalItemCount = totalItemCount;
                    currentPage++;
                }
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                onLoadMore(currentPage + 1, totalItemCount);
                loading = true;
            }
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Don't take any action on changed
        }
    }
    }

