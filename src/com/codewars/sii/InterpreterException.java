package com.codewars.sii;

class InterpreterException extends RuntimeException {
  InterpreterException(String message, Throwable cause) {
    super(message, cause);
  }

  InterpreterException(String message) {
    super(message);
  }
}
