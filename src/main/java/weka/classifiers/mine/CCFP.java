package weka.classifiers.mine;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class CCFP {
	Instances instances, onlyClass;
	Tree t;
	double minsup, minconv;
	int ruleNumLimit, numRule;
	int numAttr, numClass, numInstances;
	double []classSup;
	int necSupport;

	public CCFP(Instances insts, Instances onlyclass, double minsup, double minconv, int necSupport,
			int ruleNumLimit) {
		this.instances = insts;
		this.onlyClass = onlyclass;
		this.minsup = minsup;
		this.minconv = minconv;
		this.necSupport = necSupport;
		this.ruleNumLimit = ruleNumLimit;
		numAttr = insts.numAttributes();
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
	public Tree buildTree(FastVector headertable) throws Exception{
		Tree t = new Tree(numClass);
		t.treebuild(instances, onlyClass, headertable);
//		for(int i=0;i<headertable.size();i++){
//			HeaderNode hn = (HeaderNode)headertable.elementAt(i);
//			System.out.println(hn.attr+"  "+hn.value+"  "+hn.classcount[0]+"  "+hn.classcount[1]);
//		}
		return t;
	}

	public double[] vote(Instance instance, FastVector headertable, int []attrvalue) {
		double votePro[] = new double[numClass];
		int numHeaderNode = headertable.size();
		double sup, conf, conv;
		HeaderNode hn;
		CpbList cpbList = new CpbList(attrvalue, numClass);
		FastVector cpblist = new FastVector();
		for(int i=numHeaderNode-1; i>=0;i--){
			hn = (HeaderNode)headertable.elementAt(i);
			if(hn.containedBy(instance)){
				for(int j=0;j<numClass;j++){
					sup = (double)hn.classcount[j]/numInstances;
					conf = (double)hn.classcount[j]/hn.count;
					conv = (double)(1-classSup[j])/(1-conf);
					if(sup>minsup&&conv>minconv){
						votePro[j] += conv/numAttr;
						numRule++;
					}
				}
				cpblist = cpbList.genCpblist(instance, headertable, i);
			}
			if(cpblist.size()==0)
				continue;
			ConCCFP cfp = new ConCCFP(cpblist, numClass);
			FastVector conheadertable = cfp.buildConTreeHead(cpbList.hashAttribute.hashattr, minsup, minconv, necSupport, attrvalue);
			Tree t = cfp.contreeBuild(conheadertable);
			//ccfpGrow(hn, t, headertable, instance, numRule, minsup, minconv,ruleNumLim, vote);
		}
		return votePro;
	}
	public FastVector buildHeaderTable(int numClass, int necSupport) throws Exception {
		HeaderTable ht = new HeaderTable();
		FastVector headertable = ht.buildTreeHead(instances, numClass, necSupport);
		return headertable;
	}
}
