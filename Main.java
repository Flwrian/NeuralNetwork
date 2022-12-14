public class Main extends NeuralNetwork {
    
    public Main(int i, int h, int o) {
        super(i, h, o);
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
        Main nn = new Main(9,20,10);
        

        double[] x = {1,0,1,1,1,1,1,0,1};

        nn.setLearningRate(1);

        nn.setup();
        nn.trainOnData(nn.getX(), nn.getY(), 1000000);

        // Round the output to 3 decimal places
        System.out.println(Math.round(nn.predict(x).get(0) * 1000.0) / 1000.0);
        System.out.println(Math.round(nn.predict(x).get(1) * 1000.0) / 1000.0);
        System.out.println(nn.interpretation(x));
        nn.save("nn.ser");
    }
}
