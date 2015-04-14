/*
*  Scott Troutman & James Schultz
*  CS 350 Spring 2015 TR 2:30 - 4:00
*/

package hos;

public class MemorySegment {
    private final int size;
    private boolean inUse;
    private int wastedSpace;
    private int id;
    
    public MemorySegment(int size){
        this.size = size;
        id = -1;
        wastedSpace = size;
    }
    
    //add a job to this memory slot
    public void use(int usedSize, int id){
        inUse = true;
        wastedSpace = size - usedSize;
        this.id = id;
    }    
    
    //what job is in this space
    public int getID(){
        return id;
    }
    
    //remove the job from this memory slot
    public void release(){
        inUse = false;
        wastedSpace = size;
        id = -1;
    }
    
    //is this memory slot in use
    public boolean getState(){
        return inUse;
    }
    
    //how big is this memory slot
    public int getSize(){
        return size;
    }
    
    //how much wasted space is there
    public int getWastedSpace(){
        return wastedSpace;
    }
}
