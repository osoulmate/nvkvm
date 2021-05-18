package de.tu_darmstadt.informatik.rbg.bstickler.udflib;
import de.tu_darmstadt.informatik.rbg.mhartle.sabre.Element;
public class SabreUDFElement
  extends Element
{
  private UDFElementType udfElementType;
  public enum UDFElementType
  {
    EmptyArea,
    ReservedArea,
    VolumeRecognitionSequence,
    AnchorVolumeDescriptorPointer,
    PrimaryVolumeDescriptor,
    LogicalVolumeDescriptor,
    PartitionDescriptor,
    ImplementationUseVolumeDescriptor,
    UnallocatedSpaceDescriptor,
    TerminatingDescriptor,
    FileSetDescriptor,
    LogicalVolumeIntegrityDescriptor,
    FileEntry,
    RawFileData,
    MetadataFile,
    DescriptorTag;
  }
  public SabreUDFElement(UDFElementType udfElementType) {
    this.udfElementType = udfElementType;
  }
  public Object getId() {
    return this.udfElementType;
  }
}
