package be.redwood.the_redwood_project.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;

import be.redwood.the_redwood_project.R;

public class ProfilePageFragment extends Fragment implements View.OnClickListener {
    private SharedPreferences pref;
    private ParseObject userInformation;
    private ImageView userImage;
    private TextView username;
    private TextView mailAddress;
    private Boolean hasBlog;
    private Button manageBlog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.profile_page, container, false);

        userImage = (ImageView) v.findViewById(R.id.image_user);
        username = (TextView) v.findViewById(R.id.username_user);
        mailAddress = (TextView) v.findViewById(R.id.mail_address_user);
        manageBlog = (Button) v.findViewById(R.id.manage_blog);
        manageBlog.setOnClickListener(this);

        showUserInformationOnScreen(v);

        return v;
    }

    public static ProfilePageFragment newInstance(int page, String title) {
        ProfilePageFragment fragment = new ProfilePageFragment();
        return fragment;
    }

    public void showUserInformationOnScreen(View v) {
        final View x = v;
        pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", 0);
        String name = pref.getString("userName", null);
        username.setText(name);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.whereEqualTo("username", name);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject user, com.parse.ParseException e) {
                if (user != null) {
                    userInformation = user;
                    String mail = user.getString("email");
                    mailAddress.setText(mail);
                    String imagePath = user.getString("profilePic");
                    Picasso.with(x.getContext()).load(imagePath).into(userImage);
                    hasBlog = user.getBoolean("hasBlog");
                    if (hasBlog) {
                        manageBlog.setText("Manage your blog");
                    } else {
                        manageBlog.setText("Create a blog");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Fragment fragment;
        if (hasBlog) {
            // find the blog from the user
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Blog");
            query.whereEqualTo("user", userInformation);
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject blog, com.parse.ParseException e) {
                    if (blog != null) {
                        String title = blog.getString("blogTitle");

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


        } else {
            fragment = new CreateBlogFragment();
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
            fragmentTransaction.commit();
        }

    }
}
