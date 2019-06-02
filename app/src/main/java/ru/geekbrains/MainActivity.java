package ru.geekbrains;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    // Первое задание
    private TextView textView;
    private EditText editText;
    private Observable observable;
    private DisposableObserver<String> observer;

    // Второе задание
    private static final String TAG = MainActivity.class.getSimpleName();
    private EditText secondEditLabel;
    private EditText secondEditText;
    private AppCompatButton btnSubscribe;
    private AppCompatButton btnUnSubscribe;
    private DisposableObserver<String> observerSecondTask;
    private ObservableOnSubscribe<String> observableOnSubscribe;
    private TextWatcherForSecondTask textWatcher;
    private boolean isSubscribed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Первое задание
        initGuiForFirstTask();
        getObservable();
        getObserver();

        // Второе задание
        initGuiForSecondTask();

        observableOnSubscribe = new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                textWatcher = new TextWatcherForSecondTask(emitter);
                secondEditText.addTextChangedListener(textWatcher);
            }
        };
        subscribeBus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subscribe:
                BusManager.getInstance().getBus()
                        .send(new EventTypeFirst(EventTypeFirst.Subscribe));
                break;
            case R.id.unsubscribe:
                BusManager.getInstance().getBus()
                        .send(new EventTypeFirst(EventTypeFirst.Unsubscribe));
                break;
        }
    }

    @SuppressLint("CheckForResult")
    private void subscribeBus() {
        BusManager.getInstance().getBus().subscribe(new DisposableObserver<EventBase>() {
            @Override
            public void onNext(EventBase event) {
                switch (event.getEventsType()) {
                    case EventBase.Subscribe:
                        subscribe();
                        break;
                    case EventBase.Unsubscribe:
                        unsubscribe();
                        break;
                    default:
                        break;
                    case EventBase.Hasfocus:
                        break;
                    case EventBase.Lostfocus:
                        break;
                }
                enableButton(isSubscribed);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        BusManager.getInstance().getBus().subscribe(new DisposableObserver<EventBase>() {
            @Override
            public void onNext(EventBase event) {
                switch (event.getEventsType()) {
                    case EventBase.Hasfocus:
                        Debug.logE(TAG, "Has focus");
                        break;
                    case EventBase.Lostfocus:
                        Debug.logE(TAG, "Lost focus");
                        break;
                    default:
                        Debug.logE(TAG, "DisposableObserver<EventType2>");
                        break;
                    case EventBase.Subscribe:
                        break;
                    case EventBase.Unsubscribe:
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    public void initGuiForSecondTask() {
        secondEditLabel = findViewById(R.id.label);
        secondEditLabel.setOnFocusChangeListener(this);
        secondEditText = findViewById(R.id.text);
        secondEditText.setOnFocusChangeListener(this);

        btnSubscribe = findViewById(R.id.subscribe);
        btnSubscribe.setOnClickListener(this);
        btnUnSubscribe = findViewById(R.id.unsubscribe);
        btnUnSubscribe.setOnClickListener(this);
    }

    private void subscribe() {
        isSubscribed = true;
        Observable observable = Observable.create(observableOnSubscribe)
                .observeOn(AndroidSchedulers.mainThread());
        observerSecondTask = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                secondEditLabel.setText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observerSecondTask);
    }

    private void unsubscribe() {
        isSubscribed = false;
        observerSecondTask.dispose();

        secondEditText.removeTextChangedListener(textWatcher);
    }

    private void enableButton(boolean value) {
        btnSubscribe.setEnabled(!value);
        btnUnSubscribe.setEnabled(value);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        EventTypeSecond event = new EventTypeSecond(hasFocus ? EventTypeSecond.Hasfocus : EventTypeSecond.Lostfocus);
        BusManager.getInstance().getBus()
                .send(event);
    }


    //////////////// Первое Задание
    private void getObserver() {
        observer = new DisposableObserver<String>() {
            @Override
            public void onNext(String s) {
                textView.setText(s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    private void getObservable() {
        observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> emitter) {
                editText.addTextChangedListener(new TextWatcher() {
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
                });
            }
        });
    }

    public void initGuiForFirstTask() {
        textView = findViewById(R.id.textView);
        editText = findViewById(R.id.edit_text);
    }
    //////////////// Первое Задание КОНЕЦ

    @Override
    protected void onStart() {
        super.onStart();
        observable.subscribe(observer);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusManager.getInstance().getBus().destroy();
        observer.dispose();
    }
}
