package cn.edu.qdu;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import cn.edu.qdu.wru.InterlacingPoint;
import cn.edu.qdu.wru.WeaveRepeatUnit;
import cn.edu.qdu.wru.regular.PlainWeave_D;
import cn.edu.qdu.wru.regular.PlainWeave_S;
import cn.edu.qdu.wru.regular.Satin_Original;
import cn.edu.qdu.wru.regular.Twill_Original;

/**
 * <br>意匠纸
 * <br>大小、
 * <br>后期考虑颜色问题，以及每8个小格，线条加粗。
 * <br>
 * <br>考虑加上行/列 坐标。第几行/第几列。
 * <br>
 * <br>
 * <br><b>new 点</b>之前先locatePoint,把点坐标转成  其所在方格左上角的点的坐标。
 * <br>之后，用cellCoor.x & cellCoor.y来	new点。
 * <br>
 * <br>
 * @author zxc
 *
 */
public class DesignPaper extends Canvas {

	//for paper
	private FabricWeavesCAD fwcad = null;
	private int columns, rows;
	private int cellSize = 15;
	private int gridWidth, gridHeight;
	
	
		
	//Collection of all points
	//Design Paper's List | clicked的时候 add & new，repaint的时候画。
	/**
	 * String,小方格左上角的点。
	 */
	private HashMap<String, InterlacingPoint> ipMap = new HashMap<String, InterlacingPoint>();
	private HashMap<Point, Boolean> ipMap_Save = new HashMap<Point, Boolean>();
	

	//moving wru  | move的时候 add & new， repaint的时候画。
	private ArrayList<InterlacingPoint> movingIPList = new ArrayList<InterlacingPoint>();
	
	
	private WeaveRepeatUnit wru = null;
	
		
	//draw point

	//每个cell左上角的坐标。（在这个坐标处new 各个IPoint/WRU）（也就是说：new IPoint/WRU的时候，传入此坐标）
	private Point cellCoor = new Point();
	
	private boolean moving = false;
	private boolean drawingPoint = false;
	private boolean drawingWRU = false;
	//鼠标的对应位置。
	private Point updateCoor = new Point(-1, -1);//是-1，开始的时候，就不会画浮动的IPoint了。


	//double buffer
	Image buff = null;
	Graphics gBuff = null;
	
	//click的时候,moved的时候。
	Color movingColor = Color.BLACK;
	
	//for choice
	//凡是用到fwcad.getChoice().getSelectedIndex()的地方,请都用上selectedIndex,
	//方便 查找/管理.
	int selectedIndex;

	//move cursor
	Robot robot = null;
	int centerX, centerY;
	

	
	DesignPaper(FabricWeavesCAD fwcad, int columns, int rows) {
		this.fwcad = fwcad;
		this.columns = columns;
		this.rows = rows;
		
		initialize();
	}

	/**
	 * <br>给weftList添加组织点。
	 * <br>默认全是<i>纬<i>组织点。
	 * <br>
	 * <br>初始化cell Status。默认全部false。（<b>纬</b>）
	 * <br>
	 */
	private void initialize() {
		
		//this.setBackground(new Color(199, 237, 204));
		
		
		
		//new dp的时候赋值。
		gridWidth = columns * cellSize;
		gridHeight = rows * cellSize;
		
		
		//给weftList添加组织点。
		for (int i=0; i<columns; i++) {
			for (int j=0; j<rows; j++) {
				translatePoint(new Point(i * cellSize, j * cellSize));//此处可以直接使用左上角的点来new。
				/**
				 * 1.cellCoor.getLocation() 效果等于 new Point(cellCoor.x, cellCoor.y).
				 * 2.不能直接用cellCoor,因为如果用cellCoor,那所有的点都是用cellCoor这一个对象，
				 * 	 当然会出错(白方块)。
				 * 3.cellCoor.getLocation() ---> return a copy of cellCoor( in the same location)
				 *   这样,new 不同的点的时候,后面的就是不同的坐标对象了。.。
				 */
				ipMap.put(cellCoor.toString(), 
							new InterlacingPoint(this, cellCoor.getLocation(), false));
				
			}
		}
		
	}


	@Override
	public void paint(Graphics g) {
		
		drawGrid(g);
		drawIPMap(g);
		drawWeftMap(g);
		
		if (moving) {
			
			if (drawingPoint) {
				drawMovingIPoint(g);
			} else if (drawingWRU) {//choice 的index是0，或者选择了“---------------”的时候，不用画。
				drawMovingWRU(g);
			}
			
		}
		
	}
		
	@Override
	public void update(Graphics g) {
		if (buff == null) {
			buff = this.createImage(gridWidth, gridHeight);
		}
		gBuff = buff.getGraphics();
		
		//clear
		//gBuff.clearRect(0, 0, gridWidth+2, gridHeight+2);
		gBuff.clearRect(0, 0, this.getWidth(), this.getHeight());
		//draw
		paint(gBuff);
		//show
		g.drawImage(buff, 0, 0, null);
		
	}



	private void drawGrid(Graphics g) {
		
		Color c = g.getColor();
		g.setColor(new Color(50, 130, 50));
		
		for (int i=0; i<columns; i++) {
			for (int j=0; j<rows; j++) {
				g.drawRect(i * cellSize, j * cellSize, cellSize, cellSize);
			}
		}
		
		g.setColor(c);
		
	}

	
	/**
	 * 画出所有<b>经</b>组织点。
	 * <p>如何处理在边框上面的点?</p>
	 * <br>只有鼠标在方格内部点击/出现  才画组织/浮动组织。方格线上面  不画。（insideGrid方法）
	 * <br>
	 * <p>这样的话：
	 * <br>		此x, y乃是将要画的点，所在方块的坐标点（就是左上角的那个点的x, y）。
	 * <br>			x = (mouseX / cellSize) * cellSize
	 * <br>			y = (mouseY / cellSize) * cellSize
	 * </p>
	 * 
	 */
	private void drawIPMap(Graphics g) {
		
		for (InterlacingPoint ip : ipMap.values()) {
			ip.draw(g);//这里要不要garbage collector。是不是每次都new个对象。貌似不是。
		}
		
	}
	
	/**
	 * 画出所有的<i>纬组织点</i>。
	 * <p>如何处理在方格线上面的点？</p>
	 * <br>只有鼠标在方格内部点击/出现才画组织点/浮动组织点。方格线上面  不画。（insideGrid方法）
	 *
	 */
	private void drawWeftMap(Graphics g) {
		for (InterlacingPoint ip : ipMap.values()) {
			ip.draw(g);
		}
				
	}

	
	/**
	 * <br>画浮动的组织点  方块
	 * 
	 * @param g
	 */
	private void drawMovingIPoint(Graphics g) {
		
		//1.方格纸外面不画
		//2.为了不让刚打开程序时就画,结果是打开程序后,在右下角有个兰框,给它限定个范围.
		// 大于0,而不是大于等于0,小trick.就能解决上面的问题.
		//（因为刚开始的时候，updateCoor的默认坐标是零）
		if (insideGrid(updateCoor)) {
			
			selectedIndex = fwcad.getChoice().getSelectedIndex();
			
			if (1 == selectedIndex) {
				
				Color c = g.getColor();
				g.setColor(movingColor);
				g.drawRect((cellCoor.x-1), (cellCoor.y-1), (cellSize+2), (cellSize+2));
				g.setColor(c);
			
			}
		
		}
	}
	
	
	/**
	 * <br >画浮动的那个组织
	 * <br> 让浮动的组织盖住已有的组织，很简单，在已有的组织后面画 浮动的组织就好了。『写在后面』
	 * @param g
	 */
	private void drawMovingWRU(Graphics g) {
				
		if (insideGrid(updateCoor)) {
		
			selectedIndex = fwcad.getChoice().getSelectedIndex();
			
			//只要不是0(提示项) 和 1(单个组织点),其它的都是完全组织。
			if (0 != selectedIndex || 1 != selectedIndex) {
				
				Color c = g.getColor();
				g.setColor(movingColor);
				g.drawRect((cellCoor.x-1), (cellCoor.y-1), 
						   (wru.getRepJ()*cellSize+2), (wru.getRepW()*cellSize+2)
						   );
				g.setColor(c);
				
				//不要试图在这里改变颜色，wip那里已经写死了。
				for (InterlacingPoint ip : movingIPList) {
					ip.draw(g);
				}
				
				
			}
			
		}
		
	}
	
	

	/**
	 * 此方法是   把在方格<b>内部的点  坐标</b>，全部转成  <strong>左上角的点的坐标</strong>。
	 * <br /> 定位之后，用cellCoor.x & cellCoor.y即可在合适的位置生成组织点。
	 * @param mouseX
	 * @param mouseY
	 */
	private void translatePoint(Point p) {
		
		cellCoor.setLocation(((p.x / cellSize) * cellSize), 
							 ((p.y / cellSize) * cellSize));
		
	}
	
	public int getCellSize() {
		return cellSize;
	}

	
	void setDrawingPoint(boolean b) {
		this.drawingPoint = b;
	}
	
	void setDrawingWRU(boolean b) {
		this.drawingWRU = b;
	}
	
	
	ArrayList<InterlacingPoint> getMovingIPList() {
		return movingIPList;
	}

	
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	
	
	
	
	/**
	 * <b>将要画到意匠纸上的组织</b>
	 * <br>
	 * 
	 * <br>move的时候，添加点    到movingWarpList & movingWeftList。
	 * <br>然后repaint。
	 * @param e
	 */
	void mouseMoved(MouseEvent e) {
		
/*		moveCursor();*/
		
		//update
		updateCoor.setLocation(e.getPoint());
		
		
		//add points
		gen_Add_Moving_Points();

		//set label text | 这个得在add point后面，要不然个数就错了。是吧？
		if ( insideGrid( e.getPoint() ) ) {
			fwcad.setLabel1(e);//cursor position
		}
		fwcad.setLabel3();//moving map size
		
		//movingColor set
		movingColor = Color.BLUE;
		//draw
		setMoving(true);
		repaint();
	}
	
	
/*	private void moveCursor() {
		
		centerX = this.getLocationOnScreen().x 
				  + this.getMousePosition().x/cellSize*cellSize 
				  + cellSize/2;
		centerY = this.getLocationOnScreen().y 
				  + this.getMousePosition().y/cellSize*cellSize 
				  + cellSize/2;
		
		try {
			robot = new Robot();
			
			robot.mouseMove(centerX, centerY);
			
			robot = null;
		} catch (AWTException e) {
			e.printStackTrace();
		}
		
	}
*/
	
	
	
	
	private void gen_Add_Moving_Points() {
		
		selectedIndex = fwcad.getChoice().getSelectedIndex();
		
		//clear first
		movingIPList.clear();
		
		//如果不是1（1是代表画组织），那就locate下坐标。以做画  浮动的组织点。
		//如果是1，那就画组织。
		//后期，组织增多的时候，对应关系。细心。
		
		translatePoint(updateCoor);
		
		
		switch (selectedIndex) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			wru = new PlainWeave_S(this, updateCoor);
			movingIPList.addAll(wru.getIPList());
			break;
		case 3:
			wru = new PlainWeave_D(this, updateCoor);
			movingIPList.addAll(wru.getIPList());
			break;
		case 4 : 
			wru = new Twill_Original(this, updateCoor);
			movingIPList.addAll(wru.getIPList());
			break;
		case 5 :
			wru = new Satin_Original(this, updateCoor);
			movingIPList.addAll(wru.getIPList());
			break;
		}
		
		
	}
	
	
	
	
	
	
	
	
	/**
	 * <b>画到意匠纸上的点</b>
	 * <br>
	 * <br>clicked的时候，添加点  到warpList  & weftList 。
	 * <br>然后repaint。
	 * 
	 * 单击经组织点，右击纬组织点。
	 * <br>(非经即纬，无需证明。默认全是纬点)
	 * @author zxc
	 *
	 */
	void mouseClicked(MouseEvent e) {
		
		/**
		 * 不是0(提示项)
		 * 才进行下面的步骤。
		 */
		selectedIndex = fwcad.getChoice().getSelectedIndex();
		
		if ( 0 != selectedIndex ) {
			
			//add
			generate_AddPoints(e);
			
			//map size
			fwcad.setLabel2();
			
			
			//draw
			repaint();
		}
		
	}
	
	
	private void generate_AddPoints(MouseEvent e) {
		
		if ( insideGrid( e.getPoint() ) ) {
			
			switch (e.getButton()) {
			case MouseEvent.BUTTON1 ://左键画  经组织点   &  完全组织
				
				//movingColor
				movingColor = Color.BLACK;
				
				if (drawingPoint) {
					removePoint(e.getPoint());
					ipMap.put(cellCoor.toString(), 
								new InterlacingPoint(this, cellCoor.getLocation(), true));
				} else {
					processMovingWRU(e.getPoint());
				}
				break;
			case MouseEvent.BUTTON3 : 
				if (drawingPoint) {
					
					//movingColor
					//放在这里,画组织时,点右键  movingWRU就不会变色了。
					movingColor = Color.BLACK;
					
					removePoint(e.getPoint());
					ipMap.put(cellCoor.toString(), 
								new InterlacingPoint(this, cellCoor.getLocation(), false));
				}
				break;
			}
			
		}
		
	}

	/*
	 * 画组织的时候，处理组织 里面的组织点。
	 * 
	 * 只对左键响应。
	 * 
	 * 一次取出moving组织中的每个点。
	 * 		1.remove
	 * 		2.set status & put new point
	 * 
	 */
	private void processMovingWRU(Point p) {
		
		
		/**
		 * i, j 增加到  此完全组织的repW和repJ.
		 */
		for (int j=wru.getRepJ()-1; j>-1; j--) {
			for (int w=wru.getRepW()-1; w>-1; w--) {
				
				//判断下坐标是不是    还在方格内。是的话才进行处理，不是的skip了。
				if ((p.x+w*cellSize <= columns*cellSize) && (p.y+j*cellSize <= rows*cellSize)) {
					//remove
					removePoint(new Point((p.x + w * cellSize), (p.y + j * cellSize)));
					
					//set  | put
					if (wru.getWeaveMatrix()[w][j]) {
						ipMap.put(cellCoor.toString(),
								    new InterlacingPoint(this, cellCoor.getLocation(), 
								    true));
					} else {
						ipMap.put(cellCoor.toString(),
							        new InterlacingPoint(this, cellCoor.getLocation(), 
							        false));
					}
				}
				
			}
		}
		
		

	}

	
	//移除原来的组织点。
	//在坐标位置。精确的坐标。
	private void removePoint(Point p) {
		translatePoint(p);
		
		ipMap.remove(cellCoor.toString());
		
		
	}
	
		
	
	/**
	 * 检测鼠标当前是不是在方格里面。在方格里面才画出movingWRU & movingIPoint。
	 * 落在方格线上，不行。
	 * 
	 * 1.方格纸外面   不画
	 * 2.为了不让刚打开程序时就画,结果是打开程序后,在右下角有个兰框,给它限定个范围。
	 *（因为刚开始的时候，updateCoor.x和updateCoor.y的默认值是零） 
     * 所以，updateCoor 被new出来的时候，把默认值改成(-1, -1)就好了。
	 * 
	 * @param p
	 * 
	 * <br>鼠标的实时坐标。
	 * 
	 * @return
	 * 
	 * 在grid里面，返回true。
	 * 
	 */
	private boolean insideGrid(Point p) {
		
		if (0<p.x && p.x<gridWidth && 0<p.y && p.y<gridHeight) {
			if (p.x%cellSize != 0 && p.y%cellSize != 0) {
				return true;
			}
		}
		
		return false;
	}

	
	
	public int getIPMapSize() {
		return ipMap.size();
	}
	
	int getMovingIPListSize() {
		return movingIPList.size();
	}

	
	/**
	 * Output & Input
	 * @return
	 */
	int getGridWidth() {
		return gridWidth;
	}

	int getGridHeight() {
		return gridHeight;
	}

	
	HashMap<Point, Boolean> getIPMap_Save() {
		setIPMap_Save();
		return ipMap_Save;
	}
	
	private void setIPMap_Save() {
		
		for (InterlacingPoint ip : ipMap.values()) {
			ipMap_Save.put(ip.getIPCoor(), ip.isWarpIP());
		}
		
	}

	void setIPMap(HashMap<String, InterlacingPoint> hm) {
		this.ipMap.clear();
		this.ipMap.putAll(hm);
	}

		
	
	

	
	
	
	
	
	
	
	
	
}




























