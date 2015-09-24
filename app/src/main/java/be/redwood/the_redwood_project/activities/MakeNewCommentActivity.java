package be.redwood.the_redwood_project.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import be.redwood.the_redwood_project.R;

public class MakeNewCommentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_new_comment);
        DrawerFragmentFactory.createDrawerFragment(getSupportFragmentManager());
        getSupportActionBar().setTitle("The Redwood Project");
    }

}

