
package site.medic.visualizer.Rendering_data;

import android.graphics.Canvas;
import android.graphics.Rect;

import site.medic.visualizer.audio;
import site.medic.visualizer.data_fft;

abstract public class rendering_data
{
  protected float[] mPoints;
  protected float[] mFFTPoints;
  public rendering_data()
  {
  }

  abstract public void onRender(Canvas canvas, audio data, Rect rect);

  abstract public void onRender(Canvas canvas, data_fft data, Rect rect);


  final public void render(Canvas canvas, audio data, Rect rect)
  {
    if (mPoints == null || mPoints.length < data.bytes.length * 4) {
      mPoints = new float[data.bytes.length * 4];
    }

    onRender(canvas, data, rect);
  }


  final public void render(Canvas canvas, data_fft data, Rect rect)
  {
    if (mFFTPoints == null || mFFTPoints.length < data.bytes.length * 4) {
      mFFTPoints = new float[data.bytes.length * 4];
    }

    onRender(canvas, data, rect);
  }
}
