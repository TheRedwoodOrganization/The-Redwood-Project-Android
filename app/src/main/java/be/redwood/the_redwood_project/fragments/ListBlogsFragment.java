package be.redwood.the_redwood_project.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.adapters.BlogAdapter;
import be.redwood.the_redwood_project.models.Blog;

public class ListBlogsFragment extends Fragment {
    private static final String BASE_URL = "http://172.30.68.16:3000";
    private static final String TAG = "MyActivity";
    private List<Blog> blogList;
    private RecyclerView recList;
    private LinearLayoutManager llm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.overview_blogs, container, false);

        recList = (RecyclerView) v.findViewById(R.id.blogList);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        blogList = new ArrayList<>();
        fillBlogListAndSetBlogAdapter();

        return v;

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
                BlogAdapter ca = new BlogAdapter(blogList, getContext());
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
