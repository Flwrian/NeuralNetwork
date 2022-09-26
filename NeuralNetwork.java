
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents a neural network.
 * You can change the condition method by overriding it (You will have to extend this class).
 * If you want, you can pass your own X and Y data to the trainOnData method, the use the predict/predictMultiple method to get the output.
 */
public class NeuralNetwork implements Serializable{

	private int maxNumber;
	private int inputLength;

	private double[][] X;
	private double[][] Y;

	private int inputNodes;
	private int hiddenNodes;
	private int outputNodes;
	
	private Matrix weights_ih,weights_ho , bias_h,bias_o;
	private double l_rate=0.1;
	
	/**
	 * 
	 * @param i The number of inputs (The length of the input array)
	 * @param h The number of hidden nodes
	 * @param o The number of outputs
	 */
	public NeuralNetwork(int i,int h,int o) {
		this.inputLength = i;
		this.inputNodes = i;
		this.hiddenNodes = h;
		this.outputNodes = o;
		weights_ih = new Matrix(h,i);
		weights_ho = new Matrix(o,h);
		
		bias_h= new Matrix(h,1);
		bias_o= new Matrix(o,1);
	}
	
	public List<Double> predict(double[] X) {
		
		Matrix input = Matrix.fromArray(X);
		Matrix hidden = Matrix.multiply(weights_ih, input);
		hidden.add(bias_h);
		hidden.sigmoid();
		
		Matrix output = Matrix.multiply(weights_ho,hidden);
		output.add(bias_o);
		output.sigmoid();
		
		return output.toArray();
	}

	public List<Double> predictMultiple(double[][] X) {
		ArrayList<Double> result = new ArrayList<Double>();
		for(int i=0;i<X.length;i++) {
			result.addAll(predict(X[i]));
		}
		return result;

	}
	
	
	public void trainOnData(double[][]X,double[][]Y,int epochs) {

		long start = System.currentTimeMillis();

		for(int i=0;i<epochs;i++)
		{	
			int sampleN =  (int)(Math.random() * X.length );
			this.train(X[sampleN], Y[sampleN]);

			// Print the error every 10% of the epochs
			if(i%(epochs/10)==0 && i!=0) {
				double error = 0;
				for(int j=0;j<X.length;j++) {
					List<Double> output = predict(X[j]);
					for(int k=0;k<output.size();k++) {
						error += Math.abs(output.get(k) - Y[j][k]);
					}
				}
				System.out.println("------------------");
				System.out.println("Generation: "+i);
				System.out.println("Error: "+error);
				System.out.println("Estimated time remaining: "+(System.currentTimeMillis()-start)*(epochs-i)/(i+1)/1000+" seconds");
				System.out.println(i/(epochs/100)+"% completed");
			}
		}

		System.out.println("------------------");
		System.out.println("Training finished in "+(System.currentTimeMillis()-start)/1000+" seconds");
	}
	
	private void train(double [] X,double [] Y)
	{
		Matrix input = Matrix.fromArray(X);
		Matrix hidden = Matrix.multiply(weights_ih, input);
		hidden.add(bias_h);
		hidden.sigmoid();
		
		Matrix output = Matrix.multiply(weights_ho,hidden);
		output.add(bias_o);
		output.sigmoid();
		
		Matrix target = Matrix.fromArray(Y);
		
		Matrix error = Matrix.subtract(target, output);
		Matrix gradient = output.dsigmoid();
		gradient.multiply(error);
		gradient.multiply(l_rate);
		
		Matrix hidden_T = Matrix.transpose(hidden);
		Matrix who_delta =  Matrix.multiply(gradient, hidden_T);
		
		weights_ho.add(who_delta);
		bias_o.add(gradient);
		
		Matrix who_T = Matrix.transpose(weights_ho);
		Matrix hidden_errors = Matrix.multiply(who_T, error);
		
		Matrix h_gradient = hidden.dsigmoid();
		h_gradient.multiply(hidden_errors);
		h_gradient.multiply(l_rate);
		
		Matrix i_T = Matrix.transpose(input);
		Matrix wih_delta = Matrix.multiply(h_gradient, i_T);
		
		weights_ih.add(wih_delta);
		bias_h.add(h_gradient);
		
	}

	/**
	 * This method is used to simplify your output data.
	 * You must override this method if you want to use it.
	 * For example, if you want to predict if a number is even or odd, you can use this method to simplify your output data. You just have to return 1 if the number is even and 0 if it is odd, make a for loop to create your X and Y data and then train the neural network.
	 */
	public double[] condition(double[] input) {
		throw new UnsupportedOperationException("You must override this method");
	}

	public int getInputLength() {
		return inputLength;
	}

	public void setInputLength(int inputLength) {
		this.inputLength = inputLength;
	}

	public void setInput(double[][] x) {
		X = x;
	}

	public void setOutput(double[][] y) {
		Y = y;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

	/**
	 * The learning rate is a hyperparameter that controls how much to change the model in response to the estimated error each time the model weights are updated. If the learning rate is too small, it can be slow to converge on a good solution. If the learning rate is too large, it can overshoot and fail to converge, or even diverge. A good starting value for the learning rate is 0.1. You may need to adjust this value when using a different model or training data. The more parameters in your model, the lower the learning rate should be. The learning rate may need to be tuned differently for each model and dataset.
	 * @param l_rate
	 */
	public void setLearningRate(double l_rate) {
		this.l_rate = l_rate;
	}

	public double[][] getX() {
		return X;
	}

	public double[][] getY() {
		return Y;
	}

	/**
	 * The setup method is used to generate the X and Y data.
	 * It is a default method to quickly generate data.
	 * You will have to choose a maxNumber.
	 * The maxNumber is the maximum number that can be generated.
	 * Example: If you choose 10 and the inputLenght is 2. X will be a 2D array with 10*10 = 100 rows and 2 columns. {0,0},{0,1},{0,2}...{10,10}.
	 */
	public void setup(){
		// Ask for max number
		System.out.println("Enter the max number");
		setMaxNumber(new Scanner(System.in).nextInt());
		setMaxNumber(maxNumber + 1);
		
		// Generate X and Y so that X is a list of all possible inputs and Y is a list of all possible outputs
		X = new double[(int) Math.pow(maxNumber, inputLength)][inputLength];
		Y = new double[(int) Math.pow(maxNumber, inputLength)][outputNodes];
		System.out.println("X length: "+X.length);
		for (int i = 0; i < X.length; i++) {
			for (int j = 0; j < X[i].length; j++) {
				X[i][j] = (i / (int) Math.pow(maxNumber, j)) % maxNumber;
			}
		}
		for (int i = 0; i < Y.length; i++) {
			for (int j = 0; j < Y[i].length; j++) {
				Y[i] = condition(X[i]);
			}
		}
		
		System.out.println("Setup Complete");

		System.out.println("Do you want to see the data? (y/n)");
		if(new Scanner(System.in).nextLine().equals("y")) {
			System.out.println("X:");
			for (int i = 0; i < X.length; i++) {
				System.out.println(Arrays.toString(X[i]));
			}
			System.out.println("Y:");
			for (int i = 0; i < Y.length; i++) {
				System.out.println(Arrays.toString(Y[i]));
			}
		}
	}

	public void save(String path) {
		try {
			FileOutputStream fileOut = new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in " + path);
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static NeuralNetwork load(String path) {
		NeuralNetwork nn = null;
		try {
			FileInputStream fileIn = new FileInputStream(path);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			nn = (NeuralNetwork) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			System.out.println("NeuralNetwork class not found");
			c.printStackTrace();
			return null;
		}
		return nn;
	}


}