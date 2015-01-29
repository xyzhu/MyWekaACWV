package weka.classifiers.mine;

import java.util.Iterator;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class CpbList {

	public int numClass, numAttr, numHashValue;
	HashAttribute hashAttribute;

	public CpbList(int[] attrvalue){
		numAttr = attrvalue.length;
		for(int i=0;i<numAttr;i++){
			numHashValue+=attrvalue[i];
		}
		hashAttribute = new HashAttribute(attrvalue);
	}

	public FastVector genCpblist(Instance instance, FastVector headertable,
			int index) {
		FastVector cpb = new FastVector(), cpblist = new FastVector();
		HeaderNode hn = (HeaderNode)headertable.elementAt(index);
		Iterator<TreeNode> it = hn.link.iterator();
		TreeNode tn;
		int count;
		CpbItemSet cpbItem = new CpbItemSet(0, numClass, numAttr);
		while(it.hasNext()){
			tn = it.next();
			count = tn.count();
			while(tn.attr!=-1){
				for(int i=0;i<numClass;i++){
					cpbItem.class_count[i] = tn.classcount[i];
				}
				for(int j=0;j<numAttr;j++){
					cpbItem.setItemAt(-1, j);;
				}
				cpbItem.setItemAt(tn.value, tn.attr);
				hashAttribute.increase(tn.attr, tn.value, count);
				cpb.addElement(cpbItem);
				tn = tn.father;
			}
			cpblist.addElement(cpb);
			cpb = new FastVector();
		}
		return cpblist;
	}



}
