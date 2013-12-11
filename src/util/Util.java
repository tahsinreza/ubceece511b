package util;

public class Util {
	
	/**
	 * Swap two elements in an integer array
	 * @param i - Index of element one
	 * @param j - Index of element two
	 * @param a - Reference to the array instance
	 */
	public static void swap(int i, int  j, int[] a) {
		int t = a[i];
		a[i] = a[j];
		a[j] = t;
	}
	
	/**
	 * Returns the index of the min element in 
	 * an integer array for the given beginning 
	 * and ending indices
	 * @param a - Reference to the array instance
	 * @param i - Beginning array index
	 * @param l - Ending array index
	 * @return - Index of the min element
	 */
	public static int minIndex(int[] a, int i, int l) {
		int mI = i;
		int mV = a[i];
		for(; i<l; ) {
			if(a[i] < mV) {
				mI = i;
				mV = a[i]; 	
			}		
			i++;
		}
		return mI;
	}	
}
