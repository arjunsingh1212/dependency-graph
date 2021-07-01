package com.assignment3.exceptions;

@SuppressWarnings("PMD.CommentRequired")
public class GraphException extends Exception {
  public static final long serialVersionUID = 43287433;

  /**
   * Constructor taking custom message for custom exception.
   */
  public GraphException(final String message) {
    super(message);
  }
}
