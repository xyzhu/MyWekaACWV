package weka.classifiers.mine;

import java.util.Iterator;

import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class Tree {

	TreeNode root;
	Instances instances, onlyClass;
	int numClass;
	public Tree(Instances instances, Instances onlyClass, int numClass){
		this.instances = instances;
		this.onlyClass = onlyClass;
		this.numClass = numClass;
		root = new TreeNode(-1,-1,numClass);
		root.classcount = new int[numClass];
		for(int i=0;i<numClass;i++){
			root.classcount[i] = 0;
		}
	}
	public int countnode() {
		int nodecount[] = new int[1];
		printtree(root, nodecount);
		return nodecount[0];
	}
	private void printtree(TreeNode tn, int nodecount[]) {
		if(tn==null)
			return;
		Iterator<TreeNode> it = (Iterator<TreeNode>) tn.child.iterator();
		while(it.hasNext()){
			TreeNode t = it.next();
			nodecount[0]++;
			//System.out.println(t.attr+"   " +t.value+"    "+t.classcount[0]+","+t.classcount[1]);
			printtree(t, nodecount);
		}

	}

	/*
	 * build the CCFP tree with the Instances, Classes and the built header table
	 * @headertable the built header table
	 */

	public void build(FastVector headertable) {
		int numInstances = instances.numInstances();
		int classLabel;
		Instance inst;
		for(int i=0;i<numInstances;i++){
			TreeNode currentnode = root;
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
							hn.classcount[classLabel]++;
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
						hn.classcount[classLabel]++;
						currentnode.addChild(childnode);
						childnode.father = currentnode;
					}
					currentnode = childnode;					
				}
			}
		}

	}

}
