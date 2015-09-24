package be.redwood.the_redwood_project.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import be.redwood.the_redwood_project.R;

public class MakeNewPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_new_post);
        DrawerFragmentFactory.createDrawerFragment(getSupportFragmentManager());
        getSupportActionBar().setTitle("The Redwood Project");
    }

}

