package com.example.rxjavatemelleri2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    Button btnObservable;
    Button btnSingle;
    Button btnCompletable;
    Button btnMaybe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
        Events();
    }


    private void Init() {
        try {
            btnObservable = findViewById(R.id.btnObservable);
            btnSingle = findViewById(R.id.btnSingle);
            btnCompletable = findViewById(R.id.btnCompletable);
            btnMaybe = findViewById(R.id.btnMaybe);

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void Events() {
        try {

            btnObservable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   /* Observable -> Birden fazla kez dinleyici tetiklenecek ise kullanılır.
                    Bu yüzden onNext , onComplete adında iki metodu vardır. Her yayınlamada
                    onNext tetiklenirken tüm akış bittiğinde ise onComplete tetiklenir.*/

                    Observable
                            .fromArray("Burak", "Arslan", "BA")
                            .subscribe(new Observer() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    Toast.makeText(getApplicationContext(), "Disposable", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNext(Object o) {
                                    //Öncelik olarak on next tetiklenir
                                    //Burada kaydetme servis çağırma vb gibi işlemleri yaptığımızda UI taradında herhangi  bir zorluk çıkarmadan ön tarafta kullanıcı işlemlerini yapabilir.
                                    //Sırasıyla
                                    //Burak
                                    //Arslan
                                    //BA
                                    //Şeklinde toast verip onComplate'da düşecektir.
                                    Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete() {
                                    Toast.makeText(getApplicationContext(), "OnComplate", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });


            btnSingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Single
                                .create(new SingleOnSubscribe<Object>() {
                                    @Override
                                    public void subscribe(SingleEmitter<Object> emitter) throws Exception {

                                        //Dinleyici sadece bir kez tetiklenecek ise
                                        //Single methodunu kullanabiliriz.
                                        emitter.onSuccess("BA");

                                    }
                                })
                                .subscribe(new SingleObserver<Object>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(Object o) {

                                        Toast.makeText(getApplicationContext(), o.toString(), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            btnCompletable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                    /*    observable dan herhangi bir değer beklenmez.
                        Arkaplanda bir işlem gerçekleştirilir ama sadece
                        bitip bitmediğiyle ilgilenilir.*/

                        Completable
                                .create(new CompletableOnSubscribe() {
                                    @Override
                                    public void subscribe(CompletableEmitter emitter) throws Exception {
                                        emitter.onComplete();
                                    }
                                })
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        Toast.makeText(getApplicationContext(), "onSubscribe", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onComplete() {
                                        Toast.makeText(getApplicationContext(), "onComplete", Toast.LENGTH_SHORT).show();

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                        btnMaybe.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {


                                    /*
                                     * Maybe -> Single ile Completable birleşimi gibidir
                                     * Hem onSuccess hem de onComplete methodları mevcut.
                                     * Yani hiçbir sonuç dönmeyedebilir , tek bir sonuç dönedebilir.
                                     * */

                                    Maybe
                                            .create(new MaybeOnSubscribe() {
                                                @Override
                                                public void subscribe(MaybeEmitter e) throws Exception {
                                                    e.onSuccess("BA");
                                                }
                                            })
                                            .subscribe(new MaybeObserver() {
                                                @Override
                                                public void onSubscribe(Disposable d) {
                                                    Toast.makeText(getApplicationContext(), "Disposable", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onSuccess(Object o) {
                                                    Toast.makeText(getApplicationContext(), "onSuccess", Toast.LENGTH_SHORT).show();

                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }

                                                @Override
                                                public void onComplete() {
                                                    Toast.makeText(getApplicationContext(), "onComplete", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } catch (Exception ex) {
                                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}