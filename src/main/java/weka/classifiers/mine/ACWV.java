
package weka.classifiers.mine;

import weka.associations.LabeledItemSet;
import weka.classifiers.Classifier;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ACWV extends Classifier{
	public double minsup = 0.2;
	public double minconv = 1.1;
	public int ruleNumLimit = 80000;
	double[] classValue;
	int[] classCount;
	int numClass;
	Instances m_instances;
	Instances m_onlyclass;
	FastVector m_hashtables = new FastVector();
	public Instances m_onlyClass;
	int clIndex=0;
	int attNum=0;
	CCFP fp;
	Tree t;
	FastVector headertable;
	private int necSupport, necMaxSupport;		// minimum support
	int attrvalue[];//store number of values each attribute can be

	public void buildClassifier (Instances instances)throws Exception
	{ 

		double upperBoundMinSupport=1;
		// m_instances does not contain the class attribute
		m_instances = LabeledItemSet.divide(instances, false);

		// m_onlyClass contains only the class attribute
		m_onlyClass = LabeledItemSet.divide(instances, true);
		Calculation cal = new Calculation();
		cal.calSupport(minsup, upperBoundMinSupport, instances.numInstances());
		necSupport = cal.getNecSupport();
		attrvalue = cal.calAttrValue(m_instances);
		numClass=m_onlyClass.numDistinctValues(0);//number of classValue
		fp = new CCFP(m_instances, m_onlyClass,minsup, minconv, necSupport, ruleNumLimit, attrvalue);
		//long t1 = System.currentTimeMillis();
		headertable = fp.buildHeaderTable(numClass, necSupport);
		t = fp.buildTree(headertable);
		//		t.countnode();
		//long t2 = System.currentTimeMillis();
		//long timecost = (t2 - t1);
		//System.out.println("the time cost of building classfier is :" + timecost);
	}


	public double classifyInstance(Instance instance)
	{	
		double[] vote = new double[numClass];
		vote = fp.vote(instance, headertable);
		int max = findMax(vote);
		return max;
	}

	private int findMax(double[] d)
	{
		int l=d.length;
		int iMax=0;
		double temp=d[0];
		for(int i=1;i<l;i++)
		{
			if(d[i]>temp)
			{
				iMax=i;
				temp=d[i];
			}
		}
		return iMax;
	}


	public static void main(String[] argv){
		String[] arg1 ={"-t","test-nom.arff"};
		runClassifier(new ACWV(), arg1);

	}

	public FastVector getCCFPhead() {
		return headertable;
	}

	public Tree getCCFPTree(){
		return t;
	}
}

