### Android Mvp Demo

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
compile 'com.rengwuxian.materialedittext:library:2.1.4'   //EditTextView
```

### 第三方依赖-业务层

```
compile 'com.jakewharton:butterknife:8.4.0'
annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'
```
* butterknife集成及使用请参考网络解疑

### 使用说明

#####  使用MVP搭建自己的页面

1. 创建Contract

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

    abstract class Presenter extends AbstractPresenter<DemoContract.View> {

        /**
         * 业务功能
         */
        public abstract void doSomething();

    }

}
```

2. 创建界面继承BaseActivity,并实现Contract中的View接口
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

3. 创建Presenter继承Contract中的Presenter

```
public class DemoPresenter extends DemoContract.Presenter {
    private Context mContext;

    public DemoPresenter(Context context) {
        mContext = context;
    }

    @Override
    public void attachView(@NonNull DemoContract.View view) {
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

4. 实现接口
* 根据自己定义的业务接口进行重写实现

##### 搭建自己的API接口请求

1. 在Application中对网络层初始化

```
//可根据自己项目业务需求 更改配置文件内容
HttpRequestConfig config = new HttpRequestConfig.Builder(this)
        .setAppId("")
        .setPlatPublicKey("") //网络请求结果验签公钥
        .setVersion("")
        .setBaseUrl("")       //通用域名，以http或https开头
        .setDebug(false)      //debug--是否打印网络请求日志
        .setBundleId("")
        .build();
HttpReqManager.init(config);
```

2. 创建Service

```
public interface DemoService {

    //若使用初始化时的baseUrl作为请求域名 此处只有配置接口地址
    @POST("")
    Observable<BaseResponse> postSomething(@HeaderMap Map<String, String> headers, @Body String jsonBody);

    //若需要自定义请求域名，可将接口完整地址作为url参数传入
    Observable<BaseResponse> postSomething(@Url String url, @HeaderMap Map<String, String> headers, @Body String jsonBody);
}
```

3. 创建实现类

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

4. 在Presenter中使用

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

##### 接口返回错误处理

1. 通用业务处理

```
//在com.hw.mvpbase.basenetwork.consume.BaseApiErrorConsumer中
/**
 * 处理ApiException，这里仅仅处理一些常规、全局的异常
 *
 * @param e
 * @return 异常是否被处理掉了，具体业务相关的异常由子类自己去处理
 */
private boolean handleApiException(ApiException e) {
    //TODO 处理通用错误
    return false;
}
```

2. 具体业务错误

```
 DemoApi.postSomething("something")
.subscribe(new BaseApiResultConsumer<BaseResponse>() {
    @Override
    protected void handleResult(BaseResponse result) throws Exception {
        super.handleResult(result);
    }
}, new BaseApiErrorConsumer<Throwable>(mContext, getView()) {
    @Override
    public void handleError(Throwable t, boolean handled) throws Exception {
        super.handleError(t, handled);
        //此处对自己业务的错误进行处理
      if (!handled) {
            if (t instanceof ApiException) {
                handleException((ApiException) t);
            } else {
                getView().toastMessage(t.getMessage());
            }
        }
    }
});
```

##### 注意事项

1. 网络请求结果拦截器中，对请求进行了验签，若验签不通过将直接作为异常抛出，若项目没有加签验签操作，或加签验签方式与本Demo不一致，请自行修改。

```
//com.hw.mvpbase.basenetwork.interceptor.ResponseInterceptor

    ...
    ...
    ...

    @Override
    public Response intercept(Chain chain) throws IOException {
        ...
        ...
        ...

        boolean isValid = HttpRespUtil.checkResponse(mConfig.getAppId(), timestamp, nonce, sequence, signature, bodyStr, mConfig.getPlatPublicKey());
        if (!isValid) {
            throw new SignInvalidException(mConfig.getContext().getString(R.string.bwt_error_msg_server_error));
        } else {
            return response;
        }
    }
```

### Authors

* hewei