/*******************************************************************************
 * Copyright (c) 2008 Jeong Ju Ho.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Jeong Ju Ho - initial API and implementation
 ******************************************************************************/
package org.jhlabs.test;

import org.jhlabs.scany.client.ScanyClient;
import org.jhlabs.scany.client.ScanyClientBuilder;
import org.jhlabs.scany.client.ScanyClientException;
import org.jhlabs.scany.config.ScanyConfig;
import org.jhlabs.scany.entity.PrimaryKey;
import org.jhlabs.scany.entity.Record;
import org.jhlabs.scany.search.AnySearcher;
import org.jhlabs.scany.search.SortColumn;
import org.jhlabs.scany.search.Summarizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ScanySearcherDemo {
	
	public static ScanyClient getScanyClient() {
		try{
			String resource = "d:\\@WORK\\Gulendol\\Scany\\conf\\client.xml";
			InputStream is = new FileInputStream(new File(resource));
			ScanyClient scanyClient = ScanyClientBuilder.buildScanyClient(is, "LOCAL");
			
			return scanyClient;
		} catch(Exception e) {
			//e.printStackTrace();
			throw new ScanyClientException("Error initializing ScanyClient", e);
		}
	}
	
	public static void usualSearchDemo() {
		try {
			ScanyClient scanyClient = getScanyClient();

			AnySearcher searcher = scanyClient.getSearcher("notice");
			
			searcher.setHitsPerPage(100);
			
			Summarizer summarizer = new Summarizer();
			
			searcher.addSummarizer("title", summarizer);
			
			
			// Primary Key 생성
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setKeyValue("boardId", "java");
			primaryKey.setKeyValue("articleNo", "*");
			
			//searcher.addQueryColumn("title");
			//searcher.addQueryColumn("content");

			//searcher.addFilterColumn(primaryKey);
			//searcher.addFilterColumn("title", "사과", true);
			
	    	Record[] records = searcher.search("오 Apple 오리");
	    	//Record[] records = searcher.search("+(tag:Orange)");

	    	if(records != null) {
		    	for(int i = 0; i < records.length; i++) {
		    		System.out.print(records[i].getPrimaryKey());
		    		System.out.print(" ");
		    		System.out.println(records[i].getColumnValue("title"));
		    	}
		    	
		    	if(records.length == 0)
		    		System.out.println("결과 없음");
	    	} else {
	    		System.out.println("null");
	    	}
	    	
	    } catch(Exception e) {
	    	//System.out.println(e.toString());
	    	e.printStackTrace();
	    }
	}

	public static void randomSearchDemo() {
		try {
			ScanyClient scanyClient = getScanyClient();

			AnySearcher searcher = scanyClient.getSearcher("notice");
			
			searcher.setHitsPerPage(10);
			
			// Primary Key 생성
			PrimaryKey primaryKey = new PrimaryKey();
			primaryKey.setKeyValue("boardId", "notice");
			primaryKey.setKeyValue("articleNo", "*");
			
			// 정렬컬럼 생성
			SortColumn sortColumn = new SortColumn();
			sortColumn.addColumn(ScanyConfig.PRIMARY_KEY, SortColumn.AUTO, true);
			sortColumn.addColumn("date", SortColumn.STRING, true);

			searcher.setSortColumn(sortColumn);
			
			searcher.addFilterColumn(primaryKey);
	    	
	    	Record[] records = searcher.random();

	    	for(int i = 0; i < records.length; i++) {
	    		System.out.print(records[i].getPrimaryKey() + "   ");
	    		System.out.println(records[i].getColumnValue("date"));
	    	}
	    	
	    	if(records.length == 0)
	    		System.out.println("결과 없음");
	    	
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}

	public static void sequentialSeekDemo() {
		try {
			ScanyClient scanyClient = getScanyClient();

			AnySearcher searcher = scanyClient.getSearcher("notice");
			
	    	Record[] records = searcher.seek(0, 10, true);

	    	if(records != null) {
		    	for(int i = 0; i < records.length; i++) {
		    		System.out.print(records[i].getPrimaryKey());
		    		System.out.print(" ");
		    		System.out.println(records[i].getColumnValue("title"));
		    	}
		    	
		    	if(records.length == 0)
		    		System.out.println("결과 없음");
	    	} else {
	    		System.out.println("null");
	    	}
	    	
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	}

	public static void main(String[] args) {
		try {

			usualSearchDemo();
			//randomSearchDemo();
			//sequentialSeekDemo();
	    	
	    } catch(Exception e) {
	    	//System.out.println("===================================================");
	    	//System.out.println(e.getStackTrace());
	    	//e.printStackTrace();
	    }
	}

}
