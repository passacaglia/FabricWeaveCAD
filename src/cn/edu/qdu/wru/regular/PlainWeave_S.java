package cn.edu.qdu.wru.regular;

import java.awt.Point;

import cn.edu.qdu.DesignPaper;

/**
 * 查找单起平纹的英文名称，重新命名
 * 
 * 暂时以此名为单起平纹(single)
 * 
 * @author zxc
 *
 */
public class PlainWeave_S extends PlainWeave {
	
	
	public PlainWeave_S(DesignPaper dp, Point wruCoor) {
		super(dp, wruCoor);
	}

/*	*//**
	 * 平纹，根据单起/双起直接赋值。
	 *//*
	@Override
	protected void definePoint() {
		
		//单起

		//首列
		weaveMatrix[0][0] = false;
		weaveMatrix[0][1] = true;//单起。
		
		//第二列
		weaveMatrix[1][0] = true;
		weaveMatrix[1][1] = false;
		
	}*/
	
	
	
	
}
























