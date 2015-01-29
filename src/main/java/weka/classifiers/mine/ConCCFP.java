package weka.classifiers.mine;

import weka.core.FastVector;

public class ConCCFP {
	FastVector headertable;
	FastVector cpblist;
	Tree t;
	int numClass;

	public ConCCFP(FastVector cpblist, int numClass) {
		this.cpblist = cpblist;
		this.numClass = numClass;
	}
	FastVector buildConTreeHead(int[] hashattr, double minsup, double minconv, int necSupport, int []attrvalue){
		HeaderTable ht = new HeaderTable();
		headertable = ht.buildConTreeHead(cpblist, hashattr, numClass, necSupport, attrvalue);
		return headertable;
	}

	public Tree contreeBuild() {
		t = new Tree(numClass);
		t.contreebuild(cpblist,headertable);
		for(int i=0;i<headertable.size();i++){
			HeaderNode hn = (HeaderNode)headertable.elementAt(i);
			System.out.println(hn.attr+"  "+hn.value+"  "+hn.count+"  "+hn.classcount[0]+"  "+hn.classcount[1]);
		}
		return t;
		
	}

}
