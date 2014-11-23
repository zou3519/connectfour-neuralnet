package io;

/**
 * TerminatorThread is a thread that will terminate specific 
 * other threads/runnables when the program exits.
 *
 * This may not be neccessary on Linux but may be necessary on other
 * operating systems.
 *
 */
public class TerminatorThread extends Thread {
    
    private WriterRunnable writer;

    public TerminatorThread(WriterRunnable writer){
        this.writer = writer;     
    } 

    public void run() {
        writer.terminate();     
    }
}
