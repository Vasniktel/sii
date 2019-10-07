package com.codewars.sii;

import java.util.ArrayList;
import java.util.List;

import static com.codewars.sii.Token.Type.*;

final class Parser {
  private final Environment environment;
  private List<Token> tokens;
  private int curr;

  Parser(Environment environment) {
    this.environment = environment;
  }

  Expr parse(List<Token> tokens) {
    this.tokens = tokens;
    this.curr = 0;

    if (tokens == null || isEOF()) {
      return null;
    }

    Expr result;
    if (match(FN)) {
      result = function();
    } else {
      result = sum();
    }

    if (!isEOF()) {
      throw new InvalidSyntaxException("Multiple expressions per line");
    }

    return result;
  }

  private Expr assignment() {
    if (!match(VAR)) {
      throw new InvalidSyntaxException("Identifier expected");
    }

    var identifier = prev();
    advance();
    return new Expr.Assign(identifier.value, sum());
  }

  private Expr call() {
    var name = advance();
    int size = environment.getFunction(name.value).arguments.size();
    var arguments = new ArrayList<Expr>();

    try {
      for (int i = 0; i < size; ++i) {
        arguments.add(sum());
      }
    } catch (InterpreterException e) {
      var msg = String.format("Not enough arguments for function '%s'", name.value);
      throw new InvalidSyntaxException(msg, e);
    }

    return new Expr.Call(name.value, arguments);
  }

  private Expr factor() {
    switch (peek().type) {
      case NUM: return new Expr.Primary(Double.valueOf(advance().value));
      case VAR:
        if (next().type == ASSIGN) {
          return assignment();
        } else if (environment.isFunction(peek().value)) {
          return call();
        } else {
          return new Expr.Primary(advance().value);
        }

      case L_PAREN:
        advance();
        var expr = sum();
        if (!match(R_PAREN)) {
          throw new InvalidSyntaxException("')' expected");
        }

        return expr;

      default:
        throw new InvalidSyntaxException("Unexpected token: " + peek());
    }
  }

  private Expr mult() {
    var expr = factor();

    while (match(MUL, DIV, MOD)) {
      var operator = prev();
      expr = new Expr.Binary(expr, operator, factor());
    }

    return expr;
  }

  private Expr sum() {
    var expr = mult();

    while (match(ADD, SUB)) {
      var operator = prev();
      expr = new Expr.Binary(expr, operator, mult());
    }

    return expr;
  }

  private Expr function() {
    if (!match(VAR)) {
      throw new InvalidSyntaxException("Identifier expected");
    }

    var identifier = prev();
    var args = new ArrayList<String>();

    while (match(VAR)) {
      args.add(prev().value);
    }

    if (!match(LAMBDA)) {
      throw new InvalidSyntaxException("'=>' expected");
    }

    var body = sum();
    return new Expr.Function(identifier.value, args, body);
  }

  private boolean match(Token.Type... types) {
    for (var type : types) {
      if (peek().type == type) {
        curr++;
        return true;
      }
    }

    return false;
  }

  private Token prev() {
    return tokens.get(curr - 1);
  }

  private Token peek() {
    return tokens.get(curr);
  }

  private Token next() {
    return isEOF() ? peek() : tokens.get(curr + 1);
  }

  private Token advance() {
    return isEOF() ? peek() : tokens.get(curr++);
  }

  private boolean isEOF() {
    return peek().type == EOF;
  }
}
