
import java.io.*;

public class Copy {
  public static void main(String [] args) throws Exception {
    
    int argLength = args.length;

    if(argLength < 2)
        System.err.println("Too few command line arguments. Please try again.");

    else if(argLength > 2)
        System.err.println("Too many command line arguments. Please try again.");
    else{
      FileReader input = new FileReader(args[0]);
      FileWriter output = new FileWriter(args[1]);
      int c, charCnt = 0;
      while ((c = input.read()) != -1) {      // read() returns -1 on EOF
            output.write((char)c);
            output.write("\n");
            charCnt++;
      }
      input.close();
      output.flush();
      output.close();

      System.out.println("Total: " + charCnt + " characters");
  }}
}
