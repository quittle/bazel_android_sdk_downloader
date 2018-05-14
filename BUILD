android_binary(
    name = "test_binary",
    manifest = "src/main/AndroidManifest.xml",
    custom_package = "com.quittle.example",
    srcs = glob(["src/main/java/**/*.java"]),
    resource_files = glob(["src/main/res/**/*"]),
    deps = [
        "@com_google_guava_guava//jar",
    ],
)