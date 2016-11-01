
import java.util.Scanner;


public class Main {
    
    public static void main(String args[]) {
        
        Scanner s = new Scanner(System.in);
        
        int N = 0;
        int Sum = 0;
        
        do {
            Sum = Sum + s.nextInt();
            N = N  + 1;
        } while(N < 100);
        
        System.out.println("합 : "  + Sum);
    }
}