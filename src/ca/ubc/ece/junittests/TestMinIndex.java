package ca.ubc.ece.junittests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.Util;

import java.util.Arrays;

public class TestMinIndex {

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
	
	// a
	// Unsorted array as input
	@Test
	public final void testOne() {	
		fail("Blocked");		
		int [] a = {50, -2, 10, 90, -21, 37, 0, 11, 17, 1};
		Arrays.sort(a);
		int expected = a[0];				
		int [] testData = {50, -2, 10, 90, -21, 37, 0, 11, 17, 1};		
		int minIndex = Util.minIndex(testData, 0, testData.length);
		int actual = testData[minIndex];		
		assertEquals(expected, actual);		
	}
	
	// b
	// Sorted array as input
	@Test
	public final void testTwo() {
		fail("Blocked");	
		int [] a = {50, -2, 10, 90, -21, 37, 0, 11, 17, 1};
		Arrays.sort(a);
		int expected = a[0];
		int [] testData = a.clone();		
		int minIndex = Util.minIndex(testData, 0, testData.length);
		int actual = testData[minIndex];		
		assertEquals(expected, actual);			
	}
		
	// c
	// Array instance is null 
	@Test (expected = NullPointerException.class)
	public final void testNullPointerExceptionIsThrown() {
		 fail("Blocked");		
		 Util.minIndex(null, 0, 1);
	}
	
	// d
	// Array index is out of bound 
	@Test (expected = ArrayIndexOutOfBoundsException.class)
	public final void testArrayIndexOutOfBoundsExceptionIsThrown() {
		 fail("Blocked");		
		 Util.minIndex(new int[0], 1, 2);
	}
	
	// e
	// Single element array as input
	// Array length is zero
	@Test
	public final void testThree() {
		fail("Blocked");		
		int expected = 0;
		int [] testData = {0};		
		int minIndex = Util.minIndex(testData, 0, 0);
		int actual = testData[minIndex];		
		assertEquals(expected, actual);			
	}
}
