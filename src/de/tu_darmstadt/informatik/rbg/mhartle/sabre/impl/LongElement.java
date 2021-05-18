package de.tu_darmstadt.informatik.rbg.mhartle.sabre.impl;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Element;
public class LongElement extends Element {
  private long id = 0L;
  public LongElement(long id) {
    this.id = id;
  }
  public Object getId() {
    return new Long(this.id);
  }
  public String toString() {
    String result = null;
    result = new String();
    result = String.valueOf(result) + (char)(int)((this.id & 0xFFFFFFFFFF000000L) >> 24L);
    result = String.valueOf(result) + (char)(int)((this.id & 0xFF0000L) >> 16L);
    result = String.valueOf(result) + (char)(int)((this.id & 0xFF00L) >> 8L);
    result = String.valueOf(result) + (char)(int)(this.id & 0xFFL);
    return result;
  }
}
