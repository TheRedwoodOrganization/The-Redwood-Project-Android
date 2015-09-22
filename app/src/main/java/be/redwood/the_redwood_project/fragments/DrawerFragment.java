package be.redwood.the_redwood_project.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.activities.MakeNewBlogActivity;
import be.redwood.the_redwood_project.activities.MakeNewCommentActivity;
import be.redwood.the_redwood_project.activities.MakeNewPostActivity;
import be.redwood.the_redwood_project.activities.OverviewBlogsActivity;

public class DrawerFragment extends Fragment implements View.OnClickListener {
    Button openOverviewBlogs;
    Button createBlog;
    Button createPost;
    Button createComment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.drawer_with_menu, container, false);

        openOverviewBlogs = (Button) v.findViewById(R.id.show_overview_blogs);
        openOverviewBlogs.setOnClickListener(this);
        createBlog = (Button) v.findViewById(R.id.create_blog);
        createBlog.setOnClickListener(this);
        createPost = (Button) v.findViewById(R.id.create_post);
        createPost.setOnClickListener(this);
        createComment = (Button) v.findViewById(R.id.create_comment);
        createComment.setOnClickListener(this);

        return v;
    }

    public static DrawerFragment newInstance(int page, String title) {
        DrawerFragment fragment = new DrawerFragment();
        return fragment;
    }


    @Override
    public void onClick(View v) {
        if (v == openOverviewBlogs) {
            Intent intent = new Intent(v.getContext(), OverviewBlogsActivity.class);
            startActivity(intent);
        } else if (v == createBlog) {
            Intent intent = new Intent(v.getContext(), MakeNewBlogActivity.class);
            startActivity(intent);
        } else if (v == createPost) {
            Intent intent = new Intent(v.getContext(), MakeNewPostActivity.class);
            startActivity(intent);
        } else if (v == createComment) {
            Intent intent = new Intent(v.getContext(), MakeNewCommentActivity.class);
            startActivity(intent);
        }
    }
}
