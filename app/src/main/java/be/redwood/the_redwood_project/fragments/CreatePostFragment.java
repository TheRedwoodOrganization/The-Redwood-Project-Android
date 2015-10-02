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
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;

public class CreatePostFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView faultMessage;
    private String postTitle;
    private String postContent;
    private Button publish;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.create_post, container, false);

        faultMessage = (TextView) v.findViewById(R.id.fault_message);
        publish = (Button) v.findViewById(R.id.publish_post);
        publish.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        final View x = getView();
        EditText myPostTitle = (EditText) x.findViewById(R.id.title_post);
        postTitle = myPostTitle.getText().toString();
        EditText myPostContent = (EditText) x.findViewById(R.id.content_post);
        postContent = myPostContent.getText().toString();

        // check if the user is logged in
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        Boolean loggedIn = pref.getBoolean("isLoggedIn", false);

        if (!loggedIn) {
            // give the message that the user should log in to create a post
            AlertDialog.Builder builder = new AlertDialog.Builder(x.getContext());
            builder.setTitle("Please log in");
            builder.setMessage("You have to log in to create a post");
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
                        if (!hasBlog) {
                            // give the message that the user should have a blog to post posts
                            AlertDialog.Builder builder = new AlertDialog.Builder(x.getContext());
                            builder.setTitle("You need a blog");
                            builder.setMessage("Please create a blog first");
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
                            if ((postTitle.equals("")) || (postContent.equals(""))) {
                                faultMessage.setVisibility(View.VISIBLE);
                            } else {
                                faultMessage.setVisibility(View.INVISIBLE);

                                // search for the user ParseObject
                                pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
                                String username = pref.getString("userName", null);
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
                                query.whereEqualTo("username", username);
                                query.getFirstInBackground(new GetCallback<ParseObject>() {
                                    public void done(final ParseObject userInfo, ParseException e) {
                                        if (userInfo == null) {
                                            Log.d("user", "The getFirst request failed.");
                                        } else {
                                            // search for the blog parseObject
                                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
                                            query.whereEqualTo("user", userInfo);
                                            query.getFirstInBackground(new GetCallback<ParseObject>() {
                                                public void done(ParseObject blog, ParseException e) {
                                                    if (blog == null) {
                                                        Log.d("blog", "The getFirst request failed.");
                                                    } else {
                                                        // save the new post in Parse
                                                        ParseObject newPost = new ParseObject("BlogPost");
                                                        newPost.put("postTitle", postTitle);
                                                        newPost.put("postBody", postContent);
                                                        newPost.put("blog", blog);
                                                        newPost.put("user", userInfo);
                                                        newPost.saveInBackground();

                                                        // go to the blogpage of the user
                                                        String blogTitle = blog.getString("blogTitle");
                                                        Fragment fragment = new DetailPageBlogFromUserFragment();
                                                        Bundle arguments = new Bundle();
                                                        arguments.putString("blog_title", blogTitle);
                                                        fragment.setArguments(arguments);
                                                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                                        fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
                                                        fragmentTransaction.commit();

                                                        // give a succeed message
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(x.getContext());
                                                        builder.setTitle("Succeeded");
                                                        builder.setMessage("Your post is added to the list");
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
                                                    }
                                                }
                                            });

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
