package com.streamx.cli.interpolation;

import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import java.io.*;

public class InterpolatingYamlFactory extends YAMLFactory {

  private final InterpolationSupport interpolationSupport;

  public InterpolatingYamlFactory(InterpolationSupport interpolationSupport) {
    this.interpolationSupport = interpolationSupport;
  }

  @Override
  protected YAMLParser _createParser(InputStream in, IOContext context) throws IOException {
    System.out.println("Creating parser from InputStream - START");
    try {
      // Read the entire stream
      System.out.println("Reading input stream...");
      String content = new String(in.readAllBytes());
      System.out.println("Content read, length: " + content.length());
      System.out.println("First 100 chars: " + content.substring(0, Math.min(100, content.length())));

      // Interpolate
      System.out.println("Starting interpolation...");
      String interpolated = interpolationSupport.expand(content);
      System.out.println("Interpolation complete, length: " + interpolated.length());

      // Create stream
      System.out.println("Creating interpolated stream...");
      InputStream interpolatedStream = new ByteArrayInputStream(interpolated.getBytes());

      // Call parent
      System.out.println("Calling super._createParser...");
      YAMLParser parser = super._createParser(interpolatedStream, context);
      System.out.println("Parser created successfully");
      return parser;
    } catch (Exception e) {
      System.err.println("Exception in _createParser: " + e.getClass().getName());
      System.err.println("Message: " + e.getMessage());
      e.printStackTrace(System.err);
      throw e;
    }
  }

  @Override
  protected YAMLParser _createParser(Reader r, IOContext context) throws IOException {
    System.out.println("Creating parser from Reader");
    StringBuilder sb = new StringBuilder();
    char[] buffer = new char[8192];
    int read;
    while ((read = r.read(buffer)) != -1) {
      sb.append(buffer, 0, read);
    }
    String interpolated = interpolationSupport.expand(sb.toString());
    return super._createParser(new StringReader(interpolated), context);
  }

  @Override
  protected YAMLParser _createParser(char[] data, int offset, int len, IOContext context,
                                     boolean recyclable) throws IOException {
    System.out.println("Creating parser from char array");
    String content = new String(data, offset, len);
    String interpolated = interpolationSupport.expand(content);
    return super._createParser(interpolated.toCharArray(), 0, interpolated.length(), context, recyclable);
  }

  @Override
  protected YAMLParser _createParser(byte[] data, int offset, int len, IOContext context)
      throws IOException {
    System.out.println("Creating parser from byte array");
    String content = new String(data, offset, len);
    String interpolated = interpolationSupport.expand(content);
    byte[] interpolatedBytes = interpolated.getBytes();
    return super._createParser(interpolatedBytes, 0, interpolatedBytes.length, context);
  }
}