package testCases;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import utilities.Octree;

public class OctreeTest {

	@Test
	public void initialize() {
		Octree<Integer> tree = new Octree<Integer>(5f,5f,5f);
		assertNotNull(tree);
	}

	@Test
	public void insert() {
		Octree<Integer> tree = new Octree<Integer>(5f,5f,5f);
		assertEquals(0, tree.getElementCount());
		assertTrue(tree.insert(3, 1f, 1f, 2f, 3f, 2f, 1f));
		assertEquals(1, tree.getElementCount());
		assertTrue(tree.insert(3, 2f, 1f, 2f, 2.9f, 2f, 1f));
		assertEquals(2, tree.getElementCount());
		assertFalse(tree.insert(0, -1f,1f,1f, 1f,1f,1f));
		assertFalse(tree.insert(0, 6f,1f,1f, 1f,1f,1f));
		assertFalse(tree.insert(0, 5f,1f,1f, 1f,1f,1f));
	}
	
	@Test
	public void query() {
		Octree<Integer> tree = new Octree<Integer>(5f,5f,5f);
		tree.insert(3, 1f, 1f, 2f, 3f, 2f, 1f);
		List<Integer> l = tree.query(1f,1f,2f, 1f,1f,1f);
		assertEquals(3, (int)l.get(0));
		l = tree.query(0f,0f,0f, 0.5f,0.5f,0.5f);
		assertEquals(0, l.size());
		tree.insert(2, 1f, 2f, 2f, 3f, 2f, 1f);
		l = tree.query(0f,0f,0f, 4.9f,4.9f,4.9f);
		assertEquals(2, l.size());
	}
}
