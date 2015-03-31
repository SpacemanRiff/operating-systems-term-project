package hos;

public class Job{
    public enum Status {READY, WAITING, RUNNING, FINISHED};
    
    private final int id;
    private final int memory;
    private final int defaultTime;
    private int location;
    private int time;
    private Status status;
    
    public Job(int id, int memory, int time){
        this.id = id;
        this.memory = memory;
        this.time = time;
        location = -1;
        defaultTime = time;
        status = Status.WAITING;
    }
    
    public void setStatus(Status status){
        this.status = status;
    }
    
    public String getStatus(){
        return status.toString();
    }
    
    public void setLocation(int location){
        this.location = location;
        status = Status.READY;
    }
    
    public int getLocation(){
        return location;
    }
    
    public void cycle(){
        time--;
    }
    
    public int getTime(){
        return time;
    }
    
    public int getMemory(){
        return memory;
    }
    
    public int getID(){
        return id;
    }
    
    public String toString(){
        return id + " " + location + " " + memory + " " + time + " " + getStatus();
    }
    
    public boolean isFinished(){
        return time == 0;
    }
    
    public void reset(){
        time = defaultTime;
        status = Status.READY;
    }
}