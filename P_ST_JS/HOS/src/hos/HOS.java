package hos;

public class HOS{
    private MemorySegment [] memorySegments;
    public Job [] jobsCaseOne;
    public Job [] jobsCaseTwo;
    public Job [] jobsCaseThree;
    public Job [] jobsCaseThreePlaceHolder;
    
    public HOS(){        
        int [] sizes = {32, 48, 24, 16, 64, 48, 32, 64, 48, 32};
        memorySegments = new MemorySegment[sizes.length];
        jobsCaseOne = new Job[20];
        jobsCaseTwo = new Job[20];
        jobsCaseThree = new Job[20];
        jobsCaseThreePlaceHolder = new Job[20];
        
        for(int i = 0; i < memorySegments.length; i++){
            memorySegments[i] = new MemorySegment(sizes[i]);
        }
        
        initJobs();
    }
    
    public void initJobs(){
        for(int i = 0; i < jobsCaseOne.length; i++){
            int time = 16 + (int)(Math.random()*49);
            int memory = 2 + (int)(Math.random() * 9);
            jobsCaseOne[i] = new Job(i, time, memory);
            jobsCaseTwo[i] = new Job(i, time, memory);
            jobsCaseThree[i] = new Job(i, time, memory);
            jobsCaseThreePlaceHolder[i] = jobsCaseThree[i];
        }
    }
    
    //for case three
    public static void sort (Job [] arrayName){
        Job temp;
        for (int i = 0; i < arrayName.length-1; i++){
            if(arrayName[i].getTime() > arrayName[i+1].getTime()){
                temp=arrayName[i];
                arrayName[i]=arrayName[i+1];
                arrayName[i+1]=temp;
                i=-1;
            }
        }
    }
    
    public void firstFit(Job [] jobs){
        for(int i = 0; i < jobs.length; i++){
            int j = 0;
            boolean added = jobs[i].getLocation() != -1;
            if(jobs[i].getTime() <= 0 && jobs[i].getLocation()!= -1){
                memorySegments[jobs[i].getLocation()].release();
                jobs[i].setLocation(-1);
                jobs[i].setStatus(Job.Status.FINISHED);
            }
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
    
    public void bestFit(Job [] jobs){
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
    
    public void roundRobin(Job [] jobs){        
        for(int i = 0; i < 30; i++){
            int count = 0; 
            int j = 0;
            while(count < 4 && j < jobs.length){
                if(memorySegments[(j+(i*4))%memorySegments.length].getID()!=-1){
                    if(jobs[memorySegments[(j+(i*4))%memorySegments.length].getID()].getStatus().equals("READY")){
                        jobs[memorySegments[(j+(i*4))%memorySegments.length].getID()].cycle();
                        count++;
                    }
                }
                j++;
            }
            
            firstFit(jobs);
            printInformation(jobs, i + 1);
            
            for (int h=0; h < memorySegments.length;h++){   
                if(memorySegments[h].getID()!=-1){                    
                    if(jobs[memorySegments[h].getID()].getStatus().equals("RUNNING")){
                        jobs[memorySegments[h].getID()].setStatus(Job.Status.READY);
                    }
                }  
            }
        }
    }
    
    public void printInformation(Job [] jobs, int currentTime){        
        System.out.printf("%6s %4s %10s %8s %11s %10s%n", "Tick", "ID", "Location", "Memory", "Time Left", "Status");
        for(int i = 0; i < jobs.length; i++){
            System.out.printf("%6s %4s %10s %8s %11s %10s%n", currentTime, jobs[i].getID(), jobs[i].getLocation(), jobs[i].getMemory(), jobs[i].getTime(), jobs[i].getStatus());
        } 
        System.out.println();
    }
    
    public void release(){
        for(int i=0; i<memorySegments.length;i++)
            memorySegments[i].release();
    }
    
    public void caseOne(){
        firstFit(jobsCaseOne);
        printInformation(jobsCaseOne, 0);
        roundRobin(jobsCaseOne);
        release();        
    }
    
    //i am not sure if this is working because it is the same as case one everytime but i think it should work wesley
    public void caseTwo(){
        bestFit(jobsCaseTwo);
        printInformation(jobsCaseTwo, 0);
        release();
    }
    
    //this doesnt work quite right yet wesley
    public void caseThree(){
        sort(jobsCaseThree);
        bestFit(jobsCaseThree);
        printInformation(jobsCaseThreePlaceHolder, 0);
        release();        
    }
    
    public static void main(String [] args){
        HOS hos = new HOS();    
        hos.caseOne();        
        //hos.caseTwo();        
        //hos.caseThree();
    }
}