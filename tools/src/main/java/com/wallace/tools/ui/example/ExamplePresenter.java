package com.wallace.tools.ui.example;

import android.support.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Package com.wallace.tools.ui.example
 * Created by Wallace.
 * on 2017/4/14.
 */

class ExamplePresenter implements ExampleContract.Presenter {
    private ExampleContract.ExampleView view;

    @NonNull
    private CompositeDisposable compositeDisposable;

    ExamplePresenter(ExampleContract.ExampleView view) {
        this.view = view;
        view.setPresenter(this);
        compositeDisposable = new CompositeDisposable();
    }

//    private static Retrofit retrofit = new Retrofit.Builder()
//            .baseUrl("http://app.akmob.cn/api/")
//            .client(defaultOkHttpClient())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .build();
//
//    private static OkHttpClient defaultOkHttpClient() {
//        return new OkHttpClient.Builder()
//                .connectTimeout(3, TimeUnit.SECONDS)
//                .writeTimeout(3, TimeUnit.SECONDS)
//                .readTimeout(3, TimeUnit.SECONDS)
//                .build();
//    }


    @Override
    public void getData() {

        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<ArrayList<HashMap<String,String>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<ArrayList<HashMap<String,String>>> e) throws Exception {
                        e.onNext(getElements("8hr","1"));
                        e.onNext(getElements("8hr","2"));
                        e.onNext(getElements("imgrank","1"));
                        e.onNext(getElements("imgrank","2"));
                        e.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ArrayList<HashMap<String,String>>>() {

                    @Override
                    public void onNext(ArrayList<HashMap<String,String>> o) {
                        view.toNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showError();
                    }

                    @Override
                    public void onComplete() {
                        view.toComplete();
                    }
                    @Override
                    protected void onStart() {
                        super.onStart();
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void toStart() {

    }

    @Override
    public void toStop() {
        compositeDisposable.clear();
    }

    private ArrayList<HashMap<String,String>> getElements(String tab,String num) throws IOException{
        Document doc = Jsoup.connect("http://www.qiushibaike.com/"+ tab +"/page/" + num).get();
        Elements singerListDiv = doc.getElementsByClass("article block untagged mb15");

        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        for (Element link: singerListDiv) {
            HashMap<String,String> map = new HashMap<>();
            Elements k = link.getElementsByClass("contentHerf");
            Elements span = k.get(0).getElementsByClass("content");

            Elements author = link.getElementsByTag("h2");

            Elements godNum = link.getElementsByClass("likenum");
            Elements godName = link.getElementsByClass("cmt-name");
            Elements godText = link.getElementsByClass("main-text");


            Elements comments = link.getElementsByClass("stats");
            Elements image = link.getElementsByClass("thumb");
            Elements author2 = link.getElementsByClass("author clearfix");

            for (Element el : author2){
                Elements elements = el.getElementsByTag("img");
                for(Element element : elements) {
                    String imgSrc = element.attr("src"); //获取src属性的值
                    map.put("author",imgSrc);
                }
            }

            map.put("text",span.html());
            map.put("name",author.text());
            if (!godName.isEmpty()){
                map.put("god",godName.text() + godText.text());
                map.put("godNum",godNum.text());
            }else {
                map.put("god","");
            }

            map.put("good",comments.text());
            if (!image.isEmpty()){
                for (Element el : image){
                    Elements elements=el.getElementsByTag("img");
                    for(Element element : elements) {
                        String imgSrc = element.attr("src"); //获取src属性的值
                        map.put("image",imgSrc);
                    }
                }

            }else {
                map.put("image","false");
            }
            list.add(map);
        }
        return list;
    }
}