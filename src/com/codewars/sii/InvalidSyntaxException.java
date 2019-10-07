package com.codewars.sii;

class InvalidSyntaxException extends InterpreterException {
  InvalidSyntaxException(String message, Throwable cause) {
    super(message, cause);
  }

  InvalidSyntaxException(String message) {
    super(message);
  }
}
