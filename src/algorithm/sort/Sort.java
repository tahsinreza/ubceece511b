package algorithm.sort;

import util.Util;

public class Sort {
	
	/**
	 * An algorithm for sorting elements in an integer array
	 * @param a - Reference to the input array instance
	 * @param l - Length of the input array
	 * @return - The sorted array
	 */
	public int[] simpleSort(int[] a, int l) { 
		if(a == null) { 
			throw new IllegalArgumentException("Input array can not be null.");				
		} else if(a.length != l) {
			throw new IllegalArgumentException("Array length is invalid.");
		} 
		if(!(a instanceof int[])) {
			throw new IllegalArgumentException("Not an instance of integer array.");
		}
		int i = 0;
		for(; i<l; ) {
			int mI = Util.minIndex(a, i, l);
			if(mI != i) {
				Util.swap(i, mI, a);
			}
			i++;
		}
		return a;
	}	
}
