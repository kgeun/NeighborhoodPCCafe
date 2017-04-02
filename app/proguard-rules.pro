# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Volumes/KGLEE/Android/SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-libraryjars ../WooriPC1/app/libs/naver-oauth-login-4.1.4.1-with-sources.jar
-keep public class com.nhn.android.naverlogin.** {
       public protected *;
}