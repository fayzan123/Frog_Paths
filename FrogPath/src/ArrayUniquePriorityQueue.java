
public class ArrayUniquePriorityQueue<T> implements UniquePriorityQueueADT<T>{
	
	private T[] queue;
	private double[] priority;
	private int count;
	
	public ArrayUniquePriorityQueue() {
		//Initialize arrays and counter
		queue = (T[]) (new Object[10]);
		priority = new double[10];
		count = 0;
	}
	

	public void add(T data, double prio) {
		for (int i = 0; i < count; i++) {
			if (queue[i].equals(data)) { //Check if data already exists in queue, if so, remove data
				return;
			}
		}
		if (count == queue.length)  { //Expand capacity of queue if queue is full
			T[] largerQueue = (T[]) (new Object[queue.length + 5]);
			double[] largerPriority = new double[priority.length+5];
			for (int i = 0; i < queue.length; i++) {
				largerQueue[i] = queue[i];
				largerPriority[i] = priority[i];
			}
			queue = largerQueue;
			priority = largerPriority;
			
		}
		
		int index = 0;
		
		while (index < count && prio >= priority[index]) { //Get index of where to add data
			index++;
		}
		
		if (index < count) { //Add data and shift all other elements
		    for (int i = count; i > index; i--) {
		        queue[i] = queue[i-1];
		        priority[i] = priority[i-1];
		    }
		}
		priority[index] = prio;
		queue[index] = data;
		count++;
		
	}
	
	public boolean contains(T data) {
		for (int i = 0; i < count; i++) { //Check if data exists in queue, if so, return true
			if (queue[i].equals(data)) {
				return true;
			}
		}
		return false;
	}
	
	public T peek() throws CollectionException {
		if (count == 0) throw new CollectionException("PQ is empty"); //If queue is empty throw exception
		return queue[0]; //Return first element in queue
	}
	
	public T removeMin() throws CollectionException {
		if (count == 0) throw new CollectionException("PQ is empty"); //If queue is empty throw exception
		T temp = queue[0]; //Return the first element in queue
		for (int i = 0; i < count-1; i++) { //Shift all elements to the left
			queue[i] = queue[i+1];
		}
		queue[count-1] = null;
		count--;
		return temp;
	}
	
	public void updatePriority(T data, double newPrio) throws CollectionException{
		if (!contains(data)) {
			throw new CollectionException("Item not found in PQ"); //If queue is empty throw exception
		}
		
		int index = 0;
		while (!queue[index].equals(data)) {  //find where data exists in collection
			index++;
		}
		
		for (int i = index; i < count-1; i++) {
			queue[i] = queue[i+1];
			priority[i] = priority[i+1];
		}
	    
		count--;
	    
		add(data, newPrio);
		
	}



	public boolean isEmpty() { //check if queue is empty
		return count == 0;
	}


	@Override
	public int size() { //return number of elements in array
		return count;
	}
	
	public int getLength() { //return size of array
		return queue.length;
	}
	
	
	public String toString() {
		if (isEmpty()) {
			return "The PQ is empty";
		}
		String strBuild = "";
		for (int i = 0; i < count; i++) { //Add all elements of queue and respective priority to strBuild
			if (i == count-1) {
				strBuild += queue[i] + " [" + priority[i] + "]";
			}
			else {
				strBuild += queue[i] + " [" + priority[i] + "], ";
			}
		}
		return strBuild;
		
	}
	
}
