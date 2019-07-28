package com.erikriosetiawan.recursivemoviesfinal.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class StackWidgetTvShowService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewTvShowsFactory(this.getApplicationContext());
    }
}
