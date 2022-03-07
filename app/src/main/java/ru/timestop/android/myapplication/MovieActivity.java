package ru.timestop.android.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.timestop.android.adaptors.MovieAdaptor;
import ru.timestop.android.model.Movie;

public class MovieActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdaptor movieAdaptor;
    private List<Movie> movies;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.book);

        recyclerView = findViewById(R.id.book_root);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movies = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //movieAdaptor = new MovieAdaptor(MovieActivity.this, new ArrayList<>());
        //recyclerView.setAdapter(movieAdaptor);
        getMovies();
    }

    private void getMovies() {
        String url = "http://www.omdbapi.com/?apikey=3b8a83ef&s=superman";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET
                , url
                , null
                , response -> {
            try {
                JSONArray array = response.getJSONArray("Search");
                List<Movie> newMovies = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    Movie movie = Movie.builder()
                            .imdbid(obj.getString("imdbID"))
                            .posterUrl(obj.getString("Poster"))
                            .title(obj.getString("Title"))
                            .year(obj.getString("Year"))
                            .build();
                    newMovies.add(movie);
                }
                movieAdaptor = new MovieAdaptor(MovieActivity.this, newMovies);
                recyclerView.setAdapter(movieAdaptor);
            } catch (JSONException e) {
                Log.d("movies", "error", e);

                movieAdaptor = new MovieAdaptor(MovieActivity.this, new ArrayList<>());
                recyclerView.setAdapter(movieAdaptor);
            }
        }
                , error -> Log.d("movies", "error", error));
        requestQueue.add(request);
    }
}
