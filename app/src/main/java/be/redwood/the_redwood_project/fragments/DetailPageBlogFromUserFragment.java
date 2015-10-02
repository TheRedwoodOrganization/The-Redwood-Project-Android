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
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.adapters.PostAdapter;
import be.redwood.the_redwood_project.models.Post;

public class DetailPageBlogFromUserFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private List<Post> postList;
    private String blogTitle;
    private ImageView blogImage;
    private RecyclerView recList;
    private LinearLayoutManager llm;
    private String titlePost;
    private String contentPost;
    private Button publishPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.detailpage_blog_from_user, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            blogTitle = arguments.getString("blog_title");
        }

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        toolbar.setTitle(blogTitle);

        publishPost = (Button) v.findViewById(R.id.send_post);
        publishPost.setOnClickListener(this);

        recList = (RecyclerView) v.findViewById(R.id.postList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        showBlogInformationOnScreen(v);

        postList = new ArrayList<>();
        fillPostListAndSetPostAdapter();

        return v;
    }

    public void showBlogInformationOnScreen(View v) {
        final View x = v;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.include("user"); // includes the pointer to get the user information
        query.whereEqualTo("blogTitle", blogTitle);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject blog, com.parse.ParseException e) {
                if (blog != null) {
                    String image = blog.getString("image");
                    blogImage = (ImageView) x.findViewById(R.id.image_post);
                    Picasso.with(getActivity()).load(image).into(blogImage);
                }
            }
        });
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void fillPostListAndSetPostAdapter() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("BlogPost");
        query.include("blog"); // includes the pointer to get the user information
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject blogPost : list) {
                        String postTitle = blogPost.getString("postTitle");
                        Date date = blogPost.getCreatedAt();

                        ParseObject blog = (ParseObject) blogPost.get("blog");
                        String title = blog.getString("blogTitle");

                        if (title.equals(blogTitle)) {
                            Post p = new Post(postTitle, date);
                            postList.add(p);
                        }

                    }
                }
                PostAdapter pa = new PostAdapter(postList, getContext());
                recList.setAdapter(pa);
                recList.setLayoutManager(llm);
            }
        });
    }

    @Override
    public void onClick(View v) {
        final View x = getView();

        // get the values filled in by the user
        EditText title = (EditText) x.findViewById(R.id.new_title_post);
        titlePost = title.getText().toString();
        EditText content = (EditText) x.findViewById(R.id.new_content_post);
        contentPost = content.getText().toString();

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
                                newPost.put("postTitle", titlePost);
                                newPost.put("postBody", contentPost);
                                newPost.put("blog", blog);
                                newPost.put("user", userInfo);
                                newPost.saveInBackground();

                                // refresh the page
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