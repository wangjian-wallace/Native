# Native
工具

MVP+retrofit+RxJava demo

     private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://app.akmob.cn/api/")
            .client(defaultOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
     


[**爬虫测试**](https://github.com/wjWite/Native/blob/master/tools/src/main/java/com/wallace/tools/ui/example/ExamplePresenter.java)

获取数据

     Document doc = Jsoup.connect("http://").get();

## License

    Copyright 2017 WjWite Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
