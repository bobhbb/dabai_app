# ProGuard rules for Dabai
-keepattributes *Annotation*, InnerClasses
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class * { @kotlinx.serialization.Serializable *; }
-keep class com.dabai.app.data.local.entity.** { *; }
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class com.dabai.app.data.model.** { *; }
-keep class com.dabai.app.service.ai.** { *; }
