package weka.classifiers.mine;

import java.util.LinkedList;

public class TreeNode {

	public int attr;
	public double value;
	public TreeNode father;
	public LinkedList<TreeNode> child;
	public int classcount[];
	public TreeNode(int attr, double value){
		this.attr = attr;
		this.value = value;
	}

}
