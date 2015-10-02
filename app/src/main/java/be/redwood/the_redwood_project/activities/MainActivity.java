package be.redwood.the_redwood_project.activities;

import android.app.FragmentManager;
import android.app.LauncherActivity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.fragments.CreateBlogFragment;
import be.redwood.the_redwood_project.fragments.DetailPageBlogFromUserFragment;
import be.redwood.the_redwood_project.fragments.ProfilePageFragment;
import be.redwood.the_redwood_project.fragments.LoginPageFragment;
import be.redwood.the_redwood_project.fragments.ListBlogsFragment;
import be.redwood.the_redwood_project.fragments.RegistrationPageFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String mActivityTitle;
    private DrawerLayout myDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button openOverviewBlogs;
    private Button createBlog;
    private Button createPost;
    private Button createComment;
    private Button loginOrLogout;
    private Button registerOrShowDetailPageUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("userName", null);
        editor.commit();

        // buttons in drawer
//        openOverviewBlogs = (Button) this.findViewById(R.id.show_overview_blogs);
//        openOverviewBlogs.setOnClickListener(this);
//        createBlog = (Button) this.findViewById(R.id.create_blog);
//        createBlog.setOnClickListener(this);
//        createPost = (Button) this.findViewById(R.id.create_post);
//        createPost.setOnClickListener(this);
//        createComment = (Button) this.findViewById(R.id.create_comment);
//        createComment.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) this.findViewById (R.id.my_toolbar);
        toolbar.setTitle("Redwood project");
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitle("");
        toolbar.setSubtitleTextColor(Color.WHITE);

        mDrawerList = (ListView)findViewById(R.id.navList);

        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        setupDrawer();
        addDrawerItems();

        setSupportActionBar(toolbar);

        toolbar.inflateMenu(R.menu.menu_redwood);

        // buttons in toolbar
        registerOrShowDetailPageUser = (Button) this.findViewById(R.id.register_or_detail_page);
        registerOrShowDetailPageUser.setOnClickListener(this);
        loginOrLogout = (Button) this.findViewById(R.id.login_button);
        loginOrLogout.setOnClickListener(this);

        // set fragment
        Fragment fragment = new LoginPageFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
        fragmentTransaction.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_dialog_info);
    }

    @Override
    public void onClick(View v) {
        Toolbar toolbar = (Toolbar) findViewById (R.id.my_toolbar);
        toolbar.setTitle("");
        Fragment fragment = new LoginPageFragment();

        Boolean loggedIn = pref.getBoolean("isLoggedIn", false);
        if ((v == loginOrLogout) && (loggedIn)) {
            // change the sharedPreferences information
            editor.putBoolean("isLoggedIn", false);
            editor.commit();

            // Change the buttons in the toolbar
            Button button1 = (Button) findViewById(R.id.register_or_detail_page);
            button1.setText("Register");
            Button button2 = (Button) findViewById(R.id.login_button);
            button2.setText("Login");

            // Show message 'congratulations, you're logged in'
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Logged out");
            builder.setMessage("You can come back anytime you want");
            builder.setCancelable(true);
            final AlertDialog dlg = builder.create();
            dlg.show();
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    dlg.dismiss(); // when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }, 5000);
        } else if ((v == registerOrShowDetailPageUser) && (loggedIn)) {
            fragment = new ProfilePageFragment();
        } else if ((v == registerOrShowDetailPageUser) && !(loggedIn)) {
            fragment = new RegistrationPageFragment();
        } else if (v == openOverviewBlogs) {
            myDrawerLayout.closeDrawers();
            fragment = new ListBlogsFragment();
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_redwood, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, myDrawerLayout,

                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Navigation!");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        myDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addDrawerItems() {
        String[] osArray = { "Blogs", "Create blog", "comment", "profile" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myDrawerLayout.closeDrawers();
                Fragment fr;
                fr = getItem(position);

                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.place_for_the_real_page, fr);
                fragmentTransaction.commit();
            }
        });
    }

    // drawer + content
    public Fragment getItem(int position) {

        switch (position) {
            case 0: // Fragment # 0 - This will show FragmentOne
                return new ListBlogsFragment();
            case 1:
                return new CreateBlogFragment();
            case 2:
                return new DetailPageBlogFromUserFragment();
            case 3://TODO: implement Frag4
                return new ProfilePageFragment();
            default:
                return null;
        }
    }

}