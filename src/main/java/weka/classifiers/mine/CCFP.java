package weka.classifiers.mine;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class CCFP {
	Instances instances, onlyClass;
	FastVector headertable;
	Tree t;
	double minsup, minconv, upperBoundMinSupport;
	int ruleNumLimit, numRule;
	int numClass, numInstances;
	double []classSup;

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
		classSup = calClassSup(onlyclass);
	}
	private double[] calClassSup(Instances onlyclass2) {
		double class_sup[] = new double[numClass];
		int class_count[] = new int[numClass];
		int class_label, num_instances = onlyclass2.numInstances();
		for(int i=0;i<num_instances;i++){
			class_label = (int) onlyclass2.instance(i).value(0);
			class_count[class_label]++;
		}
		for(int j=0;j<numClass;j++){
			class_sup[j] = (double)class_count[j]/num_instances;
		}
		return class_sup;
	}
	public void buildTree() throws Exception{
		HeaderTable ht = new HeaderTable();
		headertable = ht.buildTreeHead(instances, numClass, minsup, upperBoundMinSupport);
		Tree t = new Tree(instances, onlyClass, numClass);
		t.treebuild(headertable);
		for(int i=0;i<headertable.size();i++){
			HeaderNode hn = (HeaderNode)headertable.elementAt(i);
			System.out.println(hn.attr+"  "+hn.value+"  "+hn.count+"  "+hn.classcount[0]+"  "+hn.classcount[1]);
		}
	}


	public FastVector headertable() {
		return headertable;
	}
	public double[] vote(Instance instance) {
		double votePro[] = new double[numClass];
		int numHeaderNode = headertable.size();
		double sup, conf, conv;
		HeaderNode hn;
		CpbList cpbList = new CpbList(numClass, instances.numAttributes());
		FastVector cpblist = new FastVector();
		for(int i=numHeaderNode-1; i>=0;i--){
			hn = (HeaderNode)headertable.elementAt(i);
			if(hn.containedBy(instance)){
				for(int j=0;j<numClass;j++){
					sup = (double)hn.classcount[j]/numInstances;
					conf = (double)hn.classcount[j]/hn.count;
					conv = (double)(1-classSup[j])/(1-conf);
					if(sup>minsup&&conf>minconv){
						votePro[j] += conv*2;
						numRule++;
					}
				}
				cpblist = cpbList.genCpblist(instance, headertable, i);
			}
			ConCCFP cfp = new ConCCFP();
			cfp.buildTree();
		}
		return votePro;
	}
}
