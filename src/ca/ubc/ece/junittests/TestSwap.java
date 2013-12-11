package ca.ubc.ece.junittests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import util.Util;

import java.util.Arrays;

public class TestSwap {

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
		
	// 1
	// Non-empty array as input
	// Correct indices
	@Test
	public final void testOne() {	
		fail("Blocked");				
		int [] a = new int[2];		
		a[0] = 1;
		a[1] = 2;		
		Util.swap(0, 1, a);				
		assertEquals(a[0], 2);
		assertEquals(a[1], 1);
	}
	
	// 2
	// Non-empty array as input
	// Array index is out of bound 
	@Test (expected = ArrayIndexOutOfBoundsException.class)
	public final void testArrayIndexOutOfBoundsExceptionIsThrown() {
		 fail("Blocked");		
		 int [] a = new int[2];		 
		 a[0] = 1;
		 a[1] = 2;		
		 Util.swap(0, 2, a);		
	}
	
	// 3
	// Array instance is null 
	@Test (expected = NullPointerException.class)
	public final void testNullPointerExceptionIsThrown() {
		 fail("Blocked");		 
		 Util.swap(0, 1, null);
	}
	
	// 4
	// Non-empty array as input
	// Incorrect indices
	@Test
	public final void testTwo() {	
		fail("Blocked");				
		int [] a = new int[2];		
		a[0] = 1;
		a[1] = 2;		
		Util.swap(0, -1, a);		
	}
}
