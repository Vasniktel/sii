package com.codewars.sii;

class InvalidIdentifierException extends InterpreterException {
  InvalidIdentifierException(String message, Throwable cause) {
    super(message, cause);
  }

  InvalidIdentifierException(String message) {
    super(message);
  }
}
