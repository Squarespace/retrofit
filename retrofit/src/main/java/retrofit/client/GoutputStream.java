package retrofit.client;

import java.io.IOException;
import java.io.OutputStream;

public class GoutputStream extends OutputStream {
  private final OutputStream out;
  private final ProgressListener listener;

  private int length;
  private int bytesTransferred;
  private int nextMileStone;

  public GoutputStream(OutputStream out, ProgressListener listener) {
    this.out = out;
    this.listener = listener;
  }

  @Override
  public void write(int b) throws IOException {
    bytesTransferred++;

    if(nextMileStone == 0) {
      nextMileStone = length / listener.updateCount();
    }

    if(bytesTransferred > nextMileStone) {
      nextMileStone += length / listener.updateCount();
      double progress = (double) bytesTransferred / length;
      listener.update(progress);
    }
    out.write(b);
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    length = len;
    super.write(b, off, len);
  }

  public static interface ProgressListener {
    int length();
    int updateCount();
    void update(double percent);
  }
}