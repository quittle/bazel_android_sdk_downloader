# Android SDK Downloader for Bazel [![Build Status](https://travis-ci.org/quittle/bazel_android_sdk_downloader.svg?branch=master)](https://travis-ci.org/quittle/bazel_android_sdk_downloader)

This package is to aid development of Android applications via Bazel. Without it, the [setup
instructions for building Android with Bazel](https://docs.bazel.build/versions/master/bazel-and-android.html)
involve manually downloading and installing the Android SDK. My understanding is that this is because
you cannot download the SDK without accepting the licenses, which may not be okay to automate. Why
not automate everything but the license agreement?

This project aims to serve as a drop-in replacement for native android repository rules.

# How to use

## `WORKSPACE` file changes

In your `WORKSPACE` file, replace the following block

```python
android_sdk_repository(
    name = "androidsdk",
    path = "../path/to/sdk",
    api_level = 26,
    build_tools_version = "26.0.3",
)
```

with this instead

```python
git_repository(
    name = "android_sdk_downloader",
    remote = "https://github.com/quittle/bazel_android_sdk_downloader",
    commit = "<HEAD-COMMIT>",
)

load("@android_sdk_downloader//:rules.bzl", "android_sdk_repository")

android_sdk_repository(
    name = "androidsdk",
    workspace_name = "<YOUR-WORKSPACE-NAME>",
    api_level = 26,
    build_tools_version = "26.0.3",
)
```

## One-time install step

Then run

```bash
bazel run @androidsdk//install --direct_run
```

This will attempt to download the SDK for the versions specified. If you change these values, clean
the workspace, or check it out fresh, you will need to re-run this command to download and install
the SDK again.

## Profit

You should now be able to run your build as normal.

```bash
bazel build ...
```

## Continuous Integration

In a Contuous Integration (CI) environment, you need a way of programmatically accepting the
licenses. The easiest way on unix-like systems is to pipe `yes` into the install command as part of
your install or build step.

```bash
yes | bazel run @androidsdk//install --direct_run
```

# Troubleshooting
My build fails with this cryptic error

```
no such package '@android_sdk_repository_androidsdk//': /home/ubuntu/workspace/my_project/bazel-bin/external/androidsdk/install/sdk.runfiles/my_project/_ (No such file or directory) and referenced by '//external:android/sdk'
```

or

```
ERROR: missing input file '@android_sdk_repository_androidsdk//:build-tools/26.0.1/lib/apksigner.jar'
ERROR: /home/ubuntu/workspace/my_project/BUILD:1:1: //:test_binary: missing input file '@android_sdk_repository_androidsdk//:build-tools/26.0.1/lib/apksigner.jar'
```

or

```
ERROR: /home/ubuntu/workspace/my_project/rules.bzl:88:5: no such package '@android_sdk_repository_androidsdk//': Android SDK api level 25 was requested but it is not installed in the Android SDK at /home/ubuntu/workspace/my_project/bazel-bin/external/androidsdk/install/sdk.runfiles/my_project/_. The api levels found were [26]. Please choose an available api level or install api level 25 from the Android SDK Manager. and referenced by '//external:android/sdk'
```

The first error message is for a clean workspace before you have run the `install` step. The second
occurs when you updated the `build_tools_version` but for got to run `install` again. The third
happens when you updated the api_level but forgot run `install` again. Basically, try re-running the
installation step before getting too concerned. There is no need to clean between changes.

# Notes
#### Why is the workspace_name a parameter of the rule?
It is not too intrusive for now but would like to find a way to remove this requirement.

#### Why do I need `--direct_run`
By default Bazel doesn't read from stdin when invoking a rule so you need to pass this flag in order
for it to listen for `y` to accept the licenses.
