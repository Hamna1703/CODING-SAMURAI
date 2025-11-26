import java.util.Scanner;

public class SimpleCalculator {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("1 - Add");
        System.out.println("2 - Subtract");
        System.out.println("3 - Multiply");
        System.out.println("4 - Divide");

        System.out.print("Choose Operator: ");
        int choice = sc.nextInt();

        System.out.print("Enter first number : ");
        double n1 = sc.nextDouble();

        System.out.print("Enter second number : ");
        double n2 = sc.nextDouble();

        double result = 0;

        switch (choice) {
            case 1:
                result = n1 + n2;
                break;

            case 2:
                result = n1 - n2;
                break;

            case 3:
                result = n1 * n2;
                break;

            case 4:
                if (n2 == 0) {
                    System.out.println("Error: Cannot divide by zero");
                    sc.close();
                    return;
                }
                result = n1 / n2;
                break;

            default:
                System.out.println("Entered operator is not valid");
                sc.close();
                return;
        }

        System.out.println("Result is : " + result);
        sc.close();
    }
}

