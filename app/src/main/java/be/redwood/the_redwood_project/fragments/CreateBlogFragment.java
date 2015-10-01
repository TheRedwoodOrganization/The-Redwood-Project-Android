package be.redwood.the_redwood_project.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
        View x = getView();
        EditText blogTitle = (EditText) x.findViewById(R.id.title_blog);
        title = blogTitle.getText().toString();
        EditText blogImage = (EditText) x.findViewById(R.id.image_blog);
        image = blogImage.getText().toString();

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

                        // show the blog overview page from the user
                        Fragment fragment = new BlogsUserFragment();
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

