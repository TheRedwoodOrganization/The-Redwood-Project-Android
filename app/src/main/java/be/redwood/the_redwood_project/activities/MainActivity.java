package be.redwood.the_redwood_project.activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.fragments.ProfilePageFragment;
import be.redwood.the_redwood_project.fragments.LoginPageFragment;
import be.redwood.the_redwood_project.fragments.ListBlogsFragment;
import be.redwood.the_redwood_project.fragments.RegistrationPageFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout myDrawerLayout;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Button openOverviewBlogs;
    private Button loginOrLogout;
    private Button registerOrShowDetailPageUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        myDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.putString("userName", null);
        editor.commit();

        // buttons in drawer
        openOverviewBlogs = (Button) this.findViewById(R.id.show_overview_blogs);
        openOverviewBlogs.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) this.findViewById (R.id.my_toolbar);
        toolbar.setTitle("");
        toolbar.setTitleTextColor(Color.WHITE);
//        toolbar.setNavigationIcon(R.drawable.image);
//        toolbar.inflateMenu(R.menu.toolbar_menu);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//
//                switch (menuItem.getItemId()){
//                    case R.id.action_share:
//                        Toast.makeText(ToolbarActivity.this,"Share",Toast.LENGTH_SHORT).show();
//                        return true;
//                }
//
//                return false;
//            }
//        });
        setSupportActionBar(toolbar);

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

}
