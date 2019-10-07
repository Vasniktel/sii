package com.codewars.sii;

import java.util.List;

interface Expr {
  Double accept(Executor executor, Environment environment);

  class Binary implements Expr {
    final Expr left, right;
    final Token operator;

    Binary(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    public Double accept(Executor executor, Environment environment) {
      return executor.execute(this, environment);
    }
  }

  class Assign implements Expr {
    final String name;
    final Expr expr;

    Assign(String name, Expr expr) {
      this.name = name;
      this.expr = expr;
    }

    @Override
    public Double accept(Executor executor, Environment environment) {
      return executor.execute(this, environment);
    }
  }

  class Primary implements Expr {
    final Object value;

    Primary(String value) {
      this.value = value;
    }

    Primary(Double value) {
      this.value = value;
    }

    @Override
    public Double accept(Executor executor, Environment environment) {
      return executor.execute(this, environment);
    }
  }

  class Function implements Expr {
    final String name;
    final List<String> arguments;
    final Expr body;

    Function(String name, List<String> arguments, Expr body) {
      this.name = name;
      this.arguments = arguments;
      this.body = body;
    }

    @Override
    public Double accept(Executor executor, Environment environment) {
      return executor.execute(this, environment);
    }
  }

  class Call implements Expr {
    final String name;
    final List<Expr> arguments;

    Call(String name, List<Expr> arguments) {
      this.name = name;
      this.arguments = arguments;
    }

    @Override
    public Double accept(Executor executor, Environment environment) {
      return executor.execute(this, environment);
    }
  }
}
