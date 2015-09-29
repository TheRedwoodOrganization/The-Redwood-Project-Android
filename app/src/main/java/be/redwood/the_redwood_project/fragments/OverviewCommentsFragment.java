package be.redwood.the_redwood_project.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.adapters.CommentAdapter;
import be.redwood.the_redwood_project.models.Comment;

public class OverviewCommentsFragment extends Fragment {
    private static final String BASE_URL = "http://172.30.68.16:3000";
    private static final String TAG = "MyActivity";
    private List<Comment> commentList;
    private String postTitle;
    private TextView title;
    private TextView authorAndDate;
    private TextView postBody;
    private RecyclerView recList;
    private LinearLayoutManager llm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.overview_comments, container, false);

        recList = (RecyclerView) v.findViewById(R.id.commentList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Bundle arguments = getArguments();
        if (arguments != null) {
            postTitle = arguments.getString("post_title");
        }
        showPostInformationOnScreen(v);

        commentList = new ArrayList<>();
        fillCommentListAndSetCommentAdapter();

        return v;

    }


    public void showPostInformationOnScreen(View v) {
        final View x = v;
        title = (TextView) x.findViewById(R.id.title);
        title.setText(postTitle);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("BlogPost");
        query.include("blog"); // includes the pointer to get the user information
        query.include("blog.user");
        query.whereEqualTo("postTitle", postTitle);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject post, com.parse.ParseException e) {
                if (post != null) {
                    String body = post.getString("postBody");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String date =  dateFormat.format(post.getCreatedAt());

                    ParseObject blog = (ParseObject) post.get("blog");
                    ParseObject user = (ParseObject) post.get("user");
                    String userName = user.getString("username");

                    authorAndDate = (TextView) x.findViewById(R.id.author_and_date);
                    authorAndDate.setText("Posted by " + userName + " on " + date);
                    postBody = (TextView) x.findViewById(R.id.body_of_the_post);
                    postBody.setText(body);
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
                recList.setAdapter(ca);
                recList.setLayoutManager(llm);
            }
        });
    }

}