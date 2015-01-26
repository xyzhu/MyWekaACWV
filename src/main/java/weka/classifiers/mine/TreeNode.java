package weka.classifiers.mine;

import java.util.LinkedList;


public class TreeNode {

	public int attr;
	public double value;
	public TreeNode father;
	public LinkedList<TreeNode> child;
	public int classcount[];
	public TreeNode(int attr, double value, int numClass){
		this.attr = attr;
		this.value = value;
		child = new LinkedList<TreeNode>();
		classcount = new int[numClass];
	}
	public boolean equal(HeaderNode hn) {
		if(hn.attr == this.attr&&hn.value==this.value)
			return true;
		else
			return false;
	}
	public void addChild(TreeNode tnode) {
		child.add(tnode);
		
	}

}
