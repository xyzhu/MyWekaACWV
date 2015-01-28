package weka.classifiers.mine;

import java.util.Iterator;

import weka.core.FastVector;
import weka.core.Instance;

public class CpbList {

	public int numClass, numAttr;

	public CpbList(int num_class, int num_attr){
		numClass = num_class;
		numAttr = num_attr;
	}

	public FastVector genCpblist(Instance instance, FastVector headertable,
			int index) {
		FastVector cpb = new FastVector(), cpblist = new FastVector();
		HeaderNode hn = (HeaderNode)headertable.elementAt(index);
		Iterator<TreeNode> it = hn.link.iterator();
		TreeNode tn;
		CpbItemSet cpbItem;
		while(it.hasNext()){
			tn = it.next();
			while(tn.attr!=-1){
				cpbItem = new CpbItemSet(0, numClass, numAttr);
				for(int i=0;i<numClass;i++){
					cpbItem.class_count[i] = tn.classcount[i];
				}
				for(int j=0;j<numAttr;j++){
					cpbItem.setItemAt(-1, j);;
				}
				cpbItem.setItemAt(tn.value, tn.attr);
				cpb.addElement(cpbItem);
				tn = tn.father;
			}
			cpblist.addElement(cpb);
			cpb = new FastVector();
		}
					return cpblist;
		}



	}
