/*
*  Scott Troutman & James Schultz
*  CS 350 Spring 2015 TR 2:30 - 4:00
*/

package hos;

public class Job{
    public enum Status {READY, WAITING, RUNNING, FINISHED};
    
    private final int id;
    private final int memory;
    private int location;
    private int time;
    private Status status;
    
    public Job(int id, int memory, int time){
        this.id = id;
        this.memory = memory;
        this.time = time;
        location = -1;
        status = Status.WAITING;
    }
    
    //sets the job to ready, waiting, running, or finished
    public void setStatus(Status status){
        this.status = status;
    }
    
    //returns job status
    public String getStatus(){
        return status.toString();
    }
    
    //tells this job where it is in memory
    public void setLocation(int location){
        this.location = location;
        status = Status.READY;
    }
    
    //where is this job in memory
    public int getLocation(){
        return location;
    }
    
    //sets job to running and removes 1 from time remaining
    public void cycle(){
        time--;
        status = Status.RUNNING;
    }
    
    //how much time is left
    public int getTime(){
        return time;
    }
    
    //how much memory does this job use
    public int getMemory(){
        return memory;
    }
    
    //what is this jobs id
    public int getID(){
        return id;
    }
}