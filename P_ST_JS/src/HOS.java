public class HOS{
    private int [] memorySegments;
    private Job [] jobs;
    public HOS(){
        int [] sizes = {32, 48, 24, 16, 64, 48, 32, 64, 48, 32};
        memorySegments = new int[sizes.size()];
        jobs = new Job[20];
        
        for(int i = 0; i < memorySegments.size(); i++){
            memorySegments[i] = sizes[i];
        }
        
        for(int i = 0; i < jobs.size(); i++){
            jobs[i] = new Job(i, 16 + (int)(Math.random()*48), 2 + (int)(Math.random() * 8));
        }
    }
    
    public static void main(String [] args){
    }
}