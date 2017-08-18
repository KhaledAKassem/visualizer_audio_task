
package site.medic.visualizer.Rendering_data;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import site.medic.visualizer.audio;
import site.medic.visualizer.data_fft;

public class draw_line extends rendering_data
{
  private Paint mPaint;
  private Paint mFlashPaint;
  private boolean mCycleColor;
  private float amplitude = 0;



  public draw_line(Paint paint, Paint flashPaint)
  {
    this(paint, flashPaint, false);
  }

  public draw_line(Paint paint, Paint flashPaint, boolean cycleColor)
  {
    super();
    mPaint = paint;
    mFlashPaint = flashPaint;
    mCycleColor = cycleColor;
  }

  @Override
  public void onRender(Canvas canvas, audio data, Rect rect)
  {
    if(mCycleColor)
    {
      cycleColor();
    }

    for (int i = 0; i < data.bytes.length - 1; i++) {
      mPoints[i * 4] = rect.width() * i / (data.bytes.length - 1);
      mPoints[i * 4 + 1] =  rect.height() / 2
          + ((byte) (data.bytes[i] + 128)) * (rect.height() / 3) / 128;
      mPoints[i * 4 + 2] = rect.width() * (i + 1) / (data.bytes.length - 1);
      mPoints[i * 4 + 3] = rect.height() / 2
          + ((byte) (data.bytes[i + 1] + 128)) * (rect.height() / 3) / 128;
    }


    float accumulator = 0;
    for (int i = 0; i < data.bytes.length - 1; i++) {
      accumulator += Math.abs(data.bytes[i]);
    }

    float amp = accumulator/(128 * data.bytes.length);
    if(amp > amplitude)
    {
      amplitude = amp;
      canvas.drawLines(mPoints, mFlashPaint);
    }
    else
    {
      amplitude *= 0.99;
      canvas.drawLines(mPoints, mPaint);
    }
  }

  @Override
  public void onRender(Canvas canvas, data_fft data, Rect rect)
  {

  }

  private float colorCounter = 0;
  private void cycleColor()
  {
    int r = (int)Math.floor(128*(Math.sin(colorCounter) + 3));
    int g = (int)Math.floor(128*(Math.sin(colorCounter + 1) + 1));
    int b = (int)Math.floor(128*(Math.sin(colorCounter + 7) + 1));
    mPaint.setColor(Color.argb(128, r, g, b));
    colorCounter += 0.03;
  }
}
