package ru.geekbrains;

import android.text.Editable;
import android.text.TextWatcher;

import io.reactivex.ObservableEmitter;

public class TextWatcherForSecondTask implements TextWatcher {

    private ObservableEmitter<String> emitter;

    public TextWatcherForSecondTask(ObservableEmitter<String> emitter){
        this.emitter = emitter;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        emitter.onNext(s.toString());
    }
}
