package weka.classifiers.mine;

import weka.core.FastVector;
import weka.core.Instance;

public class HeaderNode {
	int attr;
	double value;
	int count;
	FastVector link;
	
	public HeaderNode(){
		this.attr = -1;
		this.value = -1;
		this.count = 0;
	}
	public HeaderNode(int attr, double value, int count){
		this.attr = attr;
		this.value = value;
		this.count = count;
		link = new FastVector();
	}

	public boolean containedBy(Instance instance) {
		   if (instance.isMissing(attr))
		        return false;
		   if (instance.value(attr) != value)
			   return false;
		    return true;
		  }
}
