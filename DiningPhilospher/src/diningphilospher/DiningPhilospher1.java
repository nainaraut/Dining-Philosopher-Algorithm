/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diningphilospher;

import static diningphilospher.DiningPhilospher1.N;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DiningPhilospher1{

//Number of Philosphers
final static int N = 5;    

 public static void main(String[] args) {
     
     //Array of Philosphopers
     Philospher philospher[] = new Philospher[N];
     
     for (int j = 0; j < N; j++)
     {
         //Start a thread for each Philospher 
         philospher[j] = new Philospher(j);
         new Thread(philospher[j]).start();
     }
              
   }

}
 

class Philospher implements Runnable
{
    final int THINKING = 0;           //Philospher Thinking
    final int HUNGRY = 1;             //Philospher Hungry
    final int EATING = 2;             // Philospher Eating

    int status[] = new int[N];        //Status Array
    private int id;                   //Unique Id for each philospher
    
    Random sleepTime = new Random();  //Ramdom number for the time to eat and sleep
    
    public Philospher(int i)
    { 
            this.id = i;     
    }
    
@Override
    public void run()
    {
        while(true)
        {
         System.out.println("In run");
         think(id);                   //Philospher thinking
         pick_forks(id);              //Philospher hungry and try to pick forks
         put_forks(id);               //put fork after eating
        }            
    }
    
  
    private void think(int i) {

        System.out.println("Philosopher "+i+" thinking");
        System.out.flush();
        sleep(sleepTime.nextInt(10));
    }

    private synchronized void pick_forks(int i){
        
        try {
            wait(10);                     //lock for system to prevent race condition
            status[i] = HUNGRY;           //hungry status
            System.out.println("Philosopher "+i+" hungry");
            System.out.flush();
            test(i);                     //test to see if forks available to eat
            notify();                    // release lock and notify
        } catch (InterruptedException ex) {
            Logger.getLogger(Philospher.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    private void eat(int i) {
      
       System.out.println("Philosopher "+i+" eating");
       System.out.flush();
       sleep(sleepTime.nextInt(10));
    }

    private synchronized void put_forks(int i){
        
        try {
            wait(10);                    //wait lock for mutual exclusion
            status[i] =  THINKING;       //status updated to thinking
            test((i+1)%N);               //test for philosper to the right
            test((i+N-1)%N);             //test for philospher to the left
            notify();                    //release lock
        } catch (InterruptedException ex) {
            Logger.getLogger(Philospher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void test(int i) {
    // Check if forks to the right and left of philospher are available
        if(status[i] == HUNGRY && (status[(i+1)%N] != HUNGRY && status[(i+N-1)%N] != HUNGRY))
        {
            eat(i);      //philospher eating
        }
    }

    private void sleep(int time) {
    try {
        Thread.sleep(time);
    } catch (InterruptedException ex) {
        Logger.getLogger(DiningPhilospher1.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
}


