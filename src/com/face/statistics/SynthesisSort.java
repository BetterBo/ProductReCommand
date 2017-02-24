package com.face.statistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.face.entity.Product;
import com.face.entity.ProductDAO;


/**
 * ������Ʒ���ۺ�������
 * @author bobobo
 *
 */
public class SynthesisSort {
	//����AHP��Ȩֵ�����õ�Ȩֵ���ݣ�����Ϊ�³ɽ��������������ǲ����ʡ��ղ������۸���
	Double[] WEIGHTS = {0.910607876 ,0.495384298 ,1.566408053,0.240738696 ,0.587864408 ,2.198996669};
	Double[] PARAMETERS_MAX = {100000.0,10000.0,1.0,10000.0,1.0,100.0};
	Double[] PARAMETERS_MIN = {0.0,0.0,0.0,0.0,0.0,0.0};
	/**
	 * ͨ�����������map���ݣ�����Ҫ����ۺ�������õ�ǰcount����Ʒ��ͨ�����򣬿��Եõ�����Ȩֵ�Ӵ�С��һ����Ʒ���У�Խ��ǰ��ȨֵԽ��
	 * @param map ԭʼ����map
	 * @param count ��Ҫǰ����Ʒ����
	 * @return
	 */
	public ArrayList<Product> sort(Map<Integer, Double> map,int count)
	{
		if(map.isEmpty())
		{
			return null;
		}
		//map_new��Doubleֵǰcount��������γɵ�map
		Map<Integer,Double> map_new = mapSort(map, count);
		//��mapֵ����ƷID������map�е�Integer
		Integer[] goodsIds = new Integer[map_new.size()];
		Iterator<Integer> iterator_new = map_new.keySet().iterator();
		for(int i =0;i<map_new.size();i++){
			goodsIds[i] = iterator_new.next();
		}
		//�õ���Ʒ�б�
		ProductDAO prodao = new ProductDAO();
		ArrayList<Product> ProList = prodao.findByIds(goodsIds);
		if(ProList.isEmpty()){
			return null;
		}
		//���ݹ�һ��
		
		
		
		//�õ�prolist������Ȩֵ����
		Map<Product,Double> mapPro = new HashMap<Product,Double>();
		for (Product product : ProList) {
			Double weight = normalization(PARAMETERS_MAX[0], PARAMETERS_MIN[0], product.getSalesForMoon()) *WEIGHTS[0]
					+ normalization(PARAMETERS_MAX[0], PARAMETERS_MIN[0],product.getCommentCount())*WEIGHTS[1]
							+ normalization(PARAMETERS_MAX[0], PARAMETERS_MIN[0],product.getDisNegativeCommentRate())*WEIGHTS[2]
									+normalization(PARAMETERS_MAX[0], PARAMETERS_MIN[0],product.getCollection())*WEIGHTS[3]
											+normalization(PARAMETERS_MAX[0], PARAMETERS_MIN[0],(1/product.getPride()))*WEIGHTS[4]
													+normalization(PARAMETERS_MAX[0], PARAMETERS_MIN[0],map_new.get(product.getId()))*WEIGHTS[5];
			mapPro.put(product,weight);
		}
		
		//�½���������ݣ���Ȩ�شӴ�С����
		ArrayList<Product> proSortList = listSort(mapPro);
		
		return proSortList;
	}
	
	private ArrayList<Product> listSort(Map<Product,Double> mapPro) 
	{
		ArrayList<Product> proSortList = new ArrayList<Product>();
		Product[] scoreIndex = new Product[mapPro.size()];
		Double[] score = new Double[mapPro.size()];
		Iterator<Product> iterator = mapPro.keySet().iterator();
		for(int i =0;i<mapPro.size();i++){
			scoreIndex[i] = iterator.next();
			score[i] = mapPro.get(scoreIndex[i]);
		}
		for(int i = 0;i<mapPro.size();i++){
			Product bigestIndex = null;
			int jIndex = 0;
			Double bigest = -100.0;
			for(int j = 0;j<mapPro.size();j++){
				if(score[j]>bigest) {
					bigestIndex = scoreIndex[j];
					jIndex = j;
					bigest = score[j];
				}
			}
			proSortList.add(bigestIndex);
			score[jIndex] = -100.0;
		}		
		return proSortList;
	}
	
	
	private Map<Integer, Double> mapSort(Map<Integer, Double> map,int count) {
		Map<Integer,Double> map_new = new HashMap<Integer,Double>();
		Integer[] scoreIndex = new Integer[map.size()];
		Double[] score = new Double[map.size()];
		Iterator<Integer> iterator = map.keySet().iterator();
		for(int i =0;i<map.size();i++){
			scoreIndex[i] = iterator.next();
			score[i] = map.get(scoreIndex[i]);
		}
		for(int i = 0;i<count;i++){
			int bigestIndex = 0;
			int jIndex = 0;
			Double bigest = -100.0;
			for(int j = 0;j<map.size();j++){
				if(score[j]>bigest) {
					jIndex = j;
					bigestIndex = scoreIndex[j];
					bigest = score[j];
				}
			}
			map_new.put(bigestIndex, bigest);
			score[jIndex] = -100.0;
		}		
		return map_new;
	}
	
	public Double normalization(double max,double min,double val)
	{
		if(val>=max){
			return 1.0;
		}
		return Math.abs((val-min)/(max-min));
	}
}
