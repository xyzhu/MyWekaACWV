package weka.classifiers.mine;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class CCFP {
	Instances instances, onlyClass;
	double minsup, minconv;
	int ruleNumLimit, numRule;
	int numAttr, numClass, numInstances;
	double []classSup;
	int necSupport;
	int []attrvalue;

	public CCFP(Instances insts, Instances onlyclass, double minsup, double minconv, int necSupport,
			int ruleNumLimit, int[]attrvalue) {
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
		this.attrvalue = attrvalue;
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

	public double[] vote(Instance instance, FastVector headertable) {
		double votePro[] = new double[numClass];
		int numHeaderNode = headertable.size();
		double sup, conv;
		HeaderNode hn;
		FastVector prefix = new FastVector();
		CpbList cpbList = new CpbList(attrvalue, numClass);
		FastVector cpblist = new FastVector();
		int len;
		for(int i=numHeaderNode-1; i>=0;i--){
			hn = (HeaderNode)headertable.elementAt(i);
			len = 1;
			if(hn.containedBy(instance)){
				for(int j=0;j<numClass;j++){
					sup = compSup(hn, j);
					conv = compConv(hn, j);
					if(sup>minsup&&conv>minconv){
						votePro[j] += conv*len;
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
			prefix.addElement(hn);
			ccfpGrow(prefix, t, conheadertable, instance, votePro);
		}
		return votePro;
	}
	private double compConv(HeaderNode hn, int j) {
		double conf = (double)hn.classcount[j]/hn.count;
		return (double)(1-classSup[j])/(1-conf);
	}
	private double compSup(HeaderNode hn, int j) {
		return (double)hn.classcount[j]/numInstances;
	}
	private void ccfpGrow(FastVector prefix, Tree t, FastVector headertable,
			Instance instance, double[] votePro) {
		HeaderNode hn;
		double sup, conv, w;
		int len, num=1;
		CpbList cpbList = new CpbList(attrvalue, numClass);
		FastVector cpblist = new FastVector();
		//if the tree t has only one path
		if(t.hasOnePath()==true){
			//from the last header node to the first
			for(int i=headertable.size()-1;i>=0;i--){
				hn = (HeaderNode)headertable.elementAt(i);
				//for each class label cl, j is the index of cl
				for(int j=0;j<numClass;j++){
					sup = compSup(hn, j);
					conv = compConv(hn,j);
					if(sup>minsup&&conv>minconv){
						for(int k=0;k<i;k++){
							len = prefix.size()+k+1;
							w = conv*len;// weight of the rule
							for(int n=0;n<k;n++){
								num = num*(i-n)/(k-n);
							}
							votePro[j]+=num*w;
							numRule += num;
							if(numRule>ruleNumLimit){
								votePro[j]-=(numRule-ruleNumLimit)*w;
							}
						}
					}
				}
			}
		}
		//else the tree has more than one path
		else
		{
			for(int i=headertable.size()-1;i>=0;i--){
				hn = (HeaderNode)headertable.elementAt(i);
				prefix.addElement(hn);
				len = prefix.size();
				for(int j=0;j<numClass;j++){
					sup = compSup(hn,j);					
					conv = compConv(hn,j);
					if(sup>minsup&&conv>minconv){
						votePro[j] += conv*len;
						numRule++;
					}
					if(numRule>=ruleNumLimit)
						return;
				}
				cpblist = cpbList.genCpblist(instance, headertable, i);
				if(cpblist.size()==0)
					continue;
				ConCCFP cfp = new ConCCFP(cpblist, numClass);
				FastVector conheadertable = cfp.buildConTreeHead(cpbList.hashAttribute.hashattr, minsup, minconv, necSupport, attrvalue);
				Tree tree = cfp.contreeBuild(conheadertable);
				ccfpGrow(prefix, tree, conheadertable, instance, votePro);
			}
		}
	}
	public FastVector buildHeaderTable(int numClass, int necSupport) throws Exception {
		HeaderTable ht = new HeaderTable();
		FastVector headertable = ht.buildTreeHead(instances, numClass, necSupport);
		return headertable;
	}
}
