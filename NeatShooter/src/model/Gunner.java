package model;

/**
 * Created by jagnelli on 11/27/2018.
 */
public class Gunner extends PlayerAI {
    public Gunner(int inputNodes, int hiddenNodes, int outputNodes) {
        super(inputNodes, hiddenNodes, outputNodes);
    }

    public double[] move(java.lang.Double[] inputVals){

        double[] outputs = super.move(inputVals);

        //shoot
        super.addBullet(outputs[4], outputs[5]);
        return outputs;
    }
}
