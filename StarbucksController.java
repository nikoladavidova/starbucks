package com.example.demo.starbucks;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import java.util.stream.Stream;

@RestController
public class StarbucksController {

        private static List<Starbucks> loadAllFromFile() throws IOException {
            try (Stream<String> lines = Files.lines(Path.of("starbucks_shuffled.csv"))) {
                return lines
                        .skip(1)
                        .map(line -> {
                            String[] parts = line.split(";del;");


                            int rating = "N/A".equals(parts[4]) ? 0 : Integer.parseInt(parts[4]);

                            return new Starbucks(
                                    Integer.parseInt(parts[0]),
                                    parts[1],
                                    parts[2],
                                    parts[3],
                                    rating,
                                    parts[5]

                            );
                        })
                        .toList();
            }
        }

    @GetMapping("/starbucks-reviews")
    public Stream<Starbucks> getAll(
            @RequestParam(required = false) Integer minRating,
            @RequestParam(required = false) Integer maxRating,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) Integer pageNumber
    ) throws IOException {
        long itemsToSkip = (pageNumber != null && pageSize != null) ? (pageNumber - 1) * pageSize : 0;

        return loadAllFromFile()
                .stream()
                .sorted(Comparator.comparingInt(Starbucks::getId))
                .filter(starbucks -> location==null||starbucks.getLocation().toLowerCase().contains(location.toLowerCase()))
                .filter(starbucks -> (minRating == null || (starbucks.getRating() != 0 && (starbucks.getRating() >= minRating && starbucks.getRating() > 0))))

                .filter(starbucks -> (maxRating == null || (starbucks.getRating() != 0 && (starbucks.getRating() <= maxRating && starbucks.getRating() > 0))))
                .skip(itemsToSkip)
                .limit(pageSize != null ? pageSize : Long.MAX_VALUE);


    }
    @GetMapping("/starbucks-reviews/longest")
    public Optional <Starbucks> getStarbucksWithLongestReview() throws IOException {
        return loadAllFromFile()
                .stream()
                 .max(Comparator.comparingInt(starbucks -> starbucks.getReview().length()));

    }




    @GetMapping("/starbucks-reviews/worst-by/{name}")
    public Optional<Starbucks> getWorstReviewByPerson(@PathVariable(required = false) String name) throws IOException {
    

        return loadAllFromFile()
                .stream()
                .filter(starbucks -> name == null || starbucks.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(starbucks -> starbucks.getRating() > 0)
                   .min(Comparator.comparing(Starbucks::getRating)

                        .thenComparing(starbucks -> -starbucks.getReview().length()));
        }





}


