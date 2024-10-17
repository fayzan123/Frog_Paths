
public class FrogPath {
	private Pond pond;
	
	public FrogPath(String filename) { //initialize pond
		try {
			pond = new Pond(filename);
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public Hexagon findBest(Hexagon currCell) {
		
		ArrayUniquePriorityQueue<Hexagon> priorityQueue = new ArrayUniquePriorityQueue();
		
		for (int i = 0; i < 6; i++) {
			try {
				Hexagon neighbour = currCell.getNeighbour(i); //get all adjacent neighbours of current cell
	            if (!neighbour.isMarked()) {
            		double priority = calcPriority(neighbour); //calculate their priority and add it to queue
            		if (priority >= 0) priorityQueue.add(neighbour, priority);
	            }
			} catch(Exception e) {};
		}
			
		if (currCell.isLilyPadCell()) {
			for (int i = 0; i < 6; i++) { //
				try {
	        		Hexagon adjNeighbour = currCell.getNeighbour(i); //get all cells adjacent to lilypad cell
	        		for (int j = 0; j < 6; j++) {
	        			if (adjNeighbour != null) {
		        			Hexagon nextNeighbour = adjNeighbour.getNeighbour(j); //get all cells surrounding cells adjacent to lilypad cell
		        			if (nextNeighbour!= null && !nextNeighbour.isMarked()) {
		                		double priority = calcPriority(nextNeighbour); //calculate priority of the cells surroinding adjacent cells
		                		if (priority >= 0) { //priority is set to -1 for things that should be ignored
			                		if ((i+j)%2 == 0) { //If index of adjacent cell and surrounding cell sums are even, it is straight line
			                			priority += 0.5;
			                		}
			                		if ((i+j)%2 != 0) { //otherwise it is false
			                			priority += 1;
			                		}
			                		priorityQueue.add(nextNeighbour, priority);
		                		}	
		        			}
	        			}
	        		}	
		            
				} catch (Exception e) {}
			}
		}
		
		 
		 if (!priorityQueue.isEmpty()) { //if queue is not empty return minimum priority value, otherwise return null
			 return priorityQueue.removeMin();
		 }
		 
		 return null;
		

		
	}
	
	public String findPath() {
		ArrayStack<Hexagon> s = new ArrayStack(); //initialize stack and add start
		Hexagon start = pond.getStart();
		s.push(start);
		start.markInStack();
		int fliesEaten = 0;
		String strBuild = "";
		while (!s.isEmpty()) {
			Hexagon curr = s.peek();
			strBuild += curr.toString() + " "; //add id of curr to string
			if (curr.isEnd()) {
				break;
			}
			if (curr instanceof FoodHexagon) { //if curr is fly cell, add number of flies eaten and remove flies
			    int numFlies = ((FoodHexagon) curr).getNumFlies();
			    fliesEaten += numFlies;
			    ((FoodHexagon) curr).removeFlies();
			}
			Hexagon next = findBest(curr);
			if (next == null) { 
				s.pop();
				curr.markOutStack();
			} else {
				s.push(next);
				next.markInStack();
			}
			
		}
		if (s.isEmpty()) {  //if stack is empty return no solution, otherwise return string with flies eaten
			return "No solution";
		}
		else {
			return strBuild + "ate " + fliesEaten + " flies";
		}
		
	}
	
	
	private double calcPriority(Hexagon currCell) {
		double priority = 0;
		for (int i = 0; i < 6; i++) { //check if cells neighboring currCell are alligator and not reed, return -1
			try {
				Hexagon neighbour = currCell.getNeighbour(i);
				if (neighbour.isAlligator() && !currCell.isReedsCell()) return -1;
			} catch (Exception e) {}
		}
		
		if (currCell.isMudCell()) {
			return -1;
		}
		
		if (currCell.isAlligator()) return -1;
		
		if (currCell.isStart()) return -1;
		
		if (currCell.isWaterCell()) { //set base priority depending on what currCell is
	        priority = 6.0;
	    } 
		else if (currCell.isReedsCell()) {
			try {
				for (int i = 0; i < 6; i++) { //if currCell is reed adjacent to alligator, set priority accordingly
					Hexagon neighbour = currCell.getNeighbour(i);
					if (neighbour.isAlligator()) priority = 10;
					else priority = 5.0;
				}
			} catch (Exception e) {}
	    } 
		else if (currCell.isLilyPadCell()) {
	        priority = 4.0;
	    } 
		else if (currCell.isEnd()) {
	        priority = 3.0;
	    } 
		else if (currCell instanceof FoodHexagon) { //check if cell contains flies, set base priority depending on number of flies
	    	int numFlies = ((FoodHexagon) currCell).getNumFlies();
	    	if (numFlies == 1) priority = 2.0;
	    	else if (numFlies == 2) priority = 1.0;
	    	else if (numFlies == 3) priority = 0.0;
	    }
	    
	   return priority;
	}
	
	
	
	
}
