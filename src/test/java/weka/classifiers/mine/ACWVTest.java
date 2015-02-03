package weka.classifiers.mine;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import weka.classifiers.Evaluation;
import weka.core.FastVector;

public class ACWVTest{

	String [] arg1 ={"-t","test-nom.arff"};
	ACWV acwv;
	@Before
	public void setUp() throws Exception {
		acwv = new ACWV();
		Evaluation.evaluateModel(acwv, arg1);
	}

	@Test
	public void testHeaderTable() {
		try {
			FastVector head = acwv.getCCFPhead();
			assertEquals(10,head.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCCFPTree(){
		Tree t = acwv.getCCFPTree();
		assertEquals(3,t.root.child.size());
		assertEquals(29, t.countnode());
	}


}
