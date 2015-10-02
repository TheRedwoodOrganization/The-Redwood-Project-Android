package be.redwood.the_redwood_project.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;

public class CreateBlogFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView faultMessage;
    private String title;
    private String image;
    private Button publish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.create_blog, container, false);

        faultMessage = (TextView) v.findViewById(R.id.fault_message);
        publish = (Button) v.findViewById(R.id.publish_blog);
        publish.setOnClickListener(this);

        return v;

    }


    @Override
    public void onClick(View v) {
        final View x = getView();
        EditText blogTitle = (EditText) x.findViewById(R.id.title_blog);
        title = blogTitle.getText().toString();
        EditText blogImage = (EditText) x.findViewById(R.id.image_blog);
        image = blogImage.getText().toString();

        // check if the user is logged in
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        Boolean loggedIn = pref.getBoolean("isLoggedIn", false);

        if (!loggedIn) {
            // give the message that the user should log in to create a blog
            AlertDialog.Builder builder = new AlertDialog.Builder(x.getContext());
            builder.setTitle("Please log in");
            builder.setMessage("You have to log in to create a blog");
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
        } else {
            // check if the user has a blog
            ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
            String username = pref.getString("userName", null);
            query.whereEqualTo("username", username);
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                public void done(ParseUser user, com.parse.ParseException e) {
                    if (user != null) {
                        Boolean hasBlog = user.getBoolean("hasBlog");
                        if (hasBlog) {
                            // give the message that the user already has a blog
                            AlertDialog.Builder builder = new AlertDialog.Builder(x.getContext());
                            builder.setTitle("You already have a blog");
                            builder.setMessage("There can only be one blog per user");
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
                        } else {
                            if ((title.equals("")) || (image.equals(""))) {
                                faultMessage.setVisibility(View.VISIBLE);
                            } else {

                                // get the user pointer in Parse
                                pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                                String name = pref.getString("userName", null);
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                                query.whereEqualTo("username", name);
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    public void done(ParseObject user, ParseException e) {
                                        if (user == null) {
                                            Log.d("createBlogFragment", "The getFirst request to get the user failed.");
                                        } else {
                                            // create a new ParseObject blog
                                            ParseObject blog = new ParseObject("Blog");
                                            blog.put("blogTitle", title);
                                            blog.put("image", image);
                                            blog.put("user", user);
                                            blog.saveInBackground();

                                            // set Boolean hasBlog in Parse op true
                                            user.put("hasBlog", true);

                                            // show the blog overview page from the user
                                            Fragment fragment = new DetailPageBlogFromUserFragment();
                                            Bundle arguments = new Bundle();
                                            arguments.putString("blog_title", title);
                                            fragment.setArguments(arguments);
                                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
                                            fragmentTransaction.commit();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
    }
}

