package hos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class HOS{
    public enum FitTypes {FIRST, BEST};
    private MemorySegment [] memorySegments;
    public Job [] jobsCaseOne;
    public Job [] jobsCaseTwo;
    public Job [] jobsCaseThree;
    public Job [] jobsCaseThreePlaceHolder;
    
    //initializes memory sizes and the number of jobs
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
    
    //sets up the jobs with the same random values
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
    
    //sorts the jobs into shortest job first
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
    
    //first fit memory allocation
    public void firstFit(Job [] jobs){
        for(int i = 0; i < jobs.length; i++){
            int j = 0;
            //removes jobs from memory
            if(jobs[i].getTime() <= 0 && jobs[i].getLocation()!= -1){
                memorySegments[jobs[i].getLocation()].release();
                jobs[i].setLocation(-1);
                jobs[i].setStatus(Job.Status.FINISHED);
            }
            //if the job's locatin is not -1. it's already in memory, so the loop doesn't run
            boolean added = jobs[i].getLocation() != -1;
            while(j < memorySegments.length && !added && jobs[i].getTime() > 0){
                //makes sure the memory slot isn't already allocated and that this job will fit
                if(!memorySegments[j].getState() && memorySegments[j].getSize() >= jobs[i].getMemory()){
                    //adds the job to memory and sets the job to ready
                    memorySegments[j].use(jobs[i].getMemory(), jobs[i].getID());
                    added = true;
                    jobs[i].setStatus(Job.Status.READY);
                    jobs[i].setLocation(j);
                }
                j++;
            }
        }
    }
    
    //best fit memory allocation
    public void bestFit(Job [] jobs){
        for(int i = 0; i < memorySegments.length; i++){
            int j = 0;
            
            //removes finished jobs from memory
            if(memorySegments[i].getID() >= 0){
                if(jobs[memorySegments[i].getID()].getTime() <= 0 && jobs[memorySegments[i].getID()].getLocation()!= -1){
                    jobs[memorySegments[i].getID()].setLocation(-1);
                    jobs[memorySegments[i].getID()].setStatus(Job.Status.FINISHED);
                    memorySegments[i].release();
                }
            }
            int jobID = -1;
            
            //initializes wasted space to the maximum for this specific memory slot
            int wastedSpace = memorySegments[i].getSize();
            
            //makes sure the memory isn't allocated already
            if(!memorySegments[i].getState()){
                //looks for a job with the lowest wasted space for this memory slot
                while(j < jobs.length){ 
                        //makes sure this job isn't already in a location, and that it isn't already done
                        if(jobs[j].getLocation() == -1 && jobs[j].getTime() > 0){
                            //checks to see if this job can fit, and if it has less wasted space than the current selected job
                            if(memorySegments[i].getSize() - jobs[j].getMemory() >= 0 && memorySegments[i].getSize() - jobs[j].getMemory() <= wastedSpace){
                                //sets the wasted space to how much this job wastes, and then replaces the currently selected job with this one
                                jobID = j;
                                wastedSpace = memorySegments[i].getSize() - jobs[j].getMemory();
                            }
                        }
                    j++;
                }
            }
            
            //if a job was selected
            if(jobID >= 0){
                //set this job into memory and set it to ready
                memorySegments[i].use(jobs[jobID].getMemory(), jobID);
                jobs[jobID].setStatus(Job.Status.READY);
                jobs[jobID].setLocation(i);
            }
        }
    }
    
    //runs each memory section in order, 4 at a time
    public void roundRobin(Job [] jobs, File file, FitTypes fitType, Job [] printJobs){ 
        try{
            PrintWriter pw = new PrintWriter(file);
            printInformation(printJobs, 0, pw);
            for(int i = 0; i < 30; i++){
                int count = 0; 
                int j = 0;
                //finds 4 available memory locations to be added to the CPU
                while(count < 4 && j < jobs.length){
                    if(memorySegments[(j+(i*4))%memorySegments.length].getID()!=-1){
                        if(jobs[memorySegments[(j+(i*4))%memorySegments.length].getID()].getStatus().equals("READY")){
                            jobs[memorySegments[(j+(i*4))%memorySegments.length].getID()].cycle();
                            count++;
                        }
                    }
                    j++;
                }
                
                //removes finished jobs, depending on which sort is supplied
                if(fitType == FitTypes.FIRST){
                    firstFit(jobs);
                }else{
                    bestFit(jobs);
                }
                
                //prints jobs
                printInformation(printJobs, i + 1, pw);

                //sets all running jobs back to Ready
                for (int h=0; h < memorySegments.length;h++){   
                    if(memorySegments[h].getID()!=-1){                    
                        if(jobs[memorySegments[h].getID()].getStatus().equals("RUNNING")){
                            jobs[memorySegments[h].getID()].setStatus(Job.Status.READY);
                        }
                    }  
                }
            }
            pw.flush();
        }catch(FileNotFoundException ex){}
    }
    
    //prints out all relevant information aobut each job, and writes it to a file
    public void printInformation(Job [] jobs, int currentTime, PrintWriter pw){  
        System.out.printf("%6s %4s %10s %8s %11s %10s %7s%n", "Tick", "ID", "Location", "Memory", "Time Left", "Status", "Wasted");
        pw.printf("%6s %4s %10s %8s %11s %10s %7s%n", "Tick", "ID", "Location", "Memory", "Time Left", "Status", "Wasted");
        for(int i = 0; i < jobs.length; i++){
            String wastedSpace = "-";
            if(jobs[i].getLocation() >= 0){
                wastedSpace = memorySegments[jobs[i].getLocation()].getWastedSpace() + "";
            }
            System.out.printf("%6s %4s %10s %8s %11s %10s %7s%n", currentTime, jobs[i].getID(), jobs[i].getLocation(), jobs[i].getMemory(), jobs[i].getTime(), jobs[i].getStatus(), wastedSpace);
            pw.printf("%6s %4s %10s %8s %11s %10s %7s%n", currentTime, jobs[i].getID(), jobs[i].getLocation(), jobs[i].getMemory(), jobs[i].getTime(), jobs[i].getStatus(), wastedSpace);
        } 
        System.out.println();
        pw.println();
    }
    
    //cleans everything out from memory
    public void release(){
        for(int i=0; i<memorySegments.length;i++)
            memorySegments[i].release();
    }
    
    //first in first out, first fit, round robin
    public void caseOne(){
        File file = new File("src/output/case1.txt");
        firstFit(jobsCaseOne);
        roundRobin(jobsCaseOne, file, FitTypes.FIRST, jobsCaseOne);
        release();        
    }
    
    //first in first out, best fit, round robin
    public void caseTwo(){
        File file = new File("src/output/case2.txt");
        bestFit(jobsCaseTwo);
        roundRobin(jobsCaseTwo, file, FitTypes.BEST, jobsCaseTwo);
        release();
    }
    
    //Shortest job first, best fit, round robin
    public void caseThree(){
        File file = new File("src/output/case3.txt");
        sort(jobsCaseThree);
        bestFit(jobsCaseThree);
        roundRobin(jobsCaseThree, file, FitTypes.BEST, jobsCaseThreePlaceHolder);
        release();        
    }
    
    public static void main(String [] args){
        HOS hos = new HOS();    
        hos.caseOne();        
        hos.caseTwo();        
        hos.caseThree();
    }
}