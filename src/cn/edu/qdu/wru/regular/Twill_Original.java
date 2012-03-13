package cn.edu.qdu.wru.regular;

import java.awt.Point;

import cn.edu.qdu.DesignPaper;

public class Twill_Original extends RegularWeave {

	
	public Twill_Original(DesignPaper dp, Point wruCoor) {
		super(dp, wruCoor);
	}



	
	@Override
	protected void setN_J() {
		this.n_J = 2;
	}

	@Override
	protected void setShift_J() {
		
		this.shift_J = 1;//ÓÒÐ±
//		this.shift_J = -1;//×óÐ±
		
	}

	@Override
	protected void setFl_J() {
		
		this.fl_J = new int[n_J];
		
		this.fl_J[0] = 1;//Î³Ãæ  Ð±ÎÆ
		this.fl_J[1] = 2;//fl_J[0] + fl_J[1] >= 3
	
		
/*		this.fl_J[0] = 2;//
		this.fl_J[1] = 1;//¾­Ãæ  Ð±ÎÆ
*/	
		
	}

}
