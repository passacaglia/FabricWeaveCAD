package cn.edu.qdu.wru.regular;

import java.awt.Point;

import cn.edu.qdu.DesignPaper;

/**
 * ����˫��ƽ�Ƶ�Ӣ�����ƣ���������
 * 
 * ˫��double��
 * 
 * @author zxc
 *
 */
public class PlainWeave_D extends PlainWeave {
	
	
	public PlainWeave_D(DesignPaper dp, Point wruCoor) {
		super(dp, wruCoor);
		// TODO Auto-generated constructor stub
	}

	/**
	 * ƽ�ƣ����ݵ���/˫��ֱ�Ӹ�ֵ��
	 */
	@Override
	protected void definePoint() {
		
		//����
		weaveMatrix[0][0] = true;
		weaveMatrix[0][1] = false;//˫��
		
		//�ڶ���
		weaveMatrix[1][0] = false;
		weaveMatrix[1][1] = true;
		
	}
	
	
	
}
























