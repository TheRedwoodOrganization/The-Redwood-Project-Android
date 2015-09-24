package be.redwood.the_redwood_project.activities;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.models.Blog;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.http.GET;
import retrofit.http.Path;
import timber.log.Timber;

public class OverviewBlogsActivity extends AppCompatActivity {
    public static final String BASE_URL = "http://172.30.68.16:3000";
    private static final String TAG = "MyActivity";
    private List<Blog> blogList;
    RecyclerView recList;
    LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_blogs);
        DrawerFragmentFactory.createDrawerFragment(getSupportFragmentManager());
        getSupportActionBar().setTitle("The Redwood Project");

        recList = (RecyclerView) findViewById(R.id.blogList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        blogList = new ArrayList<>();
        fillBlogListAndSetBlogAdapter();
    }

    public List<Blog> getBlogList() {
        return blogList;
    }

    public void fillBlogListAndSetBlogAdapter() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
        query.include("user"); // includes the pointer to get the user information
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> list, com.parse.ParseException e) {
                if (e == null) {
                    for (ParseObject blog : list) {
                        String blogTitle = blog.getString("blogTitle");
                        String image = blog.getString("image");

                        ParseObject user = (ParseObject) blog.get("user");
                        String userName = user.getString("username");

                        Blog b = new Blog(blogTitle, image, userName);
                        blogList.add(b);
                    }
                }
                BlogAdapter ca = new BlogAdapter(blogList, OverviewBlogsActivity.this);
                recList.setAdapter(ca);
                recList.setLayoutManager(llm);
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_redwood, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


}
