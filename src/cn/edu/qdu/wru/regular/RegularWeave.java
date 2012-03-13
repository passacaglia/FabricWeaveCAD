package cn.edu.qdu.wru.regular;

import java.awt.Point;

import cn.edu.qdu.DesignPaper;
import cn.edu.qdu.wru.InterlacingPoint;
import cn.edu.qdu.wru.WeaveRepeatUnit;

abstract class RegularWeave extends WeaveRepeatUnit {
	
	RegularWeave(DesignPaper dp, Point wruCoor) {
		super(dp, wruCoor);
	}


	/**
	 * <p>给经纬纱循环数赋值。
	 * 根据经纬纱循环数，生成组织矩阵。（存放一系列的组织点）
	 * 给矩阵的首列赋值。
	 * 根据首列，给其他列赋值。
	 * 
	 * 不同的组织，具体修改。
	 */
	protected void initialize() {
	
		this.setN_J();
		this.setShift_J();
		this.setFl_J();
		
		//下面是   组织,经纬纱循环数
		defineMatrix();
		
		//下面是   何种组织点
		definePoint();
		
		//下面是   位置
		addPoint();
		
	}
	
	
	/**
	 * 根据浮长&交叉次数确定经纬循环纱线数。
	 * <br>然后再确定组织矩阵。
	 */
	@Override
	protected void defineMatrix() {
		
		//纬
		/**给<i>纬纱循环数</i>赋值。
		 * <br>把每个交叉点<i>处<i>  的浮长fl_J[i]  挨个加起来。 
		 * <br>一共n次交叉。
		 */
		for (int i=0; i<n_J; i++) {
			repW += fl_J[i]; 
		}
		
		//经
		if (repW % Math.abs(shift_J) != 0) {
			repJ = repW;
		} else {
			repJ = repW / Math.abs(shift_J);
		}

		//Basic Matrix & Weave Matrix
		_matrix = new boolean[repJ][repW];
		weaveMatrix = new boolean[repJ][repW];
				
	}
	
	
	
	
	/**
	 * 根据公式，确定矩阵中哪个点是<b>经</b>，哪个点是<i>纬</i>.
	 * 
	 * 按需重写此方法。
	 */
	@Override
	protected void definePoint() {
		
		/**
		 * 1.
		 * <strong>首列</strong>
		 * _matrix
		 */
		
		//preparation
		/**为什么不 在循环中直接使用k?
		 * 原因:那样的话,k就只能在此循环中用了.
		 * 外面不能用.
		 */		
		int[] sumFL = new int[n_J];
		sumFL[0] = fl_J[0];
		for (int j=1; j<n_J; j++) {
				sumFL[j] = sumFL[j-1] + fl_J[j];
		}
	
		
		int k = -1;
		
		//get to work
		for (int w=0; w<repW; w++) {
			
			/**
			 * <b>首列</b>  赋值
			 * 有两种情况：(貌似直接else就成了，不用else if)(???)
			 * 				(原因:不满足  行数<=第一个浮长的长度，肯定是第二个了)
			 * 
			 * 1.	第index行，index <= fl_J[0], _matrix[0][index] = true;
			 * 2.	step-1: fl_J[0]+fl_J[1]+...+fl_J[k-1] < index <= fl_J[0]+fl_J[1]+...+fl_J[k]  (k = 2, 3, ..., n_J)
			 * 		step1:	_matrix[0][index] = true;  k是奇数
			 * 		step2:	_matrix[0][index] = false; k是偶数
			 */
			if ((w+1) <= fl_J[0]) {
				_matrix[0][w] = true;
			} else {
				
				//get "k"
				for (int i=2; i<=n_J; i++) {//从第二次交叉开始.
					/**
					 *(w+1)是行号,
					 *sumFL[(2-1)]是第二次交叉后,包括此个浮长在内,之前所有浮长的和。
					 */
					if ( (w+1) - sumFL[(i-1)] <= 0) {
						k = i;
						break;
					}
					
				}
				
				//value assignment
				if ( k % 2 == 0 ) {
					_matrix[0][w] = false;
				} else {
					_matrix[0][w] = true;
				}
		
			}
			
		}
		
		
		/**
		 * 2.
		 * 其他列：
		 * 
		 * 第(j+1)列, 即从  首列  往后的
		 * j=1是第二列.
		 * 
		 */
		for (int j=1; j<repJ; j++) {
			
			for (int w=0; w<repW; w++) {
				
				if ((w+1)+shift_J <= repW) {
					_matrix[j][w+shift_J] = _matrix[j-1][w];
				} else if ((w+1)+shift_J > repW) {
					_matrix[j][w+shift_J-repW] = _matrix[j-1][w];
				}
				
			}
			
		}
		
		
		/**
		 * 3.
		 * 通过_matrix  给 weaveMatrix赋值。
		 */
		for (int j=0; j<repJ; j++) {
			for (int w=0; w<repW; w++) {
				weaveMatrix[j][w] = _matrix[j][(repW-1)-w];//注意：只是对  纬纱 反过来。所以有两个[1]。
			}
		}

	}
	
	
	
	

	
	/**
	 * 根据之前确定的true/false，
	 * 是<b>经</b>组织点,还是<i>纬组织点</i>，
	 * 在集合中添加相应的组织点。 
	 */
	@Override
	protected void addPoint() {
	
		for (int j=0; j<repJ; j++) {
			for (int w=0; w<repW; w++) {
				
				if (weaveMatrix[j][w]) {
					//根据意匠纸的方格大小，重新计算，取得真正的x,y。
					ipList.add(new InterlacingPoint(this.dp, 
												      new Point(wruCoor.x + j * dp.getCellSize(), 
														         wruCoor.y + w * dp.getCellSize()),
					                                  true) 
					             );
				} else {
					ipList.add(new InterlacingPoint(this.dp, 
												      new Point(wruCoor.x + j * dp.getCellSize(), 
							                                    wruCoor.y + w * dp.getCellSize()),
							                          false)
					            );
				}
			}
		}
	
	}
	
	
	
/*	
	
	abstract void setN();

	abstract void setShift_J();

	abstract void setFl_J();

*/	
	
	
}
