package be.redwood.the_redwood_project.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import be.redwood.the_redwood_project.R;
import be.redwood.the_redwood_project.fragments.CreatePostFragment;
import be.redwood.the_redwood_project.models.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ContactViewHolder>{
    private SharedPreferences pref;
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

        // The edit and delete icons should only be visible if the comment is from the loggedin user
        pref = context.getApplicationContext().getSharedPreferences("MyPref", 0);
        Boolean loggedIn = pref.getBoolean("isLoggedIn", false);
        String name = pref.getString("userName", null);
        if ((loggedIn) && (username.equals(name))) {
            contactViewHolder.vEditIcon.setVisibility(View.VISIBLE);
            contactViewHolder.vDeleteIcon.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.comment_in_listview_layout, viewGroup, false);
        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView vCommentText;
        protected TextView vOtherInformation;
        protected ImageView vEditIcon;
        protected ImageView vDeleteIcon;

        public ContactViewHolder(View v) {
            super(v);
            vCommentText = (TextView) v.findViewById(R.id.comment_text);
            vOtherInformation = (TextView) v.findViewById(R.id.other_information);
            vEditIcon = (ImageView) v.findViewById(R.id.edit_icon);
            vDeleteIcon = (ImageView) v.findViewById(R.id.delete_icon);
            vEditIcon.setOnClickListener(this);
            vDeleteIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Under construction");
            builder.setMessage("This is under construction");
            builder.setCancelable(true);
            final AlertDialog dlg = builder.create();
            dlg.show();
            final Timer t = new Timer();
            t.schedule(new TimerTask() {
                public void run() {
                    dlg.dismiss(); // when the task active then close the dialog
                    t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                }
            }, 5000);
        }
    }
}
