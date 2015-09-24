package be.redwood.the_redwood_project.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.models.Blog;
import be.redwood.the_redwood_project.models.Post;

public class OverviewPostsActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://172.30.68.16:3000";
    private static final String TAG = "MyActivity";
    private List<Post> postList;
    private String blogTitle;
    private ImageView blogImage;
    private TextView title;
    private TextView author;
    RecyclerView recList;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_posts);
        DrawerFragmentFactory.createDrawerFragment(getSupportFragmentManager());

        recList = (RecyclerView) findViewById(R.id.postList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            blogTitle = extras.getString("blog_title");
        }
        showBlogInformationOnScreen();

        postList = new ArrayList<>();
        fillPostListAndSetPostAdapter();
    }

    public void showBlogInformationOnScreen() {
        title = (TextView) findViewById(R.id.title);
        title.setText(blogTitle);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.include("user"); // includes the pointer to get the user information
        query.whereEqualTo("blogTitle", blogTitle);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject blog, com.parse.ParseException e) {
                if (blog != null) {
                    String image = blog.getString("image");
                    ParseObject user = (ParseObject) blog.get("user");
                    String userName = user.getString("username");

                    author = (TextView) findViewById(R.id.author);
                    author.setText("by: " + userName);
                    blogImage = (ImageView) findViewById(R.id.image_post);
                    Picasso.with(OverviewPostsActivity.this).load(image).into(blogImage);
                }
            }
        });
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void fillPostListAndSetPostAdapter() {

        // search for blogPosts, include "blog", waar ParseObject blog.getString("title") = ...
        // info nodig: createdAt (Date), postTitle

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
                PostAdapter pa = new PostAdapter(postList, OverviewPostsActivity.this);
                recList.setAdapter(pa);
                recList.setLayoutManager(llm);
            }
        });
    }

}
