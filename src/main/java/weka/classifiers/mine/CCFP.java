package weka.classifiers.mine;

import java.util.Iterator;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class CCFP {
	FastVector headertable;

	public Tree buildCCFPTree(Instances instances, Instances onlyClass, double minSupport, double upperBoundMinSupport) throws Exception{
		HeaderTable ht = new HeaderTable();
		headertable = ht.buildHeaderTable(instances, onlyClass, minSupport, upperBoundMinSupport);
		Tree t = buildCCFPTree(instances, onlyClass, headertable);
		return t;
	}
/*
 * build the CCFP tree with the Instances, Classes and the built header table
 * return the CCFP tree
 * @headertable the built header table
 */
	private Tree buildCCFPTree(Instances instances, Instances onlyClass, FastVector headertable) {
		int numInstances = instances.numInstances();
		int numClass = onlyClass.attribute(0).numValues();
		int classLabel;
		Instance inst;
		Tree t = new Tree(numClass);
		for(int l=0;l<headertable.size();l++){
			HeaderNode hn = (HeaderNode)headertable.elementAt(l);
			System.out.println(hn.attr+"--"+hn.value+"--"+hn.count);
		}
		for(int i=0;i<numInstances;i++){
			TreeNode currentnode = t.root;
			inst = instances.instance(i);
			classLabel = (int)onlyClass.instance(i).value(0);
			for(int j=0;j<headertable.size();j++){
				HeaderNode hn = (HeaderNode) headertable.elementAt(j);
				TreeNode childnode = new TreeNode(hn.attr,hn.value,numClass);
				if(hn.containedBy(inst)){
					Iterator<TreeNode> it = currentnode.child.listIterator();
					int flag = 0;
					while(it.hasNext()){
						childnode = it.next();
						if(childnode.equal(hn)){
							childnode.classcount[classLabel]++;
							flag = 1;
							break;
						}
					}
					if(flag==0){
						childnode = new TreeNode(hn.attr,hn.value,numClass);
						for(int c=0;c<numClass;c++){
							childnode.classcount[c]=0;
						}
						childnode.classcount[classLabel] = 1;
						hn.addLink(childnode);
						currentnode.addChild(childnode);
						childnode.father = currentnode;
					}
					currentnode = childnode;					
				}
			}
		}
				return t;
	}

	public FastVector headertable() {
		return headertable;
	}
}
