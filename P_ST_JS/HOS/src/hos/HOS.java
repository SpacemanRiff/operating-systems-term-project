package hos;

public class HOS{
    private MemorySegment [] memorySegments;
    public Job [] jobs;
    private int currentTime;
    
    public HOS(){
        currentTime = 0;
        
        int [] sizes = {32, 48, 24, 16, 64, 48, 32, 64, 48, 32};
        memorySegments = new MemorySegment[sizes.length];
        jobs = new Job[20];
        
        for(int i = 0; i < memorySegments.length; i++){
            memorySegments[i] = new MemorySegment(sizes[i]);
        }
        
        initJobs();
    }
    
    public void initJobs(){
        for(int i = 0; i < jobs.length; i++){
            jobs[i] = new Job(i, 16 + (int)(Math.random()*49), 2 + (int)(Math.random() * 9));
        }
    }
    
    public void firstFit(){
        for(int i = 0; i < jobs.length; i++){
            int j = 0;
            boolean added = jobs[i].getLocation() != -1;
            while(j < memorySegments.length && !added && jobs[i].getTime() > 0){
                if(!memorySegments[j].getState() && memorySegments[j].getSize() >= jobs[i].getMemory()){
                    memorySegments[j].use(jobs[i].getMemory(), jobs[i].getID());
                    added = true;
                    jobs[i].setStatus(Job.Status.READY);
                    jobs[i].setLocation(j);
                }
                j++;
            }
        }
    }
    
    public void roundRobin(){
        for(int i = 0; i < memorySegments.length; i++){
            jobs[memorySegments[i].getID()].cycle();
        }
    }
    
    public void printInformation(){        
        for(int i = 0; i < jobs.length; i++){
            System.out.printf("%2d %2d %2d %2d %2d %8s%n", currentTime, jobs[i].getID(), jobs[i].getLocation(), jobs[i].getMemory(), jobs[i].getTime(), jobs[i].getStatus());
        } 
        System.out.println();
    }
    
    public void bestFit(){
        
    }
    
    public void caseOne(){
        firstFit();
    }
    
    public void caseTwo(){
        
    }
    
    public void caseThree(){
        
    }
    
    public static void main(String [] args){
        HOS hos = new HOS();    
        hos.caseOne();   
    }
}