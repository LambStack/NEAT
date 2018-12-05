package model;

import view.Board;

/**
 * Created by jagnelli on 11/20/2018.
 */
public class PlayerAI extends Player {
	final double LR = .3;
    protected int inputNodes, hiddenNodes, outputNodes;
	double [][] ihWeights; // {w11, w21, w31, w41,
			     	    //  w12, w22, w32, w42}
	double [][] hoWeights;

	public double ihBias, hoBias;

	public PlayerAI(int inputNodes, int hiddenNodes, int outputNodes){
	    this.inputNodes = inputNodes;
	    this.hiddenNodes = hiddenNodes;
	    this.outputNodes = outputNodes;
		ihWeights = new double[inputNodes][hiddenNodes];
		hoWeights = new double[hiddenNodes][outputNodes];
		
		for( int j = 0; j < hiddenNodes; j++){
			for(int i = 0; i<inputNodes; i++){
				ihWeights[i][j] = Math.random() * 2 - 1;
			}
		}

		for( int j = 0; j < outputNodes;j++){
			for(int i = 0; i<hiddenNodes; i++){
				hoWeights[i][j] = Math.random() * 2 - 1;
			}
		}
		ihBias = Math.random() *2 -1;
		hoBias = Math.random() *2 -1;
	}

    public double[] move(java.lang.Double[] inputVals){

        double[] hiddenVals = new double[hiddenNodes];

        for( int j = 0; j < hiddenNodes; j++){
            float sum = 0;
            for(int i = 0; i<inputVals.length; i++){
                sum += ihWeights[i][j] * inputVals[i];
            }
            hiddenVals[j] = sum + ihBias;
        }

        double[] outputs = new double[outputNodes];


        for( int j = 0; j < outputNodes;j++){
            float sum = 0;
            for(int i = 0; i<hiddenVals.length; i++){
                sum += hoWeights[i][j] * hiddenVals[i];
            }
            outputs[j] = sum + hoBias;	 // should maybe normalise here with sigmoid
        }

        if (Math.abs(outputs[0] - outputs[1]) >= .05) {
            if(outputs[0] > outputs[1]){
                super.moveUp();
            }else{
                super.moveDown();
            }
        }
        // else do nothing

        if (Math.abs(outputs[2] - outputs[3]) >= .05) {
            if(outputs[2] > outputs[3]){
                super.moveLeft();
            }else{
                super.moveRight();
            }
        }
        // else do nothing

        return outputs;

    }

	// static breed method
	public static PlayerAI breed(PlayerAI p1, PlayerAI p2){
	    try{
            PlayerAI newPlayer = new PlayerAI(p1.inputNodes, p1.hiddenNodes, p1.outputNodes);
            newPlayer.color = Math.random() > .5 ? p1.color : p2.color;
            for( int j = 0; j < newPlayer.hiddenNodes; j++){
                for(int i = 0; i < newPlayer.inputNodes; i++){
                    newPlayer.ihWeights[i][j] = Math.random() > .5 ? p1.ihWeights[i][j] : p2.ihWeights[i][j];
                }
            }

            for( int j = 0; j < newPlayer.outputNodes; j++){
                for(int i = 0; i < newPlayer.hiddenNodes; i++){
                    newPlayer.hoWeights[i][j] = Math.random() > .5 ? p1.hoWeights[i][j] : p2.hoWeights[i][j];
                }
            }
            newPlayer.ihBias = Math.random() > .5 ? p1.ihBias : p2.ihBias;
            newPlayer.hoBias = Math.random() > .5 ? p1.hoBias : p2.hoBias;
            return newPlayer;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
	}

	public void mutate(){
	    super.stillAlive = true;
	    super.birthTime = System.currentTimeMillis();
	    super.lastKilledTime = System.currentTimeMillis();
	    super.clearKills();
	    setLocation((Board.WIDTH/2),(int)((Board.HEIGHT /2)));
	    myBullets.clear();
	    double numChanged = 0, total = 0;
	    for( int j = 0; j < hiddenNodes; j++){
			for(int i = 0; i < inputNodes; i++){
			    if(Math.random() > LR){
			        ihWeights[i][j] *= 1+ LR;
			        numChanged++;
                }
                total++;

			}
		}

		for( int j = 0; j < outputNodes; j++){
			for(int i = 0; i < hiddenNodes; i++){
			    if(Math.random() > LR){
			        numChanged++;
			        hoWeights[i][j] *= 1 + LR;
                }
			}
		}
        ihBias *= Math.random() > LR ? Math.random()* 1 +LR : 1;
		hoBias *= Math.random() > LR ? Math.random()* 1+ LR : 1;
		if(numChanged/total > .75){
		    super.nextColor();
        }
    }
}
