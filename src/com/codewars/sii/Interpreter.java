package com.codewars.sii;

/**
 * Grammar:
 * function        ::= fn-keyword fn-name { identifier } fn-operator expression
 * fn-name         ::= identifier
 * fn-operator     ::= '=>'
 * fn-keyword      ::= 'fn'
 *
 * expression      ::= factor | expression operator expression
 * factor          ::= number | identifier | assignment | '(' expression ')' | function-call
 * assignment      ::= identifier '=' expression
 * function-call   ::= fn-name { expression }
 *
 * operator        ::= '+' | '-' | '*' | '/' | '%'
 *
 * identifier      ::= letter | '_' { identifier-char }
 * identifier-char ::= '_' | letter | digit
 *
 * number          ::= { digit } [ '.' digit { digit } ]
 *
 * letter          ::= 'a' | 'b' | ... | 'y' | 'z' | 'A' | 'B' | ... | 'Y' | 'Z'
 * digit           ::= '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9'
 */
public final class Interpreter {
  private final Environment environment = new Environment();
  private final Scanner scanner = new Scanner();
  private final Parser parser = new Parser(environment);
  private final Executor executor = new Executor();

  public Double input(String input) {
    var tokens = scanner.tokenize(input);
    var parsed = parser.parse(tokens);
    return executor.execute(parsed, environment);
  }

  public static void main(String[] args) {
    var interpreter = new Interpreter();
    var scanner = new java.util.Scanner(System.in);

    System.out.print("> ");
    while (scanner.hasNextLine()) {
      var line = scanner.nextLine();

      try {
        System.out.println(interpreter.input(line));
      } catch (InterpreterException e) {
        System.out.println(e.getMessage());
      }

      System.out.print("> ");
    }
  }
}

