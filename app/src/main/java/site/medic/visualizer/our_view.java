/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */
package site.medic.visualizer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

import site.medic.visualizer.Rendering_data.rendering_data;


public class our_view extends View {
  private static final String TAG = "VisualizerView";

  private byte[] mBytes;
  private byte[] mFFTBytes;
  private Rect mRect = new Rect();
  private Visualizer mVisualizer;

  private Set<rendering_data> mRenderers;

  private Paint mFlashPaint = new Paint();
  private Paint mFadePaint = new Paint();

  public our_view(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs);
    init();
  }

  public our_view(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public our_view(Context context)
  {
    this(context, null, 0);
  }

  private void init() {
    mBytes = null;
    mFFTBytes = null;

    mFlashPaint.setColor(Color.argb(122, 255, 255, 255));
    mFadePaint.setColor(Color.argb(238, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
    mFadePaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));

    mRenderers = new HashSet<rendering_data>();
  }


  public void link(MediaPlayer player)
  {
    if(player == null)
    {
      throw new NullPointerException("Cannot link to null MediaPlayer");
    }

    // Create the Visualizer object and attach it to our media player.
    mVisualizer = new Visualizer(player.getAudioSessionId());
    mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

    // Pass through Visualizer data to VisualizerView
    Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener()
    {
      @Override
      public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
          int samplingRate)
      {
        updateVisualizer(bytes);
      }

      @Override
      public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
          int samplingRate)
      {
        updateVisualizerFFT(bytes);
      }
    };

    mVisualizer.setDataCaptureListener(captureListener,
        Visualizer.getMaxCaptureRate() / 2, true, true);

    mVisualizer.setEnabled(true);
    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
    {
      @Override
      public void onCompletion(MediaPlayer mediaPlayer)
      {
        mVisualizer.setEnabled(false);
      }
    });
  }

  public void addRenderer(rendering_data renderer)
  {
    if(renderer != null)
    {
      mRenderers.add(renderer);
    }
  }

  public void clearRenderers()
  {
    mRenderers.clear();
  }


  public void release()
  {
    mVisualizer.release();
  }


  public void updateVisualizer(byte[] bytes) {
    mBytes = bytes;
    invalidate();
  }


  public void updateVisualizerFFT(byte[] bytes) {
    mFFTBytes = bytes;
    invalidate();
  }

  boolean mFlash = false;


  public void flash() {
    mFlash = true;
    invalidate();
  }

  Bitmap mCanvasBitmap;
  Canvas mCanvas;


  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    // Create canvas once we're ready to draw
    mRect.set(0, 0, getWidth(), getHeight());

    if(mCanvasBitmap == null)
    {
      mCanvasBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Config.ARGB_8888);
    }
    if(mCanvas == null)
    {
      mCanvas = new Canvas(mCanvasBitmap);
    }

    if (mBytes != null) {
      audio audioData = new audio(mBytes);
      for(rendering_data r : mRenderers)
      {
        r.render(mCanvas, audioData, mRect);
      }
    }

    if (mFFTBytes != null) {
      data_fft fftData = new data_fft(mFFTBytes);
      for(rendering_data r : mRenderers)
      {
        r.render(mCanvas, fftData, mRect);
      }
    }

    mCanvas.drawPaint(mFadePaint);

    if(mFlash)
    {
      mFlash = false;
      mCanvas.drawPaint(mFlashPaint);
    }

    canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);
  }
}