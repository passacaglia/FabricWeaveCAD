package cn.edu.qdu;

import java.awt.Frame;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Launcher {
	
	static FabricWeavesCAD fwcad = null;
	
	public static void main(String[] args) {
		fwcad = new FabricWeavesCAD();
		fwcad.launchFrame();
		
		handleArgs(args);
	}
	
	private static void handleArgs(String[] args) {
		
		if (1 == args.length) {
			
			if (args[0].substring(args[0].length()-4).equals(".fwa")) {
				fwcad.openFWCAD(new File(args[0]));
			}
			
		}
	}
	
}

















































































































































































































































































































































































































































































































































