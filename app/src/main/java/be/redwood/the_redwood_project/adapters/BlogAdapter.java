package be.redwood.the_redwood_project.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.fragments.OverviewPostsFragment;
import be.redwood.the_redwood_project.models.Blog;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ContactViewHolder> {
    private static List<Blog> blogList;
    private static Context context;

    public BlogAdapter(List<Blog> blogList, Context context) {
        this.blogList = blogList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Blog blog = blogList.get(i);
        contactViewHolder.vTitle.setText(blog.getTitle());
        contactViewHolder.vAuthor.setText("by: " + blog.getUsername());

        // image als achtergrond van author zetten
        Picasso.with(context).load(blog.getImage()).into(contactViewHolder.vImage);

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.blog_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vTitle;
        protected TextView vAuthor;
        protected ImageView vImage;

        public ContactViewHolder(View v) {
            super(v);
            vTitle =  (TextView) v.findViewById(R.id.title);
            vAuthor = (TextView) v.findViewById(R.id.author);
            vImage = (ImageView) v.findViewById(R.id.imageBlog);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            try{
                Fragment fragment = new OverviewPostsFragment();
                Bundle arguments = new Bundle();
                Blog blog = blogList.get(this.getLayoutPosition());
                arguments.putString("blog_title", blog.getTitle());
                fragment.setArguments(arguments);

                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
                fragmentTransaction.commit();
            } catch (ClassCastException e) {
                Log.d("my_activity", "Can't get the fragment manager");
            }
//            Intent intent = new Intent(context, MainActivity.class);
//            Blog blog = blogList.get(this.getLayoutPosition());
//            intent.putExtra("blog_title", blog.getTitle());
//            context.startActivity(intent);
        }
    }
}