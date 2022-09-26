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

    @Override
    public double[] condition(double[] input) {
        // Recognize shapes.
        // Example: {1,0,1,0,1,0,1,0,1} is a xcross then return {1,0}
        // {0,1,0,1,1,1,0,1,0} is a plus then return {0,1}

        if (isCross(input)) {
            return new double[]{1, 0};
        }
        if (isPlus(input)) {
            return new double[]{0, 1};
        }
        if (isSquare(input)) {
            return new double[]{1, 1};
        }
        
        return new double[]{0, 0};
    }

    public static void main(String[] args) {
        Main nn = new Main(9,20,2);
        

        double[] x = {0,1,0,1,1,1,0,1,0};

        nn.setLearningRate(1);

        nn.setup();
        nn.trainOnData(nn.getX(), nn.getY(), 1000000);

        // for (double[] ds : X) {
        //     System.out.println(Math.round(nn.predict(ds).get(0) * 1000.0) / 1000.0);
        // }

        // Round the output to 3 decimal places
        System.out.println(Math.round(nn.predict(x).get(0) * 1000.0) / 1000.0);
        nn.save("nn.ser");
    }
}
