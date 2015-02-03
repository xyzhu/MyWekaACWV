package weka.classifiers.mine;

import java.util.Iterator;

import weka.core.FastVector;
import weka.core.Instance;

public class CpbList {

	public int numClass, numAttr, numHashValue;
	HashAttribute hashAttribute;
	int [] attrvalue;

	public CpbList(int[] attrvalue, int numClass){
		numAttr = attrvalue.length;
		this.numClass = numClass;
		for(int i=0;i<numAttr;i++){
			numHashValue+=attrvalue[i];
		}
		this.attrvalue = attrvalue;
	}

	//generate the conditional pattern base list
	public FastVector genCpblist(Instance instance, FastVector headertable,
			int index) {
		hashAttribute = new HashAttribute(attrvalue);
		FastVector cpblist = new FastVector();
		HeaderNode hn = (HeaderNode)headertable.elementAt(index);
		Iterator<TreeNode> it = hn.link.iterator();//all the tree node linked to hn
		TreeNode tn;
		int count;
		CpbItemSet cpbItem;
		while(it.hasNext()){
			cpbItem = new CpbItemSet(0, numClass, numAttr);
			tn = it.next();
			count = tn.count();
			for(int i=0;i<numClass;i++){
				cpbItem.class_count[i] = tn.classcount[i];
			}
			tn = tn.father;
			while(tn.attr!=-1){//tn is not the root of the tree
				cpbItem.setItemAt(tn.value, tn.attr);
				hashAttribute.increase(tn.attr, tn.value, count);//hashAttribute hold the count
				tn = tn.father;
			}
			cpblist.addElement(cpbItem);
		}
		return cpblist;
	}



}
