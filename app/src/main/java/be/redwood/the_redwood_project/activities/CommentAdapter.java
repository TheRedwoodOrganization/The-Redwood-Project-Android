package be.redwood.the_redwood_project.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.models.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ContactViewHolder>{
    private static List<Comment> commentList;
    private static Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        Comment comment = commentList.get(i);
        contactViewHolder.vCommentText.setText(comment.getCommentText());
        String username = comment.getUsername();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String date =  dateFormat.format(comment.getDate());
        contactViewHolder.vOtherInformation.setText("posted by " + username + " on " + date);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.comment_layout, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {
        protected TextView vCommentText;
        protected TextView vOtherInformation;

        public ContactViewHolder(View v) {
            super(v);
            vCommentText = (TextView) v.findViewById(R.id.comment_text);
            vOtherInformation = (TextView) v.findViewById(R.id.other_information);
        }

    }
}
