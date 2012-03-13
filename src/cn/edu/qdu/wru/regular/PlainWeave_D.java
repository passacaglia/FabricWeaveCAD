package cn.edu.qdu.wru.regular;

import java.awt.Point;

import cn.edu.qdu.DesignPaper;

/**
 * 查找双起平纹的英文名称，重新命名
 * 
 * 双起（double）
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
	 * 平纹，根据单起/双起直接赋值。
	 */
	@Override
	protected void definePoint() {
		
		//首列
		weaveMatrix[0][0] = true;
		weaveMatrix[0][1] = false;//双起。
		
		//第二列
		weaveMatrix[1][0] = false;
		weaveMatrix[1][1] = true;
		
	}
	
	
	
}
























