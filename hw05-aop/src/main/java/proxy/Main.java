package proxy;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = MyProxy.create(new CalculatorImpl());
        calculator.calculation(2343);
        calculator.calculation(4231, 342);
        calculator.calculation(21312, 312, "S");
    }
}
