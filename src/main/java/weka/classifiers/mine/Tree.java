package weka.classifiers.mine;

public class Tree {
	
	TreeNode root;
	public Tree(int numClass){
		root = new TreeNode(-1,-1,numClass);
		root.classcount = new int[numClass];
		for(int i=0;i<numClass;i++){
			root.classcount[i] = 0;
		}
	}
	
}
