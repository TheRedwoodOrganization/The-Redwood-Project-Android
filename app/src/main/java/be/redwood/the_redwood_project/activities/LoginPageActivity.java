package be.redwood.the_redwood_project.activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.models.User;

public class LoginPageActivity extends AppCompatActivity implements View.OnClickListener {
    TextView faultMessage;
    String username;
    String password;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        DrawerFragmentFactory.createDrawerFragment(getSupportFragmentManager());

        Toolbar toolbar = (Toolbar) this.findViewById (R.id.my_toolbar);
        setSupportActionBar(toolbar);


        login = (Button) this.findViewById(R.id.login);
        login.setOnClickListener(this);
        faultMessage = (TextView) this.findViewById(R.id.fault_message);

    }

    @Override
    public void onClick(View v) {
        EditText u = (EditText) this.findViewById(R.id.username);
        username = u.getText().toString();
        EditText p = (EditText) this.findViewById(R.id.password);
        password = p.getText().toString();
        final View view = v;

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {
                if (user != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Login succeeded");
                    builder.setMessage("Congratulations! You're now logged in.");
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
                    faultMessage.setVisibility(View.INVISIBLE);
                } else {
                    faultMessage.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
