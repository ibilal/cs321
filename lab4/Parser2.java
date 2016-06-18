// This is supporting software for CS321/CS322 Compilers and Language Design.
// Copyright (c) Portland State University.
//---------------------------------------------------------------------------
// For CS321 F'15 (J. Li).
//

// An LL(1) parser for
//
// Grammar1:
//  0. Program0 -> Program $
//  1. Program  -> begin StmtList end
//  2. StmtList -> Stmt ; StmtList
//  3. StmtList -> 
//  4. Stmt     -> simple
//  5. Stmt     -> begin StmtList end
//

import java.util.Scanner;
import java.io.*;

class Parser2 {

  enum TokenCode { ID, PLUS, STAR, END, EOF; }

  // Lexer 
  // - use Java's Scanner for lexing
  // - assume adjacent tokens are separated by white space
  // - only token code is returned; lexeme is not kept
  //
  static Scanner scanner;	
  static TokenCode tknCode;

  // Read from input and return next token's code
  //
  static TokenCode nextToken() throws Exception {
    String lexeme = null;
    if (scanner.hasNext()) {
      lexeme = scanner.next();
      if(lexeme.matches("[a-z]+"))
          return TokenCode.ID;
      if (lexeme.equals("*")) 
	return TokenCode.STAR;
      if (lexeme.equals("$")) 
	return TokenCode.END;
      if (lexeme.equals("+")) 
	return TokenCode.PLUS;
      throw new Exception("Illegal token: " + lexeme);
    }
    return TokenCode.EOF;
  }

  // Match a token and move input pointer to the next token
  //
  static void match(TokenCode code) throws Exception {
    if (tknCode == code)
      tknCode = nextToken();
    else
      throw new Exception("Token mismatch: expected " + code + " got " + tknCode); 
  }

  // 0. Program0 -> Program $ 
  // (The main method implements the augmented production.)
  //
  public static void main(String [] args) throws Exception {
    scanner = new Scanner(System.in);
    tknCode = nextToken();
    E(); 
    match(TokenCode.EOF);
    System.out.println("Syntax verified.");
  }

  static void E()throws Exception{if(tknCode == TokenCode.ID){
                        T();
                        E1(); 
                    }
                    else
                        throw new Exception("Token mismatch: got " + tknCode);
  }
  static void E1()throws Exception{if(tknCode == TokenCode.PLUS){
                                        match(TokenCode.PLUS);
                                        T();
                                        E1();
                                    }
                                    else if(tknCode == TokenCode.END)
                                        return;
                                    else if(tknCode == TokenCode.EOF)
                                        return;
                                    else
                                        throw new Exception("Token mismatch: got " + tknCode);
  
  }
  static void T()throws Exception{if(tknCode == TokenCode.ID){
                                        P();
                                        T1();
  }
                                    else
                                        throw new Exception("Token mismatch: got " + tknCode);
  }
  static void T1()throws Exception{if(tknCode == TokenCode.PLUS)
                                        return;
                                    else if(tknCode == TokenCode.STAR){
                                        match(TokenCode.STAR);
                                        P();
                                        T1();
                                    }
                                    else if(tknCode == TokenCode.EOF)
                                        return;
                                    else
                                        throw new Exception("Token mismatch: got " + tknCode);
  
  }
  static void P()throws Exception{if(tknCode == TokenCode.ID)
                                        match(TokenCode.ID);
                                    else
                                        throw new Exception("Token mismatch: got " + tknCode);
  }
}

