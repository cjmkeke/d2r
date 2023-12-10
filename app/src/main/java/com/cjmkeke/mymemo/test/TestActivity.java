package com.cjmkeke.mymemo.test;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cjmkeke.mymemo.R;
import com.nguyencse.URLEmbeddedData;
import com.nguyencse.URLEmbeddedView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        String url = "https://hmk1022.tistory.com/entry/jsoup-%EB%84%A4%EC%9D%B4%EB%B2%84-%EB%B8%94%EB%A1%9C%EA%B7%B8-%ED%81%AC%EB%A1%A4%EB%A7%81iframe";
//        String url = "https://blog.naver.com/PostList.naver?blogId=cjmkeke&widgetTypeCall=true&parentCategoryNo=49&directAccess=true";
        String url = "https://www.google.com";


        URLEmbeddedView urlEmbeddedView = new URLEmbeddedView(getApplicationContext());
        urlEmbeddedView.setURL(url, new URLEmbeddedView.OnLoadURLListener() {
            @Override
            public void onLoadURLCompleted(URLEmbeddedData data) {

                try{
                    Log.v("getDescription", data.getThumbnailURL());
                    Log.v("getTitle", data.getTitle());
                    Log.v("getHost", data.getHost());
                }catch (Exception e){
                    Log.v("",e.toString());
                }


            }
        });

//        Observable.fromCallable(() -> {
//                    // 네트워크 호출 및 HTML 파싱을 수행
//                    Document doc = Jsoup.connect(url).get();
//                    Elements metaTags = doc.select("meta[property^=og:]");
//
//                    for (Element metaTag : metaTags) {
//                        String property = metaTag.attr("property");
//                        String content = metaTag.attr("content");
//                        String title1 = metaTag.baseUri();
//                        String title2 = metaTag.className();
//                        String title3 = metaTag.cssSelector();
//                        String title4 = metaTag.data();
//                        String title5 = metaTag.html();
//                        String title6 = metaTag.tagName();
//                        String title7 = metaTag.cssSelector();
//                        String title8 = metaTag.text();
//                        String title9 = metaTag.wholeText();
//
//                        Log.v("property", property);
//                        Log.v("content", content);
//                        Log.v("title1", title1);
//                        Log.v("title2", title2);
//                        Log.v("title3", title3);
//                        Log.v("title4", title4);
//                        Log.v("title5", title5);
//                        Log.v("title6", title6);
//                        Log.v("title7", title7);
//                        Log.v("title8", title8);
//                        Log.v("title9", title9);
//                    }
//                    return null;
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.single()) // 또는 다른 스케줄러 선택
//                .subscribe(new Observer<Object>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        // 구독 시작 시 호출됨
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//                        // 작업이 완료되면 호출됨
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        // 에러 발생 시 호출됨
//                        Log.e("RxJava", "Error: " + e.toString());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        // 작업이 성공적으로 완료되면 호출됨
//                    }
//                });
    }
}
