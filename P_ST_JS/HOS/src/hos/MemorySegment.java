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
    
    public void use(int usedSize, int id){
        inUse = true;
        wastedSpace = size - usedSize;
        this.id = id;
    }
    
    public int getID(){
        return id;
    }
    
    public void release(){
        inUse = false;
    }
    
    public boolean getState(){
        return inUse;
    }
    
    public int getSize(){
        return size;
    }
    
    public int getWastedSpace(){
        return wastedSpace;
    }
}
