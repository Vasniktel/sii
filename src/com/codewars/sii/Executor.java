package com.codewars.sii;

final class Executor {
  Double execute(Expr expr, Environment environment) {
    if (expr == null) {
      return null;
    }

    return expr.accept(this, environment);
  }

  Double execute(Expr.Binary binary, Environment environment) {
    Double left = execute(binary.left, environment);
    Double right = execute(binary.right, environment);

    assert left != null && right != null;

    switch (binary.operator.type) {
      case ADD: return left + right;
      case SUB: return left - right;
      case MUL: return left * right;
      case DIV: return left / right;
      case MOD: return left % right;
    }

    throw new AssertionError("Unreachable");
  }

  Double execute(Expr.Assign assign, Environment environment) {
    String name = assign.name;
    Double value = execute(assign.expr, environment);
    environment.putValue(name, value);
    return value;
  }

  Double execute(Expr.Primary primary, Environment environment) {
    if (primary.value instanceof Double) {
      return (Double) primary.value;
    } else if (primary.value instanceof String) {
      return environment.getValue((String) primary.value);
    }

    throw new AssertionError("Unreachable");
  }

  Double execute(Expr.Function function, Environment environment) {
    var args = new Environment();

    for (var arg : function.arguments) {
      if (args.isValue(arg)) {
        throw new InvalidSyntaxException("Argument was already defined");
      }

      args.putValue(arg, 1.0);
    }

    try {
      execute(function.body, args); // correctness check
    } catch (InterpreterException e) {
      throw new InvalidIdentifierException("Invalid argument name", e);
    }

    environment.putFunction(function.name, function);
    return null;
  }

  Double execute(Expr.Call call, Environment environment) {
    var function = environment.getFunction(call.name);
    var args = new Environment();

    for (int i = 0; i < call.arguments.size(); ++i) {
      Double value = execute(call.arguments.get(i), environment);
      args.putValue(function.arguments.get(i), value);
    }

    return execute(function.body, args);
  }
}
