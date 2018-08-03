package com.example.pc.testeverything.model;

/**
 * Created by PC on 2018/7/24.
 */

public class Keys {
    boolean isEnableFunctionKey;
    KeyCode keyCode;

    public boolean isEnableFunctionKey() {
        return isEnableFunctionKey;
    }

    public void setEnableFunctionKey(boolean enableFunctionKey) {
        isEnableFunctionKey = enableFunctionKey;
    }

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(KeyCode keyCode) {
        this.keyCode = keyCode;
    }
}
