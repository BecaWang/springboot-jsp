package main.java.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.junit.Test;

import com.opencsv.CSVReader;

import junit.framework.TestCase;
import main.java.com.model.Stock;

public class test extends TestCase{
	
	String preDay = "20180227";
	String toDay = "20180301";
	
	
	@Test
	public void testGo() throws Exception {
		getDailyFile(preDay);
		getDailyFile(toDay);
		getDailyLegalPersonFile(toDay);
		test();

	}
	
	

	@Test
	public void getDailyFile(String date) {
		try {
			String url = "http://www.twse.com.tw/exchangeReport/MI_INDEX?response=csv&date="+date+"&type=ALLBUT0999";
			Runtime.getRuntime().exec("open " + url);
			
			TimeUnit.SECONDS.sleep(5);

			File afile = new File("/Users/becawang/Downloads/MI_INDEX.csv");
			if (afile.renameTo(new File("/Users/becawang/Documents/股票研究所/每日收盤行情/"+date+".csv"))) {
				System.out.println("File is moved successful: "+ date);
			} else {
				System.out.println("File is failed to move: "+ date);
			}
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void getDailyLegalPersonFile(String date) {
		//http://www.twse.com.tw/fund/T86?response=csv&date=20180301&selectType=ALLBUT0999
		try {
			String url = "http://www.twse.com.tw/fund/T86?response=csv&date="+date+"&selectType=ALLBUT0999";
			Runtime.getRuntime().exec("open " + url);
			
			TimeUnit.SECONDS.sleep(3);

			File afile = new File("/Users/becawang/Downloads/T86.csv");
			if (afile.renameTo(new File("/Users/becawang/Documents/股票研究所/三大法人買賣超日報/"+date+".csv"))) {
				System.out.println("DailyLegalPersonFile is moved successful: "+ date);
			} else {
				System.out.println("DailyLegalPersonFile is failed to move: "+ date);
			}
			
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void test() throws Exception{
		Map yesyterdayMap = getMap("/Users/becawang/Documents/股票研究所/每日收盤行情/"+preDay+".csv"); //昨天
		Map todayMap = getMap("/Users/becawang/Documents/股票研究所/每日收盤行情/"+toDay+".csv"); //今天
		Map buyAndSellMap = getBuyAndSellMap("/Users/becawang/Documents/股票研究所/三大法人買賣超日報/"+toDay+".csv");//取外資買超個股


	    List<String> greenKList =  getGreenK(yesyterdayMap);
	    List<String> resultList = getResult(greenKList, yesyterdayMap, todayMap);
	    resultList = getResultWithForeignInvestor(buyAndSellMap, resultList); //結合外資買超
	    
	    showResult(resultList); //顯示股號
//	    openUrl(resultList); //開網頁
	}
	
	public Map getMap(String csvFile){
		boolean startRecordPoint = false;
		Map map = new HashMap();
		
		 try {
			
			FileInputStream fis = new FileInputStream(csvFile); 
			InputStreamReader isr = new InputStreamReader(fis, "BIG5");
			CSVReader reader = new CSVReader(isr);
			
			
			for (String[] row; (row = reader.readNext()) != null;) {
//			    System.out.println(Arrays.toString(row));
			    
			    if(row[0].equals("1101")){
			    	startRecordPoint = true;
			    }
			    
			    //結束條件
			    if(row[0].equals("備註:")&&startRecordPoint==true){
			    	break;
			    }
			    
			    
			    if(startRecordPoint){
			    	
			    	//欄位非數字則跳過
					if(!isInteger(row[5])||!isInteger(row[6])||!isInteger(row[7])||!isInteger(row[8])){
					continue;
					}
					
			    	Stock stock = new Stock();
			    	stock.setStockNbr(row[0]);
					stock.setStockName(row[1]);
					
					stock.setOpenPrice(Double.parseDouble(row[5]));
					stock.setHighPrice(Double.parseDouble(row[6]));
					stock.setLowPrice(Double.parseDouble(row[7]));
					stock.setClosePrice(Double.parseDouble(row[8]));
					map.put(row[0], stock);
					
			    }
			    
			}
			
			reader.close();
			isr.close();
			fis.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return map;
	}
	
    public static boolean isInteger(String value) {
		
	    Pattern pattern = Pattern.compile("^([-+]?\\d+)(\\.\\d+)?$");
	    return pattern.matcher(value).matches();
	}
	
    
	public 	List<String> getGreenK(Map map){
		List list = new ArrayList<String>();
		
		Iterator iterator = map.entrySet().iterator();
		  while (iterator.hasNext()) {
		   Map.Entry mapEntry = (Map.Entry) iterator.next();
		   		   
		   Stock stock = (Stock) mapEntry.getValue();
		    double open = stock.getOpenPrice();
			double high = stock.getHighPrice();
			double low  = stock.getLowPrice();
			double close =stock.getClosePrice();  
			
			if(low == close && open == high && close != open && close>10){
				list.add(stock.getStockNbr());
			}
			

			//倒垂線先忽略
//			if(low == close &&  close == open && close>10){
//				list.add(stock.getStockNbr());
//			}
			
		  }
		return list;
	}
	
	//取技術型態符合的
	public ArrayList<String> getResult(List<String> greenKList,Map yesyterdayMap, Map todayMap){
       List<String> resultList = new ArrayList<String>();
		
		for(String stockNbr :greenKList){
			
			if(todayMap.get(stockNbr)!= null){
				Stock todayStock = (Stock) todayMap.get(stockNbr);
				Stock yesterdayStock = (Stock) yesyterdayMap.get(stockNbr);
				
				if(todayStock.getClosePrice() < todayStock.getOpenPrice() && todayStock.getOpenPrice() > yesterdayStock.getClosePrice()){
					resultList.add(todayStock.getStockNbr());
//					System.out.println(stockNbr+":"+todayStock.getOpenPrice()+":"+yesterdayStock.getClosePrice());
					
				}
				
			}
		}
		
		return (ArrayList<String>) resultList;
	}
	
	public void showResult(List<String> resultList){
		for(String stockNbr :resultList){
			System.out.println(stockNbr);
		}
	}
	
	public void openUrl(List<String> resultList) throws Exception{
		
		for(String stockNbr :resultList){
			double value = Double.parseDouble(stockNbr);
			int b = (int) value;
			String stringValue = String.valueOf(b);
			String url = "http://www.wantgoo.com/stock/astock/techchart?StockNo="+stringValue;
			Runtime.getRuntime().exec("open " + url);
			}
	}
	
	@Test
	public Map getBuyAndSellMap(String csvFile){
		
		boolean startRecordPoint = false;
		Map map = new HashMap();

		try {
		FileInputStream fis = new FileInputStream(csvFile);
		InputStreamReader isr = new InputStreamReader(fis, "BIG5");
		CSVReader reader = new CSVReader(isr);
		
		for (String[] row; (row = reader.readNext()) != null;) {
		    
		    if(row[0].equals("證券代號")){
		    	startRecordPoint = true;
		    	continue;
		    }
		    
		    //結束條件
		    if(row[0].equals("說明:")&&startRecordPoint==true){
		    	break;
		    }
		    
		    if(startRecordPoint){
		    	
				
				if(Double.parseDouble(row[4].replace(",", "")) > 0){
					map.put(row[0], Double.parseDouble(row[4].replace(",", "")));
				}
		    }
		    
		}
		
		reader.close();
		isr.close();
		fis.close();
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("外資買超數量："+ map.size());
		return map;
	}

	public ArrayList<String> getResultWithForeignInvestor( Map buyAndSellMap, List<String> resultList){
		List list = new ArrayList<String>();

		
		for(String stockNbr: resultList){
			
			if(buyAndSellMap.get(stockNbr)!=null){
				list.add(stockNbr);
			}
		}
		
		return (ArrayList<String>) list;
	}
	
}
