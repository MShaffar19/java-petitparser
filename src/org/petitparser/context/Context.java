package org.petitparser.context;

import org.petitparser.buffer.Buffer;

/**
 * Abstract parse context with result type {@code T}.
 *
 * @author Lukas Renggli (renggli@gmail.com)
 */
public class Context<T> {

  private final Buffer buffer;
  private final int position;

  /**
   * Constructs an immutable context with a {@code buffer} and a
   * {@code position}.
   */
  public Context(Buffer buffer, int position) {
    this.buffer = buffer;
    this.position = position;
  }

  /**
   * Returns the input buffer.
   */
  public Buffer getBuffer() {
    return buffer;
  }

  /**
   * Returns the current position.
   */
  public int getPosition() {
    return position;
  }

  /**
   * Tests if the receiver is a successful parse.
   */
  public boolean isSuccess() {
    return false;
  }

  /**
   * Returns the result of this parse context.
   */
  public T get() {
    return null;
  }

  /**
   * Returns a successful parse result.
   */
  public <S> SuccessContext<S> success(S result) {
    return new SuccessContext<S>(getBuffer(), getPosition(), result);
  }

  /**
   * Tests if the receiver is a failure.
   */
  public boolean isFailure() {
    return false;
  }

  /**
   * Returns a parse failure on the receiving context with the provided
   * {@code message}.
   */
  public FailureContext failure(String message) {
    return new FailureContext(getBuffer(), getPosition(), message);
  }

}