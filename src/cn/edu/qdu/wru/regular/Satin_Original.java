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
		 * 1 < (shift_J == shift_W) < (��fl_J[i]-1)
		 * 
		 * ����:
		 * 
		 * shift_J��shift_W ��  ��fl_J[i] �޹�Լ����
		 * 
		 * Note:
		 * ����ͨ��choice�ȣ�����ʱ,ע�� setShift_J() & setFl_J()��RegularWeave��ĵ���˳��
		 */
		
		this.shift_J = 2;
		
	}

	@Override
	protected void setFl_J() {
		/**
		 * ��fl_J[i] >= 5; ��fl_J[i] != 6;
		 * 
		 * ���ң�
		 * 
		 * fl_J[0] == 1 ���� fl_J[1] == 1��
		 */
		
		fl_J = new int[n_J];
		
		fl_J[0] = 1;
		fl_J[1] = 4;
	}

}






























































































































































































