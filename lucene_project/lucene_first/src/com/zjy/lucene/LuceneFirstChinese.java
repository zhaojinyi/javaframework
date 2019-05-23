package com.zjy.lucene;////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//            佛祖保佑       永不宕机     永无BUG                    	  //
////////////////////////////////////////////////////////////////////

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @Auther: 无聊的人
 * @Date: 2019/5/23
 * @Description: com.zjy.lucene
 */
public class LuceneFirstChinese {

    @Test
    public void createIndex() throws IOException {
        //  1创建一个Director对象。指定索引库保存位置
        //   RAMDirectory 吧索引库保存在磁盘上
        Directory directory = FSDirectory.open(new File("E:\\javaframework\\lucene_project\\temp\\index").toPath());
        //  2. 基于Directory对象创建一个IndexWriter对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(new IKAnalyzer());
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        //  3. 读取磁盘上的文件， 对应每个文件创建一个文档对象
        File dir = new File("E:\\javaframework\\lucene_project\\searchsource");
        File[] files = dir.listFiles();
        for(File f:files) {
            //  文件名称
            String fileName = f.getName();
            //   文件路径
            String filePath = f.getPath();
            //  文件内容
            String fileContent = FileUtils.readFileToString(f, "utf-8");
            //  文件大小
            long fileSize = FileUtils.sizeOf(f);
            //   创建Field
            //   参数1： 域的名称   参数2： 域的内容  参数3： 是否存储在磁盘
            Field fieldName = new TextField("name", fileName, Field.Store.YES);
            // Field fieldPath = new TextField("path", filePath, Field.Store.YES);
            Field fieldPath = new StoredField("path", filePath);
            Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
            // Field fieldSize = new TextField("size", fileSize+"", Field.Store.YES);
            Field fieldSizeValue = new LongPoint("size",fileSize);
            Field fieldSizeStore = new StoredField("size", fileSize);
            //  创建文档对象
            Document document = new Document();
            //  4.  向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            // document.add(fieldSize);
            document.add(fieldSizeStore);
            document.add(fieldSizeValue);
            //  5.  把文档对象写入索引库
            indexWriter.addDocument(document);
        }
        //  6.  关闭IndexWriter对象
        indexWriter.close();

    }

    @Test
    public void searchIndex() throws IOException {
        // 1.创建一个Directory对象，指定索引库位置
        Directory directory = FSDirectory.open(new File("E:\\javaframework\\lucene_project\\temp\\index").toPath());
        // 2. 创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 3. 创建一个IndexSearch对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 4.  创建一个Query对象TermQuery
        Query query = new TermQuery(new Term("name", "spring"));
        // 5. 执行查询，得到一个TopDocs对象
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 6. 去查询结果的总记录数
        System.out.println("总记录数"+topDocs.totalHits);
        // 7. 取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 8. 打印文档中的内容
        for (ScoreDoc scoreDoc:scoreDocs) {
            //  取文档id
            int docId = scoreDoc.doc;
            //  根据id取文档对象
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
//            System.out.println(document.get("content"));
            System.out.println(document.get("size"));
            System.out.println("-------寂寞的分割线---------");
        }
        // 9. 关闭IndexReader对象
        indexReader.close();
    }

    @Test
    public void testTokenStream() throws Exception {
        //  1. 创建一个Analyzer对象， standardAnalyzer对象
//        Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        //  2. 使用分析器对象的tokenStream方法获得一个TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("", "\n" +
                "传智播客的Spring框架是由于软件开发的复杂性而创建的。Spring使用的是基本的JavaBean来完成以前只可能由EJB完成");
        //  3. 向token Stream对象中设置一个引用，相当于一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        //  4. 调用tokenStream对象的reset方法，如果不调用抛出异常
        tokenStream.reset();
        //  5. 使用while循环
        while(tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
        //  6. 关闭tokenStream对象
        tokenStream.close();
    }

}
