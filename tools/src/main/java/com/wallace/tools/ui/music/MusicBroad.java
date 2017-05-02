package com.wallace.tools.ui.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Package com.wallace.tools.ui.music
 * Created by Wallace.
 * on 2017/4/1.
 */
@Deprecated
public class MusicBroad extends BroadcastReceiver {
    public static final String MY_BROADCAST_NAME = "com.wallace.music.broadcast";
    private static final String PAUSE_BROADCAST_NAME = "com.wallace.music.pause.broadcast";
    private static final String NEXT_BROADCAST_NAME = "com.wallace.music.next.broadcast";
    private static final String PRE_BROADCAST_NAME = "com.wallace.music.pre.broadcast";
    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
