package weka.classifiers.mine;

import java.util.Iterator;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class CCFP {
	Instances instances, onlyClass;
	FastVector headertable;
	Tree t;
	double minsup, minconv, upperBoundMinSupport;
	int ruleNumLimit;
	int numClass, numInstances;

	public CCFP(Instances insts, Instances onlyclass, double minsup, double minconv, double upperBoundMinSupport,
			int ruleNumLimit) {
		this.instances = insts;
		this.onlyClass = onlyclass;
		this.minsup = minsup;
		this.minconv = minconv;
		this.upperBoundMinSupport = upperBoundMinSupport;
		this.ruleNumLimit = ruleNumLimit;
		numClass = onlyclass.numDistinctValues(0);
		numInstances = insts.numInstances();
	}
	public void buildTree() throws Exception{
		HeaderTable ht = new HeaderTable();
		headertable = ht.build(instances, numClass, minsup, upperBoundMinSupport);
		Tree t = new Tree(instances, onlyClass, numClass);
		t.build(headertable);
		for(int i=0;i<headertable.size();i++){
			HeaderNode hn = (HeaderNode)headertable.elementAt(i);
			System.out.println(hn.attr+"  "+hn.value+"  "+hn.count+"  "+hn.classcount[0]+"  "+hn.classcount[1]);
		}
	}


	public FastVector headertable() {
		return headertable;
	}
	public int[] vote(Instance instance) {
		int vote[] = new int[numClass];
		int numHeaderNode = headertable.size();
		double sup, conf, conv;
		HeaderNode hn;
		for(int i=numHeaderNode-1; i>=0;i--){
			hn = (HeaderNode)headertable.elementAt(i);
			if(hn.containedBy(instance)){
				for(int j=0;j<numClass;j++){
					sup = (double)hn.classcount[j]/numInstances;
					conf = (double)hn.classcount[j]/hn.count;
					//sup_class = (double)hn.classcount[]
				}
			}
		}
		return vote;
	}
}
