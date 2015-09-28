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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.fragments.OverviewCommentsFragment;
import be.redwood.the_redwood_project.models.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ContactViewHolder> {
    private static List<Post> postList;
    private static Context context;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Post post = postList.get(i);
        contactViewHolder.vTitle.setText(post.getTitle());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String s =  dateFormat.format(post.getDate());
        contactViewHolder.vDate.setText("posted on: " + s);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.post_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vTitle;
        protected TextView vDate;

        public ContactViewHolder(View v) {
            super(v);
            vTitle = (TextView) v.findViewById(R.id.title);
            vDate = (TextView) v.findViewById(R.id.date);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try{
                Fragment fragment = new OverviewCommentsFragment();
                Bundle arguments = new Bundle();
                Post post = postList.get(this.getLayoutPosition());
                arguments.putString("post_title", post.getTitle());
                fragment.setArguments(arguments);

                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.place_for_the_real_page, fragment);
                fragmentTransaction.commit();
            } catch (ClassCastException e) {
                Log.d("my_activity", "Can't get the fragment manager");
            }
//            Intent intent = new Intent(context, MainActivity.class);
//            Post post = postList.get(this.getLayoutPosition());
//            intent.putExtra("post_title", post.getTitle());
//            context.startActivity(intent);
        }
    }
}