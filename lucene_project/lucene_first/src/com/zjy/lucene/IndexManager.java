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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

/**
 * @Auther: 无聊的人
 * @Date: 2019/5/23
 * @Description: com.zjy.lucene
 */
public class IndexManager {

    private IndexWriter indexWriter;

    @Before
    public void init() throws Exception{
        // 创建一个IndexWriter对象，需要使用IKAnalyzer作为分析器
        indexWriter = new IndexWriter(FSDirectory.open(new File("E:\\javaframework\\lucene_project\\temp\\index").toPath()), new IndexWriterConfig(new IKAnalyzer()));
    }

    @Test
    public void addDocument() throws IOException {
        // 创建一个IndexWriter对象，需要使用IKAnalyzer作为分析器
        IndexWriter indexWriter = new IndexWriter(FSDirectory.open(new File("E:\\javaframework\\lucene_project\\temp\\index").toPath()), new IndexWriterConfig(new IKAnalyzer()));
        //  创建一个Document对象
        Document document = new Document();
        // 向document对象添加域
        document.add(new TextField("name", "新添加的文件", Field.Store.YES));
        document.add(new TextField("content", "新添加的文件内容", Field.Store.NO));
        document.add(new StoredField("path", "c:/temp/htllo"));
        // 吧文章写入索引库
        indexWriter.addDocument(document);
        // 关闭索引库
        indexWriter.close();
    }

    @Test
    public void deleteAllDocument() throws IOException {
        indexWriter.deleteAll();
        indexWriter.close();
    }

    @Test
    public void deleteDocumentByQuery() throws IOException {
        indexWriter.deleteDocuments(new Term("name", "apache"));
        indexWriter.close();
    }

    @Test
    public void updateDocument() throws IOException {
        //  创建一个新的文档对象
        Document document = new Document();
        document.add(new TextField("name", "更新之后得到文档", Field.Store.YES));
        document.add(new TextField("name2", "更新之后得到文档2", Field.Store.YES));
        document.add(new TextField("name3", "更新之后得到文档3", Field.Store.YES));
        //  更新操作
        indexWriter.updateDocument(new Term("name", "spring"), document);
        //  关闭索引库
        indexWriter.close();
    }


}
