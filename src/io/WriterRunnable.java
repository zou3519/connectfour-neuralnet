package io;

import java.util.LinkedList;
import genetic.Genome;
import neuralnet.NeuralNet;

/**
 * WriterRunnable is a Runnable object.
 *
 * It writes files in a seperate thread.
 *
 */
public class WriterRunnable extends IO implements Runnable {

    //queneS is the list of filenames
    //queneG is the list of genomes 
    //they should have the same lengths
    private volatile
        LinkedList<String> queueS = new LinkedList<String>();
    private volatile
        LinkedList<Genome> queueG = new LinkedList<Genome>();
    
    private volatile boolean terminateSignal = false;

    //initialize a WriterRunnable with
    //(new Thread(new WriterRunnable())).start()

    public void addRequest(String filename, Genome g) {
        queueS.addLast(filename);     
        queueG.addLast(g);
    }

    public void terminate() {
        terminateSignal = true;     
        System.out.println("Terminating WriterRunnable thread");
    }
        
    public void run() {
        System.out.println("Starting WriterRunnable thread");
        while (!terminateSignal || (queueS.size() != 0) ) {
            if (queueS.size() != 0){
                String request = queueS.removeFirst();    
                Genome g = queueG.removeFirst();

                NeuralNet net = new NeuralNet(g);
                String data = net.toStringNoLine();

                writeFile(request,data);
            }

            if (queueS.size() == 0) {
                try{Thread.sleep(1000);}
                catch (InterruptedException e) {
                    //do nothing     
                }   
            } 
        } 
    }    

}
