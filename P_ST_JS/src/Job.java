public class Job{
    private final int ID;
    private int memory;
    private int location;
    private int time;
    private int status;
    
    public Job(int id, int memory, int time){
        this.id = id;
        this.memory = memory;
        this.time = time;
    }
    
    public void setLocation(int location){
        this.location = location
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
}