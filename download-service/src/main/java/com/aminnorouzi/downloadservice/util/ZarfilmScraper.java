package com.aminnorouzi.downloadservice.util;

import com.aminnorouzi.downloadservice.model.*;
import lombok.ToString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ToString
public class ZarfilmScraper {

    private static final String MOVIE_DOWNLOAD_TYPE = "نسخه زیرنویس فارسی چسبیده";

    private final String baseUrl;
    private final String searchQuery;

    public ZarfilmScraper(String baseUrl, String searchQuery) {
        this.baseUrl = baseUrl;
        this.searchQuery = searchQuery;
    }

    public String search(String imdbId) {
        Elements found = getDocument(baseUrl + searchQuery + imdbId)
                .select(".posts_hoder_archive .item_body_widget");

        if (!found.isEmpty()) return found.first()
                .select(".title_item_holder a")
                .attr("href");

        return null; // here should throw an exception
    }

    public Download downloadSeries(String seriesUrl) {
        Document document = getDocument(seriesUrl);
        List<Season> seasons = new ArrayList<>();

        Integer number = 0;

        for (Element seasonRow : document.select(".body_series_dlbox .item_season")) {
            List<String> qualities = new ArrayList<>();
            List<Link> links = new ArrayList<>();

            Integer episodes = Integer.valueOf(
                    seasonRow.select(".head_quality_season .item_season_quality_right.part_count")
                            .select(".value_text_head_right_qulity")
                            .first()
                            .text()
                            .replace("تعداد قسمت :", "")
                            .trim());

            for (Element seasonQualityRow : seasonRow.select(".inner_body_item_season .item_quality_season")) {
                if (!seasonQualityRow.select(".head_quality_season .right_side")
                        .select(".label_subtitle").text().equals("زیرنویس")
                        || seasonQualityRow.select(".item_parts_holder .item_part").isEmpty()) {
                    continue;
                }

                String quality = seasonQualityRow.select(".head_quality_season .item_season_quality_right")
                        .select(".value_text_head_right_qulity")
                        .first()
                        .text()
                        .replace("کیفیت :", "")
                        .trim();

                String size = seasonQualityRow.select(".head_quality_season .item_season_quality_right.size_average")
                        .select(".value_text_head_right_qulity")
                        .text()
                        .replace("گیگابایت", "GB")
                        .replace("مگابایت", "MB")
                        .trim();

                for (Element downloadRow : seasonQualityRow.select(".item_parts_holder .item_part")) {
                    String url = downloadRow.select("a").attr("href");
                    links.add(Link.builder()
                            .quality(quality)
                            .size(size)
                            .url(url)
                            .build());
                }

                qualities.add(quality);
            }

            number++;

            if (qualities.isEmpty()) {
                continue;
            }

            seasons.add(Season.builder()
                    .number(number)
                    .episodes(episodes)
                    .links(links)
                    .qualities(qualities)
                    .build());
        }

        Series series = Series.builder()
                .seasons(seasons)
                .build();

        return Download.builder()
                .provider(getProvider())
                .type(Type.SERIES)
                .series(series)
                .build();
    }

    public Download downloadMovie(String movieUrl) {
        Document document = getDocument(movieUrl);
        Elements typeRow = document.select(".dllink_box.yellow");

        List<Link> links = new ArrayList<>();
        for (Element downloadRow : typeRow.select(".body_dllink_box .item_dllink_box.hasaccess")) {
            if (downloadRow.select(".right_side a").attr("href").equals("")) {
                continue;
            }

            String quality = downloadRow.select(".left_side .qualites_text_decode")
                    .select(".quality_text")
                    .text();

            String size = downloadRow.select(".left_side .size_item")
                    .first()
                    .text()
                    .replace("گیگابایت", "GB")
                    .replace("مگابایت", "MB")
                    .trim();

            String url = downloadRow.select(".right_side a").attr("href");

            links.add(Link.builder()
                    .quality(quality)
                    .size(size)
                    .url(url)
                    .build());

        }

        return Download.builder()
                .title("Moonshot 2022") // The title field must be omitted from the model.
                .url(movieUrl)          // Also, I think this method should return a list of links not a download object.
                .type(Type.MOVIE)
                .provider(getProvider())
                .movie(Movie.builder()
                        .links(links)
                        .build())
                .build();
    }

    private Provider getProvider() {
        return Provider.builder()
                .title("Zarfilm")
                .address(baseUrl)
                .isFiltered(false)
                .build();
    }

    private Document getDocument(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document;
    }
}
