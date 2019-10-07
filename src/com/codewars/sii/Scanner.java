package com.codewars.sii;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codewars.sii.Token.Type.*;
import static java.lang.Character.isAlphabetic;
import static java.lang.Character.isDigit;
import static java.lang.Character.isWhitespace;

final class Scanner {
  private static final Map<String, Token> keyWords;

  private String code;
  private List<Token> tokens;
  private int start, curr;

  static {
    keyWords = new HashMap<>();
    keyWords.put("fn", new Token(FN));
  }

  List<Token> tokenize(String code) {
    this.code = code;
    this.tokens = new ArrayList<>();
    this.curr = 0;

    while (!isEOF()) {
      start = curr;
      scanToken();
    }

    tokens.add(new Token(EOF));
    return tokens;
  }

  private void scanToken() {
    char c = advance();
    switch (c) {
      case '*': tokens.add(new Token(MUL)); break;
      case '/': tokens.add(new Token(DIV)); break;
      case '+': tokens.add(new Token(ADD)); break;
      case '-': tokens.add(new Token(SUB)); break;
      case '%': tokens.add(new Token(MOD)); break;
      case '(': tokens.add(new Token(L_PAREN)); break;
      case ')': tokens.add(new Token(R_PAREN)); break;
      case '=': tokens.add(new Token(match('>') ? LAMBDA : ASSIGN)); break;
      case '.':
        if (isDigit(peek())) {
          number(false);
          break;
        } else {
          error(c, start);
        }

      default:
        if (isWhitespace(c)) break;
        if (isAlpha(c)) {
          identifier();
        } else if (isDigit(c)) {
          number(true);
        } else {
          error(c, start);
        }
    }
  }

  private void identifier() {
    while (isAlphanumeric(peek())) advance();
    var value = code.substring(start, curr);
    var token = keyWords.get(value);
    tokens.add(token == null ? new Token(VAR, value) : token);
  }

  private void number(boolean withDot) {
    while (isDigit(peek())) advance();

    if (withDot && peek() == '.' && isDigit(next())) {
      advance();
      while (isDigit(peek())) advance();
    }

    var value = code.substring(start, curr);
    tokens.add(new Token(NUM, value));
  }

  private boolean isAlpha(char c) {
    return isAlphabetic(c) || c == '_';
  }

  private boolean isAlphanumeric(char c) {
    return isAlpha(c) || isDigit(c);
  }

  private char advance() {
    return isEOF() ? '\0' : code.charAt(curr++);
  }

  private boolean isEOF() {
    return curr == code.length();
  }

  private char peek() {
    return isEOF() ? '\0' : code.charAt(curr);
  }

  private char next() {
    if (isEOF() || curr + 1 == code.length()) {
      return '\0';
    }

    return code.charAt(curr + 1);
  }

  private boolean match(char expected) {
    if (peek() != expected) return false;
    curr++;
    return true;
  }

  private static void error(char c, int at) {
    var message = String.format("Unexpected character '%c' at %d", c, at);
    throw new InvalidSyntaxException(message);
  }
}
