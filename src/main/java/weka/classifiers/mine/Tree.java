package weka.classifiers.mine;

import java.util.Iterator;

public class Tree {
	
	TreeNode root;
	public Tree(int numClass){
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
	
}
