# NibobNebob Android

<br/> <br/>

# 패키지 구성

<br/><br/>

```xml
├── manifests/
|   └── AndroidManifest.xml
|
└──java
    ├── app/
    |   ├── di/
    |   |   ├── RepositoryModule
    |   |   ├── ApiModule
    |   |   └── NetworkModule
    |   |
    |   └── App 
    | 
    ├── config/
    |   
    ├── data/
    |   └── model/   
    |   |   ├── response/
    |   |   └── request/
    |   |
    |   ├── local/
    |   ├── remote/  
    |   └── repository/   
    |
    └── presentation/
        ├── base/
        ├── ui/ 
        |   ├── ex) intro/
        |   |    ├── mapper/ 
        |   |    ├── model/
        |   |    ├── login/
        |   |    |    ├── LoginFragment
        |   |    |    └── LoginViewModel
        |   |    |
        |   |    ├── signup/ 
        |   |    |    ├── SignupFragment
        |   |    |    └── SignupViewModel
        |   |    |
        |   |    ├── IntroActivity
        |   |    └── IntroViewModel
        |   |
        |   ├── BindingAdapters 
        |   ├──   
        |
        └── util/  

```