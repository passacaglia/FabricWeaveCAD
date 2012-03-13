package cn.edu.qdu.wru;

import java.awt.Point;
import java.util.ArrayList;

import cn.edu.qdu.DesignPaper;

public abstract class WeaveRepeatUnit {
	
	/**
	 * <br>n_J:交叉数,也是经向的；  
	 * <br>fl_J：浮长序列；  
	 * <br>shift_J：飞数；
	 */
	protected int n_J;
	protected int shift_J;
	protected int[] fl_J;//Floating length
	protected int repJ;//Repetition
	protected int repW;
	protected boolean[][]	 _matrix;//the basic matrix
	protected boolean[][] weaveMatrix ;//[][]跟HashMap里面的String有关；中间每个的值才跟实际画出来的效果有关。
	
	protected DesignPaper dp = null;
	protected Point wruCoor = new Point();//这个组织所在的  位置。（组织左上角的点）
	
	
	/**
	 * 此处String,(xy)本来也应该用小方格左上角的点，但是位置不确定。
	 * 所以可以使用经/纬纱index来代替，后面实际clicked的时候，再进行计算。
	 */
	protected ArrayList<InterlacingPoint> ipList = new ArrayList<InterlacingPoint>();


	
	/**
	 * 目前来看，主要是为了使用dp的方格位置。
	 * <br>鼠标的位置是从dp获得 ，还是再添加一个mouseMoveListener.
	 * @param dp
	 */
	protected WeaveRepeatUnit(DesignPaper dp, Point wruCoor) {
		this.dp = dp;
		this.translatePoint(wruCoor);//左上角的点//从左上角开始画的组织
		initialize();
	}
	
	
	
	//各种组织根据自身情况，实现各个方法。
	protected abstract void initialize();
	
	//这里写这三个方法的目的是：在生成 准规则/不规则 组织的时候，别忘了使用相类似的方法。
	protected abstract void defineMatrix();

	protected abstract void definePoint();

	protected abstract void addPoint();

	
	

	/**
	 * 转换成左上角的 点
	 * @param p
	 */
	private void translatePoint(Point p) {
		
		wruCoor.setLocation((p.x / dp.getCellSize()) * dp.getCellSize(), 
							(p.y / dp.getCellSize()) * dp.getCellSize());
		
	}

	
	
	protected abstract void setN_J();

	protected abstract void setShift_J();

	protected abstract void setFl_J();
	
	

	
	public ArrayList<InterlacingPoint> getIPList() {
		return ipList;
	}


	public int getRepJ() {
		return repJ;
	}
	
	public int getRepW() {
		return repW;
	}
	
	
	public boolean[][] getWeaveMatrix() {
		return weaveMatrix;
	}

	

	
	
}


































