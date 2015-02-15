
package weka.classifiers.mine;

import weka.associations.LabeledItemSet;
import weka.classifiers.Classifier;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ACWV extends Classifier{
	public double minsup = 0.01;
	public double minconv = 1.1;
	public int ruleNumLimit = 100000;
	double[] classValue;
	int[] classCount;
	int numClass;
	static int buildCount = 0;
	Instances m_instances;
	Instances m_onlyclass;
	FastVector m_hashtables = new FastVector();
	public Instances m_onlyClass;
	int clIndex=0;
	int numAttr=0;
	CCFP fp;
	Tree t;
	FastVector headertable;
	private int necSupport, necMaxSupport;		// minimum support
	int attrvalue[];//store number of values each attribute can be

	public void buildClassifier (Instances instances)throws Exception
	{ 

		double upperBoundMinSupport=1;
		buildCount++;
		// m_instances does not contain the class attribute
		m_instances = LabeledItemSet.divide(instances, false);

		// m_onlyClass contains only the class attribute
		m_onlyClass = LabeledItemSet.divide(instances, true);
		Calculation cal = new Calculation();
		cal.calSupport(minsup, upperBoundMinSupport, instances.numInstances());
		necSupport = cal.getNecSupport();
		attrvalue = cal.calAttrValue(m_instances);
		numClass=m_onlyClass.numDistinctValues(0);//number of classValue
		numAttr = m_instances.numAttributes();
		if(buildCount>1){		
			fp = new CCFP(m_instances, m_onlyClass,minsup, minconv, necSupport, ruleNumLimit, attrvalue);
			//long t1 = System.currentTimeMillis();
			headertable = fp.buildHeaderTable(numClass, necSupport);
			t = fp.buildTree(headertable);
			t.countnode();
		}

		//long t2 = System.currentTimeMillis();
		//long timecost = (t2 - t1);
		//System.out.println("the time cost of building classfier is :" + timecost);
	}


	public double classifyInstance(Instance instance)
	{	
		int max = 0;
		if(buildCount>1){
			double[] vote = new double[numClass];
			vote = fp.vote(instance, headertable);
			//			System.out.println(vote[0]+"   "+vote[1]);
			//			System.out.println("****************");
			max = findMax(vote);
		}
		//		for(int i=0;i<numAttr; i++){
		//			System.out.println(instance.value(i));
		//		}
		//		System.out.println(max);
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
		String[] arg ={"-t","tictest.arff"};
		runClassifier(new ACWV(), arg);

//		String[] arg1 ={"-t","vehicleout.arff"};
//		runClassifier(new ACWV(), arg1);
//
//		String[] arg2 ={"-t","balloons.arff"};
//		runClassifier(new ACWV(), arg2);
//
//		String[] arg3 ={"-t","car.arff"};
//		runClassifier(new ACWV(), arg3);
		
//		String[] arg4 ={"-t","lenses.arff"};
//		runClassifier(new ACWV(), arg4);
//		
//		String[] arg5 ={"-t","tic-tac-toe.arff"};
//		runClassifier(new ACWV(), arg5);
//		
//		String[] arg6 ={"-t","ionoout2.arff"};
//		runClassifier(new ACWV(), arg6);
//		
//		String[] arg7 ={"-t","pimaout.arff"};
//		runClassifier(new ACWV(), arg7);
//		
//		String[] arg8 ={"-t","taeout.arff"};
//		runClassifier(new ACWV(), arg8);
//		
//		String[] arg9 ={"-t","habermanout.arff"};
//		runClassifier(new ACWV(), arg9);
//		
//		String[] arg10={"-t","glassout.arff"};
//		runClassifier(new ACWV(), arg10);
//		
//		String[] arg11={"-t","breastout.arff"};
//		runClassifier(new ACWV(), arg11);
		
//		String[] arg12={"-t","cmcout.arff"};
//		runClassifier(new ACWV(), arg12);
//		
//		String[] arg13={"-t","ecoliout.arff"};
//		runClassifier(new ACWV(), arg13);
//		
//		String[] arg14={"-t","liverout.arff"};
//		runClassifier(new ACWV(), arg14);
//		
//		String[] arg15={"-t","postout.arff"};
//		runClassifier(new ACWV(), arg15);
//		
//		String[] arg16={"-t","hypoout2.arff"};
//		runClassifier(new ACWV(), arg16);
//		
//		String[] arg17={"-t","yeastout.arff"};
//		runClassifier(new ACWV(), arg17);
//		
//		String[] arg18={"-t","autoout.arff"};
//		runClassifier(new ACWV(), arg18);
//		
//		String[] arg19={"-t","cleveout.arff"};
//		runClassifier(new ACWV(), arg19);
//		
//		String[] arg20={"-t","diabetesout.arff"};
//		runClassifier(new ACWV(), arg20);
//		
//		String[] arg21={"-t","heartout.arff"};
//		runClassifier(new ACWV(), arg21);
//		
//		String[] arg22={"-t","irisout.arff"};
//		runClassifier(new ACWV(), arg22);
//		
//		String[] arg23={"-t","laborout.arff"};
//		runClassifier(new ACWV(), arg23);
//		
//		String[] arg24={"-t","led7.arff"};
//		runClassifier(new ACWV(), arg24);
//		
//		String[] arg25={"-t","wineout.arff"};
//		runClassifier(new ACWV(), arg25);
//		
//		String[] arg26={"-t","zoo.arff"};
//		runClassifier(new ACWV(), arg26);
//		
//		String[] arg27={"-t","crxout.arff"};
//		runClassifier(new ACWV(), arg27);
		
//		String[] arg28={"-t","vehicleout.arff"};
//		runClassifier(new ACWV(), arg28);
//		
//		String[] arg29={"-t","lymph.arff"};
//		runClassifier(new ACWV(), arg29);
		
//		String[] arg30={"-t","austraout.arff"};
//		runClassifier(new ACWV(), arg30);
//		
//		String[] arg31={"-t","hepatiout.arff"};
//		runClassifier(new ACWV(), arg31);
//		
//		String[] arg32={"-t","germanout2.arff"};
//		runClassifier(new ACWV(), arg32);
//		
//		String[] arg33={"-t","sickout.arff"};
//		runClassifier(new ACWV(), arg33);
//		
//		String[] arg34={"-t","horseout2.arff"};
//		runClassifier(new ACWV(), arg34);
//		
//		String[] arg35={"-t","annealout.arff"};
//		runClassifier(new ACWV(), arg35);
//		
//		String[] arg36={"-t","sonarout2.arff"};
//		runClassifier(new ACWV(), arg36);

	}

	public FastVector getCCFPhead() {
		return headertable;
	}

	public Tree getCCFPTree(){
		return t;
	}
}

