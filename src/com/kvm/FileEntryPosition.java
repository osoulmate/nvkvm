package com.kvm;
public class FileEntryPosition
{
  private long entryBlock;
  private long entryLocation;
  private long dataBlock;
  private long dataLocation = 0L;
  private long uniqueIds;
  public FileEntryPosition(long entryBlock, long entryLocation, long dataBlock, long dataLocation, long uniqueIds) {
    this.entryBlock = entryBlock;
    this.entryLocation = entryLocation;
    this.dataBlock = dataBlock;
    this.dataLocation = dataLocation;
    this.uniqueIds = uniqueIds;
  }
  public FileEntryPosition() {}
  public long getEntryBlock() {
    return this.entryBlock;
  }
  public void setEntryBlock(long entryBlock) {
    this.entryBlock = entryBlock;
  }
  public long getEntryLocation() {
    return this.entryLocation;
  }
  public void setEntryLocation(long entryLocation) {
    this.entryLocation = entryLocation;
  }
  public long getDataBlock() {
    return this.dataBlock;
  }
  public void setDataBlock(long dataBlock) {
    this.dataBlock = dataBlock;
  }
  public long getDataLocation() {
    return this.dataLocation;
  }
  public void setDataLocation(long dataLocation) {
    this.dataLocation = dataLocation;
  }
  public long getUniqueIds() {
    return this.uniqueIds;
  }
  public void setUniqueIds(long uniqueIds) {
    this.uniqueIds = uniqueIds;
  }
}
