package cn.edu.qdu.wru;

import java.awt.Point;
import java.util.ArrayList;

import cn.edu.qdu.DesignPaper;

public abstract class WeaveRepeatUnit {
	
	/**
	 * <br>n_J:������,Ҳ�Ǿ���ģ�  
	 * <br>fl_J���������У�  
	 * <br>shift_J��������
	 */
	protected int n_J;
	protected int shift_J;
	protected int[] fl_J;//Floating length
	protected int repJ;//Repetition
	protected int repW;
	protected boolean[][]	 _matrix;//the basic matrix
	protected boolean[][] weaveMatrix ;//[][]��HashMap�����String�йأ��м�ÿ����ֵ�Ÿ�ʵ�ʻ�������Ч���йء�
	
	protected DesignPaper dp = null;
	protected Point wruCoor = new Point();//�����֯���ڵ�  λ�á�����֯���Ͻǵĵ㣩
	
	
	/**
	 * �˴�String,(xy)����ҲӦ����С�������Ͻǵĵ㣬����λ�ò�ȷ����
	 * ���Կ���ʹ�þ�/γɴindex�����棬����ʵ��clicked��ʱ���ٽ��м��㡣
	 */
	protected ArrayList<InterlacingPoint> ipList = new ArrayList<InterlacingPoint>();


	
	/**
	 * Ŀǰ��������Ҫ��Ϊ��ʹ��dp�ķ���λ�á�
	 * <br>����λ���Ǵ�dp��� �����������һ��mouseMoveListener.
	 * @param dp
	 */
	protected WeaveRepeatUnit(DesignPaper dp, Point wruCoor) {
		this.dp = dp;
		this.translatePoint(wruCoor);//���Ͻǵĵ�//�����Ͻǿ�ʼ������֯
		initialize();
	}
	
	
	
	//������֯�������������ʵ�ָ���������
	protected abstract void initialize();
	
	//����д������������Ŀ���ǣ������� ׼����/������ ��֯��ʱ�򣬱�����ʹ�������Ƶķ�����
	protected abstract void defineMatrix();

	protected abstract void definePoint();

	protected abstract void addPoint();

	
	

	/**
	 * ת�������Ͻǵ� ��
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


































