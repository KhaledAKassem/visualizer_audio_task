package site.medic.main_app;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.medic.visualizer.R;

import java.io.IOException;

import site.medic.utils.workaround;
import site.medic.visualizer.our_view;
import site.medic.visualizer.Rendering_data.draw_line;


public class MainActivity extends Activity {
  private MediaPlayer mPlayer;
  private MediaPlayer mSilentPlayer;
  private our_view mVisualizerView;

  Button b1,b2,b3,b4;
  Paint paint;
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater=getMenuInflater();
    inflater.inflate(R.menu.main_menu,menu);
    return true;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    b1=(Button) findViewById(R.id.one);

    b1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Paint linePaint = new Paint();
        //first sign wave signal
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);

        linePaint.setColor(Color.RED);

        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.setColor(Color.RED);
        draw_line lineRenderer = new draw_line(linePaint, lineFlashPaint, true);
        mVisualizerView.addRenderer(lineRenderer);

      }
    });
    b2=(Button) findViewById(R.id.two);
    b2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Paint linePaint = new Paint();
        //first sign wave signal
        linePaint.setStrokeWidth(5f);
        linePaint.setAntiAlias(true);

        linePaint.setColor(Color.BLUE);
        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.setColor(Color.BLUE);
        draw_line lineRenderer = new draw_line(linePaint, lineFlashPaint, false);
        mVisualizerView.addRenderer(lineRenderer);

      }
    });

    b3=(Button) findViewById(R.id.three);

    b3.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Paint linePaint = new Paint();
        //first sign wave signal
        linePaint.setStrokeWidth(1f);
        linePaint.setAntiAlias(true);


        Paint lineFlashPaint = new Paint();
        lineFlashPaint.setStrokeWidth(5f);
        lineFlashPaint.setAntiAlias(true);
        lineFlashPaint.clearShadowLayer();
        draw_line lineRenderer = new draw_line(linePaint, lineFlashPaint, false);
        mVisualizerView.addRenderer(lineRenderer);

      }
    });


  }

  @Override
  protected void onResume()
  {
    super.onResume();
    initTunnelPlayerWorkaround();
    init();
  }

  @Override
  protected void onPause()
  {
    cleanUp();
    super.onPause();
  }

  @Override
  protected void onDestroy()
  {
    cleanUp();
    super.onDestroy();
  }

  private void init()
  {
    mPlayer = MediaPlayer.create(this, R.raw.test);
    mPlayer.setLooping(true);
    mPlayer.start();

    mVisualizerView = (our_view) findViewById(R.id.visualizerView);
    mVisualizerView.link(mPlayer);

    addLineRenderer();
  }

  private void cleanUp()
  {
    if (mPlayer != null)
    {
      mVisualizerView.release();
      mPlayer.release();
      mPlayer = null;
    }

    if (mSilentPlayer != null)
    {
      mSilentPlayer.release();
      mSilentPlayer = null;
    }
  }

  private void initTunnelPlayerWorkaround() {

    if (workaround.isTunnelDecodeEnabled(this)) {
      mSilentPlayer = workaround.createSilentMediaPlayer(this);
    }
  }

  private void addLineRenderer()
  {
    Paint linePaint = new Paint();
    //first sign wave signal
    linePaint.setStrokeWidth(1f);
    linePaint.setAntiAlias(true);

    linePaint.setColor(Color.BLUE);

    Paint lineFlashPaint = new Paint();
    lineFlashPaint.setStrokeWidth(5f);
    lineFlashPaint.setAntiAlias(true);
    lineFlashPaint.setColor(Color.CYAN);
    draw_line lineRenderer = new draw_line(linePaint, lineFlashPaint, true);
    mVisualizerView.addRenderer(lineRenderer);
  }

  // Actions for buttons defined in xml
  public void startPressed(View view) throws IllegalStateException, IOException
  {
    if(mPlayer.isPlaying())
    {
      return;
    }
    mPlayer.prepare();
    mPlayer.start();
  }

  public void stopPressed(View view)
  {
    mPlayer.stop();
  }

  public void linePressed(View view)
  {
    addLineRenderer();
  }

  public void clearPressed(View view)
  {
    mVisualizerView.clearRenderers();
  }
}