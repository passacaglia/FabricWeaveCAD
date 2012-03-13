package cn.edu.qdu.wru.regular;

import java.awt.Point;

import cn.edu.qdu.DesignPaper;


abstract class PlainWeave extends RegularWeave {

	
	PlainWeave(DesignPaper dp, Point wruCoor) {
		super(dp, wruCoor);
	}



	@Override
	protected void setN_J() {
		this.n_J = 2;//ƽ�Ƶ���֯����������2.
	}
	
	
	@Override
	protected void setShift_J() {
		this.shift_J = 1;//ƽ�Ƶķ�����1.
	}
	
	
	@Override
	protected void setFl_J() {
		this.fl_J = new int[n_J];
		//����
		fl_J[0] = 1;
		fl_J[1] = 1;
		
	}
	
	
	
	
}
























