package be.redwood.the_redwood_project.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;

public class RegistrationPageFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String emailUser;
    private String userNameUser;
    private String passwordUser;
    private String imageUser;
    private Button register;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.registration_page, container, false);

        register = (Button) v.findViewById(R.id.register_user);
        register.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View v) {
        View x = getView();
        final View view = x;
        EditText emailU = (EditText) x.findViewById(R.id.fill_in_email);
        emailUser = emailU.getText().toString();
        EditText userNameU = (EditText) x.findViewById(R.id.fill_in_username);
        userNameUser = userNameU.getText().toString();
        EditText passwordU = (EditText) x.findViewById(R.id.fill_in_password);
        passwordUser = passwordU.getText().toString();
        EditText imageU = (EditText) x.findViewById(R.id.fill_in_image_url);
        imageUser = imageU.getText().toString();

        if ((emailUser.equals("")) || (userNameUser.equals("")) || (passwordUser.equals("")) || (imageUser.equals(""))) {
            // Show message 'all fields must be filled in'
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Registration failed");
            builder.setMessage("Please fill in all fields.");
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
            ParseUser newUser = new ParseUser();
            newUser.setUsername(userNameUser);
            newUser.setPassword(passwordUser);
            newUser.setEmail(emailUser);
            newUser.put("profilePic", imageUser);
            newUser.put("hasBlog", false);
            newUser.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e != null) {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                    } else {
                        // Save the login in your sharedPreferences
                        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                        editor = pref.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("userName", userNameUser);
                        editor.commit();

                        // Change the buttons in the toolbar
                        Button button1 = (Button) getActivity().findViewById(R.id.register_or_detail_page);
                        button1.setText("Your page");
                        Button button2 = (Button) getActivity().findViewById(R.id.login_button);
                        button2.setText("Logout");

                        // open detailpage user
                        Fragment fragment = new DetailPageUserFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
                        fragmentTransaction.commit();

                        // Show message 'user is saved'
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Registration succeeded");
                        builder.setMessage("You can now create your own blog.");
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
                    }
                }
            });

        }

    }
}
