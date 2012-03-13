package cn.edu.qdu.wru.regular;

import java.awt.Point;

import cn.edu.qdu.DesignPaper;


abstract class PlainWeave extends RegularWeave {

	
	PlainWeave(DesignPaper dp, Point wruCoor) {
		super(dp, wruCoor);
	}



	@Override
	protected void setN_J() {
		this.n_J = 2;//平纹的组织交叉数就是2.
	}
	
	
	@Override
	protected void setShift_J() {
		this.shift_J = 1;//平纹的飞数是1.
	}
	
	
	@Override
	protected void setFl_J() {
		this.fl_J = new int[n_J];
		//浮长
		fl_J[0] = 1;
		fl_J[1] = 1;
		
	}
	
	
	
	
}
























