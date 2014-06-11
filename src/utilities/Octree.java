package utilities;

import java.util.ArrayList;
import java.util.List;

public class Octree<E> {
	private float width, height, depth;
	private int elementCount = 0;
	private TreeNode<E> root;

	public Octree(float width, float height, float depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		root = new TreeNode<E>(0f, 0f, 0f, width, height, depth);
	}

	public int getElementCount() {
		return elementCount;
	}

	public boolean insert(E e, float ex, float ey, float ez, float exScale,
			float eyScale, float ezScale) {
		this.elementCount++;	

		if (contains(ex, ey, ez, exScale, eyScale, ezScale, 0f, 0f, 0f,
				this.width, this.height, this.depth)) {
			root.insert(e, ex, ey, ez, exScale, eyScale, ezScale);
			return true;
		}
		return false;
	}

	public List<E> query(float ex, float ey, float ez, float exScale, float eyScale,
			float ezScale) {
		if (contains(ex, ey, ez, exScale, eyScale, ezScale, 0f, 0f, 0f,
				this.width, this.height, this.depth)) {
			ArrayList<E> toReturn = new ArrayList<E>();
			toReturn = root.query(toReturn, ex, ey, ez, exScale, eyScale, ezScale);
			return toReturn;
		}
		System.out.println("Query box out of bounds");
		return null;
	}

	private boolean contains(float x1, float y1, float z1, float w1, float h1,
			float d1, float x2, float y2, float z2, float w2, float h2, float d2) {
		if ((x1 >= x2 && x1 + w1 < x2 + w2) && (y1 >= y2 && y1 + h1 < y2 + h2)
				&& (z1 >= z2 && z1 + d1 < z2 + d2))
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

	private class TreeNode<Elem> {
		private class InsertObject<El>{
			private El element;
			private float xScale, yScale, zScale;
			private float x, y, z;
			public InsertObject(El e, float xScale, float yScale, float zScale, float x, float y, float z){
				this.element = e;
				this.xScale = xScale;
				this.yScale = yScale;
				this.zScale = zScale;
				this.x = x;
				this.y = y;
				this.z = z;
			}
		}
		public static final int UPPER_TOP_RIGHT = 0;
		public static final int UPPER_TOP_LEFT = 1;
		public static final int UPPER_BOTTOM_RIGHT = 2;
		public static final int UPPER_BOTTOM_LEFT = 3;
		public static final int LOWER_TOP_RIGHT = 4;
		public static final int LOWER_TOP_LEFT = 5;
		public static final int LOWER_BOTTOM_RIGHT = 6;
		public static final int LOWER_BOTTOM_LEFT = 7;
		private ArrayList<TreeNode<Elem>> children;
		private ArrayList<InsertObject<Elem>> xList = new ArrayList<>();
		private ArrayList<InsertObject<Elem>> yList = new ArrayList<>();
		private ArrayList<InsertObject<Elem>> zList = new ArrayList<>();

		private float xScale, yScale, zScale;
		private float x, y, z;
		private float xLine, yLine, zLine;

		public TreeNode(float x, float y, float z, float width, float height,
				float depth) {
			this.x = x;
			this.y = y;
			this.z = z;
			this.xScale = width;
			this.yScale = height;
			this.zScale = depth;
			xLine = x + xScale / 2;
			yLine = y + yScale / 2;
			zLine = z + zScale / 2;
			children = new ArrayList<TreeNode<Elem>>();
			for (int i = 0; i < 8; i++)
				children.add(null);
		}
		
		private boolean interSecting(InsertObject<Elem> e, float ex, float ey, float ez, float exScale, float eyScale, float ezScale){
			return ((ex >= e.x && ex <= e.x+e.xScale) || (ex+exScale >= e.x && ex+exScale <= e.x+e.xScale) || (ex <= e.x && ex+exScale > e.x)) && 
					((ey >= e.y && ey <= e.y+e.yScale) || (ey+eyScale >= e.y && ey+eyScale <= e.y+e.yScale) || (ex <= e.x && ex+exScale > e.x)) && 
					((ez >= e.z && ez <= e.z+e.zScale) || (ez+ezScale >= e.z && ez+ezScale <= e.z+e.zScale) || (ex <= e.x && ex+exScale > e.x));
		}

		public ArrayList<Elem> query(ArrayList<Elem> accumulator, float ex, float ey, float ez, float exScale,
				float eyScale, float ezScale) {
			for(InsertObject<Elem> io : xList)
				if(interSecting(io, ex, ey, ez, exScale, eyScale, ezScale))
					accumulator.add(io.element);
			for(InsertObject<Elem> io : yList)
				if(interSecting(io, ex, ey, ez, exScale, eyScale, ezScale))
					accumulator.add(io.element);
			for(InsertObject<Elem> io : zList)
				if(interSecting(io, ex, ey, ez, exScale, eyScale, ezScale))
					accumulator.add(io.element);
			
			if(children.get(LOWER_BOTTOM_LEFT) != null && interSecting(new InsertObject<Elem>(null, x, y, z, xScale/2, yScale/2, zScale/2), ex, ey, ez, exScale, eyScale, ezScale))
				accumulator = children.get(LOWER_BOTTOM_LEFT).query(accumulator, ex, ey, ez, exScale, eyScale, ezScale);
			
			if(children.get(LOWER_TOP_LEFT) != null && interSecting(new InsertObject<Elem>(null, x, y, zLine, xScale/2, yScale/2, zScale/2), ex, ey, ez, exScale, eyScale, ezScale))
				accumulator = children.get(LOWER_TOP_LEFT).query(accumulator, ex, ey, ez, exScale, eyScale, ezScale);
			
			if(children.get(UPPER_BOTTOM_LEFT) != null && interSecting(new InsertObject<Elem>(null, x, yLine, z, xScale/2, yScale/2, zScale/2), ex, ey, ez, exScale, eyScale, ezScale))
				accumulator = children.get(UPPER_BOTTOM_LEFT).query(accumulator, ex, ey, ez, exScale, eyScale, ezScale);
			
			if(children.get(UPPER_TOP_LEFT) != null && interSecting(new InsertObject<Elem>(null, x, yLine, zLine, xScale/2, yScale/2, zScale/2), ex, ey, ez, exScale, eyScale, ezScale))
				accumulator = children.get(UPPER_TOP_LEFT).query(accumulator, ex, ey, ez, exScale, eyScale, ezScale);

			if(children.get(LOWER_BOTTOM_RIGHT) != null && interSecting(new InsertObject<Elem>(null, xLine, y, z, xScale/2, yScale/2, zScale/2), ex, ey, ez, exScale, eyScale, ezScale))
				accumulator = children.get(LOWER_BOTTOM_RIGHT).query(accumulator, ex, ey, ez, exScale, eyScale, ezScale);
			
			if(children.get(LOWER_TOP_RIGHT) != null && interSecting(new InsertObject<Elem>(null, xLine, y, zLine, xScale/2, yScale/2, zScale/2), ex, ey, ez, exScale, eyScale, ezScale))
				accumulator = children.get(LOWER_TOP_RIGHT).query(accumulator, ex, ey, ez, exScale, eyScale, ezScale);
			
			if(children.get(UPPER_BOTTOM_RIGHT) != null && interSecting(new InsertObject<Elem>(null, xLine, yLine, z, xScale/2, yScale/2, zScale/2), ex, ey, ez, exScale, eyScale, ezScale))
				accumulator = children.get(UPPER_BOTTOM_RIGHT).query(accumulator, ex, ey, ez, exScale, eyScale, ezScale);
			
			if(children.get(UPPER_TOP_RIGHT) != null && interSecting(new InsertObject<Elem>(null, xLine, yLine, zLine, xScale/2, yScale/2, zScale/2), ex, ey, ez, exScale, eyScale, ezScale))
				accumulator = children.get(UPPER_TOP_RIGHT).query(accumulator, ex, ey, ez, exScale, eyScale, ezScale);

			return accumulator;
		}

		private TreeNode<Elem> findOctant(float ex, float ey, float ez) {
			if (ex <= xLine) {
				if (ey <= yLine) {
					if (ez <= zLine) {
						TreeNode<Elem> node = children.get(LOWER_BOTTOM_LEFT);
						if (node == null) {
							children.set(LOWER_BOTTOM_LEFT, new TreeNode<Elem>(x, y, z, xScale/2, yScale/2, zScale/2));
						}
						return node;
						
					} 
					else {
						TreeNode<Elem> node = children.get(LOWER_TOP_LEFT);
						if (node == null) {
							children.set(LOWER_TOP_LEFT, new TreeNode<Elem>(x, y, zLine, xScale/2, yScale/2, zScale/2));
						}
						return node;
					}
				} 
				else {
					if (ez <= zLine) {
						TreeNode<Elem> node = children.get(UPPER_BOTTOM_LEFT);
						if (node == null) {
							children.set(UPPER_BOTTOM_LEFT, new TreeNode<Elem>(x, yLine, z, xScale/2, yScale/2, zScale/2));
						}
						return node;
					} 
					else {
						TreeNode<Elem> node = children.get(UPPER_TOP_LEFT);
						if (node == null) {
							children.set(UPPER_TOP_LEFT, new TreeNode<Elem>(x, yLine, zLine, xScale/2, yScale/2, zScale/2));
						}
						return node;
					}
				}
			} 
			else {
				if (ey <= yLine) {
					if (ez <= zLine) {
						TreeNode<Elem> node = children.get(LOWER_BOTTOM_RIGHT);
						if (node == null) {
							children.set(LOWER_BOTTOM_RIGHT, new TreeNode<Elem>(xLine, y, z, xScale/2, yScale/2, zScale/2));
						}
						return node;
					} 
					else {
						TreeNode<Elem> node = children.get(LOWER_TOP_RIGHT);
						if (node == null) {
							children.set(LOWER_TOP_RIGHT, new TreeNode<Elem>(xLine, y, zLine, xScale/2, yScale/2, zScale/2));
						}
						return node;
					}
				} 
				else {
					if (ez <= zLine) {
						TreeNode<Elem> node = children.get(UPPER_BOTTOM_RIGHT);
						if (node == null) {
							children.set(UPPER_BOTTOM_RIGHT, new TreeNode<Elem>(xLine, yLine, z, xScale/2, yScale/2, zScale/2));
						}
						return node;
					} 
					else {
						TreeNode<Elem> node = children.get(UPPER_TOP_RIGHT);
						if (node == null) {
							children.set(UPPER_TOP_RIGHT, new TreeNode<Elem>(xLine, yLine, zLine, xScale/2, yScale/2, zScale/2));
						}
						return node;
					}
				}
			}
		}

		private void insert(Elem e, float ex, float ey, float ez,
				float exScale, float eyScale, float ezScale) {

			if (ex <= xLine && ex + exScale > xLine) {
				xList.add(new InsertObject<Elem>(e, exScale, eyScale, ezScale, ex, ey, ez));
			} 
			else if (ey <= yLine && ey + eyScale > yLine) {
				yList.add(new InsertObject<Elem>(e, exScale, eyScale, ezScale, ex, ey, ez));
			} 
			else if (ez <= zLine && ez + ezScale > zLine) {
				zList.add(new InsertObject<Elem>(e, exScale, eyScale, ezScale, ex, ey, ez));
			} 
			else {
				TreeNode<Elem> nextNode = findOctant(ex, ey, ez);
				nextNode.insert(e, ex, ey, ez, exScale, eyScale, ezScale);
			}
		}
	}
}
