package be.redwood.the_redwood_project.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.adapters.CommentAdapter;
import be.redwood.the_redwood_project.models.Comment;
import be.redwood.the_redwood_project.models.Post;

public class OverviewCommentsFragment extends Fragment implements View.OnClickListener {
    private static final String BASE_URL = "http://172.30.68.16:3000";
    private static final String TAG = "MyActivity";
    private List<Comment> commentList;
    private String postTitle;
    private TextView postBody;
    private RecyclerView recList;
    private LinearLayoutManager llm;
    private String commentText;
    private Button sendComment;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    ParseObject blogPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.overview_comments, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            postTitle = arguments.getString("post_title");
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById (R.id.my_toolbar);
        toolbar.setTitle(postTitle);

        sendComment = (Button) v.findViewById(R.id.send_comment);
        sendComment.setOnClickListener(this);

        recList = (RecyclerView) v.findViewById(R.id.commentList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        showPostInformationOnScreen(v);

        commentList = new ArrayList<>();
        fillCommentListAndSetCommentAdapter();

        return v;

    }


    public void showPostInformationOnScreen(View v) {
        final View x = v;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("BlogPost");
        query.include("blog"); // includes the pointer to get the user information
        query.include("blog.user");
        query.whereEqualTo("postTitle", postTitle);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject post, com.parse.ParseException e) {
                if (post != null) {
                    String body = post.getString("postBody");
                    postBody = (TextView) x.findViewById(R.id.body_of_the_post);
                    postBody.setText(body);
                    blogPost = post;
                }
            }
        });
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void fillCommentListAndSetCommentAdapter() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
        query.include("user"); // includes the pointer to get the user information
        query.include("blogPost"); // includes the pointer to get the user information
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject comment : list) {
                        String body = comment.getString("commentText");
                        Date date = comment.getCreatedAt();

                        ParseObject user = (ParseObject) comment.get("user");
                        String userName = user.getString("username");

                        ParseObject post = (ParseObject) comment.get("blogPost");
                        String title = post.getString("postTitle");
                        if (title.equals(postTitle)) {
                            Comment b = new Comment(body, userName, date);
                            commentList.add(b);
                        }
                    }
                }
                CommentAdapter ca = new CommentAdapter(commentList, getContext());
                Log.w("TEST", "commentlist " + commentList.size());
                recList.setAdapter(ca);
                recList.setLayoutManager(llm);
            }
        });
    }

    @Override
    public void onClick(View v) {
        View x = getView();
        // search if the user is logged in or not
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = pref.edit();
        Boolean loggedIn = pref.getBoolean("isLoggedIn", false);
        String username = pref.getString("userName", null);

        if (loggedIn) {
            // sla comment op
            EditText givenComment = (EditText) x.findViewById(R.id.new_comment_text);
            commentText = givenComment.getText().toString();
            final ParseObject newComment = new ParseObject("Comment");
            newComment.put("commentText", commentText);

            ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
            query.whereEqualTo("username", username);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject userInfo, ParseException e) {
                    if (userInfo == null) {
                        Log.d("user", "The getFirst request failed.");
                    } else {
                        newComment.put("user", userInfo);
                        newComment.put("blogPost", blogPost);
                        newComment.saveInBackground();

                        // refresh the page
                        Fragment fragment = new OverviewCommentsFragment();
                        Bundle arguments = new Bundle();
                        arguments.putString("post_title", postTitle);
                        fragment.setArguments(arguments);
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
                        fragmentTransaction.commit();
                    }
                }
            });


        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(x.getContext());
            builder.setTitle("Please log in");
            builder.setMessage("Only then you can post comments");
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
}