/**
 * Copyright 2013, Haruki Hasegawa
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */

package site.medic.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import com.medic.visualizer.R;

public class workaround {
  private static final String TAG = "workaround";

  private static final String SYSTEM_PROP_TUNNEL_DECODE_ENABLED = "tunnel.decode";

  private workaround()
  {
  }


  public static boolean isTunnelDecodeEnabled(Context context)
  {
    return pro.getBoolean(
        context, SYSTEM_PROP_TUNNEL_DECODE_ENABLED, false);
  }


  public static MediaPlayer createSilentMediaPlayer(Context context)
  {
    boolean result = false;

    MediaPlayer mp = null;
    try {
      mp = MediaPlayer.create(context, R.raw.workaround_1min);
      mp.setAudioStreamType(AudioManager.STREAM_MUSIC);


      result = true;
    } catch (RuntimeException e) {
      Log.e(TAG, "createSilentMediaPlayer()", e);
    } finally {
      if (!result && mp != null) {
        try {
          mp.release();
        } catch (IllegalStateException e) {
        }
      }
    }

    return mp;
  }
}
