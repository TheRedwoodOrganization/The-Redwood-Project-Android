package be.redwood.the_redwood_project.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import be.redwood.the_redwood_project.R;

public class LoginPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        DrawerFragmentFactory.createDrawerFragment(getSupportFragmentManager());
    }

}
