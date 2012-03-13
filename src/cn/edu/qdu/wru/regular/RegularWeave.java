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
	 * <p>����γɴѭ������ֵ��
	 * ���ݾ�γɴѭ������������֯���󡣣����һϵ�е���֯�㣩
	 * ����������и�ֵ��
	 * �������У��������и�ֵ��
	 * 
	 * ��ͬ����֯�������޸ġ�
	 */
	protected void initialize() {
	
		this.setN_J();
		this.setShift_J();
		this.setFl_J();
		
		//������   ��֯,��γɴѭ����
		defineMatrix();
		
		//������   ������֯��
		definePoint();
		
		//������   λ��
		addPoint();
		
	}
	
	
	/**
	 * ���ݸ���&�������ȷ����γѭ��ɴ������
	 * <br>Ȼ����ȷ����֯����
	 */
	@Override
	protected void defineMatrix() {
		
		//γ
		/**��<i>γɴѭ����</i>��ֵ��
		 * <br>��ÿ�������<i>��<i>  �ĸ���fl_J[i]  ������������ 
		 * <br>һ��n�ν��档
		 */
		for (int i=0; i<n_J; i++) {
			repW += fl_J[i]; 
		}
		
		//��
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
	 * ���ݹ�ʽ��ȷ���������ĸ�����<b>��</b>���ĸ�����<i>γ</i>.
	 * 
	 * ������д�˷�����
	 */
	@Override
	protected void definePoint() {
		
		/**
		 * 1.
		 * <strong>����</strong>
		 * _matrix
		 */
		
		//preparation
		/**Ϊʲô�� ��ѭ����ֱ��ʹ��k?
		 * ԭ��:�����Ļ�,k��ֻ���ڴ�ѭ��������.
		 * ���治����.
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
			 * <b>����</b>  ��ֵ
			 * �����������(ò��ֱ��else�ͳ��ˣ�����else if)(???)
			 * 				(ԭ��:������  ����<=��һ�������ĳ��ȣ��϶��ǵڶ�����)
			 * 
			 * 1.	��index�У�index <= fl_J[0], _matrix[0][index] = true;
			 * 2.	step-1: fl_J[0]+fl_J[1]+...+fl_J[k-1] < index <= fl_J[0]+fl_J[1]+...+fl_J[k]  (k = 2, 3, ..., n_J)
			 * 		step1:	_matrix[0][index] = true;  k������
			 * 		step2:	_matrix[0][index] = false; k��ż��
			 */
			if ((w+1) <= fl_J[0]) {
				_matrix[0][w] = true;
			} else {
				
				//get "k"
				for (int i=2; i<=n_J; i++) {//�ӵڶ��ν��濪ʼ.
					/**
					 *(w+1)���к�,
					 *sumFL[(2-1)]�ǵڶ��ν����,�����˸���������,֮ǰ���и����ĺ͡�
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
		 * �����У�
		 * 
		 * ��(j+1)��, ����  ����  �����
		 * j=1�ǵڶ���.
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
		 * ͨ��_matrix  �� weaveMatrix��ֵ��
		 */
		for (int j=0; j<repJ; j++) {
			for (int w=0; w<repW; w++) {
				weaveMatrix[j][w] = _matrix[j][(repW-1)-w];//ע�⣺ֻ�Ƕ�  γɴ ������������������[1]��
			}
		}

	}
	
	
	
	

	
	/**
	 * ����֮ǰȷ����true/false��
	 * ��<b>��</b>��֯��,����<i>γ��֯��</i>��
	 * �ڼ����������Ӧ����֯�㡣 
	 */
	@Override
	protected void addPoint() {
	
		for (int j=0; j<repJ; j++) {
			for (int w=0; w<repW; w++) {
				
				if (weaveMatrix[j][w]) {
					//�����⽳ֽ�ķ����С�����¼��㣬ȡ��������x,y��
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
