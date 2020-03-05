package com.atfotiad.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.atfotiad.retrofit.adapter.MoviesAdapter;
import com.atfotiad.retrofit.model.Movie;
import com.atfotiad.retrofit.model.MoviesResponse;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final static String API_KEY = "754a1193";
    private String SEARCH= "superman";
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    Disposable disposable;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = findViewById(R.id.edit_query);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);




        final ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

                disposable = (Disposable) RxTextView.textChanges(editText)
                        .filter(new Predicate<CharSequence>() {
                            @Override
                            public boolean test(CharSequence charSequence) throws Exception {
                                return charSequence.length() >2;
                            }
                        })
                .debounce(500, TimeUnit.MILLISECONDS)
                .flatMap(new Function<CharSequence, Observable<MoviesResponse>>() {
                    @Override
                    public Observable<MoviesResponse> apply(CharSequence charSequence) throws Exception {
                        return apiService.getSampleMovies(API_KEY,charSequence.toString());
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<MoviesResponse>() {
                            @Override
                            public void accept(MoviesResponse moviesResponse) throws Exception {
                                displayData(moviesResponse);
                            }
                        });


        //compositeDisposable.add(apiService.getSampleMovies(API_KEY,SEARCH)
         //       .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
           //     .subscribe(new Consumer<MoviesResponse>() {
             //       @Override
               //     public void accept(MoviesResponse moviesResponse) throws Exception {
                 //       displayData(moviesResponse);
                   // }
                //}));

        /*Call<MoviesResponse> call = apiService.getSampleMovies(API_KEY,SEARCH);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                List<Movie> movies = response.body().getResults();
                Log.d(TAG, "Status Code: " + statusCode);
                Log.d(TAG, "Number of movies received: " + movies.size());
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.recycler_row, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<MoviesResponse>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });*/
    }
    private void displayData(MoviesResponse movies){
        MoviesAdapter adapter = new MoviesAdapter(movies.getResults(),R.layout.recycler_row,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        //compositeDisposable.clear();
        disposable.dispose();
        super.onStop();
    }
}
