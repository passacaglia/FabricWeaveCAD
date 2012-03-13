package cn.edu.qdu.wru;


import java.awt.*;
import cn.edu.qdu.DesignPaper;



public class InterlacingPoint {
	
	private transient DesignPaper dp = null;
	//��֯���λ��
	private Point ipCoor = null;//Interlacing Point Coordinate
	private boolean isWarpIP = false; 
	
	/** 
	 * ��֯��
	 * @param x
	 * �����������Ͻǵĵ�x
	 * @param y
	 * �����������Ͻǵĵ�x
	 * @param dp
	 * �������⽳ֽ�ϻ�ѽ����������ֽ�ķ����С�����㡣
	 * 
	 */
	public InterlacingPoint(DesignPaper dp, Point ipCoordinate, boolean isWarpIP) {
		this.dp = dp;
		this.ipCoor = ipCoordinate; 
		this.isWarpIP = isWarpIP;
	}
	
	public void draw(Graphics g) {
		
		if (isWarpIP) {
			
			Color c = g.getColor();
			g.setColor(Color.DARK_GRAY);
			g.fillRect(ipCoor.x + 2, ipCoor.y + 2, dp.getCellSize() - 3, dp.getCellSize() - 3);
			g.setColor(c);
			
		} else {
			
			Color c = g.getColor();
			g.setColor(Color.GRAY);
			g.fillRect(ipCoor.x + 2, ipCoor.y + 2, dp.getCellSize() - 3, dp.getCellSize() - 3);
			g.setColor(c);
			
		}
		
	}

	
	public Point getIPCoor() {
		return ipCoor;
	}
	
	
	public boolean isWarpIP() {
		return isWarpIP;
	}
	
	
	

	
}




























