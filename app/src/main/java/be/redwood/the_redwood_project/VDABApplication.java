package be.redwood.the_redwood_project;

import android.app.Application;

import be.redwood.the_redwood_project.BuildConfig;
import com.parse.Parse;
import com.parse.ParseInstallation;

public class VDABApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "nrAMgoqfu4ANZbCJMgmJZjW4FBEMLxatKnqCxl1M", "T39TPsxaUvJwbYYHHnRsS3278r04RMKQ1rpsIlGn");
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

}