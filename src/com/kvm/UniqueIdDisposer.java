package com.kvm;
public class UniqueIdDisposer
{
  private int currentUniqueId = 15;
  public int getNextUniqueId() {
    this.currentUniqueId++;
    if (this.currentUniqueId == 0) {
      this.currentUniqueId |= 0x10;
    }
    return this.currentUniqueId;
  }
}
