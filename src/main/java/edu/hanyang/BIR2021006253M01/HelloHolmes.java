package edu.hanyang.BIR2021006253M01;



import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.apache.lucene.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class HelloHolmes implements SimpleSE{
	

	
	public Directory createIndex(String[][] docs, Analyzer analyzer) throws IOException {
			
			Directory index=new RAMDirectory();
			
			IndexWriterConfig config=new IndexWriterConfig(analyzer);
			IndexWriter w =new IndexWriter(index,config);
			
			for(String[] doc: docs) {
				addDoc(w,doc[0],doc[1]);
			}
			
			w.close();
			
			return index;
			
			// TODO Auto-generated method stub
	
		}
	
	public String[][] search(Directory index, String querystr,Analyzer analyzer) throws ParseException, IOException {
		Query q= new QueryParser("txt",analyzer).parse(querystr);
		
		int hitsPerPage=10;
		IndexReader reader=DirectoryReader.open(index);
		IndexSearcher searcher=new IndexSearcher(reader);
		
		TopScoreDocCollector collector=TopScoreDocCollector.create(hitsPerPage);
		searcher.search(q,collector);
		ScoreDoc[] hits=collector.topDocs().scoreDocs;
		
		String[][] result=new String[hits.length][2];
		for(int i=0;i<hits.length;i++) {
				int docId=hits[i].doc;
				Document d = searcher.doc(docId);
				result[i][0]=d.get("lnum");
				result[i][1]=d.get("txt");
		}
		
		reader.close();
		
		return result;
	}
	
	public void addDoc(IndexWriter w, String lnum,String txt) throws IOException{
			Document doc=new Document();
			doc.add(new StringField("lnum", lnum, Field.Store.YES));
			doc.add(new TextField("txt", txt, Field.Store.YES));
			
			w.addDocument(doc);
		}

}
