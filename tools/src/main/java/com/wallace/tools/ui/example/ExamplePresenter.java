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

    @Override
    public void getData() {

        Disposable disposable = Observable
                .create(new ObservableOnSubscribe<ArrayList<HashMap<String,String>>>() {
                    @Override
                    public void subscribe(ObservableEmitter<ArrayList<HashMap<String,String>>> e) throws Exception {
//                        e.onNext(getElements("8hr","1"));
//                        e.onNext(getElements("8hr","2"));
//                        e.onNext(getElements("imgrank","1"));
//                        e.onNext(getElements("imgrank","2"));
                        e.onNext(getElements());
//                        e.onNext(getElements());
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


    /**
     * 糗事百科爬虫测试数据
     * @param tab 分类
     * @param num 分页
     * @return 展示数据
     * @throws IOException IOException
     */
    private ArrayList<HashMap<String,String>> getElements(String tab,String num) throws IOException{
        Document doc = Jsoup.connect("http://www.qiushibaike.com/"+ tab +"/page/" + num).get();
        Elements singerListDiv = doc.getElementsByClass("article block untagged mb15 typs_hot");

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

    private ArrayList<HashMap<String,String>> getElements() throws IOException{
        ArrayList<HashMap<String,String>> list = new ArrayList<>();
        Document doc = Jsoup.connect("http://www.budejie.com/").get();
        Elements userListDiv = doc.getElementsByClass("j-list-user");
        Elements contentListDiv = doc.getElementsByClass("j-r-list-c");

        int i = 0;
        for (Element link : userListDiv){
            HashMap<String,String> map = new HashMap<>();
            Elements author = link.getElementsByClass("u-img");
            for (Element el : author){
                Elements elements = el.getElementsByTag("img");
                Elements elements2 = el.getElementsByTag("span");
                map.put("time",elements2.text());
                for(Element element : elements) {
                    String imgSrc = element.attr("data-original"); //获取src属性的值
                    String name = element.attr("alt"); //获取src属性的值
                    map.put("author",imgSrc);
                    map.put("name",name);
                }
            }
            if (contentListDiv.get(i) != null){
                Elements content = contentListDiv.get(i).getElementsByClass("j-r-list-c-desc");
                Elements image = contentListDiv.get(i).getElementsByClass("j-r-list-c-img");
                Elements video = contentListDiv.get(i).getElementsByClass("j-video-c");
                map.put("text",content.text());
                if (!image.isEmpty()){
                    for (Element el : image){
                        Elements elements=el.getElementsByTag("img");
                        for(Element element : elements) {
                            String imgSrc = element.attr("data-original"); //获取src属性的值
                            map.put("image",imgSrc);
                        }
                    }

                }else {
                    map.put("image","false");
                }
                if (!video.isEmpty()){
                    for (Element el : video){
                        Elements elements=el.getElementsByClass("j-video");
                        map.put("videoImage",elements.attr("data-poster"));
                        map.put("video",elements.attr("data-mp4"));
                    }
                }else {
                    map.put("video","false");
                }
            }

            list.add(map);
            i++;
        }
        return list;
    }
}
