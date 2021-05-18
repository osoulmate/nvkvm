package de.tu_darmstadt.informatik.rbg.bstickler.udflib.structures;
import de.tu_darmstadt.informatik.rbg.bstickler.udflib.tools.BinaryTools;
import java.io.IOException;
import java.io.RandomAccessFile;
public class ImplementationUseVolumeDescriptor
  extends VolumeDescriptorSequenceItem
{
  public EntityID ImplementationIdentifier;
  public LVInformation ImplementationUse;
  public ImplementationUseVolumeDescriptor() {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.TagIdentifier = 4;
    this.ImplementationIdentifier = new EntityID();
    this.ImplementationUse = new LVInformation();
  }
  public void read(RandomAccessFile myRandomAccessFile) throws IOException {
    this.DescriptorTag = new Tag();
    this.DescriptorTag.read(myRandomAccessFile);
    this.VolumeDescriptorSequenceNumber = BinaryTools.readUInt32AsLong(myRandomAccessFile);
    this.ImplementationIdentifier = new EntityID();
    this.ImplementationIdentifier.read(myRandomAccessFile);
    this.ImplementationUse = new LVInformation();
    this.ImplementationUse.read(myRandomAccessFile);
  }
  public byte[] getBytesWithoutDescriptorTag() {
    byte[] ImplementationIdentifierBytes = this.ImplementationIdentifier.getBytes();
    byte[] ImplementationUseBytes = this.ImplementationUse.getBytes();
    byte[] rawBytes = new byte[4 + 
        ImplementationIdentifierBytes.length + 
        ImplementationUseBytes.length];
    int pos = 0;
    pos = BinaryTools.getUInt32BytesFromLong(this.VolumeDescriptorSequenceNumber, rawBytes, pos);
    System.arraycopy(ImplementationIdentifierBytes, 0, rawBytes, pos, ImplementationIdentifierBytes.length);
    pos += ImplementationIdentifierBytes.length;
    System.arraycopy(ImplementationUseBytes, 0, rawBytes, pos, ImplementationUseBytes.length);
    return rawBytes;
  }
}
