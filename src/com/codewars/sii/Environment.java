package com.codewars.sii;

import java.util.HashMap;
import java.util.Map;

final class Environment {
  private final Map<String, Expr.Function> functions = new HashMap<>();
  private final Map<String, Double> values = new HashMap<>();

  Double getValue(String name) {
    var result = values.get(name);

    if (result == null) {
      throw new InvalidIdentifierException(name + " is not defined");
    }

    return result;
  }

  Expr.Function getFunction(String name) {
    var result = functions.get(name);

    if (result == null) {
      throw new InvalidIdentifierException(name + " is not defined");
    }

    return result;
  }

  boolean isValue(String name) {
    return values.containsKey(name);
  }

  boolean isFunction(String name) {
    return functions.containsKey(name);
  }

  void putValue(String name, Double value) {
    assert value != null;

    if (isFunction(name)) {
      throw new InvalidIdentifierException(name + " was already defined");
    }

    values.put(name, value);
  }

  void putFunction(String name, Expr.Function function) {
    assert function != null;

    if (isValue(name)) {
      throw new InvalidIdentifierException(name + " was already defined");
    }

    functions.put(name, function);
  }
}
