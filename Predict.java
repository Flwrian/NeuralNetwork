import java.util.Scanner;

public class Predict extends NeuralNetwork{

    public Predict(int i, int h, int o) {
        super(i, h, o);
        //TODO Auto-generated constructor stub
    }

    private boolean isCross(double[] input) {
        if (input[0] == 1 && input[1] == 0 && input[2] == 1 && input[3] == 0 && input[4] == 1 && input[5] == 0 && input[6] == 1 && input[7] == 0 && input[8] == 1) {
            return true;
        }
        return false;
    }

    private boolean isPlus(double[] input){
        if (input[0] == 0 && input[1] == 1 && input[2] == 0 && input[3] == 1 && input[4] == 1 && input[5] == 1 && input[6] == 0 && input[7] == 1 && input[8] == 0) {
            return true;
        }
        return false;
    }

    private boolean isSquare(double[] input){
        if (input[0] == 1 && input[1] == 1 && input[2] == 1 && input[3] == 1 && input[4] == 0 && input[5] == 1 && input[6] == 1 && input[7] == 1 && input[8] == 1) {
            return true;
        }
        return false;
    }

    private boolean isH(double[] input){
        if (input[0] == 1 && input[1] == 0 && input[2] == 1 && input[3] == 1 && input[4] == 1 && input[5] == 1 && input[6] == 1 && input[7] == 0 && input[8] == 1) {
            return true;
        }
        return false;
    }

    private boolean isU(double[] input){
        if (input[0] == 1 && input[1] == 1 && input[2] == 1 && input[3] == 1 && input[4] == 0 && input[5] == 1 && input[6] == 1 && input[7] == 0 && input[8] == 1) {
            return true;
        }
        return false;
    }

    private boolean isC(double[] input){
        if (input[0] == 1 && input[1] == 1 && input[2] == 1 && input[3] == 1 && input[4] == 0 && input[5] == 0 && input[6] == 1 && input[7] == 1 && input[8] == 1) {
            return true;
        }
        return false;
    }

    @Override
    public double[] condition(double[] input) {
        // Recognize shapes and letters.

        // Shapes
        if (isCross(input)) {
            return new double[]{0,0,0,0,0,0,0,0,0,1};
        }
        if (isPlus(input)) {
            return new double[]{0,0,0,0,0,0,0,0,1,0};
        }
        if (isSquare(input)) {
            return new double[]{0,0,0,0,0,0,0,1,0,0};
        }

        // Letters
        if (isH(input)) {
            return new double[]{1,1,1,1,1,1,1,1,1,0};
        }
        if (isU(input)) {
            return new double[]{1,1,1,1,1,1,1,1,0,1};
        }
        if (isC(input)) {
            return new double[]{1,1,1,1,1,1,1,0,1,1};
        }

        return new double[]{0,0,0,0,0,0,0,0,0,0};



    }

    public String interpretation(double[] input) {
        if (isCross(input)) {
            return "Cross";
        }
        if (isPlus(input)) {
            return "Plus";
        }
        if (isSquare(input)) {
            return "Square";
        }
        if (isH(input)) {
            return "H";
        }
        if (isU(input)) {
            return "U";
        }
        if (isC(input)) {
            return "C";
        }
        return "Unknown";
    }
    
    public static void main(String[] args) {
        // load the model
        Predict nn = new Predict(9, 20, 3);
        NeuralNetwork.load("nn.ser");

        // Let the user input a shape
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a shape: ");
        String input = scanner.nextLine();
        scanner.close();
        
        // Convert the input to a double array
        double[] x = new double[input.length()];
        for (int i = 0; i < input.length(); i++) {
            x[i] = input.charAt(i) == '1' ? 1 : 0;
        }

        // Predict the shape
        System.out.println(nn.interpretation(x));
    }
}
