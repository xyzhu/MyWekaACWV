package weka.classifiers.mine;

import weka.associations.ItemSet;
import weka.core.FastVector;
import weka.core.Instances;

public class HeaderTable {
	int m_positiveIndex = 2;
	HeaderNode [] ht = new HeaderNode[5];
	public void insertHeaderNode(HeaderNode hn, int n){
		ht[n].attr = hn.attr;
		ht[n].count = hn.count;
		ht[n].link = null;
	}
	
	public FastVector buildTreeHead(Instances instances, int numClass, double minSupport, double upperBoundMinSupport) throws Exception{
		// minimum support
		int necSupport, necMaxSupport;
		FastVector kSets;
	    double nextMinSupport = minSupport * instances.numInstances();
	    double nextMaxSupport = upperBoundMinSupport * instances.numInstances();
	    if (Math.rint(nextMinSupport) == nextMinSupport) {
	      necSupport = (int) nextMinSupport;
	    } else {
	      necSupport = Math.round((float) (nextMinSupport + 0.5));
	    }
	    if (Math.rint(nextMaxSupport) == nextMaxSupport) {
	      necMaxSupport = (int) nextMaxSupport;
	    } else {
	      necMaxSupport = Math.round((float) (nextMaxSupport + 0.5));
	    }

	    // find item sets of length one
	    kSets = ItemSet.singletons(instances);
	    ItemSet.upDateCounters(kSets, instances);
	    /*for(int k=0;k<kSets.size();k++){
	    	LabeledItemSet ls = (LabeledItemSet)kSets.elementAt(k);
	    	System.out.println("good");
	    	System.out.println(ls.itemAt(0)+","+ls.itemAt(1)+","+ls.itemAt(2)+","+ls.itemAt(3)+","+ls.counter());
	    }*/

	    // check if a item set of length one is frequent, if not delete it
	    kSets = ItemSet.deleteItemSets(kSets, necSupport,
	        instances.numInstances());
	    if (kSets.size() == 0)
	      return null;
	    FastVector ht = Transform(kSets, numClass);
	    quicksort(ht,0,ht.size()-1);
	    return ht;
	}
	private FastVector Transform(FastVector kSets, int numClass) {
		FastVector ht = new FastVector();
		ItemSet ls;
		HeaderNode hn;
		for(int i=0;i<kSets.size();i++){
			hn = new HeaderNode(numClass);
			ls = (ItemSet) kSets.elementAt(i);
			int items[] = ls.items();
			for(int k=0;k<items.length;k++){
				if (items[k]!=-1){
					hn.attr = k;
					hn.value = items[k];
					hn.count = ls.counter();
				}
			}
			ht.addElement(hn);
		}
		return ht;
	}

	private void quicksort(FastVector headertable, int start, int end) {
		if  (start < end ){
			int k = partition(headertable, start, end );
			quicksort(headertable, start, k - 1);
			quicksort(headertable, k + 1, end);
		}
	}

	private int partition(FastVector headertable, int start, int end) {
		int pivot = ((HeaderNode)headertable.elementAt(start)).count;
		int r = end;
		int l = start;
		while(l<r){
			while(r>l&&((HeaderNode)headertable.elementAt(r)).count<=pivot){
				r--;
			}
			while(r>l&&((HeaderNode)headertable.elementAt(l)).count>=pivot){
				l++;
			}
			swap(headertable,l,r);
		}
		swap(headertable,start,l);
		return l;
	}

	private void swap(FastVector sets, int l, int r) {
		HeaderNode tmp = (HeaderNode)sets.elementAt(l);
		sets.setElementAt(sets.elementAt(r), l);
		sets.setElementAt(tmp, r);
		
	}
	public static void main(String[] argv){

	}

}
