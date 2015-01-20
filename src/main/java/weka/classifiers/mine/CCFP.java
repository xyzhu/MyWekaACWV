package weka.classifiers.mine;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class CCFP {

	public Tree buildCCFPTree(Instances instances, Instances onlyClass, double minSupport, double upperBoundMinSupport) throws Exception{
		HeaderTable ht = new HeaderTable();
		FastVector headertable = ht.buildHeaderTable(instances, onlyClass, minSupport, upperBoundMinSupport);
		Tree t = buildCCFPTree(instances, onlyClass, headertable);
		return t;
	}

	private Tree buildCCFPTree(Instances instances, Instances onlyClass, FastVector headertable) {
		int numInstances = instances.numInstances();
		int numAttr = instances.numAttributes();
		int numClass = onlyClass.attribute(0).numValues();
		Instance inst;
		Tree t = new Tree(numClass);
		for(int i=0;i<numInstances;i++){
			TreeNode root = t.root;
			for(int j=0;j<headertable.size();j++){
				inst = instances.instance(j);
				HeaderNode hn = (HeaderNode) headertable.elementAt(j);
				if(hn.containedBy(inst)){
					TreeNode node = new TreeNode(hn.attr, hn.value);
				}
			}
		}
				return null;
	}
}
