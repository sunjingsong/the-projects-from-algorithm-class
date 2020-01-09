import java.io.IOException;
import java.util.*;


public class Assignment01 {
	// Two data field
	Map<Integer, Schedule> myclasses = new LinkedHashMap<Integer, Schedule>();
	Map<Integer, Schedule> sortbyendmyclasses = new LinkedHashMap<Integer, Schedule>();
	/* Main Method */
	public static void main(String[] args) throws IOException {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("Enter the number of classes: (e.g.10)");
		int numberofclasses = scan.nextInt();
		System.out.println("Enter start time of the classes: (e.g.5)");
		int mystarttime = scan.nextInt();
		System.out.println("Enter end time of the classes: (e.g.30)");
		int myendtime = scan.nextInt();
		
		scan.close();
		
		Assignment01 Assignment = new Assignment01();
		Assignment.generateclasses(numberofclasses, mystarttime, myendtime);
		
		//print the class list (not sorted yet)
		System.out.println("\nThe list of the classes:");
		for(int key :Assignment.myclasses.keySet()) {
			Schedule sch = Assignment.myclasses.get(key);
			System.out.println("Class number:" + sch.getnumber()  + ", [" + sch.getstart() + ", " + sch.getend()+ "]");
		}
		
		//form PPT 60
		build_max_heap(Assignment.myclasses);
		
		//from PPT 60
		int classsize=Assignment.myclasses.size(); // since map's size can't change, add a size counter
		for(int i=Assignment.myclasses.size(); i>=2; i--) {
			Schedule temp = Assignment.myclasses.get(i);
			Assignment.myclasses.replace(i, Assignment.myclasses.get(1));
			Assignment.myclasses.replace(1, temp);
			classsize = classsize-1;
			MAX_heapfy(Assignment.myclasses,classsize,1);
		}
		//show sorted classes
		System.out.println("\nSorted list of the classes(by end time):");
		for(int key :Assignment.myclasses.keySet()) {
			Schedule sch = Assignment.myclasses.get(key);
			System.out.println("Class number:" + sch.getnumber()  + ", [" + sch.getstart() + ", " + sch.getend()+ "]");
		}
		
		//for interval scheduling
		List<Schedule> Inter_schedule = new ArrayList<Schedule>();
		//Inter_schedule.add(Assignment.myclasses.get(1));
		int endtimepointer = 0;
		for(int key :Assignment.myclasses.keySet()) {
			if (endtimepointer <= Assignment.myclasses.get(key).getstart()) {
				Inter_schedule.add(Assignment.myclasses.get(key));
				endtimepointer = Assignment.myclasses.get(key).getend();
			}
		}
		System.out.println("\nInterval scheduling:"); 
		System.out.println("The maximum schedule for one classroom:"); 
		for(int key = 0; key<Inter_schedule.size(); key++) {
			System.out.print("["+Inter_schedule.get(key).getnumber()+", ("+Inter_schedule.get(key).getstart()+", "+Inter_schedule.get(key).getend()+")]");
		}
		
		//for interval partitioning

		Map<Integer, LinkedHashMap<Integer, Schedule>> inter_P = new LinkedHashMap<Integer, LinkedHashMap<Integer ,Schedule>>();
		LinkedHashMap<Integer, Schedule> empty = new LinkedHashMap<Integer ,Schedule>();
		
		//add the first class (always available)
		inter_P.put(1, empty);
		inter_P.get(1).put(1,Assignment.myclasses.get(1));
		System.out.println("\n"+inter_P.get(1).get(1).getend());
		System.out.println(inter_P.size());
		System.out.println(inter_P.get(1).size());
		System.out.println(inter_P.get(1).values().size());
		//System.out.println(inter_P.get(2).size());
		//System.out.println(inter_P.get(2).values().size());
		inter_P.put(2, empty);
		inter_P.get(2).put(1,Assignment.myclasses.get(2));
		System.out.println("\n"+inter_P.get(2).get(1).getend());
		//System.out.println("\n"+inter_P.get(1).get(1).getend());
		System.out.println(inter_P.size());
		System.out.println(inter_P.get(1).size());
		System.out.println(inter_P.get(1).values().size());
		System.out.println(inter_P.get(2).size());
		System.out.println(inter_P.get(2).values().size());

		inter_P.get(1).put(2,Assignment.myclasses.get(3));
		System.out.println("\n"+inter_P.get(1).get(2).getend());
		
		System.out.println(inter_P.size());
		System.out.println(inter_P.get(1).size());
		System.out.println(inter_P.get(1).values().size());
		System.out.println(inter_P.get(2).size());
		System.out.println(inter_P.get(2).values().size());
		System.out.println("\n"+inter_P.get(2).get(2).getend());
		
		
		/*for(int key :Assignment.myclasses.keySet()) {
			int currentclasssize = inter_P.size();
			boolean addclasscounter = true;
			if (key==1){
				continue;
			}
			for(int i = 1; i<=currentclasssize; i++) {
				int schedulesize = inter_P.get(i).size();
				System.out.print("s"+Assignment.myclasses.get(key).getstart());
				System.out.println("t"+inter_P.get(i).get(schedulesize).getend()+"  size:"+schedulesize);
				if(Assignment.myclasses.get(key).getstart() >= inter_P.get(i).get(schedulesize).getend()) {
					//System.out.print("s"+Assignment.myclasses.get(key).getstart());
					//System.out.println("t"+inter_P.get(i).get(inter_P.get(i).size()-1).getend());
					//inter_P.get(i).add(Assignment.myclasses.get(key));
					inter_P.get(i).put(inter_P.get(i).size()+1,Assignment.myclasses.get(key));
					addclasscounter = false;
					System.out.println("i add");
					break ;
				}
			}
			if (addclasscounter) {
				inter_P.put(currentclasssize + 1, empty);
				//inter_P.get(inter_P.size()).add(Assignment.myclasses.get(key));
				inter_P.get(inter_P.size()).put(inter_P.get(inter_P.size()).size()+1,Assignment.myclasses.get(key));
				System.out.println("list size"+inter_P.get(currentclasssize).size());
			}
			System.out.println(inter_P.size());
			System.out.println(inter_P.get(1).size());
			
		}*/

		System.out.println("Answer");
		System.out.println(inter_P.size());
		
		
		
	}
	//generate class schedule
	void generateclasses(int classnumbers, int starttime, int endtime) {
		Random random = new Random();
		
		for(int i = 1; i <= classnumbers; i++) {
			Schedule Schedule = new Schedule();

			int start = random.nextInt(endtime-starttime) + starttime;
			int end = random.nextInt(endtime-starttime) + starttime;
			if(start>end) {
				int t = start;
				start = end;
				end = t;
			}
			if(start==end) {
				end++;
			}
			Schedule.setnumber(i);
			Schedule.setstart(start);
			Schedule.setend(end);
			
			myclasses.put(i, Schedule);	
		}
	}
	
	//from PPT 59/60
	static void build_max_heap(Map<Integer, Schedule> nonsortclasses) {
		int heapsize = nonsortclasses.size();
		for(int i =  (int) Math.floor(heapsize/2); i >= 1; i--) {
			MAX_heapfy(nonsortclasses, heapsize, i);
		}
	}
	
	//from PPT 59
	static void MAX_heapfy(Map<Integer, Schedule> nonsortclasses,int size, int nodes) {
		int largest = nodes;
		int L = nodes*2;
		int R = (nodes*2)+1;
		if(L<=size && nonsortclasses.get(L).getend()>nonsortclasses.get(nodes).getend()) {
			largest = L;
		}
		if(R<=size && nonsortclasses.get(R).getend()>nonsortclasses.get(largest).getend()) {
			largest = R;
		}
		if(largest != nodes) {
			Schedule temp = nonsortclasses.get(largest);
			nonsortclasses.replace(largest, nonsortclasses.get(nodes));
			nonsortclasses.replace(nodes, temp);
			MAX_heapfy(nonsortclasses,size,largest);
		}
	}
	
}
