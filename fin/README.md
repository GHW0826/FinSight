# Fin Module

## News Crawling Module

This module contains a Google News finance crawler built on top of
`spring-modules:crawl`.

### What it does

- Collects cards from Google News finance search
- Scrolls the page with Selenium to load more cards
- Resolves original publisher URLs from Google News article pages
- Crawls the full article body through `CrawlOperations`
- Stores article bodies in the database
- Prevents duplicate crawling by normalized original URL
- Exposes CRUD APIs for stored news articles

### Target source

- Search URL:
  `https://news.google.com/search?q=finance&hl=ko&gl=KR&ceid=KR%3Ako`

### Storage

- Database: file-based H2
- File path: `./data/finsight-news`
- Duplicate key: `normalizedOriginalUrl`

### APIs

- `POST /api/news/crawl/google-finance`
- `GET /api/news/articles`
- `GET /api/news/articles/{id}`
- `POST /api/news/articles`
- `PUT /api/news/articles/{id}`
- `DELETE /api/news/articles/{id}`

### Crawl request example

```json
{
  "maxScrollCount": 6,
  "maxArticlesPerRun": 30
}
```

### Configuration

Default values live in `src/main/resources/application.properties`.

- `app.news.google.search-url`
- `app.news.google.keyword`
- `app.news.google.max-scroll-count`
- `app.news.google.scroll-pause-millis`
- `app.news.google.max-articles-per-run`
- `app.news.google.headless`
- `app.news.google.page-load-timeout-seconds`

### Runtime notes

- A local browser is required for Selenium.
- The code tries `EdgeDriver` first and then `ChromeDriver`.
- Google News can throttle repeated automated access.
- Only successfully saved articles are treated as duplicates.

### Important classes

- `com.finsight.fin.news.crawl.GenericNewsArticleCrawler`
- `com.finsight.fin.news.crawl.GoogleNewsBrowserClient`
- `com.finsight.fin.news.service.GoogleFinanceNewsCrawlService`
- `com.finsight.fin.news.service.NewsArticleService`
