package com.example.cookit.Model;

public interface WithFailMessageListener {
    public void onSuccess();
    public void onFail(String failMessage);
}
