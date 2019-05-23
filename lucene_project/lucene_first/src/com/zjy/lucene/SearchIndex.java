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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

/**
 * @Auther: 无聊的人
 * @Date: 2019/5/23
 * @Description: com.zjy.lucene
 */
public class SearchIndex {

    private IndexReader indexReader;
    private IndexSearcher indexSearcher;


    @Before
    public void init() throws Exception{
        // 创建一个IndexWriter对象，需要使用IKAnalyzer作为分析器
        indexReader = DirectoryReader.open(FSDirectory.open(new File("E:\\javaframework\\lucene_project\\temp\\index").toPath()));
        indexSearcher = new IndexSearcher(indexReader);
    }

    @Test
    public void testRangeQuery() throws Exception{
        //  创建一个query对象，
        Query query = LongPoint.newRangeQuery("size", 0l, 100l);
        printResult(query);
    }

    @Test
    public void testQueryParser() throws Exception{
        //  创建一个queryParser对象，两个参数
        QueryParser queryParser = new QueryParser("name", new IKAnalyzer());
        //  参数1： 默认搜索域，  参数2： 分析器对象
        //  使用queryParser创建一个Query对象
        Query query = queryParser.parse("lucene是一个Java开发的全文检索工具包");
        printResult(query);
    }

    private void printResult(Query query) throws Exception{
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
}
