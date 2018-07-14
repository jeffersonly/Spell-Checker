package sjsu.ly.cs146.project3;

//RedBlackTree class, used potika's template
public class RedBlackTree<Key extends Comparable<Key>> {	
		protected static RedBlackTree.Node<String> root;

		public static class Node<Key extends Comparable<Key>> { //changed to static 
			
			  Key key;  		  
			  Node<String> parent;
			  Node<String> leftChild;
			  Node<String> rightChild;
			  boolean isRed;
			  int color;
			  
			  public Node(Key data){
				  this.key = data;
				  leftChild = null;
				  rightChild = null;
			  }		
			  
			  public int compareTo(Node<Key> n){ 	//this < that  <0
			 		return key.compareTo(n.key);  	//this > that  >0
			  }
			  
			  public boolean isLeaf(){
				  if (this.equals(root) && this.leftChild == null && this.rightChild == null) return true;
				  if (this.equals(root)) return false;
				  if (this.leftChild == null && this.rightChild == null){
					  return true;
				  }
				  return false;
			  }
		}

		 public boolean isLeaf(RedBlackTree.Node<String> n){
			  if (n.equals(root) && n.leftChild == null && n.rightChild == null) return true;
			  if (n.equals(root)) return false;
			  if (n.leftChild == null && n.rightChild == null){
				  return true;
			  }
			  return false;
		  }
		
		public interface Visitor<Key extends Comparable<Key>> {
			/**
			This method is called at each node.
			@param n the visited node
			*/
			void visit(Node<Key> n);  
		}
		
		public void visit(Node<Key> n){
			System.out.println(n.key);
		}
		
		public void printTree(){  //preorder: visit, go left, go right
			RedBlackTree.Node<String> currentNode = root;	
			printTree(currentNode);
		}
		
		public void printTree(RedBlackTree.Node<String> node){
			System.out.print(node.key);
			if (node.isLeaf()){
				return;
			}
			
			if(node.leftChild != null)
			{
				printTree(node.leftChild);
			}
			if(node.rightChild != null)
			{
				printTree(node.rightChild);
			}
		}
		
		// place a new node in the RB tree with data the parameter and color it red. 
				public void addNode(String data){  	//this < that  <0.  this > that  >0
					Node<String> holder = null;
					Node<String> current = root;
					Node<String> addedNode = new Node<String>(data);
					while(current != null)
					{
						holder = current;
						if(addedNode.compareTo(current) < 0) //if addednode is less than current, go to current's left child
						{
							current = current.leftChild;
						}
						else
						{
							current = current.rightChild; //if addednode is greater than current, go to currents right child
						}
					}
					addedNode.parent = holder;
					if(holder == null)
					{
						root = addedNode;
					}
					else if(addedNode.compareTo(holder) < 0)
					{
						holder.leftChild = addedNode;
					}
					else
					{
						holder.rightChild = addedNode;
					}
					addedNode.leftChild = null;
					addedNode.rightChild = null;
					addedNode.color = 0;
					fixTree(addedNode);
				}
				
				public void insert(String data){
					addNode(data);
				}
		
		//Search up a node in the RBT
				//Recursively searches for a node in RBT
		public RedBlackTree.Node<String> lookup(Node current, String k){ 
			Node<String> searchedNode = new Node<String>(k);
			if(current == null || searchedNode.compareTo(current) == 0)
			{
				return current;
			}
			if(searchedNode.compareTo(current) < 0)
			{
				return lookup(current.leftChild, k);
			}
			return lookup(current.rightChild, k);
		}
	 	
		//Returns the sibling of a node in the RBT
		public RedBlackTree.Node<String> getSibling(RedBlackTree.Node<String> n){  
			Node<String> parentOfN = n.parent;
			if(parentOfN != null)
			{
				if(parentOfN.compareTo(n) < 0)
				{
					return parentOfN.leftChild;
				}
				else if(parentOfN.compareTo(n) > 0)
				{
					return parentOfN.rightChild;
				}
			}
			return null;
		}
		
		//Returns the aunt of a node in the RBT
		public RedBlackTree.Node<String> getAunt(RedBlackTree.Node<String> n){
			Node<String> grandparent = getGrandparent(n);
			if(grandparent != null)
			{
				if(grandparent.compareTo(n) < 0)
				{
					return grandparent.leftChild;
				}
				else if(grandparent.compareTo(n) > 0)
				{
					return grandparent.rightChild;
				}
			}
			return null;
		}
		
		//Returns the grandparent of a node in the RBT
		public RedBlackTree.Node<String> getGrandparent(RedBlackTree.Node<String> n){
			if(n.parent != null)
			{
				if(n.parent.parent != null)
				{
					return n.parent.parent;
				}
			}
			return null;
		}
		
		public void rotateLeft(RedBlackTree.Node<String> n){
			//let x and y be the main nodes
			//where the RBT is set up so it is X A Y B C
			//		X
			//	A	   Y
			//	 	  B  C
			//rotating left turns it to 
			//		Y
			//	 X	  C
			// A   B
			//The node being rotated is node n, let n be x
			Node<String> rightChildOfN = n.rightChild;
			n.rightChild = rightChildOfN.leftChild; 			//turns y's left subtree into x's right subtree
			if(rightChildOfN.leftChild != null)
			{
				rightChildOfN.leftChild.parent = n;
			}
			rightChildOfN.parent = n.parent;					//links x's parents to y
			if(n.parent == null)
			{
				root = rightChildOfN;	
			}
			else if(n == n.parent.leftChild)
			{
				n.parent.leftChild = rightChildOfN; 
			}
			else
			{
				n.parent.rightChild = rightChildOfN;
				
			}
			rightChildOfN.leftChild = n;					//put x on y's left
			n.parent = rightChildOfN;
		}
		
		public void rotateRight(RedBlackTree.Node<String> n){
			//right rotation is the reverse of left rotation
			Node<String> leftChildOfN = n.leftChild;
			n.leftChild = leftChildOfN.rightChild;
			if(leftChildOfN.rightChild != null)
			{
				leftChildOfN.rightChild.parent = n;
			}
			leftChildOfN.parent = n.parent;
			if(n.parent == null)
			{
				root = leftChildOfN;
			}
			else if(n == n.parent.rightChild)
			{
				n.parent.rightChild = leftChildOfN;
			}
			else
			{
				n.parent.leftChild = leftChildOfN;
				
			}
			leftChildOfN.rightChild = n;
			n.parent = leftChildOfN;
		}
		
		//Followed the pseudocode in the book, turned it into code
		public void fixTree(RedBlackTree.Node<String> current) {
			if(current == root)
			{
				current.color = 1;
			}
			else if(current.color == 0 && current.parent.color == 0)
			{
				if(getAunt(current) == null || getAunt(current).color == 1)
				{
					if(current.parent == getGrandparent(current).leftChild && current == current.parent.rightChild)
					{
						rotateLeft(current.parent);
						current = current.parent;
						fixTree(current);
					}
					else if(current.parent == getGrandparent(current).rightChild && current == current.parent.leftChild)
					{
						rotateRight(current.parent);
						current = current.parent;
						fixTree(current);
					}
					else if(current.parent == getGrandparent(current).leftChild && current == current.parent.leftChild)
					{
						current.parent.color = 1;
						getGrandparent(current).color = 0;
						rotateRight(getGrandparent(current));
					}
					else if(current.parent == getGrandparent(current).rightChild && current == current.parent.rightChild)
					{
						current.parent.color = 1;
						getGrandparent(current).color = 0;
						rotateLeft(getGrandparent(current));
					}
				}
				else
				{
					current.parent.color = 1;
					getAunt(current).color = 1;
					getGrandparent(current).color = 0;
					current = getGrandparent(current);
					fixTree(current);
				}
			}
		}
		
		public boolean isEmpty(RedBlackTree.Node<String> n){
			if (n.key == null){
				return true;
			}
			return false;
		}
		 
		public boolean isLeftChild(RedBlackTree.Node<String> parent, RedBlackTree.Node<String> child)
		{
			if (child.compareTo(parent) < 0 ) {//child is less than parent
				return true;
			}
			return false;
		}
		
		
		public void preOrderVisit(Visitor<String> v) {
		   	preOrderVisit(root, v);
		}
		 
		 
		private static void preOrderVisit(RedBlackTree.Node<String> n, Visitor<String> v) {
		  	if (n == null) {
		  		return;
		  	}
		  	v.visit(n);
		  	preOrderVisit(n.leftChild, v);
		  	preOrderVisit(n.rightChild, v);
		}	
		
		public static void main(String[] args)
		{
			//RedBlackTree rbt = new RedBlackTree();
			/**
			rbt.insert("D");
	        rbt.insert("B");
	        rbt.insert("A");
	        rbt.insert("Q");
	        rbt.insert("F");
	        rbt.insert("E");
	        rbt.insert("C");
	        rbt.insert("G");
	        rbt.insert("I");
	        rbt.insert("J");
			rbt.printTree();
			System.out.println("");
			System.out.println(rbt.lookup("B").color + " expected: 1");
			System.out.println(rbt.lookup("B").rightChild.key + " expected: E");
			System.out.println(rbt.lookup("B").leftChild.key + " expected: A");
			System.out.println("");
			System.out.println(rbt.lookup("A").color + " expected: 1");
			//System.out.println(rbt.lookup("A").rightChild.key + " expected: null");
			//System.out.println(rbt.lookup("A").leftChild.key + " expected: null");
			System.out.println("");
			System.out.println(rbt.lookup("E").color + " expected: 0");
			System.out.println(rbt.lookup("E").rightChild.key + " expected: I");
			System.out.println(rbt.lookup("E").leftChild.key + " expected: C");
			System.out.println("");
			System.out.println(rbt.lookup("C").color + " expected: 1");
			//System.out.println(rbt.lookup("C").rightChild.key + " expected: null");
			//System.out.println(rbt.lookup("C").leftChild.key + " expected: null");
			System.out.println("");
			System.out.println(rbt.lookup("I").color + " expected: 1");
			System.out.println(rbt.lookup("I").rightChild.key + " expected: J");
			System.out.println(rbt.lookup("I").leftChild.key + " expected: G");
			System.out.println("");
			System.out.println(rbt.lookup("J").color + " expected: 0");
			//System.out.println(rbt.lookup("J").rightChild.key + " expected: null");
			//System.out.println(rbt.lookup("J").leftChild.key + " expected: null");
			System.out.println("");
			
			
			rbt.insert("D");
	        rbt.insert("B");
	        rbt.insert("A");
	        rbt.insert("C");
	        rbt.insert("F");
	        rbt.insert("E");
	        rbt.insert("H");
	        rbt.insert("G");
	        rbt.insert("I");
	        rbt.insert("J");
	        rbt.printTree();**/
	        //rbt.rotateRight(root);
	        //System.out.println("");
	       // rbt.printTree();
	        

	        
	       // System.out.println("");
	        
			
			
			
			//System.out.println(rbt.lookup("D").rightChild.key);
			
			//System.out.println(rbt.lookup("D").parent.key);
			//System.out.println(rbt.lookup("B").parent.key + " expected:D");
			//System.out.println(rbt.lookup("A").parent.key + " expected:B");
			//System.out.println(rbt.lookup("Q").parent.key + " expected:A");
			//System.out.println(rbt.lookup("F").parent.key + " expected:Q");
			//System.out.println(rbt.lookup("E").parent.key + " expected:F");
			//System.out.println(rbt.lookup("C").parent.key + " expected:E");
			//System.out.println(rbt.lookup("G").parent.key + " expected:C");
			//System.out.println(rbt.lookup("I").parent.key + " expected:G");
			//System.out.println(rbt.lookup("J").parent.key + " expected:I");
		}
}


