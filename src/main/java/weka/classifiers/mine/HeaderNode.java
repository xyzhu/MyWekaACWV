package weka.classifiers.mine;

import java.util.LinkedList;

import weka.core.Instance;

public class HeaderNode {
	int attr;
	double value;
	int count;
	LinkedList<TreeNode> link;
	
	public HeaderNode(){
		this.attr = -1;
		this.value = -1;
		this.count = 0;
		link = new LinkedList<TreeNode>();
	}
	public HeaderNode(int attr, double value, int count){
		this.attr = attr;
		this.value = value;
		this.count = count;
		link = new LinkedList<TreeNode>();
	}
/*
 * judge if an instance covers the header node
 */
	public boolean containedBy(Instance instance) {
		   if (instance.isMissing(attr))
		        return false;
		   if (instance.value(attr) != value)
			   return false;
		    return true;
		  }
	
	public void addLink(TreeNode tn){
		link.add(tn);
	}
	
}
