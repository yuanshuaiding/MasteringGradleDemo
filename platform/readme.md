改模块用于全局依赖的版本管理，其他模块依赖此模块，指定依赖项时无需指明版本号，会自动应用此模块里的约束

其他模块通过platform引入此项目，用法如下：

```kotlin
//1.依赖版本管理模块
implementation platform(project(":platform"))
//2.不指定版本号，自动通过platform模块的约束来指定
implementation 'androidx.appcompat:appcompat'
implementation 'com.google.android.material:material'
```

