package de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools;
public class UniqueIdDisposer
{
  private long currentUniqueId = 15L;
  public long getNextUniqueId() {
    this.currentUniqueId++;
    if ((this.currentUniqueId & 0xFFFFFFFFFFFFFFFFL) == 0L)
    {
      this.currentUniqueId |= 0x10L;
    }
    return this.currentUniqueId;
  }
}
