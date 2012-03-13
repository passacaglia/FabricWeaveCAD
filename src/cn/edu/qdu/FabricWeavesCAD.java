package cn.edu.qdu;

/**
 * import
 * ����ʱ�������֪��������Щ�࣬�Ͱ�import�Ķ�����del��Ȼ��һ�������롣
 */
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;

import cn.edu.qdu.wru.InterlacingPoint;

/**
 * ���֣�
 * panel���м䣬northҲ�Ÿ�panel������Ų˵���toolbar)
 * 
 * 
 * 
 * @author zxc
 *
 */
class FabricWeavesCAD extends Frame {

	private static final int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static int posX = (int)(tk.getScreenSize().getWidth()/2 - WINDOW_WIDTH/2);
	private static int posY = (int)(tk.getScreenSize().getHeight()/2 - WINDOW_HEIGHT/2);
	
	//structure | layout
	private Panel panelDP = null;
	
	//menu
	private ActionMonitor am = new ActionMonitor();
	private FileDialog fd = null;
	private File filePath;//file path of "Open" or "Save".
	private String userHome = System.getProperty("user.home");
		//Save & Open
	BufferedImage bi = null;
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	private HashMap<String, InterlacingPoint> ipMap = new HashMap<String, InterlacingPoint>();
	private HashMap<Point, Boolean> hm = null;
	
	
	//west
	private Panel leftPanel = null;
	private Label label0 = null;
	private Choice choice = null;
	
	//south
	private Panel labelPanel = null;
	private Label label1 = null;//dp����set |��move
	private Label label2 = null;//dp����set | click
	private Label label3 = null;//this����set |��item��changed
	
	//content
	private DesignPaper dp = new DesignPaper(this, 8, 8);
	
	
	
	
	void launchFrame() {
		//the Frame itself
		setBounds(posX, posY, WINDOW_WIDTH, WINDOW_HEIGHT);
		setTitle("Fabric Weaves CAD");
		setBackground(Color.WHITE);
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				exitFWCAD();
			}
			
		});
		
		//structure | layout
		
		
		
		//center
		/**
		 * ʹ��BorderLayout֮�󣬻�����design paper�Ͳ��ٱ���סһ�����ˡ�
		 * <br /> ò�Ʋ��ܸ�panelָ��λ�á�
		 *
		 *
		 */
		panelDP = new Panel(new BorderLayout());
		/*
		panelDP.setSize(InterlacingPoint.SIZE * dp.getColumns(),
		      			InterlacingPoint.SIZE * dp.getRows());
		 * 
		 */
		this.add(panelDP);//Ĭ����ӵ�CENTER��
		
		
		
		
		//menu
		//new | setActionCommand | addActionListener | add to Menu
		Menu file = new Menu("File");
		//0.
		MenuItem open = new MenuItem("Open");
		MenuItem saveAsImg = new MenuItem("Save As Image");
		MenuItem saveAsFWA = new MenuItem("Save As FWA");
		MenuItem exit = new MenuItem("Exit");
		//1.
		open.setActionCommand("Open");
		saveAsImg.setActionCommand("Save As Image");
		saveAsFWA.setActionCommand("Save As FWA");
		exit.setActionCommand("Exit");
		//2.
		open.addActionListener(am);
		saveAsImg.addActionListener(am);
		saveAsFWA.addActionListener(am);
		exit.addActionListener(am);
		//3.
		file.add(open);
		file.add(saveAsImg);
		file.add(saveAsFWA);
		file.add(exit);
		
		MenuBar mb = new MenuBar();
		mb.add(file);
		this.setMenuBar(mb);
		
		
		
		
		
		//west
		//left panel
		leftPanel = new Panel(new GridLayout(2, 1));
		this.add(leftPanel, BorderLayout.WEST);
		
		choice = new Choice();
		addChoiceItems();
		choice.addItemListener(new ItemMonitor());
		leftPanel.add(choice);

		label0 = new Label("��ѡ��");
		leftPanel.add(label0);
		
		
		
		//south
		//label 
		labelPanel = new Panel(new GridLayout(3, 1));
		this.add(labelPanel, BorderLayout.SOUTH);
		
		label1 = new Label("Cursor Position : ");//mouseMoved��ʱ��setText��
		labelPanel.add(label1);
		
		label2 = new Label("Map Size : ");
		setLabel2();
		labelPanel.add(label2);
		
		label3 = new Label("Moving Map Size : ");
		setLabel3();
		labelPanel.add(label3);
		
		//content
		panelDP.add(dp, BorderLayout.CENTER);

		dp.addMouseListener(new MouseMonitor());//this listener is for design-paper.
		dp.addMouseMotionListener(new MouseMonitor());
		

		setVisible(true);
		
		
	}

	/**
	 * ���choiceѡ�
	 * <br>
	 * <br>		1.ItemMonitor��</br>
	 * <br>		2.DP�����õ��ĵط���</br>
	 */
	private void addChoiceItems() {
		
		/**
		 * ��HashMap��Ҫ��	�����Ķ�/��Ӧ  index & String��
		 */
		HashMap<Integer, String> choiceItems = new HashMap<Integer, String>();
		
		//2012.2.22��,����ӵ���֯,ֻҪ�޸�generateAndAddWRUPoints�����ˡ�
		//& Item Monitor
		choiceItems.put(0, "��ѡ��");
		choiceItems.put(1, "Interlacing Point");//IP
		choiceItems.put(2, "Plain Weave _ S");
		choiceItems.put(3, "Plain Weave _ D");
		choiceItems.put(4, "Twill _ Original");
		choiceItems.put(5, "Satin _ Original");
		
		for (String str : choiceItems.values()) {
			choice.add(str);
		}
		

	}
	
	
	
	private void exitFWCAD() {
		System.exit(0);
	}
	
	
	
	Choice getChoice() {
		return choice;
	}
	
	
/*
	Label getlabel0() {
		return label0;
	}
	
	Label getLabel1() {
		return label1;
	}
	
	Label getLabel2() {
		return label2;
	}
	
	

	Label getLabel3() {
		return label3;
	}

*/	
	
	//�����ʱ�򣬸�һ�£���Ϊ������/���С�
	void setLabel1(MouseEvent e) {
		
		label1.setText("Cursor Position : ( " 
		     			+ (e.getX() / dp.getCellSize() + 1) + ", " 
			    		+ (e.getY() / dp.getCellSize() + 1) + " )"
				    	);
		
	}
	
	
	void setLabel2() {
		
		label2.setText("IPMap size : " + dp.getIPMapSize());
		
	}
	

	void setLabel3() {
		
		label3.setText("movingIPMap size : " + dp.getMovingIPListSize());
		
	}
	

	
	
	
	
	
	@Override
	public void paint(Graphics g) {
		
		dp.paint(g);
		
	}

		
	private class ItemMonitor implements ItemListener {

		@Override
		public void itemStateChanged(ItemEvent e) {
			
//			String s = (String)e.getItem();
			
			int i = choice.getSelectedIndex();
			
			if (0 == i) {
				dp.setDrawingPoint(false);
				dp.setDrawingWRU(false);
			} else if (1 == i) {
				dp.setDrawingPoint(true);
				dp.setDrawingWRU(false);
			} else {
				dp.setDrawingPoint(false);
				dp.setDrawingWRU(true);
			}
			
			switch(i) {
			
			case 0 :
				label0.setText("��ѡ��");
				break;
			case 1 :
				label0.setText("����(��)/�һ�(γ)");
				break;
			case 2 :
				label0.setText("����ƽ��");
				break;
			case 3 :
				label0.setText("˫��ƽ��");
				break;
			case 4 :
				label0.setText("ԭ��֯б��");
				break;
			case 5 :
				label0.setText("ԭ��֯����");
				break;
				
			}
			

		}
		
	}
	
	
	
	private class MouseMonitor extends MouseAdapter {

		
		@Override
		public void mouseMoved(MouseEvent e) {
			dp.mouseMoved(e);
//System.out.println("mouse moved");
		}
		
		
		@Override
		public void mouseClicked(MouseEvent e) {
			dp.mouseClicked(e);
		}


		@Override
		public void mouseExited(MouseEvent e) {
			dp.getMovingIPList().clear();
						
			dp.setMoving(false);//����mouseEntered,setMoving(true), because mouse moved()�����ˡ�
			dp.repaint();
		}

		
	}
	
	
	
	private class ActionMonitor implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if ("Open" == e.getActionCommand()) {
				openFileDialog("Open");
				filePath = new File(fd.getDirectory() + "/" + fd.getFile());
				openFWCAD(filePath);
			}
			
			if ("Save As Image" == e.getActionCommand()) {
				openFileDialog("Save As Image");
				filePath = new File(fd.getDirectory() + "/" + fd.getFile() + ".png");
				saveAsImage(filePath , "png");
			}
			
			if ("Save As FWA" == e.getActionCommand()) {
				openFileDialog("Save As FWA");
				filePath = new File(fd.getDirectory() + "/" + fd.getFile() + ".fwa");
				saveAsFWCAD(filePath);
			}
			
			if ("Exit" == e.getActionCommand()) {
				exitFWCAD();
			}
			
		}
		
	}
	
	
	/**
	 * Input & Output
	 */
	
	
	/**
	 * Ϊʲô��д��ActionMonitor�������?
	 * new FileDialog(���ﲻ��д);
	 * @param s
	 */
	private void openFileDialog(String s) {
		
		fd = new FileDialog(this);

		
		if ("Open" == s) {
			fd.setTitle("Open");
			fd.setMode(FileDialog.LOAD);
			fd.setFile("*.fwa");
		}

		if ("Save As Image" == s) {
			fd.setTitle("Save As Image");
			fd.setMode(FileDialog.SAVE);
			fd.setFile("*.png");
		}
		
		if ("Save As FWA" == s) {
			fd.setTitle("Save As FWA");
			fd.setMode(FileDialog.SAVE);
			fd.setFile("���и�ʽ");
		}
		
		
		fd.setLocation(0, 30);
		fd.setDirectory(userHome);
		
				
		fd.setVisible(true);
		
		
	}
	
	
	
	private void saveAsImage(File imgSavePath, String imageType) {
		
		try {
		
			bi = new BufferedImage(this.dp.getGridWidth()+2, this.dp.getGridHeight()+2, 
												  BufferedImage.TYPE_4BYTE_ABGR);
			Graphics g = bi.getGraphics();
			
			g.translate(1, 1);
			
			//fwcad.getChoice().select(0);//�����ַ���select��,��Ӧ��label������֮���ġ�
			this.repaint();
			paint(g);
			
			g.dispose();
			bi.flush();
			
			ImageIO.write(bi, imageType, imgSavePath);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	private void saveAsFWCAD(File fileSavePath) {
		
		try {
			oos = new ObjectOutputStream(new FileOutputStream(fileSavePath));
			oos.writeObject(this.dp.getIPMap_Save());
			
			oos.flush();
			oos.close();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	
	
	void openFWCAD(File fileOpenPath) {
		
		try {
			
			ois = new ObjectInputStream(new FileInputStream(fileOpenPath));
			hm = (HashMap<Point, Boolean>)ois.readObject();
			
			Iterator i = hm.entrySet().iterator();
			while (i.hasNext()) {
				Map.Entry<Point, Boolean> entry = (Map.Entry<Point, Boolean>)i.next();
				ipMap.put(entry.getKey().toString(), 
						  new InterlacingPoint(this.dp, entry.getKey(), entry.getValue()));
			}
			
			ois.close();
			this.dp.setIPMap(ipMap);
			this.dp.repaint();
			
			
		} catch (java.io.StreamCorruptedException sce) {
			sce.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	
	
	
}






















