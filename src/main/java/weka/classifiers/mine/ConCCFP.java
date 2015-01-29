package weka.classifiers.mine;

import weka.core.FastVector;

public class ConCCFP {
	HeaderTable headertable;
	Tree t;

	public ConCCFP(FastVector cpblist, int[] hashattr, int numClass, double minsup, double minconv, int necSupport, int []attrvalue) {
		HeaderTable ht = new HeaderTable();
		ht.buildConTreeHead(cpblist, hashattr, numClass ,minsup, minconv, necSupport, attrvalue);
	}

	public void buildTree() {
		// TODO Auto-generated method stub
		
	}

}
