package com.goga74.platform.service;

import com.microsoft.playwright.*;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

@Service
public class WebParsingService
{

    private String url;
    private String title;
    private long crc;
    private long pageSize;
    private String content;

    public WebParsingService()
    {
    }

    public void setUrl(String url)
    {
        this.url = url;
        fetchDataFromUrl();
    }

    /*
    private void fetchDataFromUrl()
    {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate(url);

            this.title = page.title();
            this.content = page.content();
            this.pageSize = content.getBytes(StandardCharsets.UTF_8).length;
            this.crc = calculateCRC(content);
        }
    }
    */

    private void fetchDataFromUrl()
    {
        if (url == null || url.isEmpty())
        {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        try (Playwright playwright = Playwright.create();
             Browser browser = playwright.chromium().launch())
        {
            Page page = browser.newPage();
            page.navigate(url);

            this.title = page.title();

            // get text from body tag
            Locator bodyLocator = page.locator("body");
            if (bodyLocator.count() > 0)
            {
                this.content = bodyLocator.innerText();
            } else {
                this.content = null;
            }
            if (this.content == null)
            {
                this.content = page.content();
            }
            this.pageSize = this.content.getBytes(StandardCharsets.UTF_8).length;
            this.crc = calculateCRC(this.content);
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    private long calculateCRC(String text)
    {
        CRC32 crc = new CRC32();
        crc.update(text.getBytes(StandardCharsets.UTF_8));
        return crc.getValue();
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public long getCrc() {
        return crc;
    }

    public long getPageSize() {
        return pageSize;
    }

    public String getContent()
    {
        return content;
    }
}