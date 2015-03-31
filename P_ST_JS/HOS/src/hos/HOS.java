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
    public void release(){
        for(int i=0; i<memorySegments.length;i++)
            memorySegments[i].release();
    }
    
    public void printInformation(){        
        for(int i = 0; i < jobs.length; i++){
            System.out.printf("%2d %2d %2d %2d %2d %8s%n", currentTime, jobs[i].getID(), jobs[i].getLocation(), jobs[i].getMemory(), jobs[i].getTime(), jobs[i].getStatus());
        } 
        System.out.println();
    }
    
    public void bestFit(){
        for(int i = 0; i < jobs.length; i++){
            int j = 0;
            int best=0;
            int wasted=64;
            memorySegments[best].use(jobs[i].getMemory(), jobs[i].getID());
            boolean added = jobs[i].getLocation() != -1;
            while(j < memorySegments.length && jobs[i].getTime() > 0){
                memorySegments[j].use(jobs[i].getMemory(), jobs[i].getID());
                if(memorySegments[j].getWastedSpace()>memorySegments[best].getWastedSpace()){
                    best=j;
                }
                j++;
            }
            if(!memorySegments[best].getState() ){
                    memorySegments[best].use(jobs[i].getMemory(), jobs[i].getID());
                    
                    jobs[i].setStatus(Job.Status.READY);
                    jobs[i].setLocation(j);
                }
        }
    }
    
    public void caseOne(){
        firstFit();
        printInformation();
        release();
        
    }
    //i am not sure if this is working because it is the same as case one everytime but i think it should work wesley
    public void caseTwo(){
        bestFit();
        printInformation();
        release();
    }
    //for case three
    public static void sort (Job [] arrayName){
    Job temp;
    for (int i = 0; i < arrayName.length-1; i++)
    {
        if(arrayName[i].getTime() > arrayName[i+1].getTime())
        {
            temp=arrayName[i];
            arrayName[i]=arrayName[i+1];
            arrayName[i+1]=temp;
            i=-1;
        }
    }
}
    //this doesnt work quite right yet wesley
    public void caseThree(){
        sort(jobs);
        bestFit();
        printInformation();
        release();
        
    }
    
    public static void main(String [] args){
        HOS hos = new HOS();    
        hos.caseOne();
        
        hos.caseTwo();
        
        hos.caseThree();
    }
}