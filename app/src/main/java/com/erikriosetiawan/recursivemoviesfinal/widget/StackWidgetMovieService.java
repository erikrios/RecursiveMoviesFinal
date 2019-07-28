package com.erikriosetiawan.recursivemoviesfinal.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackWidgetMovieService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewMoviesFactory(this.getApplicationContext());
    }
}
