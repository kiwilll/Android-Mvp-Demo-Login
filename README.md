### android-mvp demo

使用MVP架构，对网络层进行封装，提供基本的加签验签，加密解密。以登录模块业务功能为范本的一个Demo.

### 功能介绍

* MVP底层封装
* 使用retrofit和rxjava对网络层进行封装
* 提供基本的界面组件、动画样式等
* 提供基本的工具类

### 第三方依赖库-底层

```
compile 'io.reactivex.rxjava2:rxjava:2.0.1'
compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
compile 'com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0'
compile 'com.squareup.retrofit2:converter-gson:2.1.0'
compile 'com.rengwuxian.materialedittext:library:2.1.4'
```

### 第三方依赖-业务层

```
compile 'com.jakewharton:butterknife:8.4.0'
annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'
```
* butterknife集成及使用请参考网络解疑

### 使用说明

#####  使用MVP搭建自己的页面

1.创建Contract

```
public interface DemoContract {

    interface View extends BaseView {
        /**
         * 页面处理
         *
         * @param something 待显示的内容
         */
        void showSomething(String something);
    }

    abstract class Presenter extends AbstractPresenter<LoginContract.View> {

        /**
         * 业务功能
         */
        public abstract void doSomething();

    }

}
```

2.创建界面继承BaseActivity,并实现Contract中的View接口
* BaseActivity中已经实现了底层中的多个基础方法，有利于通用业务的集中处理

```
public class DemoActivity extends BaseActivity implements DemoContract.View {

    @Override
    public void showSomething(String something) {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public String getPageTitle() {
        return null;
    }
}
```

3.创建Presenter继承Contract中的Presenter

```
public class DemoPresenter extends DemoContract.Presenter {
    private Context mContext;

    public DemoPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void attachView(@NonNull LoginContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        mContext = null;
    }

    @Override
    public void doSomething() {

    }
}
```

4.实现接口
* 根据自己定义的业务接口进行重写实现

##### 搭建自己的API接口请求

1. 创建Service

```
public interface DemoService {

    @POST("")
    Observable<BaseResponse> postSomething(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    Observable<BaseResponse> postSomething(@Url String url, @HeaderMap Map<String, String> headers, @Body String jsonBody);
}
```

2. 创建实现类

```
public class DemoApi extends BaseApi {

    private static DemoService getDemoService() {
        return getService(DemoService.class);
    }

    public static Observable<BaseResponse> postSomething(String something) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("something", something);

        String bodyJson = strMapToJson(map);
        Map<String, String> headerMap = getHeaderMap(bodyJson, null);

        Observable<BaseResponse> observable = getDemoService().postSomething(headerMap, bodyJson)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }
}
```

3. 在Presenter中使用

```
@Override
public void doSomething() {
    removeDisposable(mDisposable);

    mDisposable = DemoApi.postSomething("something")
            .subscribe(new BaseApiResultConsumer<BaseResponse>() {
                @Override
                protected void handleResult(BaseResponse result) throws Exception {
                    super.handleResult(result);
                }
            }, new BaseApiErrorConsumer<Throwable>(mContext, getView()) {
                @Override
                public void handleError(Throwable t, boolean handled) throws Exception {
                    super.handleError(t, handled);
                }
            });

    addDisposable(mDisposable);
}
```

### Authors

* hewei