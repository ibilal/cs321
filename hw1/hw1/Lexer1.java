//----------------------------------------------------------------------
// A starter version of miniJava lexer (manual version). (For CS321 HW1)
//----------------------------------------------------------------------
//
//  Iman Bilal
//  JavaCC Lexer
//  Homework 1
//  Late Days Used: 1
//
//
import java.io.*;

public class Lexer1 {
  private static FileReader input = null;
  private static int nextC = -1;   // buffer for holding next char	
  private static int line = 1;     // currect line position
  private static int column = 0;   // currect column position
  private static HashMap keywordLookupTable= new HashMap (); 
  // Internal token code
  //
  enum TokenCode {
    // Tokens with multiple lexemes
    ID, INTLIT, DBLLIT, STRLIT,

    // Keywords
    //   "class", "extends", "static", "public", "main", "void", "boolean", 
    //   "int", "double", "String", "true", "false", "new", "this", "if", 
    //   "else", "while", "return", "System", "out", "println"
    CLASS, EXTENDS, STATIC, PUBLIC, MAIN, VOID, BOOLEAN, INT, DOUBLE, STRING, 
    TRUE, FALSE, NEW, THIS, IF, ELSE, WHILE, RETURN, SYSTEM, OUT, PRINTLN,

    // Operators and delimiters
    //   +, -, *, /, &&, ||, !, ==, !=, <, <=, >, >=, =, 
    //   ;, ,, ., (, ), [, ], {, }
    ADD, SUB, MUL, DIV, AND, OR, NOT, EQ, NE, LT, LE, GT, GE,  ASSGN,
    SEMI, COMMA, DOT, LPAREN, RPAREN, LBRAC, RBRAC, LCURLY, RCURLY;
  }

  // Token representation
  //
  static class Token {
    TokenCode code;
    String lexeme;
    int line;	   	// line # of token's first char
    int column;    	// column # of token's first char
    
    public Token(TokenCode code, String lexeme, int line, int column) {
      this.code=code; this.lexeme=lexeme;
      this.line=line; this.column=column; 
    }

    public String toString() {
      return String.format("(%d,%2d) %-10s %s", line, column, code, 
			   (code==TokenCode.STRLIT)? "\""+lexeme+"\"" : lexeme);
    }
  }

  static void init(FileReader in) throws Exception { 
    input = in; 
    nextC = input.read();
  }

  //--------------------------------------------------------------------
  // Do not modify the code listed above. Add your code below. 
  //

  // Return next char
  //
  // - need to track both line and column numbers
  // 
  private static int nextChar() throws Exception {
    int c = nextC;
    nextC = input.read();
    if(c == '\n'){
        line++;
        column = 0;
    }
   // else if(c == '\r'){
    //    line++;
     //   column =0;
//        System.out.println("BLERB 78");
//    }
    else if(c != -1)
        ++column;
    return c;
  }

  // Return next token (the main lexer routine)
  //
  // - need to capture the line and column numbers of the first char 
  //   of each token
  //
  static Token nextToken() throws Exception {
    int c = nextChar();
    while (isSpace(c))
      c = nextChar();
     
     //Check if the nextChar is newLine or EOF flag
    if(c == '\n')
        return nextToken();
    if(c == '\r')
        return nextToken();
    if(c == -1)
        return null;


    //Checking if the nextToken is a string literal
    if (isLetter(c)){
      StringBuilder buffer = new StringBuilder(); 
      buffer.append((char) c);
    while (isLetter(nextC) || isDigit(nextC)) {
	c = nextChar();
	buffer.append((char) c);
      }
      String lex = buffer.toString();
      TokenCode keyword = keywordLookupTable.get(lex.hashCode());
      if(keyword != null)
          return new Token(keyword, lex, line, (column - lex.length() + 1));
      return new Token(TokenCode.ID, lex, line, (column - lex.length() + 1));
    }

    //Check Division operator/comments (BLOCK NOT YET IMPLEMENTED
    if (c == '/') {
      StringBuilder buffer = new StringBuilder(); 
      // recognize single-line comments
      if (nextC == '/') {
	do {
	  buffer.append((char) c);
	  c = nextChar();
	} while (c != '\n' && c != -1);
	return nextToken();
      }
      else if(nextC == '*'){
            int rightLine = line;
            int rightColumn = column;
            while(c != -1){
                if(c == '*' && nextC == '/')
                {
                    c = nextChar();
                    return nextToken();
                }
                c = nextChar();
            }
            throw new LexError("at (" + rightLine + "," + rightColumn + "). Unclosed block comments");
      }
      else
          return new Token(TokenCode.DIV, "/", line, column);
    }

    //Check all Operators and Delimiters (Except for DOT delimiter)
    switch (c) {
    case '+': 
      return new Token(TokenCode.ADD, "+", line, column);
    case '-': 
      return new Token(TokenCode.SUB, "-", line, column);
    case '*': 
      return new Token(TokenCode.MUL, "*", line, column);
    case ';': 
      return new Token(TokenCode.SEMI, ";", line, column);
    case ',': 
      return new Token(TokenCode.COMMA, ",", line, column);
    case '(': 
      return new Token(TokenCode.LPAREN, "(", line, column);
    case ')': 
      return new Token(TokenCode.RPAREN, ")", line, column);
    case '[': 
      return new Token(TokenCode.LBRAC, "[", line, column);
    case ']': 
      return new Token(TokenCode.RBRAC, "]", line, column);
    case '{': 
      return new Token(TokenCode.LCURLY, "{", line, column);
    case '}': 
      return new Token(TokenCode.RCURLY, "}", line, column);
    case '=':  
      if (nextC == '=') {
	c = nextChar();
	return new Token(TokenCode.EQ, "==", line, column -1);
      }
      else
        return new Token(TokenCode.ASSGN, "=", line, column);
    case '<':  
      if (nextC == '=') {
	c = nextChar();
	return new Token(TokenCode.LE, "<=", line, column -1);
      }
      else
        return new Token(TokenCode.LT, "<", line, column);

    case '>':  
      if (nextC == '=') {
	c = nextChar();
	return new Token(TokenCode.GE, ">=", line, column -1);
      }
      else
          return new Token(TokenCode.GT, ">", line, column);
    case '|':  
      if (nextC == '|') {
	c = nextChar();
	return new Token(TokenCode.OR, "||", line, column -1);
      }
    case '&':  
      if (nextC == '&') {
	c = nextChar();
	return new Token(TokenCode.AND, "&&", line, column -1);
      }
    case '!':  
      if (nextC == '=') {
	c = nextChar();
	return new Token(TokenCode.NE, "!=", line, column -1);
      }
      else
        return new Token(TokenCode.NOT, "!", line, column);
    }

    //Check StringLiteral by checking " " marks
    if (c == '"'){
        int rightLine = line;
        int rightColumn = column;
        c = nextChar();
        StringBuilder buffer = new StringBuilder(); 
        buffer.append((char) c);
        while (c != '"' && c != -1 && c != '\r'){
            c = nextChar();
            buffer.append((char) c);
        }
        String lex = buffer.toString();
        if(lex.substring(lex.length() -1, lex.length()).equals("\"") && lex.length() == 1)
            return new Token(TokenCode.STRLIT, "", line, (column - lex.length()));
        if(lex.substring(lex.length() -1, lex.length()).equals("\""))
        {
            String [] correct = lex.split("\"");
            return new Token(TokenCode.STRLIT, correct[0], line, (column - correct[0].length()-1));
        }

        if(lex.contains("\r")){
            String [] parts = lex.split("\r");
            throw new LexError("at (" + rightLine + "," + rightColumn + "). Ill-formed or unclosed string: " + "\"" + parts[0]);
        }
        if(c != '"'){
            if(lex.substring(lex.length()-2, lex.length()-1).equals("\n") || lex.substring(lex.length()-2, lex.length()-1).equals("\r"))
             throw new LexError("at (" + rightLine + "," + rightColumn + "). Ill-formed or unclosed string: " + "\"" + lex.substring(0, lex.length()-2)); 
        } 
        return new Token(TokenCode.STRLIT, lex, line, (column - lex.length()));
    }

    //Check the DOT operator including possibility for DBLLIT token
    if(c == '.')
    {
        if(isDigit(nextC)){
            int correctColumn = column;
            StringBuilder buffer = new StringBuilder(); 
            buffer.append((char) c);
            while(isDigit(nextC))
            {
                c = nextChar();
                buffer.append((char) c);
            }
            String lex = buffer.toString();
            return new Token(TokenCode.DBLLIT, lex, line, correctColumn); 
        }
        else
            return new Token(TokenCode.DOT, ".", line, column);
    }

    //Check for INTLIT (Hex, Octal, Integer) and possibly DBLLIT
    if (isDigit(c)) {

        //If it begins with a 0
        if(c == '0'){
              StringBuilder buffer = new StringBuilder(); 
              buffer.append((char) c);
                int correctLine = line;
                int correctColumn = column;
              //Check if the seqence is a Hexidecimal INTLIT
              if(nextC == 'x' || nextC == 'X')
              {
                    c = nextChar();
                    buffer.append((char) c);
                    while(isDigit(nextC) || isHexLetter(nextC))
                    {
                        c = nextChar();
                        buffer.append((char) c);
                    }
                    String lex = buffer.toString();
                    try {
                     // Integer.parseInt(String.valueOf(Integer.decode(lex)), 16); 
                      Integer.parseInt(String.valueOf((lex).substring(2)), 16); 
                      return new Token(TokenCode.INTLIT, lex, line, (column - lex.length() +1));
                    }
                    catch (Exception e) { 
                    throw new LexError("at (" + line + "," + correctColumn + "). Invalid hexadecimal literal: " + lex);
                    }
              }

              //Check if the sequence begins with 0 and is a DBLLIT or OCTAL
              else if(nextC == '.' || isDigit(nextC)){
                  while (isDigit(nextC) || nextC == '.'){
                    c = nextChar();
                    buffer.append((char) c);
                  }
                  String lex = buffer.toString();

                  //Check if the number is a DBLLIT
                  if(lex.contains("."))
                      return new Token(TokenCode.DBLLIT, lex, line, correctColumn);

                  //Check if it is octal (Begins with 0)
                  try {
                    Integer.parseInt(lex, 8); 
                    return new Token(TokenCode.INTLIT, lex, correctLine, correctColumn);
                  }
                  catch (Exception e) { 
                    throw new LexError("at (" + correctLine + "," + correctColumn + "). Invalid octal literal: " + lex);
                      }
              }
              else
              //Check if c is simply 0
                  return new Token(TokenCode.INTLIT, "0", line, column);
        }
        else{
              StringBuilder buffer = new StringBuilder(); 
              buffer.append((char)c);
              int correctColumn = column;
              while (isDigit(nextC) || nextC == '.'){
                  c = nextChar();
                  buffer.append((char) c);
              }
              String lex = buffer.toString();

              //Check if the sequence is a DBLLIT
              if(lex.contains("."))
                  return new Token(TokenCode.DBLLIT, lex, line, correctColumn);
              
              //Check if it is an INTLIT
              try {
                    Integer.parseInt(lex); 
                    return new Token(TokenCode.INTLIT, lex, line, (column - lex.length() + 1));
              }
              catch (Exception e) { 
                   throw new LexError("at (" + line + "," + ((column - lex.length()) + 1)  + "). Invalid decimal literal: " + lex);
              }
        }
    }
    else
        throw new LexError("at (" + line + "," + column + "). Illegal character: " + (char) c); 
  }

  //Helper class of customized error messages and/or exceptions
  static class LexError extends Exception{
    LexError(String error){
        super(error);
    }
  }

  //Helper routines to Identify types of stream character
  static boolean isSpace(int c) {
    return (c == ' ' || c == '\t' || c == '\r');
  }

  public static boolean isLetter(int c) {
    return (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z'));
  }

  public static boolean isHexLetter(int c) {
    return (('a' <= c && c <= 'f') || ('A' <= c && c <= 'F'));
  }

  static boolean isDigit(int c){
    return ('0' <= c && c <= '9');
  }

  static class HashTableChain{
    private int key;
    private TokenCode value; 
    private HashTableChain next;

    HashTableChain(int aKey, TokenCode aValue){
       key = aKey;
       value = aValue;
       next = null;
    }
    public TokenCode getValue(){
        return value;
    }
    public void setValue(TokenCode aValue){
        value = aValue;
    }
    public int getKey(){
        return key;
    }
    public HashTableChain getNext(){
        return next;
    }
    public void setNext(HashTableChain aNext){
        next = aNext;
    }
  }

  static class HashMap{
    private final static int TABLE_SIZE = 23;
    HashTableChain[] table;

    HashMap(){
        table = new HashTableChain[TABLE_SIZE];
        for(int i = 0; i < TABLE_SIZE; ++i)
            table[i] = null;
        fillTable();
    }
    public TokenCode get(int key){
        int hash = Math.abs(key % TABLE_SIZE);
        if(table[hash] == null)
            return null;
        else
        {
            HashTableChain entry = table[hash];
            while(entry != null && entry.getKey() != key)
                entry = entry.getNext();
            if(entry == null)
                return null;
            else 
                return entry.getValue();
        }
    }
    public void put(int key, TokenCode value){
        int hash = Math.abs(key % TABLE_SIZE);
        if(table[hash] == null)
            table[hash] = new HashTableChain(key, value);
        else
        {
            HashTableChain entry = table[hash];
            while(entry.getNext() != null && entry.getKey() != key)
                entry = entry.getNext();
            if(entry.getKey() == key)
                entry.setValue(value);
            else
                entry.setNext(new HashTableChain(key, value));
        }
    }
    public void remove (int key){
        int hash = Math.abs(key % TABLE_SIZE);
        if(table[hash] != null){
            HashTableChain prevEntry = null;
            HashTableChain entry = table[hash];
            while(entry.getNext() != null && entry.getKey() != key){
                prevEntry = entry;
                entry = entry.getNext(); 
            }
            if(entry.getKey() == key)
            {
                if(prevEntry == null)
                    table[hash] = entry.getNext();
                else
                    prevEntry.setNext(entry.getNext());
            }
        }
    }
    public void fillTable(){
        put("class".hashCode(), TokenCode.CLASS);
        put("extends".hashCode(), TokenCode.EXTENDS);
        put("static".hashCode(), TokenCode.STATIC);
        put("public".hashCode(), TokenCode.PUBLIC);
        put("main".hashCode(), TokenCode.MAIN);
        put("void".hashCode(), TokenCode.VOID);
        put("int".hashCode(), TokenCode.INT);
        put("double".hashCode(), TokenCode.DOUBLE);
        put("String".hashCode(), TokenCode.STRING);
        put("true".hashCode(), TokenCode.TRUE);
        put("false".hashCode(), TokenCode.FALSE);
        put("new".hashCode(), TokenCode.NEW);
        put("this".hashCode(), TokenCode.THIS);
        put("if".hashCode(), TokenCode.IF);
        put("else".hashCode(), TokenCode.ELSE);
        put("while".hashCode(), TokenCode.WHILE);
        put("return".hashCode(), TokenCode.RETURN);
        put("System".hashCode(), TokenCode.SYSTEM);
        put("out".hashCode(), TokenCode.OUT);
        put("println".hashCode(), TokenCode.PRINTLN);
        put("boolean".hashCode(), TokenCode.BOOLEAN);
    }
  }
}
