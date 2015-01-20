package weka.classifiers.mine;

import java.util.ArrayList;

import weka.associations.LabeledItemSet;
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
	
	public FastVector buildHeaderTable(Instances instances, Instances onlyClass, double minSupport, double upperBoundMinSupport) throws Exception{
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
	    kSets = LabeledItemSet.singletons(instances, onlyClass);
	    LabeledItemSet.upDateCounters(kSets, instances, onlyClass);

	    // check if a item set of lentgh one is frequent, if not delete it
	    kSets = LabeledItemSet.deleteItemSets(kSets, necSupport,
	        instances.numInstances());
	    if (kSets.size() == 0)
	      return null;
	    quicksort(kSets,0,kSets.size()-1);
	    FastVector ht = Transform(kSets);
	    return ht;
	}
	private FastVector Transform(FastVector kSets) {
		FastVector ht = new FastVector();
		LabeledItemSet ls;
		HeaderNode hn = null;
		for(int i=0;i<kSets.size();i++){
			ls = (LabeledItemSet) kSets.elementAt(i);
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

	private void quicksort(FastVector itemSets, int start, int end) {
		if  (start < end ){
			int k = partition(itemSets, start, end );
			quicksort(itemSets, start, k - 1);
			quicksort(itemSets, k + 1, end);
		}
	}

	private int partition(FastVector itemSets, int start, int end) {
		int pivot = ((LabeledItemSet)itemSets.elementAt(start)).counter();
		int r = end;
		int l = start;
		while(l<r){
			while(r>l&&((LabeledItemSet)itemSets.elementAt(r)).counter()>=pivot){
				r--;
			}
			while(r>l&&((LabeledItemSet)itemSets.elementAt(l)).counter()<=pivot){
				l++;
			}
			swap(itemSets,l,r);
		}
		swap(itemSets,start,l);
		return l;
	}

	private void swap(FastVector sets, int l, int r) {
		LabeledItemSet tmp = (LabeledItemSet)sets.elementAt(l);
		sets.setElementAt(sets.elementAt(r), l);
		sets.setElementAt(tmp, r);
		
	}
	public static void main(String[] argv){

	}

}
