package be.redwood.the_redwood_project.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.fragments.LoginPageFragment;
import be.redwood.the_redwood_project.fragments.MakeNewBlogFragment;
import be.redwood.the_redwood_project.fragments.MakeNewCommentFragment;
import be.redwood.the_redwood_project.fragments.MakeNewPostFragment;
import be.redwood.the_redwood_project.fragments.OverviewBlogsFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button openOverviewBlogs;
    Button createBlog;
    Button createPost;
    Button createComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        Toolbar toolbar = (Toolbar) this.findViewById (R.id.my_toolbar);
        setSupportActionBar(toolbar);

        Fragment fragment = new LoginPageFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
        fragmentTransaction.commit();

        openOverviewBlogs = (Button) this.findViewById(R.id.show_overview_blogs);
        openOverviewBlogs.setOnClickListener(this);
        createBlog = (Button) this.findViewById(R.id.create_blog);
        createBlog.setOnClickListener(this);
        createPost = (Button) this.findViewById(R.id.create_post);
        createPost.setOnClickListener(this);
        createComment = (Button) this.findViewById(R.id.create_comment);
        createComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Fragment fragment = new LoginPageFragment();
        if (v == openOverviewBlogs) {
            fragment = new OverviewBlogsFragment();
        } else if (v == createBlog) {
            fragment = new MakeNewBlogFragment();
        } else if (v == createPost) {
            fragment = new MakeNewPostFragment();
        } else if (v == createComment) {
            fragment = new MakeNewCommentFragment();
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
        fragmentTransaction.commit();
    }

}
