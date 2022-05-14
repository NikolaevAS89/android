package ru.timestop.android.movies;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    String title;
    String posterUrl;
    String year;
    String imdbid;
}
