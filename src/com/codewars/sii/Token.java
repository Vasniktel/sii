package com.codewars.sii;

final class Token {
  final Type type;
  final String value;

  Token(Type type, String value) {
    this.type = type;
    this.value = value;
  }

  Token(Type type) {
    this(type, null);
  }

  @Override
  public String toString() {
    return type.toString();
  }

  enum Type {
    FN,
    VAR,
    NUM,

    LAMBDA,
    MUL,
    DIV,
    ADD,
    SUB,
    MOD,
    ASSIGN,

    L_PAREN,
    R_PAREN,

    EOF
  }
}
