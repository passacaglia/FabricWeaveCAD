package cn.edu.qdu.wru;


import java.awt.*;
import cn.edu.qdu.DesignPaper;



public class InterlacingPoint {
	
	private transient DesignPaper dp = null;
	//组织点的位置
	private Point ipCoor = null;//Interlacing Point Coordinate
	private boolean isWarpIP = false; 
	
	/** 
	 * 组织点
	 * @param x
	 * 方块所在左上角的点x
	 * @param y
	 * 方块所在左上角的点x
	 * @param dp
	 * 在哪张意匠纸上画呀？根据这张纸的方格大小来画点。
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




























