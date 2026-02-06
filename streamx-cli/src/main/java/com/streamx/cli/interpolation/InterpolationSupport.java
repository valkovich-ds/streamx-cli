package com.streamx.cli.interpolation;

import static io.smallrye.common.expression.Expression.Flag;

import com.streamx.cli.framework.CliException;
import io.smallrye.common.expression.Expression;
import jakarta.enterprise.context.Dependent;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

@Dependent
public class InterpolationSupport {

  String expand(final String rawValue) {
    System.out.println("expand() START");
    System.out.flush(); // Force output

    try {
      System.out.println("expand() - checking null");
      System.out.flush();

      if (rawValue == null) {
        throw new IllegalArgumentException("Expression cannot be null");
      }

      System.out.println("expand() - checking for $");
      System.out.flush();

      // Avoid extra StringBuilder allocations from Expression
      if (rawValue.indexOf('$') == -1) {
        System.out.println("No $ found, returning as-is");
        System.out.flush();
        return rawValue;
      }

      System.out.println("Found $ character");
      System.out.flush();

      System.out.println("Getting ConfigProviderResolver...");
      System.out.flush();

      ConfigProviderResolver resolver = ConfigProviderResolver.instance();

      System.out.println("Got resolver, getting config...");
      System.out.flush();

      final Config config = resolver.getConfig();

      System.out.println("Got config");
      System.out.flush();

      System.out.println("Escaping dollars...");
      System.out.flush();

      String escaped = escapeDollarIfExists(rawValue);

      System.out.println("Compiling expression...");
      System.out.flush();

      final Expression expression = Expression.compile(
          escaped,
          Flag.LENIENT_SYNTAX,
          Flag.NO_TRIM,
          Flag.NO_SMART_BRACES
      );

      System.out.println("Expression compiled");
      System.out.flush();

      System.out.println("Evaluating...");
      System.out.flush();

      String result = expression.evaluate((resolveContext, stringBuilder) -> {
        System.out.println("Resolving key: " + resolveContext.getKey());
        System.out.flush();

        Optional<String> resolve = config.getOptionalValue(resolveContext.getKey(), String.class);

        if (resolve.isEmpty()) {
          String systemProperty = System.getProperty(resolveContext.getKey());
          if (systemProperty != null) {
            resolve = Optional.of(systemProperty);
          }
        }

        if (resolve.isPresent()) {
          String value = resolve.get();
          if (value.indexOf('$') != -1) {
            value = expand(value);
          }
          stringBuilder.append(value);
        } else if (resolveContext.hasDefault()) {
          resolveContext.expandDefault();
        } else {
          throw new CliException("Could not expand value: " + resolveContext.getKey());
        }
      });

      System.out.println("Evaluation complete");
      System.out.flush();

      return result;

    } catch (Exception e) {
      System.err.println("Exception: " + e.getClass().getName() + ": " + e.getMessage());
      e.printStackTrace(System.err);
      System.err.flush();
      throw e;
    }
  }

  private String escapeDollarIfExists(final String value) {
    int index = value.indexOf("\\$");
    if (index != -1) {
      int start = 0;
      StringBuilder builder = new StringBuilder();
      while (index != -1) {
        builder.append(value, start, index).append("$$");
        start = index + 2;
        index = value.indexOf("\\$", start);
      }
      builder.append(value.substring(start));
      return builder.toString();
    }
    return value;
  }
}