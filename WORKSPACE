workspace(name = "android_sdk_downloader")

load(":rules.bzl", "android_sdk_repository")

android_sdk_repository(
    name = "androidsdk",
    workspace_name = "android_sdk_downloader",
    api_level = 26,
    build_tools_version = "26.0.3",
)

maven_jar(
    name = "com_google_guava_guava",
    artifact = "com.google.guava:guava:20.0"
)