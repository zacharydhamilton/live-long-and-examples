/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.github.zacharydhamilton.events;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class PeanutButter extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 4391297911870437223L;


  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PeanutButter\",\"namespace\":\"com.github.zacharydhamilton.events\",\"fields\":[{\"name\":\"smooth\",\"type\":\"boolean\",\"doc\":\"Is this smooth or not. Not implies chunky\"},{\"name\":\"organic\",\"type\":\"boolean\",\"doc\":\"Is this organic or not.\"}],\"version\":\"1\"}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static final SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<PeanutButter> ENCODER =
      new BinaryMessageEncoder<PeanutButter>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<PeanutButter> DECODER =
      new BinaryMessageDecoder<PeanutButter>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<PeanutButter> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<PeanutButter> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<PeanutButter> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<PeanutButter>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this PeanutButter to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a PeanutButter from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a PeanutButter instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static PeanutButter fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  /** Is this smooth or not. Not implies chunky */
  private boolean smooth;
  /** Is this organic or not. */
  private boolean organic;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public PeanutButter() {}

  /**
   * All-args constructor.
   * @param smooth Is this smooth or not. Not implies chunky
   * @param organic Is this organic or not.
   */
  public PeanutButter(java.lang.Boolean smooth, java.lang.Boolean organic) {
    this.smooth = smooth;
    this.organic = organic;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return smooth;
    case 1: return organic;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: smooth = (java.lang.Boolean)value$; break;
    case 1: organic = (java.lang.Boolean)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'smooth' field.
   * @return Is this smooth or not. Not implies chunky
   */
  public boolean getSmooth() {
    return smooth;
  }


  /**
   * Sets the value of the 'smooth' field.
   * Is this smooth or not. Not implies chunky
   * @param value the value to set.
   */
  public void setSmooth(boolean value) {
    this.smooth = value;
  }

  /**
   * Gets the value of the 'organic' field.
   * @return Is this organic or not.
   */
  public boolean getOrganic() {
    return organic;
  }


  /**
   * Sets the value of the 'organic' field.
   * Is this organic or not.
   * @param value the value to set.
   */
  public void setOrganic(boolean value) {
    this.organic = value;
  }

  /**
   * Creates a new PeanutButter RecordBuilder.
   * @return A new PeanutButter RecordBuilder
   */
  public static com.github.zacharydhamilton.events.PeanutButter.Builder newBuilder() {
    return new com.github.zacharydhamilton.events.PeanutButter.Builder();
  }

  /**
   * Creates a new PeanutButter RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new PeanutButter RecordBuilder
   */
  public static com.github.zacharydhamilton.events.PeanutButter.Builder newBuilder(com.github.zacharydhamilton.events.PeanutButter.Builder other) {
    if (other == null) {
      return new com.github.zacharydhamilton.events.PeanutButter.Builder();
    } else {
      return new com.github.zacharydhamilton.events.PeanutButter.Builder(other);
    }
  }

  /**
   * Creates a new PeanutButter RecordBuilder by copying an existing PeanutButter instance.
   * @param other The existing instance to copy.
   * @return A new PeanutButter RecordBuilder
   */
  public static com.github.zacharydhamilton.events.PeanutButter.Builder newBuilder(com.github.zacharydhamilton.events.PeanutButter other) {
    if (other == null) {
      return new com.github.zacharydhamilton.events.PeanutButter.Builder();
    } else {
      return new com.github.zacharydhamilton.events.PeanutButter.Builder(other);
    }
  }

  /**
   * RecordBuilder for PeanutButter instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PeanutButter>
    implements org.apache.avro.data.RecordBuilder<PeanutButter> {

    /** Is this smooth or not. Not implies chunky */
    private boolean smooth;
    /** Is this organic or not. */
    private boolean organic;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$, MODEL$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.github.zacharydhamilton.events.PeanutButter.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.smooth)) {
        this.smooth = data().deepCopy(fields()[0].schema(), other.smooth);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.organic)) {
        this.organic = data().deepCopy(fields()[1].schema(), other.organic);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing PeanutButter instance
     * @param other The existing instance to copy.
     */
    private Builder(com.github.zacharydhamilton.events.PeanutButter other) {
      super(SCHEMA$, MODEL$);
      if (isValidValue(fields()[0], other.smooth)) {
        this.smooth = data().deepCopy(fields()[0].schema(), other.smooth);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.organic)) {
        this.organic = data().deepCopy(fields()[1].schema(), other.organic);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'smooth' field.
      * Is this smooth or not. Not implies chunky
      * @return The value.
      */
    public boolean getSmooth() {
      return smooth;
    }


    /**
      * Sets the value of the 'smooth' field.
      * Is this smooth or not. Not implies chunky
      * @param value The value of 'smooth'.
      * @return This builder.
      */
    public com.github.zacharydhamilton.events.PeanutButter.Builder setSmooth(boolean value) {
      validate(fields()[0], value);
      this.smooth = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'smooth' field has been set.
      * Is this smooth or not. Not implies chunky
      * @return True if the 'smooth' field has been set, false otherwise.
      */
    public boolean hasSmooth() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'smooth' field.
      * Is this smooth or not. Not implies chunky
      * @return This builder.
      */
    public com.github.zacharydhamilton.events.PeanutButter.Builder clearSmooth() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'organic' field.
      * Is this organic or not.
      * @return The value.
      */
    public boolean getOrganic() {
      return organic;
    }


    /**
      * Sets the value of the 'organic' field.
      * Is this organic or not.
      * @param value The value of 'organic'.
      * @return This builder.
      */
    public com.github.zacharydhamilton.events.PeanutButter.Builder setOrganic(boolean value) {
      validate(fields()[1], value);
      this.organic = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'organic' field has been set.
      * Is this organic or not.
      * @return True if the 'organic' field has been set, false otherwise.
      */
    public boolean hasOrganic() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'organic' field.
      * Is this organic or not.
      * @return This builder.
      */
    public com.github.zacharydhamilton.events.PeanutButter.Builder clearOrganic() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PeanutButter build() {
      try {
        PeanutButter record = new PeanutButter();
        record.smooth = fieldSetFlags()[0] ? this.smooth : (java.lang.Boolean) defaultValue(fields()[0]);
        record.organic = fieldSetFlags()[1] ? this.organic : (java.lang.Boolean) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<PeanutButter>
    WRITER$ = (org.apache.avro.io.DatumWriter<PeanutButter>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<PeanutButter>
    READER$ = (org.apache.avro.io.DatumReader<PeanutButter>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeBoolean(this.smooth);

    out.writeBoolean(this.organic);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.smooth = in.readBoolean();

      this.organic = in.readBoolean();

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.smooth = in.readBoolean();
          break;

        case 1:
          this.organic = in.readBoolean();
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










