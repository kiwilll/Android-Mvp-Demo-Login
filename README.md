### android-mvp-login demo

### 功能介绍

* 验证码登录；
* 密码登录；
* 用户注册；
* 找回密码；
* 阅读用户协议界面；

### 安装

在工程根目录里的build.gradle文件里添加如下maven地址：

```
allprojects {
    repositories {		
        maven { url "http://60.190.227.164:8088/nexus/repository/maven-releases/" }
    }
}
```

在gradle.properties里添加配置：
```
android.enableAapt2=false
```

在项目模块的build.gradle文件里添加如下依赖：

远程依赖库：
```
compile 'com.bwton.android:bwtlogin:1.0.0'
```

本地依赖库（发布本地maven库，自己管理）：
```
compile 'com.bwton.android:bwtloginlocal:1.0.0'
```

### 第三方依赖库

```
compile 'com.jakewharton:butterknife:8.4.0'
annotationProcessor 'com.jakewharton:butterknife-compiler:8.4.0'
compile 'com.bwton.android:bwtbasebiz:1.0.0'
```

### 使用说明

##### 1. 支持的路由功能

页面 | 路由url | 备注 |
-- | -- | --
验证码登录 | msx://m.bwton.com/login/code |  |
密码登录 | msx://m.bwton.com/login/password |  |
注册 | msx://m.bwton.com/login/register |  |
忘记密码 | msx://m.bwton.com/login/findpwd |  |

路由文件配置如下：
```
[
    {
      "url": "msx://m.bwton.com/login/code?jumptype=*",
      "iclass": "BWTLoginCodeViewController",
      "aclass": "com.bwton.metro.usermanager.business.login.views.QuickLoginActivity"
    },
    {
      "url": "msx://m.bwton.com/login/password",
      "iclass": "BWTLoginPasswordViewController",
      "aclass": "com.bwton.metro.usermanager.business.login.views.PwdLoginActivity"
    },
    {
      "url": "msx://m.bwton.com/login/register",
      "iclass": "BWTLoginRegisterViewController",
      "aclass": "com.bwton.metro.usermanager.business.register.views.RegisterActivity"
    },
    {
      "url": "msx://m.bwton.com/login/findpwd",
      "iclass": "BWTFindPasswordViewController",
      "aclass": "com.bwton.metro.usermanager.business.resetpwd.views.ResetPwdActivity"
    }
]
```

##### 2. 特殊事件通知
* 登录成功后，会通过EventBus发出通知事件：LoginSuccEvent；
* 登录成功后，会自动去同步一次用户信息，用户信息同步成功后会发出通知事件：UserInfoRefreshEvent；

### 集成说明
集成本业务模块，必须保证以下功能或业务模块有正确初始化，否则会运行异常：

1. bwtbase
2. bwtbasebiz

其他模块如何初始化，请具体参考该模块的接入说明文档。

### Authors

* hewei