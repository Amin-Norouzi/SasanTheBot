package com.aminnorouzi.downloadservice.util;

import com.aminnorouzi.downloadservice.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvaMovieScraper {

    private static final String MOVIE_DOWNLOAD_TYPE = "نسخه زیرنویس فارسی چسبیده";

    private final String baseUrl;
    private final String searchQuery;

    public AvaMovieScraper(String baseUrl, String searchQuery) {
        this.baseUrl = baseUrl;
        this.searchQuery = searchQuery;
    }

    public String search(String imdbId) {
        return getDocument(baseUrl + searchQuery + imdbId)
                .select(".col.col-70.home_loop_post article.sitePost")
                .first()
                .select(".more a")
                .attr("href");
    }

    public Download downloadMovie(String movieUrl) {
        Document document = getDocument(movieUrl);
        Movie movie = null;

        for (Element typeRow : document.select(".collapse.siteSingle__boxContent__download")) {
            String downloadType = typeRow.select(".siteSingle__boxContent__downloadHeader.s-abasi .abasi-dlbox-title").text();
            if (downloadType.equals(MOVIE_DOWNLOAD_TYPE)) {

                List<Link> links = new ArrayList<>();
                for (Element downloadRow :
                        typeRow.select(".collapse-content.siteSingle__boxContent__downloadContent.abasi-dlbox .item")) {
                    if (downloadRow.select(".right-area a").attr("href").equals("")) {
                        continue;
                    }
                    String quality = downloadRow.select(".left-area .quality").text();
                    String size = downloadRow.select(".left-area .details")
                            .select("span.size").text()
                            .replace("حجم:", "").trim();
                    String url = downloadRow.select(".right-area a").attr("href");


                    links.add(Link.builder()
                            .quality(quality)
                            .size(size)
                            .url(url)
                            .build());

                }
                movie = Movie.builder()
                        .links(links)
                        .build();
                break;
            }
        }

        return Download.builder()
                .title("The lobster 2015")
                .url(movieUrl)
                .type(Type.MOVIE)
                .provider(getProvider())
                .movie(movie)
                .build();
    }

    private Provider getProvider() {
        return Provider.builder()
                .title("Avamovie")
                .address(baseUrl)
                .isFiltered(false)
                .build();
    }

    public Download downloadSeries(String seriesUrl) {
        return null;
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
