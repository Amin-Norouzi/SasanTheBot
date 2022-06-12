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
