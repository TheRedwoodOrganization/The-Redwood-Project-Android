package be.redwood.the_redwood_project.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;

public class LoginPageFragment extends Fragment implements View.OnClickListener {
    TextView faultMessage;
    String username;
    String password;
    Button login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.login_page, container, false);

        faultMessage = (TextView) v.findViewById(R.id.fault_message);
        login = (Button) v.findViewById(R.id.login);
        login.setOnClickListener(this);

        return v;

    }

    public static LoginPageFragment newInstance(int page, String title) {
        LoginPageFragment fragment = new LoginPageFragment();
        return fragment;
    }

    @Override
    public void onClick(View v) {
        View x = getView();
        EditText u = (EditText) x.findViewById(R.id.username);
        username = u.getText().toString();
        EditText p = (EditText) x.findViewById(R.id.password);
        password = p.getText().toString();
        final View view = x;

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
