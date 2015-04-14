HOS - Operating Systems Term Project

The purpose of this program is to simulate job scheduling, CPU scheduling, and memory allocation strategies that an operating system does regularly.
There are 3 separate cases that are tested:

 Case | Memory Allocation | Job Scheduling         | CPU Scheudling
------|-------------------|------------------------|---------------
    1 | First Fit         | First Come First Serve | Round Robin
    2 | Best Fit          | First Come First Serve | Round Robi
    3 | Best Fit          | Shortest Job First     | Round Robin

      Classes | Descriptions
---------------|-------------
           HOS | Runs the job and memory scheduling as well as prints output to the screen and an output file.
           Job | Stores all relevant information for the Job objects, including the ID, the size, the amount of CPU time it will use, the status, and where in memory it is being stored.
 MemorySegment | Stores information like size, wasted space, and what Job is in this current memory segment, if there is one

To run the program, just simply compile and run. Everything should work out right. The package needed to run is called "hos".
All files are output to "HOS/src/output", and are called "case1.txt", "case2.txt", "case3.txt" for each respective case