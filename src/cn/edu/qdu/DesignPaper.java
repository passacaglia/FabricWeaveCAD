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
 * <br>�⽳ֽ
 * <br>��С��
 * <br>���ڿ�����ɫ���⣬�Լ�ÿ8��С�������Ӵ֡�
 * <br>
 * <br>���Ǽ�����/�� ���ꡣ�ڼ���/�ڼ��С�
 * <br>
 * <br>
 * <br><b>new ��</b>֮ǰ��locatePoint,�ѵ�����ת��  �����ڷ������Ͻǵĵ�����ꡣ
 * <br>֮����cellCoor.x & cellCoor.y��	new�㡣
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
	//Design Paper's List | clicked��ʱ�� add & new��repaint��ʱ�򻭡�
	/**
	 * String,С�������Ͻǵĵ㡣
	 */
	private HashMap<String, InterlacingPoint> ipMap = new HashMap<String, InterlacingPoint>();
	private HashMap<Point, Boolean> ipMap_Save = new HashMap<Point, Boolean>();
	

	//moving wru  | move��ʱ�� add & new�� repaint��ʱ�򻭡�
	private ArrayList<InterlacingPoint> movingIPList = new ArrayList<InterlacingPoint>();
	
	
	private WeaveRepeatUnit wru = null;
	
		
	//draw point

	//ÿ��cell���Ͻǵ����ꡣ����������괦new ����IPoint/WRU����Ҳ����˵��new IPoint/WRU��ʱ�򣬴�������꣩
	private Point cellCoor = new Point();
	
	private boolean moving = false;
	private boolean drawingPoint = false;
	private boolean drawingWRU = false;
	//���Ķ�Ӧλ�á�
	private Point updateCoor = new Point(-1, -1);//��-1����ʼ��ʱ�򣬾Ͳ��ử������IPoint�ˡ�


	//double buffer
	Image buff = null;
	Graphics gBuff = null;
	
	//click��ʱ��,moved��ʱ��
	Color movingColor = Color.BLACK;
	
	//for choice
	//�����õ�fwcad.getChoice().getSelectedIndex()�ĵط�,�붼����selectedIndex,
	//���� ����/����.
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
	 * <br>��weftList�����֯�㡣
	 * <br>Ĭ��ȫ��<i>γ<i>��֯�㡣
	 * <br>
	 * <br>��ʼ��cell Status��Ĭ��ȫ��false����<b>γ</b>��
	 * <br>
	 */
	private void initialize() {
		
		//this.setBackground(new Color(199, 237, 204));
		
		
		
		//new dp��ʱ��ֵ��
		gridWidth = columns * cellSize;
		gridHeight = rows * cellSize;
		
		
		//��weftList�����֯�㡣
		for (int i=0; i<columns; i++) {
			for (int j=0; j<rows; j++) {
				translatePoint(new Point(i * cellSize, j * cellSize));//�˴�����ֱ��ʹ�����Ͻǵĵ���new��
				/**
				 * 1.cellCoor.getLocation() Ч������ new Point(cellCoor.x, cellCoor.y).
				 * 2.����ֱ����cellCoor,��Ϊ�����cellCoor,�����еĵ㶼����cellCoor��һ������
				 * 	 ��Ȼ�����(�׷���)��
				 * 3.cellCoor.getLocation() ---> return a copy of cellCoor( in the same location)
				 *   ����,new ��ͬ�ĵ��ʱ��,����ľ��ǲ�ͬ����������ˡ�.��
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
			} else if (drawingWRU) {//choice ��index��0������ѡ���ˡ�---------------����ʱ�򣬲��û���
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
	 * ��������<b>��</b>��֯�㡣
	 * <p>��δ����ڱ߿�����ĵ�?</p>
	 * <br>ֻ������ڷ����ڲ����/����  �Ż���֯/������֯������������  ��������insideGrid������
	 * <br>
	 * <p>�����Ļ���
	 * <br>		��x, y���ǽ�Ҫ���ĵ㣬���ڷ��������㣨�������Ͻǵ��Ǹ����x, y����
	 * <br>			x = (mouseX / cellSize) * cellSize
	 * <br>			y = (mouseY / cellSize) * cellSize
	 * </p>
	 * 
	 */
	private void drawIPMap(Graphics g) {
		
		for (InterlacingPoint ip : ipMap.values()) {
			ip.draw(g);//����Ҫ��Ҫgarbage collector���ǲ���ÿ�ζ�new������ò�Ʋ��ǡ�
		}
		
	}
	
	/**
	 * �������е�<i>γ��֯��</i>��
	 * <p>��δ����ڷ���������ĵ㣿</p>
	 * <br>ֻ������ڷ����ڲ����/���ֲŻ���֯��/������֯�㡣����������  ��������insideGrid������
	 *
	 */
	private void drawWeftMap(Graphics g) {
		for (InterlacingPoint ip : ipMap.values()) {
			ip.draw(g);
		}
				
	}

	
	/**
	 * <br>����������֯��  ����
	 * 
	 * @param g
	 */
	private void drawMovingIPoint(Graphics g) {
		
		//1.����ֽ���治��
		//2.Ϊ�˲��øմ򿪳���ʱ�ͻ�,����Ǵ򿪳����,�����½��и�����,�����޶�����Χ.
		// ����0,�����Ǵ��ڵ���0,Сtrick.���ܽ�����������.
		//����Ϊ�տ�ʼ��ʱ��updateCoor��Ĭ���������㣩
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
	 * <br >���������Ǹ���֯
	 * <br> �ø�������֯��ס���е���֯���ܼ򵥣������е���֯���滭 ��������֯�ͺ��ˡ���д�ں��桻
	 * @param g
	 */
	private void drawMovingWRU(Graphics g) {
				
		if (insideGrid(updateCoor)) {
		
			selectedIndex = fwcad.getChoice().getSelectedIndex();
			
			//ֻҪ����0(��ʾ��) �� 1(������֯��),�����Ķ�����ȫ��֯��
			if (0 != selectedIndex || 1 != selectedIndex) {
				
				Color c = g.getColor();
				g.setColor(movingColor);
				g.drawRect((cellCoor.x-1), (cellCoor.y-1), 
						   (wru.getRepJ()*cellSize+2), (wru.getRepW()*cellSize+2)
						   );
				g.setColor(c);
				
				//��Ҫ��ͼ������ı���ɫ��wip�����Ѿ�д���ˡ�
				for (InterlacingPoint ip : movingIPList) {
					ip.draw(g);
				}
				
				
			}
			
		}
		
	}
	
	

	/**
	 * �˷�����   ���ڷ���<b>�ڲ��ĵ�  ����</b>��ȫ��ת��  <strong>���Ͻǵĵ������</strong>��
	 * <br /> ��λ֮����cellCoor.x & cellCoor.y�����ں��ʵ�λ��������֯�㡣
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
	 * <b>��Ҫ�����⽳ֽ�ϵ���֯</b>
	 * <br>
	 * 
	 * <br>move��ʱ����ӵ�    ��movingWarpList & movingWeftList��
	 * <br>Ȼ��repaint��
	 * @param e
	 */
	void mouseMoved(MouseEvent e) {
		
/*		moveCursor();*/
		
		//update
		updateCoor.setLocation(e.getPoint());
		
		
		//add points
		gen_Add_Moving_Points();

		//set label text | �������add point���棬Ҫ��Ȼ�����ʹ��ˡ��ǰɣ�
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
		
		//�������1��1�Ǵ�����֯�����Ǿ�locate�����ꡣ������  ��������֯�㡣
		//�����1���Ǿͻ���֯��
		//���ڣ���֯�����ʱ�򣬶�Ӧ��ϵ��ϸ�ġ�
		
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
	 * <b>�����⽳ֽ�ϵĵ�</b>
	 * <br>
	 * <br>clicked��ʱ����ӵ�  ��warpList  & weftList ��
	 * <br>Ȼ��repaint��
	 * 
	 * ��������֯�㣬�һ�γ��֯�㡣
	 * <br>(�Ǿ���γ������֤����Ĭ��ȫ��γ��)
	 * @author zxc
	 *
	 */
	void mouseClicked(MouseEvent e) {
		
		/**
		 * ����0(��ʾ��)
		 * �Ž�������Ĳ��衣
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
			case MouseEvent.BUTTON1 ://�����  ����֯��   &  ��ȫ��֯
				
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
					//��������,����֯ʱ,���Ҽ�  movingWRU�Ͳ����ɫ�ˡ�
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
	 * ����֯��ʱ�򣬴�����֯ �������֯�㡣
	 * 
	 * ֻ�������Ӧ��
	 * 
	 * һ��ȡ��moving��֯�е�ÿ���㡣
	 * 		1.remove
	 * 		2.set status & put new point
	 * 
	 */
	private void processMovingWRU(Point p) {
		
		
		/**
		 * i, j ���ӵ�  ����ȫ��֯��repW��repJ.
		 */
		for (int j=wru.getRepJ()-1; j>-1; j--) {
			for (int w=wru.getRepW()-1; w>-1; w--) {
				
				//�ж��������ǲ���    ���ڷ����ڡ��ǵĻ��Ž��д������ǵ�skip�ˡ�
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

	
	//�Ƴ�ԭ������֯�㡣
	//������λ�á���ȷ�����ꡣ
	private void removePoint(Point p) {
		translatePoint(p);
		
		ipMap.remove(cellCoor.toString());
		
		
	}
	
		
	
	/**
	 * �����굱ǰ�ǲ����ڷ������档�ڷ�������Ż���movingWRU & movingIPoint��
	 * ���ڷ������ϣ����С�
	 * 
	 * 1.����ֽ����   ����
	 * 2.Ϊ�˲��øմ򿪳���ʱ�ͻ�,����Ǵ򿪳����,�����½��и�����,�����޶�����Χ��
	 *����Ϊ�տ�ʼ��ʱ��updateCoor.x��updateCoor.y��Ĭ��ֵ���㣩 
     * ���ԣ�updateCoor ��new������ʱ�򣬰�Ĭ��ֵ�ĳ�(-1, -1)�ͺ��ˡ�
	 * 
	 * @param p
	 * 
	 * <br>����ʵʱ���ꡣ
	 * 
	 * @return
	 * 
	 * ��grid���棬����true��
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




























