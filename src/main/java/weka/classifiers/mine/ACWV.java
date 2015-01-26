
package weka.classifiers.mine;

import java.util.Iterator;

import weka.associations.LabeledItemSet;
import weka.classifiers.Classifier;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ACWV extends Classifier{
	public double minsup = 0.2;
	public double minconv;
	double[] classValue;
	int[] classCount;
	Instances m_instances;
	Instances m_onlyclass;
	FastVector m_hashtables = new FastVector();
	public Instances m_onlyClass;
	int clIndex=0;
	int attNum=0;
	CCFP fp;
	int count = 0;
	static int c = 0;
	Tree t;

	public void buildClassifier (Instances instances)throws Exception
	{ 

		double upperBoundMinSupport=1;
		// m_instances does not contain the class attribute
	    m_instances = LabeledItemSet.divide(instances, false);

	    // m_onlyClass contains only the class attribute
	    m_onlyClass = LabeledItemSet.divide(instances, true);
		attNum=m_instances.numAttributes();
		clIndex=instances.classIndex();//index of the class
		//int numClass=m_onlyClass.numDistinctValues(0);//number of classValue
		int numClass = m_onlyClass.attribute(0).numValues();

		double[] supB = new double[numClass];
		//classCount=new int[numClass];
		//double[] clValue=m_onlyClass.attributeToDoubleArray(0);
		//classValue=differentiate(clValue);//find all the different class value
		//count(clValue);
		fp = new CCFP();
		//long t1 = System.currentTimeMillis();
		t = fp.buildCCFPTree(m_instances, m_onlyClass, minsup, 1);
		printtree(t.root);
		//long t2 = System.currentTimeMillis();
		//long timecost = (t2 - t1);
		//System.out.println("the time cost of building classfier is :" + timecost);
//		classValue = getSupB();
		count = 0;
		c++;
	}
	
	private void printtree(TreeNode tn) {
		if(tn==null)
			return;
		Iterator<TreeNode> it = (Iterator<TreeNode>) tn.child.iterator();
		while(it.hasNext()){
			TreeNode t = it.next();
			System.out.println(t.attr+"   " +t.value+"    "+t.classcount[0]+","+t.classcount[1]);
			printtree(t);
		}
		
	}

	public double classifyInstance(Instance instance)
	{		
		return 0;
	}

	
	public static void main(String[] argv){
		String[] arg1 ={"-t","test-nom.arff"};
		runClassifier(new ACWV(), arg1);

	}

	public FastVector getCCFPhead() {
		// TODO Auto-generated method stub
		return fp.headertable();
	}
}

