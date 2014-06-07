package utilities;

import java.util.ArrayList;
import java.util.List;

public class Octree<E> {
	private float width, height, depth;
	private int elementCount = 0;
	private treeNode<E> root;
	public Octree(float width, float height, float depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		root = new treeNode<E>(0f, 0f, 0f, width, height, depth);
	}

	public int getElementCount() {
		return elementCount;
	}

	public boolean insert(E e, float x, float y, float z, float width, float height, float depth) {
		this.elementCount++;
		//TODO continue implementation
		return contains(x,y,z,width,height,depth, 0f,0f,0f, this.width, this.height, this.depth);
	}

	public List<E> query(float x, float y, float z, float width, float height, float depth) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean contains(float x1, float y1, float z1, float w1, float h1, float d1, float x2, float y2, float z2, float w2, float h2, float d2){
		if((x1 >= x2 && x1+w1 < x2+w2) && (y1 >= y2 && y1+h1 < y2+h2) && (z1 >= z2 && z1+d1 < z2+d2))
			return true;
		return false;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getDepth() {
		return depth;
	}
	
	private class treeNode<Elem>{
		public static final int UPPER_TOP_RIGHT = 0;
		public static final int UPPER_TOP_LEFT = 1;
		public static final int UPPER_BOTTOM_RIGHT = 2;
		public static final int UPPER_BOTTOM_LEFT = 3;
		public static final int LOWER_TOP_RIGHT = 4;
		public static final int LOWER_TOP_LEFT = 5;
		public static final int LOWER_BOTTOM_RIGHT = 6;
		public static final int LOWER_BOTTOM_LEFT = 7;
		private ArrayList<treeNode<Elem>> children;

		private float width, height, depth;
		private float x, y, z;
		public treeNode(float x, float y, float z, float width, float height, float depth){
			this.x = x;
			this.y = y;
			this.z = z;
			this.width = width;
			this.height = height;
			this.depth = depth;
			children = new ArrayList<treeNode<Elem>>();
			for(int i = 0; i < 8; i++)
				children.add(null);
		}
	}
}
