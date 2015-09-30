package be.redwood.the_redwood_project.fragments;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;

public class LoginPageFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView faultMessage;
    private String username;
    private String password;
    private Button login;
    private Boolean hasBlog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.login_page, container, false);

        faultMessage = (TextView) v.findViewById(R.id.fault_message);
        login = (Button) v.findViewById(R.id.login);
        login.setOnClickListener(this);

        return v;

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

                    //Show the correct fragment (detailpage user or overview blogs)
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                    query.whereEqualTo("username", username);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject user, ParseException e) {
                            if (user == null) {
                                Log.d("score", "The getFirst request failed.");
                            } else {
                                hasBlog = user.getBoolean("hasBlog");
                                Fragment fragment;
                                if (hasBlog) {
                                    fragment = new DetailPageUserFragment();
                                } else {
                                    fragment = new OverviewBlogsFragment();
                                }
                                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
                                fragmentTransaction.commit();
                            }
                        }
                    });

                    // Save the login in your sharedPreferences
                    pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                    editor = pref.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("userName", username);
                    editor.commit();

                    // Change the buttons in the toolbar
                    Button button1 = (Button) getActivity().findViewById(R.id.register_or_detail_page);
                    button1.setText("Your page");
                    Button button2 = (Button) getActivity().findViewById(R.id.login_button);
                    button2.setText("Logout");

                    // Show message 'congratulations, you're logged in'
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Login succeeded");
                    builder.setMessage("Congratulations! You're logged in.");
                    builder.setCancelable(true);
                    final AlertDialog dlg = builder.create();
                    dlg.show();
                    final Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            dlg.dismiss(); // when the task active then close the dialog
                            t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                        }
                    }, 4000);

                } else {
                    faultMessage.setVisibility(View.VISIBLE);
                }
            }
        });

    }
}
