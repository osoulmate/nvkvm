package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import java.io.IOException;
import java.io.RandomAccessFile;
public class VolumeRecognitionSequence
{
  private NSRVersion nsrVersion;
  public enum NSRVersion
  {
    NSR02,
    NSR03;
  }
  public VolumeRecognitionSequence(NSRVersion nsrVersion) {
    this.nsrVersion = nsrVersion;
  }
  public void write(RandomAccessFile myRandomAccessFile) throws IOException {
    VolumeStructureDescriptor beginningExtendedAreaDescriptor = new VolumeStructureDescriptor();
    beginningExtendedAreaDescriptor.StructureType = 0;
    beginningExtendedAreaDescriptor.StandardIdentifier = new byte[] { 66, 69, 65, 48, 49 };
    beginningExtendedAreaDescriptor.StructureVersion = 1;
    beginningExtendedAreaDescriptor.write(myRandomAccessFile);
    VolumeStructureDescriptor NSRDescriptor = new VolumeStructureDescriptor();
    NSRDescriptor.StructureType = 0;
    NSRDescriptor.StructureVersion = 1;
    if (this.nsrVersion == NSRVersion.NSR02) {
      NSRDescriptor.StandardIdentifier = new byte[] { 78, 83, 82, 48, 50 };
    }
    else if (this.nsrVersion == NSRVersion.NSR03) {
      NSRDescriptor.StandardIdentifier = new byte[] { 78, 83, 82, 48, 51 };
    } 
    NSRDescriptor.write(myRandomAccessFile);
    VolumeStructureDescriptor terminatingExtendedAreaDescriptor = new VolumeStructureDescriptor();
    terminatingExtendedAreaDescriptor.StructureType = 0;
    terminatingExtendedAreaDescriptor.StandardIdentifier = new byte[] { 84, 69, 65, 48, 49 };
    terminatingExtendedAreaDescriptor.StructureVersion = 1;
    terminatingExtendedAreaDescriptor.write(myRandomAccessFile);
  }
  public byte[] getBytes() {
    VolumeStructureDescriptor beginningExtendedAreaDescriptor = new VolumeStructureDescriptor();
    beginningExtendedAreaDescriptor.StructureType = 0;
    beginningExtendedAreaDescriptor.StandardIdentifier = new byte[] { 66, 69, 65, 48, 49 };
    beginningExtendedAreaDescriptor.StructureVersion = 1;
    byte[] beginningExtendedAreaDescriptorBytes = beginningExtendedAreaDescriptor.getBytes();
    VolumeStructureDescriptor NSRDescriptor = new VolumeStructureDescriptor();
    NSRDescriptor.StructureType = 0;
    NSRDescriptor.StructureVersion = 1;
    if (this.nsrVersion == NSRVersion.NSR02) {
      NSRDescriptor.StandardIdentifier = new byte[] { 78, 83, 82, 48, 50 };
    }
    else if (this.nsrVersion == NSRVersion.NSR03) {
      NSRDescriptor.StandardIdentifier = new byte[] { 78, 83, 82, 48, 51 };
    } 
    byte[] NSRDescriptorBytes = NSRDescriptor.getBytes();
    VolumeStructureDescriptor terminatingExtendedAreaDescriptor = new VolumeStructureDescriptor();
    terminatingExtendedAreaDescriptor.StructureType = 0;
    terminatingExtendedAreaDescriptor.StandardIdentifier = new byte[] { 84, 69, 65, 48, 49 };
    terminatingExtendedAreaDescriptor.StructureVersion = 1;
    byte[] terminatingExtendedAreaDescriptorBytes = terminatingExtendedAreaDescriptor.getBytes();
    byte[] rawBytes = new byte[beginningExtendedAreaDescriptorBytes.length + 
        NSRDescriptorBytes.length + 
        terminatingExtendedAreaDescriptorBytes.length];
    int pos = 0;
    System.arraycopy(beginningExtendedAreaDescriptorBytes, 0, rawBytes, pos, beginningExtendedAreaDescriptorBytes.length);
    pos += beginningExtendedAreaDescriptorBytes.length;
    System.arraycopy(NSRDescriptorBytes, 0, rawBytes, pos, NSRDescriptorBytes.length);
    pos += NSRDescriptorBytes.length;
    System.arraycopy(terminatingExtendedAreaDescriptorBytes, 0, rawBytes, pos, terminatingExtendedAreaDescriptorBytes.length);
    pos += terminatingExtendedAreaDescriptorBytes.length;
    return rawBytes;
  }
}
