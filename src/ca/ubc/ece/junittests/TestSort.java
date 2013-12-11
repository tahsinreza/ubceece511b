package ca.ubc.ece.junittests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import algorithm.sort.Sort;

import java.util.Arrays;


public class TestSort {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("@BeforeClass - oneTimeSetUp");		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("@AfterClass - oneTimeTearDown");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("@Before - setUp");
	}

	@After
	public void tearDown() throws Exception {
		 System.out.println("@After - tearDown");
	}	
	
	// Test_One
	// A
	// Unsorted array as input
	@Test
	public final void testOne() {	
		fail("Blocked");	
		int [] expected = {50, -2, 10, 90, -21, 37, 0, 11, 17, 1};
		Arrays.sort(expected);		
		int [] testData = {50, -2, 10, 90, -21, 37, 0, 11, 17, 1};
		int [] actual = new Sort().simpleSort(testData, testData.length);						
		assertArrayEquals("", expected, actual);
	}
	
	// Test_Two
	// B
	// Empty array as input with non-zero length
	@Test
	public final void testTwo() {
		fail("Blocked");		
		boolean isException = false;
		try {
			new Sort().simpleSort(new int[10], 10);
		} catch(Exception e) {
			isException = true;
		}
		assertFalse(isException);
	}

	// Test_Three
	// C
	// Sorted array as input
	@Test
	public final void testThree() {
		fail("Blocked");		
		int [] expected = {50, -2, 10, 90, -21, 37, 0, 11, 17, 1};
		Arrays.sort(expected);	
		int [] testData = (int[])expected.clone();		
		int [] actual = new Sort().simpleSort(testData, testData.length);						
		assertArrayEquals("", expected, actual);
	}
	
	// D
	// Empty array as input with length zero
	@Test
	public final void testFour() {
		fail("Blocked");		
		boolean isException = false;
		try {
			new Sort().simpleSort(new int[0], 0);
		} catch(Exception e) {
			isException = true;
		}
		assertFalse(isException);
	}
	
	// E	
	// Length parameter smaller than actual array length  
	// Test_Seven
	@Test(expected = IllegalArgumentException.class)
	  public void testIllegalArgumentExceptionIsThrown_2() {
		fail("Blocked");		
		new Sort().simpleSort(new int[10], 0);
	 }
	
	// F
	// Array instance is null 
	// Test_Six
	@Test(expected = IllegalArgumentException.class)
	  public void testIllegalArgumentExceptionIsThrown_1() {
		fail("Blocked");		
		new Sort().simpleSort(null, 0);	
	 }	
	
	// G	
	// Unsorted array as input with non-distinct elements 
	@Test
	public final void testFive() {	
		fail("Blocked");		
		int [] expected = {90, -21, 10, 90, -21, 37, 0, 11, 17, 11};
		Arrays.sort(expected);		
		int [] testData = {90, -21, 10, 90, -21, 37, 0, 11, 17, 11};
		int [] actual = new Sort().simpleSort(testData, testData.length);						
		assertArrayEquals("", expected, actual);
	}
}
