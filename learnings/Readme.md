# Learnings

This doc contains the stuff I've learnt while making this wallpaper app

# Issues

- Progaurd Gson
    - **Issue**: Release build doesn't work
    - **Detailed**: Gson needs exact names of the variables that it'll work with but `minifyenabled true` messes with variable names
    - **Solution**:

        [So post](https://stackoverflow.com/a/37507155/8608146)

        Modify Progaurd rules.pro file
        ```java
        -keepattributes Signature

        -keep class com.rithvij.scrolltest.models.** { *; }
        ```
    - **Resources**:
        - [So post](https://stackoverflow.com/a/23826357/8608146)
        - [So post](https://stackoverflow.com/a/37507155/8608146)
- Logging in release mode
    - **Issue**: *title*
    - **Solution**:

        Write to a log file

    - **Resources**:
        - [So post](https://stackoverflow.com/a/6209739/8608146) to logfile
