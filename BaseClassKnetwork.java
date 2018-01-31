import java.util.*;


abstract public class BaseClassKnetwork
{
  	public final static double NEURON_ON = 0.9;
  	public final static double NEURON_OFF = 0.1;
  	protected double output[];
  	protected double totalError;
  	protected int inputNeuronCount;
  	protected int outputNeuronCount;
  	protected Random random = new Random(System.currentTimeMillis());

  	abstract public void learn() throws RuntimeException;
  	abstract void trial(double[] input);

  	double calculateTrialError(TrainingSet train) throws RuntimeException
  	{
  	  	int i, tclass;
    	double diff;

    	totalError = 0.0;

    	for (int t = 0; t < train.getTrainingSetCount(); t++)
    	{

      		trial(train.getOutputSet(t));
      		tclass = (int) (train.getClassify(train.getInputCount() - 1));

      		System.out.println("tclass="+tclass);

      		for (i = 0; i < train.getOutputCount(); i++)
      		{
        		if (tclass == i)
        	  		diff = NEURON_ON - output[i];
        		else
          			diff = NEURON_OFF - output[i];
        		totalError += diff * diff;
      		}
		    for (i = 0; i < train.getOutputCount(); i++)
      		{
        		diff = train.getOutput(t, i) - output[i];
        		totalError += diff * diff;
			}
    	}
   		totalError /= (double) train.getTrainingSetCount();
   		return totalError;
  	}


  	static double vectorLength(double v[])
  	{
    	double rtn = 0.0;
    	for (int i = 0; i < v.length; i++)
      		rtn += v[i] * v[i];
    	return rtn;
  	}

  	double dotProduct(double vec1[], double vec2[])
  	{
    	int k, v;
    	double rtn;
    	rtn = 0.0;
    	k = vec1.length;

        v = 0;
        while ( (k--)>0 ) {
          rtn += vec1[v] * vec2[v];
          v++;
        }
        return rtn;
  	}

  	void randomizeWeights(double weight[][])
  	{
  		double r;
    	int temp = (int) (3.464101615 / (2. * Math.random()));
		for (int y = 0; y < weight.length; y++)
    	{
      		for (int x = 0; x < weight[0].length; x++)
      		{
        		r = (double) random.nextInt() + (double) random.nextInt()-(double) random.nextInt() - (double) random.nextInt();
        		weight[y][x] = temp * r;
      		}
    	}
		/*System.out.println("Randomizing weights in BaseKNetwork");
		for(int i=0;i<weight.length;i++){
		 for(int j=0;j<weight[i].length;j++){
			System.out.print("weight[][]="+weight[i][j]+"\t");}
			System.out.println();}*/
  	}
}
