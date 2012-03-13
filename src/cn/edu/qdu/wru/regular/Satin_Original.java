package cn.edu.qdu.wru.regular;

import java.awt.Point;

import cn.edu.qdu.DesignPaper;

public class Satin_Original extends RegularWeave {

	public Satin_Original(DesignPaper dp, Point wruCoor) {
		super(dp, wruCoor);
	}

	@Override
	protected void setN_J() {
		this.n_J = 2;
	}

	@Override
	protected void setShift_J() {
		/**
		 * 1 < (shift_J == shift_W) < (∑fl_J[i]-1)
		 * 
		 * 而且:
		 * 
		 * shift_J、shift_W 与  ∑fl_J[i] 无公约数。
		 * 
		 * Note:
		 * 后期通过choice等，设置时,注意 setShift_J() & setFl_J()在RegularWeave里的调用顺序。
		 */
		
		this.shift_J = 2;
		
	}

	@Override
	protected void setFl_J() {
		/**
		 * ∑fl_J[i] >= 5; ∑fl_J[i] != 6;
		 * 
		 * 而且：
		 * 
		 * fl_J[0] == 1 或者 fl_J[1] == 1。
		 */
		
		fl_J = new int[n_J];
		
		fl_J[0] = 1;
		fl_J[1] = 4;
	}

}






























































































































































































