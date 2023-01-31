# Keep SafeParcelable value, needed for reflection. This is required to support backwards
# compatibility of some classes.
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keep class com.atomicrobot.carbon.navigation.CarbonScreens { *; }
-keep class com.atomicrobot.carbon.navigation.CarbonScreens$* { *; }
-keep class com.atomicrobot.carbon.ui.designsystem.DesignSystemScreens* { *; }
-keep class com.atomicrobot.carbon.ui.designsystem.DesignSystemScreens$* { *; }

# Kotlin Parcelize will throw Proguard warnings without this
-dontwarn kotlin.internal.annotations.AvoidUninitializedObjectCopyingCheck

# OkHttp3
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

-dontwarn retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
