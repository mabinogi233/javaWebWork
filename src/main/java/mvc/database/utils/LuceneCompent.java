package mvc.database.utils;

import mvc.database.entity.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.w3c.dom.Text;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class LuceneCompent {


    public static final String INDEX_FILE_PATH = "/Users/liuwenze/Documents/dx/20220726_作业_刘文泽/indexs";

    /**
     * 添加索引
     * @param book
     * @throws IOException
     */

    public static synchronized void add(Book book) throws IOException {
        Document document = new Document();

        document.add(new TextField("name", book.getBname().toLowerCase(), Field.Store.YES));
        document.add(new TextField("author", String.valueOf(book.getBauthor()), Field.Store.YES));
        document.add(new StringField("mysql_primary_key", String.valueOf(book.getBid()), Field.Store.YES));
        document.add(new StringField("AllFieldFlag", "*", Field.Store.YES));

        Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
        Analyzer analyzer = new IKAnalyzer();

        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, conf);
        indexWriter.addDocument(document);
        indexWriter.commit();
        indexWriter.close();
    }

    /**
     * 标题查询
     * @param name
     * @param type 0为起始页
     * @param page
     * @param size
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static synchronized List<Integer> searchByName(String name, int type, int page, int size) throws IOException, ParseException {
        Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("name", new IKAnalyzer());
        Query query;
        if(type==0) {
            //普通查询
            query = parser.parse(name);
        }else if(type==1) {
            //通配符查询
            query = new WildcardQuery(new Term("name", "*"+ name +"*"));;
        }else if(type==2){
            //模糊查询
            query = new FuzzyQuery(new Term("name",name),2);
        }else {
            query = parser.parse(name);
        }
        Sort sort = new Sort(new SortField("mysql_primary_key", SortField.Type.INT,true));
        TopDocs topDocs = searcher.search(query, (page+1)*size,sort);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<Integer> result = new ArrayList<>();
        int start = Math.max(0,page * size);
        int end = Math.min(scoreDocs.length,(page+1)*size);
        for (int i=start;i<end;i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            // 取出文档编号
            int docID = scoreDoc.doc;
            // 根据编号去找文档
            Document doc = reader.document(docID);
            result.add(Integer.parseInt(doc.get("mysql_primary_key")));
        }
        return result;
    }

    public static synchronized List<Integer> searchByAuthor(String author, int type, int page, int size) throws IOException, ParseException {
        Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("author", new IKAnalyzer());
        Query query;
        if(type==0) {
            //普通查询
            query = parser.parse(author);
        }else if(type==1) {
            //通配符查询
            query = new WildcardQuery(new Term("author", "*"+ author +"*"));;
        }else if(type==2){
            //模糊查询
            query = new FuzzyQuery(new Term("author",author),2);
        }else {
            query = parser.parse(author);
        }
        Sort sort = new Sort(new SortField("mysql_primary_key", SortField.Type.INT,true));
        TopDocs topDocs = searcher.search(query, (page+1)*size,sort);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<Integer> result = new ArrayList<>();
        int start = Math.max(0,page * size);
        int end = Math.min(scoreDocs.length,(page+1)*size);
        for (int i=start;i<end;i++) {
            ScoreDoc scoreDoc = scoreDocs[i];

            int docID = scoreDoc.doc;

            Document doc = reader.document(docID);
            result.add(Integer.parseInt(doc.get("mysql_primary_key")));

        }
        return result;
    }


    /**
     * 主键查询
     * @param id
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static synchronized String searchById(int id) throws IOException, ParseException {
        Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        Query query = new TermQuery(new Term("mysql_primary_key",String.valueOf(id)));

        TopDocs topDocs = searcher.search(query,1);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        ScoreDoc scoreDoc = scoreDocs[0];

        int docID = scoreDoc.doc;
        Document doc = reader.document(docID);

        return doc.get("name");
    }

    /**
     * 全部查询
     * @param page 0为起始页
     * @param size
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static synchronized List<Integer> searchAll(int page, int size){
        try {
            Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
            IndexReader reader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(reader);
            Query query = new TermQuery(new Term("AllFieldFlag", "*"));

            Sort sort = new Sort(new SortField("mysql_primary_key", SortField.Type.INT,true));
            TopDocs topDocs = searcher.search(query, (page + 1) * size,sort);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            List<Integer> result = new ArrayList<>();
            int start = Math.max(0, page * size);
            int end = Math.min(scoreDocs.length, (page + 1) * size);
            for (int i = start; i < end; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                // 取出文档编号
                int docID = scoreDoc.doc;
                // 根据编号去找文档
                Document doc = reader.document(docID);
                result.add(Integer.parseInt(doc.get("mysql_primary_key")));
            }
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * 名称类型查询
     * @param name
     * @param author
     * @param type 0为起始页
     * @param page
     * @param size
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public static synchronized List<Integer> searchByName(String name,String author,int type, int page, int size) throws IOException, ParseException {
        Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));

        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser("name", new IKAnalyzer());
        // 创建查询对象
        Query query1;
        if(type==0) {
            //普通查询
            query1 = parser.parse(name);
        }else if(type==1) {
            //通配符查询
            query1 = new WildcardQuery(new Term("name", "*"+ name +"*"));;
        }else if(type==2){
            //模糊查询
            query1 = new FuzzyQuery(new Term("name",name),2);
        }else {
            query1 = parser.parse(name);
        }

        Query query2 = new TermQuery(new Term("author",author));

        BooleanQuery booleanQuery=new BooleanQuery();
        booleanQuery.add(query1,BooleanClause.Occur.MUST);
        booleanQuery.add(query2,BooleanClause.Occur.MUST);

        Sort sort = new Sort(new SortField("mysql_primary_key", SortField.Type.INT,true));
        TopDocs topDocs = searcher.search(booleanQuery, (page+1)*size,sort);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List<Integer> result = new ArrayList<>();
        int start = Math.max(0,page * size);
        int end = Math.min(scoreDocs.length,(page+1)*size);
        for (int i=start;i<end;i++) {
            ScoreDoc scoreDoc = scoreDocs[i];
            // 取出文档编号
            int docID = scoreDoc.doc;
            // 根据编号去找文档
            Document doc = reader.document(docID);
            result.add(Integer.parseInt(doc.get("mysql_primary_key")));

        }
        return result;
    }

    /**
     * 修改索引
     * @param page
     * @throws IOException
     */
    public static synchronized void update(Book page) throws IOException {
        Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        IndexWriter writer = new IndexWriter(directory, conf);
        Document doc = new Document();
        doc.add(new TextField("name", page.getBname().toLowerCase(), Field.Store.YES));
        doc.add(new StringField("mysql_primary_key", String.valueOf(page.getBid()), Field.Store.YES));
        doc.add(new TextField("author", page.getBauthor(),Field.Store.YES));
        doc.add(new StringField("AllFieldFlag", "*", Field.Store.YES));
        writer.updateDocument(new Term("mysql_primary_key",String.valueOf(page.getBid())), doc);
        writer.commit();
        writer.close();
    }

    /**
     * 删除索引
     * @param id
     * @throws IOException
     */
    public static synchronized void delete(int id) throws IOException {
        Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        IndexWriter writer = new IndexWriter(directory, conf);
        //根据词条进行删除
        Query query = new TermQuery(new Term("mysql_primary_key",String.valueOf(id)));
        writer.deleteDocuments(query);
        writer.commit();
        writer.close();
    }

    public static synchronized void removeAll() throws IOException {
        Directory directory = FSDirectory.open(new File(INDEX_FILE_PATH));
        IndexWriterConfig conf = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        IndexWriter writer = new IndexWriter(directory, conf);

        writer.deleteAll();
        writer.commit();
        writer.close();
    }

}
