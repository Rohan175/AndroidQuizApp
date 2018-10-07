package com.rsking175453.com.iaa;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    ArrayList<qaModel> qaArrayList;
    ConstraintLayout c;
    FloatingActionButton fab;
    ConstraintSet c1,c2;
    questionAdapter adapter;
    private static int firstView;
    AppBarLayout appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qaArrayList = new ArrayList<qaModel>();
        c = (ConstraintLayout) findViewById(R.id.include);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        appBar = (AppBarLayout)findViewById(R.id.appBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Quiz Time");
        getSupportActionBar().setSubtitle("LearonCodeOnline.in");

        //c1 = new ConstraintSet();
        //c2 = new ConstraintSet();
        //c1.clone(c);
        //c2.clone(this,R.layout.activity_main_fullscreeen);

        getJsondata();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", qaArrayList);
                bundle.putInt("position", 0);
                bundle.putInt("Type",1);


                FragmentTransaction ft = getFragmentManager().beginTransaction();

                dialogViewPager newd = dialogViewPager.newInstance();

                newd.setArguments(bundle);
                newd.show(ft, "slideshow");
            }
        });

        View banner = (View) findViewById(R.id.bannerInclude);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://learncodeonline.in/"));
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    public void getJsondata(){

        String url = "http://rsking175453.000webhostapp.com/datastructersMode.json";
        //String url = "https://learncodeonline.in/api/android/datastructure.json";
        GetData task = new GetData();
        task.execute(url);
    }

    private class GetData extends AsyncTask<String, Void, Void> {

        private static final String TAG = "GetDataTask";
        ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(String... S) {
            httpHandler sh = new httpHandler();
            String jsonStr = sh.makeServiceCall(S[0]);

            Log.v("loging", "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject q = new JSONObject(jsonStr);
                    JSONArray qArray = q.getJSONArray("questions");

                    for(int i =0 ; i <qArray.length(); i++){

                        JSONObject qaObject = (JSONObject) qArray.get(i);
                        qaArrayList.add(new qaModel(
                                qaObject.getString("question"),
                                qaObject.getString("Answer")
                        ));
                    }



                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            super.onPostExecute(result);
                pDialog.dismiss();
                //Show data on UI

            RecyclerView r1 = findViewById(R.id.dataRecycleView);
            adapter = new questionAdapter(MainActivity.this, qaArrayList);
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return true;
                }
            };
            //r1.setNestedScrollingEnabled(false);
            r1.setLayoutManager(linearLayoutManager);
            r1.setAdapter(adapter);
            firstView = linearLayoutManager.findFirstVisibleItemPosition();

            r1.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    if(newState == RecyclerView.SCROLL_STATE_IDLE){
                        fab.show();
                    }else{
                        fab.hide();
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Log.v("loging","dy : "+dy);
                    int currentFirstVisible = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    Log.v("loging"," currentFirstVisible : "+currentFirstVisible);
//                    if(dy > 0 && (currentFirstVisible < 3)){
//                        Log.v("loging", "scroll up!");
//                        c2.applyTo(c);
//                    }
//                    if(dy < 0 && currentFirstVisible < 5){
//                        Log.v("loging", "scroll down!");
//                        c1.applyTo(c);
//                    }
//                    firstView = currentFirstVisible;

                }
            });

        }

    }



}
