package tcss450ajloria.uw.edu.phishapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

<<<<<<< HEAD:app/src/main/java/tcss450ajloria/uw/edu/phishapp/HomeActivity.java
import me.pushy.sdk.Pushy;
import tcss450ajloria.uw.edu.phishapp.blog.BlogPost;
import tcss450ajloria.uw.edu.phishapp.model.Credentials;
import tcss450ajloria.uw.edu.phishapp.setlist.SetListPost;
import tcss450ajloria.uw.edu.phishapp.utils.GetAsyncTask;
=======
import tcss450.uw.edu.phishapp.blog.BlogPost;
import tcss450.uw.edu.phishapp.setlist.SetListPost;
import tcss450.uw.edu.phishapp.utils.GetAsyncTask;
>>>>>>> bbd0136b073379a155b34495dd655067c50ad96c:app/src/main/java/tcss450/uw/edu/phishapp/HomeActivity.java

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , BlogFragment.OnListFragmentInteractionListener
        , BlogPostFragment.OnFragmentInteractionListener
        , SetListFragment.OnListFragmentInteractionListener
        , SetListPostFragment.OnFragmentInteractionListener
        , WaitFragment.OnFragmentInteractionListener{


    String myMsg;

    private String mJwToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String msg = intent.getStringExtra("info");

        myMsg = msg;

        mJwToken = getIntent().getStringExtra(getString(R.string.keys_intent_jwt));


        if(savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                //lf = new LoginFragment();
                SuccessFragment loginFragment;
                loginFragment = new SuccessFragment();

                Bundle args = new Bundle();
                args.putSerializable("info", msg);
                loginFragment.setArguments(args);

                /*Credentials credentials = (Credentials) getIntent()
                        .getSerializableExtra(getString(R.string.key_email));
                String emailAddress = mEmail = credentials.getEmail();
                final Bundle args = new Bundle();
                args.putString(getString(R.string.key_email), emailAddress);*/

                Fragment fragment;
                if (getIntent().getBooleanExtra(getString(R.string.keys_intent_notification_msg), false)) {
                    fragment = new ChatFragment();
                } else {
                    fragment = new SuccessFragment();
                    fragment.setArguments(args);
                }

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, fragment)
                        .commit();
            } }
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            SuccessFragment success;
            success = new SuccessFragment();

            Bundle args = new Bundle();
            args.putSerializable("info", myMsg);
            success.setArguments(args);
            loadFragment(success);
        } else if (id == R.id.nav_blog_post) {
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_blog))
                    .appendPath(getString(R.string.ep_get))
                    .build();
            new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleBlogGetOnPostExecute)
                    .addHeaderField("authorization", mJwToken) //add the JWT as a header
                    .build().execute();

        } else if (id == R.id.nav_set_lists){
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_phish))
                    .appendPath(getString(R.string.ep_setlists))
                    .appendPath(getString(R.string.ep_recent))
                    .build();
            new GetAsyncTask.Builder(uri.toString())
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleSetListGetOnPostExecute)
                    .addHeaderField("authorization", mJwToken) //add the JWT as a header
                    .build().execute();
<<<<<<< HEAD:app/src/main/java/tcss450ajloria/uw/edu/phishapp/HomeActivity.java
        } else if (id == R.id.nav_global_chat) {
            ChatFragment chat = new ChatFragment();
            Bundle args = new Bundle();
            SharedPreferences prefs = getSharedPreferences(getString(R.string.keys_shared_prefs),
                    Context.MODE_PRIVATE);
            String email = prefs.getString(getString(R.string.keys_prefs_email), "");
            args.putSerializable(getString(R.string.keys_intent_credentials), email);
            args.putSerializable(getString(R.string.keys_intent_jwt), mJwToken);
            chat.setArguments(args);
            loadFragment(chat);


=======
>>>>>>> bbd0136b073379a155b34495dd655067c50ad96c:app/src/main/java/tcss450/uw/edu/phishapp/HomeActivity.java
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(BlogPost item) {
//        Log.wtf("A Blog Is CLicked", item.getTitle());
//        Log.wtf("A Blog Is CLicked", item.getPubDate());
//        Log.wtf("A Blog Is CLicked", item.getAuthor());
//        Log.wtf("A Blog Is CLicked", item.getTeaser());
//        Log.wtf("A Blog Is CLicked", item.getUrl());

        BlogPostFragment success;
        success = new BlogPostFragment();

        Bundle args = new Bundle();
        args.putSerializable("info", item.getTitle() + "$ "
                                    + item.getPubDate() + "$ "
                                    + item.getTeaser() + "$ "
                                    + item.getUrl());
        success.setArguments(args);
        loadFragment(success);
        //loadFragment(new BlogPostFragment());
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainer, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    private void handleBlogGetOnPostExecute(final String result) { //parse JSON

        Log.e("ERROR!", "YEEEEEEEEEEEEEEEE");
        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_blogs_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_blogs_response));
                if (response.has(getString(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_blogs_data));
                    List<BlogPost> blogs = new ArrayList<>();
                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);
                        blogs.add(new BlogPost.Builder(
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_pubdate)),
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_title)))
                                .addTeaser(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_teaser)))
                                .addUrl(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_url)))
                                .build());
                    }

                    BlogPost[] blogsAsArray = new BlogPost[blogs.size()];
                    blogsAsArray = blogs.toArray(blogsAsArray);
                    Bundle args = new Bundle();
                    args.putSerializable(BlogFragment.ARG_BLOG_LIST, blogsAsArray);
                    Fragment frag = new BlogFragment();
                    frag.setArguments(args);
                    onWaitFragmentInteractionHide();
                    loadFragment(frag);
                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response"); //notify user onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace(); Log.e("ERROR!", e.getMessage()); //notify user onWaitFragmentInteractionHide();
        }
    }

    private void handleSetListGetOnPostExecute(final String result) {
        // Parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("response")) {
                JSONObject response = root.getJSONObject("response");
                if (response.has("data")) {
                    JSONArray data = response.getJSONArray("data");
                    List<SetListPost> setLists = new ArrayList<>();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonSetList = data.getJSONObject(i);
                        setLists.add(new SetListPost.Builder(jsonSetList.getString("location"),
                                jsonSetList.getString("long_date"),
                                jsonSetList.getString("venue"))
                                .addURL(jsonSetList.getString("url"))
                                .addSetListNotes(jsonSetList.getString("setlistnotes"))
                                .addSetListData(jsonSetList.getString("setlistdata"))
                                .build());
                    }

                    SetListPost[] setListsAsArray = new SetListPost[setLists.size()];
                    setListsAsArray = setLists.toArray(setListsAsArray);

                    Bundle args = new Bundle();
                    args.putSerializable(SetListFragment.ARG_SETLIST_LIST, setListsAsArray);
                    Fragment frag = new SetListFragment();
                    frag.setArguments(args);

                    onWaitFragmentInteractionHide();
                    loadFragment(frag);
                } else {
                    Log.e("ERROR!", "No data array");
                    // Notify user
                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                // Notify user
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            // Notify user
            onWaitFragmentInteractionHide();
        }
    }


    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null);
        transaction.commit();
    }

<<<<<<< HEAD:app/src/main/java/tcss450ajloria/uw/edu/phishapp/HomeActivity.java
    private void logout() {
        new DeleteTokenAsyncTask().execute();
    }

=======
>>>>>>> bbd0136b073379a155b34495dd655067c50ad96c:app/src/main/java/tcss450/uw/edu/phishapp/HomeActivity.java
    @Override
    public void onListFragmentInteraction(SetListPost item) {
        SetListPostFragment slpf = new SetListPostFragment();
        Bundle args = new Bundle();
        args.putSerializable("setlistpost", item);
        slpf.setArguments(args);
        loadFragment(slpf);

    }

    @Override
    public void onFullPostClicked(SetListPost setListPost) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(setListPost.getURL()));
        startActivity(browserIntent);
    }

<<<<<<< HEAD:app/src/main/java/tcss450ajloria/uw/edu/phishapp/HomeActivity.java
    // Deleting the Pushy device token must be done asynchronously. Good thing
    // we have something that allows us to do that.
    class DeleteTokenAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onWaitFragmentInteractionShow();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //since we are already doing stuff in the background, go ahead
            // and remove the credentials from shared prefs here.
            SharedPreferences prefs =
                    getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
            prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();
            //unregister the device from the Pushy servers
            Pushy.unregister(HomeActivity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //close the app
            finishAndRemoveTask();
            //or close this activity and bring back the Login
            //            Intent i = new Intent(this, MainActivity.class);
            //            startActivity(i);
            //            //Ends this Activity and removes it from the Activity back stack.
            //            finish();
        }
    }

=======
>>>>>>> bbd0136b073379a155b34495dd655067c50ad96c:app/src/main/java/tcss450/uw/edu/phishapp/HomeActivity.java
}
